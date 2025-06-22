package com.example.androidweeklyplanner.domain.eventBus

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CelebrationEventBus @Inject constructor() {
    private val _events = MutableSharedFlow<LocalDate>(replay = 1)
    val events: SharedFlow<LocalDate> = _events.asSharedFlow()

    suspend fun send(date: LocalDate) {
        _events.emit(date)
    }
}