package com.example.taskplanner.domain.interactor.notification

import com.example.taskplanner.domain.model.Notification
import java.util.UUID

interface NotificationInteractor {
    suspend fun saveNotification(notification: Notification)

    suspend fun deleteNotification(notificationId: UUID)
}