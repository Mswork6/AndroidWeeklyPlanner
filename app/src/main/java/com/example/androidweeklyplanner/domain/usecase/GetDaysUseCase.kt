package com.example.androidweeklyplanner.domain.usecase

import com.example.androidweeklyplanner.domain.MainScreenSortRepo
import com.example.androidweeklyplanner.domain.assignedToWeek
import com.example.androidweeklyplanner.domain.model.Day
import com.example.androidweeklyplanner.domain.model.Task
import com.example.androidweeklyplanner.domain.model.Week
import com.example.androidweeklyplanner.domain.provider.TaskComparatorProvider
import com.example.androidweeklyplanner.domain.repository.SortRepository
import com.example.androidweeklyplanner.domain.repository.TaskRepository
import com.example.androidweeklyplanner.domain.repository.WeekRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import java.time.LocalDate
import javax.inject.Inject

class GetDaysUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
    @MainScreenSortRepo private val sortRepository: SortRepository,
    private val weekRepository: WeekRepository,
    private val comparatorProvider: TaskComparatorProvider
) {
    // Получает таски с репозитория, формирует дни, потом сортирует их
    operator fun invoke(): Flow<List<Day>> =
        combine(
            taskRepository.getTasks(),
            weekRepository.getWeek(),
            sortRepository.getSortConfig()
        ) { tasks, week, config ->
            val comparator = comparatorProvider(config)
            buildDays(week, tasks)
                .map { day ->
                    day.copy(tasks = day.tasks.sortedWith(comparator))
                }
        }
            .flowOn(Dispatchers.Default)

    private fun buildDays(week: Week, tasks: List<Task>): List<Day> {
        val dayToTasks: Map<LocalDate, List<Task>> = tasks
            .filter { it.assignedToWeek(week) }
            .groupBy(Task::date)

        return week.iterator().asSequence().mapIndexed { index, (date, type) ->
            Day(
                id = index,
                type = type,
                date = date,
                tasks = dayToTasks.getOrDefault(date, emptyList())
            )
        }.toList()
    }

//    private fun createComparator(config: SortConfig): Comparator<Task> {
//        // базовый компаратор по времени
//        var comparator: Comparator<Task> = compareBy { it.time }
//
//        // вторичный ключ — сложность
//        when (config.difficultyOrder) {
//            SortType.INCREASE -> comparator = compareBy<Task> { it.difficulty }.then(comparator)
//            SortType.DECREASE -> comparator = compareByDescending<Task> { it.difficulty }.then(comparator)
//            else -> { /* ничего не делаем */ }
//        }
//
//        // первичный ключ — приоритет
//        when (config.priorityOrder) {
//            SortType.INCREASE -> comparator = compareBy<Task> { it.priority }.then(comparator)
//            SortType.DECREASE -> comparator = compareByDescending<Task> { it.priority }.then(comparator)
//            else -> { /* ничего не делаем */ }
//        }
//
//        return comparator
//    }
}