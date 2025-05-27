// NotificationEventBus.kt
package com.example.androidweeklyplanner.domain

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Singleton-сервис для рассылки и получения событий «пришло уведомление».
 */
@Singleton
class NotificationEventBus @Inject constructor() {
    private val _events = MutableSharedFlow<Unit>(replay = 1)
    val events: SharedFlow<Unit> = _events

    fun notifyReceived() {
        _events.tryEmit(Unit)
    }
}