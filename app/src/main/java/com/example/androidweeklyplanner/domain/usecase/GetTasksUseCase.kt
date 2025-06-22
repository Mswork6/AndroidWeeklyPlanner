package com.example.androidweeklyplanner.domain.usecase

import com.example.androidweeklyplanner.domain.FilterScreenSortRepo
import com.example.androidweeklyplanner.domain.model.FilterConfig
import com.example.androidweeklyplanner.domain.model.SortConfig
import com.example.androidweeklyplanner.domain.model.SortType
import com.example.androidweeklyplanner.domain.model.Task
import com.example.androidweeklyplanner.domain.provider.TaskComparatorProvider
import com.example.androidweeklyplanner.domain.repository.FilterRepository
import com.example.androidweeklyplanner.domain.repository.SortRepository
import com.example.androidweeklyplanner.domain.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
    private val filterRepository: FilterRepository,
    private val filterTasks: FilterTasksUseCase,
    @FilterScreenSortRepo private val sortRepository: SortRepository,
    private val comparatorProvider: TaskComparatorProvider
) {
    operator fun invoke(): Flow<List<Task>> =
        taskRepository.getTasks()
            .combine(filterRepository.getFilterConfig()) { tasks, filter ->
                filterTasks(tasks, filter)
            }
            .combine(sortRepository.getSortConfig()) { tasks, sort ->
                val comparator = comparatorProvider(sort)
                tasks.sortedWith(comparator)
            }
            .flowOn(Dispatchers.Default)

}