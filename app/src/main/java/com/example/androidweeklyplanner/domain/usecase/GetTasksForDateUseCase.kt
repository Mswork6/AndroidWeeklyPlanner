package com.example.androidweeklyplanner.domain.usecase

import com.example.androidweeklyplanner.domain.model.Task
import com.example.androidweeklyplanner.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class GetTasksForDateUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    operator fun invoke(date: LocalDate): Flow<List<Task>> =
        repository.getTasksByDate(date)
}