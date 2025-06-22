package com.example.taskplanner.domain.usecase

import com.example.taskplanner.data.CelebratedDatesDataStore
import com.example.taskplanner.domain.eventBus.CelebrationEventBus
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import javax.inject.Inject

class HandleDayCelebrationUseCase @Inject constructor(
    private val getTasksForDate: GetTasksForDateUseCase,
    private val celebratedDatesDataStore: CelebratedDatesDataStore,
    private val celebrationEventBus: CelebrationEventBus
) {
    suspend operator fun invoke(date: LocalDate) {
        val allTasksDone = getTasksForDate(date)
            .first()
            .all { it.isDone }

        if (allTasksDone) {
            celebratedDatesDataStore.markDateCelebrated(date)
            celebrationEventBus.send(date)
        } else {
            celebratedDatesDataStore.unmarkDateCelebrated(date)
        }
    }
}