package com.example.androidweeklyplanner.domain.interactor.saver

import com.example.androidweeklyplanner.domain.model.Task
import java.util.UUID

interface TaskInteractor {
    suspend fun getTask(taskId: UUID): Task?

    suspend fun updateTask(task: Task)

    suspend fun addTask(task: Task)

    suspend fun deleteTask(task: Task)
}