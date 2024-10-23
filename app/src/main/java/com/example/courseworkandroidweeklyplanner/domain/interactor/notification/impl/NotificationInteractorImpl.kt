package com.example.courseworkandroidweeklyplanner.domain.interactor.notification.impl

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.courseworkandroidweeklyplanner.domain.TASK_ID_KEY
import com.example.courseworkandroidweeklyplanner.domain.interactor.notification.NotificationCreator
import com.example.courseworkandroidweeklyplanner.domain.interactor.notification.NotificationInteractor
import com.example.courseworkandroidweeklyplanner.domain.model.Notification
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID
import javax.inject.Inject

class NotificationInteractorImpl @Inject constructor(
    private val alarmManager: AlarmManager,
    @ApplicationContext private val context: Context
) : NotificationInteractor {
    override suspend fun saveNotification(notification: Notification) {
        val trigger = notification.scheduledTime
        val intent = Intent(
            /* packageContext = */ context,
            /* cls = */ NotificationCreator::class.java
        ).apply {
            putExtra(
                /* name = */ TASK_ID_KEY,
                /* value = */ notification.taskId.toString()
            )
        }
        val pendingID = notification.taskId.hashCode()
        val pending = PendingIntent.getBroadcast(
            /* context = */ context,
            /* requestCode = */ pendingID,
            /* intent = */ intent,
            /* flags = */ PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.setAndAllowWhileIdle(
            /* type = */ AlarmManager.RTC_WAKEUP,
            /* triggerAtMillis = */ trigger.toEpochMilli(),
            /* operation = */ pending
        )
    }

    override suspend fun deleteNotification(notificationId: UUID) {
        val intent = Intent(
            /* packageContext = */ context,
            /* cls = */ NotificationCreator::class.java
        )
        val pendingID = notificationId.hashCode()
        val pending = PendingIntent.getBroadcast(
            /* context = */ context,
            /* requestCode = */ pendingID,
            /* intent = */ intent,
            /* flags = */ PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pending)
    }
}