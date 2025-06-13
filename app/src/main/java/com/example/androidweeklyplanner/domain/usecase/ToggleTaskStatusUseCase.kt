package com.example.androidweeklyplanner.domain.usecase

import com.example.androidweeklyplanner.domain.model.Task
import com.example.androidweeklyplanner.domain.repository.TaskRepository
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