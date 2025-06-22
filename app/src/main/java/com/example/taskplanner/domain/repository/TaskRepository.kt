package com.example.taskplanner.domain.repository

import com.example.taskplanner.domain.model.Task
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.util.UUID

interface TaskRepository {
    fun getTasks(): Flow<List<Task>>

    suspend fun getTask(taskId: UUID): Task?

    fun getTasksForDateRange(start: LocalDate, end: LocalDate): Flow<List<Task>>

    fun getTasksByDate(date: LocalDate): Flow<List<Task>>

    suspend fun addTask(task: Task)

    suspend fun updateTask(task: Task)

    suspend fun deleteTask(task: Task)

    suspend fun countTasksByDateAndDifficulty(
        date: Long,
        difficulty: String,
        excludeID: UUID? = null): Int
}
