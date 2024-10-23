package com.example.courseworkandroidweeklyplanner.domain.repository

import com.example.courseworkandroidweeklyplanner.domain.model.Task
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface TaskRepository {
    fun getTasks(): Flow<List<Task>>

    suspend fun getTask(taskId: UUID): Task?

    suspend fun addTask(task: Task)

    suspend fun updateTask(task: Task)

    suspend fun deleteTask(task: Task)
}
