package com.example.androidweeklyplanner.domain.usecase

import com.example.androidweeklyplanner.domain.model.Task
import com.example.androidweeklyplanner.domain.repository.TaskRepository
import javax.inject.Inject

class ToggleTaskStatusUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
    private val handleCelebration: HandleDayCelebrationUseCase
) {
    suspend operator fun invoke(task: Task) {
        taskRepository.updateTask(
            with(task) {
                copy(isDone = isDone.not())
            }
        )
        handleCelebration(task.date)
    }
}