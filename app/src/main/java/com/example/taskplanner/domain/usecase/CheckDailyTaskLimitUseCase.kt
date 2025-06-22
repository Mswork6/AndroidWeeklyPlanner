package com.example.taskplanner.domain.usecase

import com.example.taskplanner.domain.model.Difficulty
import com.example.taskplanner.domain.repository.TaskRepository
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

class CheckDailyTaskLimitUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
) {
    suspend operator fun invoke(
        date: LocalDate,
        difficulty: Difficulty,
        excludeId: UUID?): Boolean {
        val count = taskRepository.countTasksByDateAndDifficulty(
            date.toEpochDay(),
            difficulty.toString(),
            excludeId
        )
        val limit = when (difficulty) {
            Difficulty.HARD -> 1
            Difficulty.MEDIUM -> 3
            Difficulty.EASY -> 5
        }
        return count < limit
    }
}
