package com.example.courseworkandroidweeklyplanner.domain.usecase

import com.example.courseworkandroidweeklyplanner.domain.model.Difficulty
import com.example.courseworkandroidweeklyplanner.domain.repository.TaskRepository
import java.time.LocalDate
import javax.inject.Inject

class CheckDailyTaskLimitUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
) {
    suspend operator fun invoke(date: LocalDate, difficulty: Difficulty): Boolean {
        val count = taskRepository.countTasksByDateAndDifficulty(
            date.toEpochDay(),
            difficulty.toString()
        )
        val limit = when (difficulty) {
            Difficulty.HARD -> 1
            Difficulty.MEDIUM -> 3
            Difficulty.EASY -> 5
        }
        return count < limit
    }
}
