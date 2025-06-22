package com.example.taskplanner.domain.eventBus.model

import java.time.LocalDate

sealed class CelebrationEvent {
    data class Celebrate(val date: LocalDate): CelebrationEvent()
    data class Uncelebrate(val date: LocalDate): CelebrationEvent()
}