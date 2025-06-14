package com.example.androidweeklyplanner.domain.usecase

import com.example.androidweeklyplanner.domain.model.FilterConfig
import com.example.androidweeklyplanner.domain.model.SortConfig
import com.example.androidweeklyplanner.domain.model.SortType
import com.example.androidweeklyplanner.domain.model.Task
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
    private val sortRepository: SortRepository,
) {
    operator fun invoke(): Flow<List<Task>> =
        taskRepository.getTasks()
            .combine(filterRepository.getFilterConfig()) { tasks, filter ->
                applyFilter(tasks, filter)
            }
            .combine(sortRepository.getSortConfig()) { tasks, sort ->
                applySort(tasks, sort)
            }
            .flowOn(Dispatchers.Default)


    private fun applyFilter(tasks: List<Task>, cfg: FilterConfig): List<Task> {
        return tasks.filter { task ->
            // 2.1 Диапазон дат
            (cfg.startDate == null || !task.date.isBefore(cfg.startDate)) &&
                    (cfg.endDate == null || !task.date.isAfter(cfg.endDate)) &&
                    // 2.2 Приоритет
                    (cfg.priorityFilter.isEmpty() || cfg.priorityFilter.contains(task.priority)) &&
                    // 2.3 Сложность
                    (cfg.difficultyFilter.isEmpty() || cfg.difficultyFilter.contains(task.difficulty)) &&
                    // 2.4 Категории
                    (cfg.categoryFilter.isEmpty() || cfg.categoryFilter.contains(task.category))
        }
    }

    private fun applySort(tasks: List<Task>, cfg: SortConfig): List<Task> {
        // Строим компаратор так же, как в GetDaysUseCase
        val comparator = buildComparator(cfg)
        return tasks.sortedWith(comparator)
    }

    private fun buildComparator(cfg: SortConfig): Comparator<Task> {
        var comparator: Comparator<Task> = compareBy { it.time }

        // Вторичный ключ — сложность
        when (cfg.difficultyOrder) {
            SortType.INCREASE -> comparator = compareBy<Task> { it.difficulty }.then(comparator)
            SortType.DECREASE -> comparator = compareByDescending<Task> { it.difficulty }.then(comparator)
            else -> { }
        }

        // Первичный ключ — приоритет
        when (cfg.priorityOrder) {
            SortType.INCREASE -> comparator = compareBy<Task> { it.priority }.then(comparator)
            SortType.DECREASE -> comparator = compareByDescending<Task> { it.priority }.then(comparator)
            else -> { }
        }

        return comparator
    }
}