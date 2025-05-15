package com.example.courseworkandroidweeklyplanner.domain.interactor.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Bundle
import android.util.Log
import com.example.courseworkandroidweeklyplanner.R
import com.example.courseworkandroidweeklyplanner.domain.TASK_ID_KEY
import com.example.courseworkandroidweeklyplanner.domain.getTaskId
import com.example.courseworkandroidweeklyplanner.domain.interactor.saver.TaskInteractor
import com.example.courseworkandroidweeklyplanner.presentation.MainActivity
import com.example.courseworkandroidweeklyplanner.presentation.description
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
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
                            importance = NotificationManager.IMPORTANCE_DEFAULT
                        )
                        notificationManager.createNotificationChannel(channel)
                        val notification = buildNotification(
                            context = context,
                            icon = R.drawable.icon_checkbox_done,
                            title = context.getString(task.priority.description),
                            text = task.name,
                            onClickIntent = buildOnClickIntent(context, task.id),
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
        cancellingIntent: PendingIntent
    ): Notification = Notification.Builder(context, CHANNEL_ID)
        .setAutoCancel(true)
        .setSmallIcon(icon)
        .setContentTitle(title)
        .setContentText(text)
        .setContentIntent(onClickIntent)
        .addAction(
            Notification.Action.Builder(
                /* icon = */ Icon.createWithResource(context, R.drawable.baseline_more_time_24),
                /* title = */ context.getString(R.string.postpone_for_day),
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
    )

    private fun buildOnClickIntent(context: Context, id: UUID): PendingIntent {
        return PendingIntent.getActivity(
            /* context = */ context,
            /* requestCode = */ id.hashCode(),
            /* intent = */ Intent(context, MainActivity::class.java),
            /* flags = */ PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
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
}