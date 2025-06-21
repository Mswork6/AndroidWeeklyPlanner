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

//    private fun applyFilter(tasks: List<Task>, cfg: FilterConfig): List<Task> {
//        return tasks.filter { task ->
//            // Диапазон дат
//            (cfg.startDate == null || !task.date.isBefore(cfg.startDate)) &&
//                    (cfg.endDate == null || !task.date.isAfter(cfg.endDate)) &&
//                    // Приоритет
//                    (cfg.priorityFilter.isEmpty() || cfg.priorityFilter.contains(task.priority)) &&
//                    // Сложность
//                    (cfg.difficultyFilter.isEmpty() || cfg.difficultyFilter.contains(task.difficulty)) &&
//                    // Категории
//                    (cfg.categoryFilter.isEmpty() || cfg.categoryFilter.contains(task.category))
//        }
//    }
//
//    private fun applySort(tasks: List<Task>, cfg: SortConfig): List<Task> {
//        val comparator = buildComparator(cfg)
//        return tasks.sortedWith(comparator)
//    }
//
//    private fun buildComparator(cfg: SortConfig): Comparator<Task> {
//        var comparator: Comparator<Task> = compareBy<Task> { it.date }
//            .thenBy { it.time }
//
//        // Сложность
//        when (cfg.difficultyOrder) {
//            SortType.INCREASE -> comparator = compareBy<Task> { it.difficulty }.then(comparator)
//            SortType.DECREASE -> comparator = compareByDescending<Task> { it.difficulty }.then(comparator)
//            else -> { }
//        }
//
//        // Приоритет
//        when (cfg.priorityOrder) {
//            SortType.INCREASE -> comparator = compareBy<Task> { it.priority }.then(comparator)
//            SortType.DECREASE -> comparator = compareByDescending<Task> { it.priority }.then(comparator)
//            else -> { }
//        }
//
//        return comparator
//    }
}