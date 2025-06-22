package com.example.taskplanner.domain.usecase

import com.example.taskplanner.domain.MainScreenSortRepo
import com.example.taskplanner.domain.assignedToWeek
import com.example.taskplanner.domain.model.Day
import com.example.taskplanner.domain.model.Task
import com.example.taskplanner.domain.model.Week
import com.example.taskplanner.domain.provider.TaskComparatorProvider
import com.example.taskplanner.domain.repository.SortRepository
import com.example.taskplanner.domain.repository.TaskRepository
import com.example.taskplanner.domain.repository.WeekRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import java.time.LocalDate
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class GetDaysUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
    @MainScreenSortRepo private val sortRepository: SortRepository,
    private val weekRepository: WeekRepository,
    private val comparatorProvider: TaskComparatorProvider
) {
    // Получает таски с репозитория, формирует дни, потом сортирует их
    operator fun invoke(): Flow<List<Day>> =
        weekRepository.getWeek()
            .flatMapLatest { week ->
                taskRepository.getTasksForDateRange(week.start, week.end)
                    .combine(sortRepository.getSortConfig()) { tasks, sort ->
                        val comparator = comparatorProvider(sort)
                        buildDays(week, tasks).map { day ->
                            day.copy(tasks = day.tasks.sortedWith(comparator))
                        }
                    }
            }


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
}