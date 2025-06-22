package com.example.taskplanner.domain.eventBus

import com.example.taskplanner.domain.eventBus.model.CelebrationEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CelebrationEventBus @Inject constructor() {
    private val _channel = Channel<CelebrationEvent>(Channel.BUFFERED)
    val events = _channel.receiveAsFlow()

    suspend fun celebrate(date: LocalDate) {
        _channel.send(CelebrationEvent.Celebrate(date))
    }
    suspend fun uncelebrate(date: LocalDate) {
        _channel.send(CelebrationEvent.Uncelebrate(date))
    }
}