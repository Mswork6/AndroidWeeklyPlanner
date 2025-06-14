package com.example.androidweeklyplanner.domain.interactor.notification.impl

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.androidweeklyplanner.domain.TASK_ID_KEY
import com.example.androidweeklyplanner.domain.interactor.notification.NotificationCreator
import com.example.androidweeklyplanner.domain.interactor.notification.NotificationInteractor
import com.example.androidweeklyplanner.domain.model.Notification
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.ZoneId
import java.util.UUID
import javax.inject.Inject

class NotificationInteractorImpl @Inject constructor(
    private val alarmManager: AlarmManager,
    @ApplicationContext private val context: Context
) : NotificationInteractor {
    @SuppressLint("ScheduleExactAlarm")
    override suspend fun saveNotification(notification: Notification) {
        val trigger = notification.scheduledTime
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
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

        alarmManager.setExactAndAllowWhileIdle(
            /* type = */ AlarmManager.RTC_WAKEUP,
            /* triggerAtMillis = */ trigger,
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