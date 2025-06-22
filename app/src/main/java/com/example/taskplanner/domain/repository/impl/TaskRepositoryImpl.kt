package com.example.taskplanner.domain.repository.impl

import com.example.taskplanner.data.dao.TaskDao
import com.example.taskplanner.data.entity.TaskEntity
import com.example.taskplanner.domain.Converter
import com.example.taskplanner.domain.localTimeOfMilliOfDay
import com.example.taskplanner.domain.model.Category
import com.example.taskplanner.domain.model.Difficulty
import com.example.taskplanner.domain.model.Priority
import com.example.taskplanner.domain.model.Task
import com.example.taskplanner.domain.repository.TaskRepository
import com.example.taskplanner.domain.toMilliOfDay
import com.example.taskplanner.presentation.asyncMap
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

    override fun getTasksForDateRange(
        start: LocalDate,
        end: LocalDate
    ): Flow<List<Task>> = taskDao.getTasksForDateRange(start.toEpochDay(), end.toEpochDay())
        .map { tasks -> tasks.asyncMap(::convertToState).toList() }.flowOn(Dispatchers.IO)

    override fun getTasksByDate(date: LocalDate): Flow<List<Task>> =
        taskDao.getTasksByDate(date.toEpochDay()).map { tasks ->
            tasks.asyncMap(::convertToState).toList()
        }.flowOn(Dispatchers.IO)

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
                notificationTimeOffset = notificationTimeOffset,
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
                notificationTimeOffset = notificationTimeOffset,
                isDone = isDone,
            )
        }
    }
}
