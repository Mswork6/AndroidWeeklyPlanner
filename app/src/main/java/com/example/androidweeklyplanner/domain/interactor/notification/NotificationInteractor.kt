package com.example.androidweeklyplanner.domain.interactor.notification

import com.example.androidweeklyplanner.domain.model.Notification
import java.util.UUID

interface NotificationInteractor {
    suspend fun saveNotification(notification: Notification)

    suspend fun deleteNotification(notificationId: UUID)
}