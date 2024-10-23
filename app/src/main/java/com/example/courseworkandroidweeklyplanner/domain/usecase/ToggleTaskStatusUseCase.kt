package com.example.courseworkandroidweeklyplanner.domain.usecase

import com.example.courseworkandroidweeklyplanner.domain.model.Task
import com.example.courseworkandroidweeklyplanner.domain.repository.TaskRepository
import javax.inject.Inject

class ToggleTaskStatusUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(task: Task) {
        taskRepository.updateTask(
            with(task) {
                copy(isDone = isDone.not())
            }
        )
    }
}