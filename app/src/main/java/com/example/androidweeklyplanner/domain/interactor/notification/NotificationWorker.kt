package com.example.androidweeklyplanner.domain.interactor.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.androidweeklyplanner.R
import com.example.androidweeklyplanner.domain.TASK_ID_KEY
import com.example.androidweeklyplanner.domain.interactor.saver.TaskInteractor
import com.example.androidweeklyplanner.domain.notification
import com.example.androidweeklyplanner.presentation.MainActivity
import com.example.androidweeklyplanner.presentation.dateTimeToString
import com.example.androidweeklyplanner.presentation.timeToString
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val taskInteractor: TaskInteractor,
    private val notificationManager: NotificationManager
) : CoroutineWorker(appContext, params) {

    companion object {
        const val KEY_TASK_ID   = "TASK_ID"
        const val CHANNEL_ID    = "TODO-NOTIFY"
        const val CHANNEL_NAME  = "ToDo Notification"
    }

    override suspend fun doWork(): Result {
        val taskIdString = inputData.getString(KEY_TASK_ID)
            ?: return Result.failure()
        val taskId = UUID.fromString(taskIdString)

        val task = taskInteractor.getTask(taskId) ?: return Result.success()

        // Создаём канал, если ещё нет
        if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableLights(true)
                enableVibration(true)
                description = "Напоминание о задачах"
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Заголовок
        val titleRes = if (task.notification?.scheduledTime
                ?.isAfter(LocalDateTime.of(task.date, task.time)) == true
        ) R.string.description_hurry_up
        else R.string.description_reminder

        val title = applicationContext.getString(titleRes)

        // Текст
        val whenStr = if (LocalDate.now() == task.date)
            timeToString(task.time)
        else
            dateTimeToString(task.date, task.time)

        val textRes = if (task.notification?.scheduledTime
                ?.isAfter(LocalDateTime.of(task.date, task.time)) == true
        ) R.string.description_notification_message_expired
        else R.string.description_notification_message

        val taskName = if (task.name.length > 35) {
            task.name.substring(0, 35).plus("...")
        } else {
            task.name
        }

        val text = applicationContext.getString(textRes, taskName, whenStr)

        // Intent на открытие задачи
        val clickIntent = PendingIntent.getActivity(
            applicationContext,
            taskId.hashCode(),
            Intent(Intent.ACTION_VIEW, Uri.parse("todo://view/$taskId"), applicationContext, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Intent для кнопки «Завершить»
        val completeIntent = Intent(applicationContext, NotificationCompleter::class.java).apply {
            putExtra(TASK_ID_KEY, taskId.toString())
        }
        val completePending = PendingIntent.getBroadcast(
            applicationContext,
            taskId.hashCode().inv(),
            completeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Intent для кнопки «Отложить»
        val postponeIntent = Intent(applicationContext, NotificationPostponer::class.java).apply {
            putExtra(TASK_ID_KEY, taskId.toString())
        }
        val postponePending = PendingIntent.getBroadcast(
            applicationContext,
            taskId.hashCode(),
            postponeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.icon_checkbox_done)
            .setContentTitle(title)
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setContentIntent(clickIntent)
            .addAction(
                R.drawable.baseline_more_time_24,
                applicationContext.getString(R.string.postpone_for_15_minutes),
                postponePending
            )
            .addAction(
                R.drawable.icon_checkbox_done,
                applicationContext.getString(R.string.action_complete),
                completePending
            )
            .setAutoCancel(true)
            .build()

        notificationManager.notify(taskId.hashCode(), notification)
        return Result.success()
    }
}