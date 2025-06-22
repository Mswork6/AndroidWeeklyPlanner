package com.example.taskplanner.domain.interactor.notification.impl

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.taskplanner.domain.interactor.notification.NotificationInteractor
import com.example.taskplanner.domain.interactor.notification.NotificationWorker
import com.example.taskplanner.domain.model.Notification
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
        ).also {
            Log.d("NotifInteractor", "Enqueued work for task=${notification.taskId}, delay=${delay}ms")

            val workName = "notification_${notification.taskId}"
            WorkManager.getInstance(context)
                .getWorkInfosForUniqueWork(workName)
                .addListener({
                    val infos = WorkManager.getInstance(context)
                        .getWorkInfosForUniqueWork(workName)
                        .get()   // это List<WorkInfo>
                    infos.forEach {
                        Log.d("WorkDebug", "id=${it.id} state=${it.state} attempts=${it.runAttemptCount}")
                    }
                }, ContextCompat.getMainExecutor(context))
        }

    }

    override suspend fun deleteNotification(notificationId: UUID) {
        workManager.cancelUniqueWork("notification_$notificationId")
    }
}