package com.example.androidweeklyplanner.domain.interactor.notification.impl

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.androidweeklyplanner.domain.TASK_ID_KEY
import com.example.androidweeklyplanner.domain.interactor.notification.NotificationCreator
import com.example.androidweeklyplanner.domain.interactor.notification.NotificationInteractor
import com.example.androidweeklyplanner.domain.interactor.notification.NotificationWorker
import com.example.androidweeklyplanner.domain.model.Notification
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.ZoneId
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NotificationInteractorImpl @Inject constructor(
    private val workManager: WorkManager,
    @ApplicationContext private val context: Context
) : NotificationInteractor {
    @SuppressLint("ScheduleExactAlarm")
    override suspend fun saveNotification(notification: Notification) {
        val now = System.currentTimeMillis()
        val triggerAt = notification.scheduledTime
            .atZone(ZoneId.systemDefault())
            .toInstant().toEpochMilli()
        val delay = (triggerAt - now).coerceAtLeast(0L)

        val data = workDataOf(NotificationWorker.KEY_TASK_ID to notification.taskId.toString())

        val request = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInputData(data)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()

        workManager.enqueueUniqueWork(
            "notification_${notification.taskId}",
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    override suspend fun deleteNotification(notificationId: UUID) {
        workManager.cancelUniqueWork("notification_$notificationId")
    }
}