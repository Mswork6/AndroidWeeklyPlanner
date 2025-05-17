package com.example.courseworkandroidweeklyplanner.domain.repository.impl

import com.example.courseworkandroidweeklyplanner.data.dao.TaskDao
import com.example.courseworkandroidweeklyplanner.data.entity.TaskEntity
import com.example.courseworkandroidweeklyplanner.domain.Converter
import com.example.courseworkandroidweeklyplanner.domain.fromLocalDateTime
import com.example.courseworkandroidweeklyplanner.domain.localTimeOfMilliOfDay
import com.example.courseworkandroidweeklyplanner.domain.model.Category
import com.example.courseworkandroidweeklyplanner.domain.model.Difficulty
import com.example.courseworkandroidweeklyplanner.domain.model.NotificationTime
import com.example.courseworkandroidweeklyplanner.domain.model.Priority
import com.example.courseworkandroidweeklyplanner.domain.model.Task
import com.example.courseworkandroidweeklyplanner.domain.repository.TaskRepository
import com.example.courseworkandroidweeklyplanner.domain.toLocalDateTime
import com.example.courseworkandroidweeklyplanner.domain.toMilliOfDay
import com.example.courseworkandroidweeklyplanner.presentation.asyncMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao,
) : TaskRepository, Converter<TaskEntity, Task> {
    override fun getTasks(): Flow<List<Task>> = taskDao.getTasks().map { tasks ->
        tasks.asyncMap(::convertToState).toList()
    }.flowOn(Dispatchers.IO)

    override suspend fun getTask(taskId: UUID): Task? = taskDao.getTask(taskId)?.let {
        convertToState(it)
    }

    override suspend fun addTask(task: Task) = withContext(Dispatchers.IO) {
        taskDao.insertTask(convertToEntity(task))
    }

    override suspend fun updateTask(task: Task) = withContext(Dispatchers.IO) {
        taskDao.updateTask(convertToEntity(task))
    }

    override suspend fun deleteTask(task: Task) = withContext(Dispatchers.IO) {
        taskDao.deleteTask(convertToEntity(task))
    }

    override suspend fun countTasksByDateAndDifficulty(
        date: Long,
        difficulty: String,
        excludeID: UUID?): Int {
        return withContext(Dispatchers.IO) {
            taskDao.countTasksByDateAndDifficulty(date, difficulty, excludeID)
        }
    }


    override fun convertToEntity(state: Task): TaskEntity {
        return with(state) {
            TaskEntity(
                id = id,
                name = name,
                description = description,
                date = date.toEpochDay(),
                time = time.toMilliOfDay(),
                notificationTime = fromLocalDateTime(notificationTime),
                priority = priority.toString(),
                difficulty = difficulty.toString(),
                category = category.toString(),
                isDone = isDone
            )
        }
    }

    override fun convertToState(entity: TaskEntity): Task {
        return with(entity) {
            Task(
                id = id,
                name = name,
                description = description,
                priority = Priority.valueOf(priority),
                difficulty = Difficulty.valueOf(difficulty),
                category = Category.valueOf(category),
                date = LocalDate.ofEpochDay(entity.date),
                time = localTimeOfMilliOfDay(time),
                notificationTime = toLocalDateTime(notificationTime),
                isDone = isDone,
            )
        }
    }
}
