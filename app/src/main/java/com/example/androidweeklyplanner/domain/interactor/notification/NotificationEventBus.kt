package com.example.androidweeklyplanner.domain.interactor.notification

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationEventBus @Inject constructor() {
    private val _events = MutableSharedFlow<Unit>(replay = 1)
    val events: SharedFlow<Unit> = _events

    fun notifyReceived() {
        _events.tryEmit(Unit)
    }
}