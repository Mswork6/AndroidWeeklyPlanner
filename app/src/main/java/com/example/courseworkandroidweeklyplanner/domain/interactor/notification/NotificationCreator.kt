package com.example.courseworkandroidweeklyplanner.domain.interactor.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.net.Uri
import android.util.Log
import com.example.courseworkandroidweeklyplanner.R
import com.example.courseworkandroidweeklyplanner.domain.TASK_ID_KEY
import com.example.courseworkandroidweeklyplanner.domain.getTaskId
import com.example.courseworkandroidweeklyplanner.domain.interactor.saver.TaskInteractor
import com.example.courseworkandroidweeklyplanner.domain.notification
import com.example.courseworkandroidweeklyplanner.presentation.MainActivity
import com.example.courseworkandroidweeklyplanner.presentation.dateTimeToString
import com.example.courseworkandroidweeklyplanner.presentation.timeToString
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

@AndroidEntryPoint
class NotificationCreator : BroadcastReceiver() {
    @Inject
    lateinit var taskInteractor: TaskInteractor

    @Inject
    lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var scope: CoroutineScope

    private companion object {
        const val CHANNEL_ID = "TODO-NOTIFY"
        const val CHANNEL_NAME = "ToDo Notification"
    }

    override fun onReceive(context: Context, intent: Intent) {
        try {
            scope.launch {
                when (val task = taskInteractor.getTask(getTaskId(intent.extras))) {
                    null -> Unit
                    else -> {
                        val channel = buildChannel(
                            id = CHANNEL_ID,
                            name = CHANNEL_NAME,
                            importance = NotificationManager.IMPORTANCE_HIGH
                        )
                        notificationManager.createNotificationChannel(channel)
                        val title = if (task.notification?.scheduledTime?.
                            isAfter(LocalDateTime.of(task.date, task.time)) == true) {
                            context.getString(R.string.description_hurry_up)
                        } else {
                            context.getString(R.string.description_reminder)
                        }

                        val taskName = if (task.name.length > 35) {
                            task.name.substring(0, 35).plus("...")
                        } else {
                            task.name
                        }

                        val completionDate = if (LocalDate.now().equals(task.date)){
                            timeToString(task.time)
                        } else {
                            dateTimeToString(task.date, task.time)
                        }

                        val textPattern: Int = if (task.notification?.scheduledTime?.
                            isAfter(LocalDateTime.of(task.date, task.time)) == true) {
                            R.string.description_notification_message_expired
                        } else {
                            R.string.description_notification_message
                        }

                        val text = context.getString(
                            textPattern,
                            taskName,
                            completionDate
                        )

                        val notification = buildNotification(
                            context = context,
                            icon = R.drawable.icon_checkbox_done,
                            title = title,
                            text = text,
                            onClickIntent = buildOnClickIntent(context, task.id),
                            completeIntent = buildCompleteIntent(context, task.id),
                            cancellingIntent = buildPostponeIntent(context, task.id)
                        )
                        notificationManager.notify(task.id.hashCode(), notification)
                        //taskInteractor.updateTask(task.copy(notificationTime = null))
                    }
                }
            }
        } catch (exception: Exception) {
            Log.d(this::class.java.toString(), exception.stackTraceToString())
        }
    }

    private fun buildNotification(
        context: Context,
        icon: Int,
        title: String,
        text: String,
        onClickIntent: PendingIntent,
        completeIntent: PendingIntent,
        cancellingIntent: PendingIntent,
    ): Notification = Notification.Builder(context, CHANNEL_ID)
        .setAutoCancel(true)
        .setSmallIcon(icon)
        .setContentTitle(title)
        .setContentText(text)
        .setContentIntent(onClickIntent)
        .setPriority(Notification.PRIORITY_HIGH)
        .setDefaults(Notification.DEFAULT_ALL)
        .setStyle(Notification.BigTextStyle().bigText(text))
        .addAction(
            Notification.Action.Builder(
                Icon.createWithResource(context, R.drawable.baseline_more_time_24),
                context.getString(R.string.action_complete),
                completeIntent
            ).build()
        )

        .addAction(
            Notification.Action.Builder(
                /* icon = */ Icon.createWithResource(context, R.drawable.baseline_more_time_24),
                /* title = */ context.getString(R.string.postpone_for_15_minutes),
                /* intent = */ cancellingIntent
            ).build()
        ).build()

    private fun buildChannel(
        id: String,
        name: String,
        importance: Int
    ) = NotificationChannel(
        /* id = */ id,
        /* name = */ name,
        /* importance = */ importance
    ). apply {
        enableLights(true)
        enableVibration(true)
        description = "Напоминание о задачах"
    }

    private fun buildOnClickIntent(context: Context, id: UUID): PendingIntent {
        val uri = Uri.parse("todo://view/$id")
        val intent = Intent(Intent.ACTION_VIEW, uri, context, MainActivity::class.java)
        return PendingIntent.getActivity(
            context,
            id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun buildPostponeIntent(context: Context, id: UUID): PendingIntent {
        return PendingIntent.getBroadcast(
            /* context = */ context,
            /* requestCode = */ id.hashCode(),
            /* intent = */ Intent(context, NotificationPostponer::class.java).apply {
                putExtra(
                    /* name = */ TASK_ID_KEY,
                    /* value = */ id.toString()
                )
            },
            /* flags = */ PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun buildCompleteIntent(context: Context, id: UUID): PendingIntent {
        return PendingIntent.getBroadcast(
            context,
            id.hashCode().inv(),
            Intent(context, NotificationCompleter::class.java).apply {
                putExtra(TASK_ID_KEY, id.toString())
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}