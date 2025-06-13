package com.example.androidweeklyplanner.domain.usecase

import com.example.androidweeklyplanner.domain.model.SortType
import com.example.androidweeklyplanner.domain.model.Task
import com.example.androidweeklyplanner.domain.repository.SortRepository
import com.example.androidweeklyplanner.domain.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
    private val sortRepository: SortRepository
) {
    /** Возвращает все задачи, отсортированные согласно текущему SortType */
    operator fun invoke(): Flow<List<Task>> =
        taskRepository.getTasks().flowOn(Dispatchers.Default)
//            .combine(sortRepository.getSort()) { tasks, sort ->
//                when (sort) {
//                    SortType.INCREASE -> tasks.sortedWith(
//                        compareBy<Task> { it.priority }
//                            .thenBy { it.date }
//                            .thenBy { it.time }
//                    )
//                    SortType.DECREASE -> tasks.sortedWith(
//                        compareByDescending<Task>
//                        { it.priority }
//                            .thenBy { it.date }
//                            .thenBy { it.time }
//                    )
//                    SortType.STANDARD -> tasks.sortedWith(
//                        compareBy<Task> { it.date }
//                            .thenBy { it.time }
//                    )
//                }
//            }

}