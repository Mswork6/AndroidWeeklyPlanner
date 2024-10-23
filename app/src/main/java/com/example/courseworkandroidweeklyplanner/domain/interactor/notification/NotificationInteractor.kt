package com.example.courseworkandroidweeklyplanner.domain.interactor.notification

import com.example.courseworkandroidweeklyplanner.domain.model.Notification
import java.util.UUID

interface NotificationInteractor {
    suspend fun saveNotification(notification: Notification)

    suspend fun deleteNotification(notificationId: UUID)
}