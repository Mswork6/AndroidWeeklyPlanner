package com.example.courseworkandroidweeklyplanner.domain.interactor.saver

import com.example.courseworkandroidweeklyplanner.domain.interactor.notification.NotificationInteractor
import com.example.courseworkandroidweeklyplanner.domain.model.Task
import com.example.courseworkandroidweeklyplanner.domain.notification
import com.example.courseworkandroidweeklyplanner.domain.repository.TaskRepository
import java.util.UUID
import javax.inject.Inject

class TaskInteractorImpl @Inject constructor(
    private val taskRepository: TaskRepository,
    private val notificationInteractor: NotificationInteractor
) : TaskInteractor {
    override suspend fun getTask(taskId: UUID): Task? = taskRepository.getTask(taskId)

    override suspend fun updateTask(task: Task) {
        taskRepository.updateTask(task)
        when (val notification = task.notification) {
            null -> notificationInteractor.deleteNotification(task.id)
            else -> notificationInteractor.saveNotification(notification)
        }
    }

    override suspend fun addTask(task: Task) {
        taskRepository.addTask(task)
        when (val notification = task.notification) {
            null -> Unit
            else -> notificationInteractor.saveNotification(notification)
        }
    }

    override suspend fun deleteTask(task: Task) {
        taskRepository.deleteTask(task)
        notificationInteractor.deleteNotification(task.id)
    }
}