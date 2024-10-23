package com.example.courseworkandroidweeklyplanner.domain.usecase

import com.example.courseworkandroidweeklyplanner.domain.assignedToWeek
import com.example.courseworkandroidweeklyplanner.domain.model.Day
import com.example.courseworkandroidweeklyplanner.domain.model.SortType
import com.example.courseworkandroidweeklyplanner.domain.model.Task
import com.example.courseworkandroidweeklyplanner.domain.model.Week
import com.example.courseworkandroidweeklyplanner.domain.repository.SortRepository
import com.example.courseworkandroidweeklyplanner.domain.repository.TaskRepository
import com.example.courseworkandroidweeklyplanner.domain.repository.WeekRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import java.time.LocalDate
import javax.inject.Inject

class GetDaysUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
    private val sortRepository: SortRepository,
    private val weekRepository: WeekRepository,
) {
    operator fun invoke(): Flow<List<Day>> = taskRepository.getTasks()
        .combine(weekRepository.getWeek()) { tasks, week ->
            buildDays(week = week, tasks = tasks)
        }.combine(sortRepository.getSort()) { days, sort ->
            sortTasks(days = days, sort = sort)
        }.flowOn(Dispatchers.Default)

    private fun buildDays(week: Week, tasks: List<Task>): List<Day> {
        val dayToTasks: Map<LocalDate, List<Task>> = tasks
            .filter { it.assignedToWeek(week) }
            .groupBy(Task::date)
        return week.iterator().asSequence().mapIndexed { index, (date, type) ->
            Day(
                id = index,
                type = type,
                date = date,
                tasks = dayToTasks.getOrDefault(date, listOf())
            )
        }.toList()
    }

    private fun sortTasks(days: List<Day>, sort: SortType): List<Day> {
        return days.map {
            with(it) {
                copy(tasks = tasks.sort(sort))
            }
        }
    }

    private fun List<Task>.sort(sort: SortType): List<Task> {
        return when (sort) {
            SortType.INCREASE -> this.sortedBy { it.priority }
            SortType.DECREASE -> this.sortedByDescending { it.priority }
            SortType.STANDARD -> this
        }
    }
}