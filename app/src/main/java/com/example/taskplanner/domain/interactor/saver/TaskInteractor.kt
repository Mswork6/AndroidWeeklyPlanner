package com.example.taskplanner.domain.interactor.saver

import com.example.taskplanner.domain.model.Task
import java.util.UUID

interface TaskInteractor {
    suspend fun getTask(taskId: UUID): Task?

    suspend fun updateTask(task: Task)

    suspend fun addTask(task: Task)

    suspend fun deleteTask(task: Task)
}