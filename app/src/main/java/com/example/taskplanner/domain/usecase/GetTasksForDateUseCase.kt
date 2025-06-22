package com.example.taskplanner.domain.usecase

import com.example.taskplanner.domain.model.Task
import com.example.taskplanner.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class GetTasksForDateUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    operator fun invoke(date: LocalDate): Flow<List<Task>> =
        repository.getTasksByDate(date)
}