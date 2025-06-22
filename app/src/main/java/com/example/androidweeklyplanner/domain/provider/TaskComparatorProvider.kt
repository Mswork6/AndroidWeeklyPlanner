package com.example.androidweeklyplanner.domain.provider

import com.example.androidweeklyplanner.domain.model.SortConfig
import com.example.androidweeklyplanner.domain.model.SortType
import com.example.androidweeklyplanner.domain.model.Task
import javax.inject.Inject

class TaskComparatorProvider @Inject constructor() {
    operator fun invoke(config: SortConfig) = buildComparator(config)


    private fun buildComparator(config: SortConfig): Comparator<Task> {
        // базовый компаратор по времени
        var comparator: Comparator<Task> = compareBy<Task> { it.date }
            .thenBy { it.time }

        // вторичный ключ — сложность
        when (config.difficultyOrder) {
            SortType.INCREASE -> comparator = compareBy<Task> { it.difficulty }.then(comparator)
            SortType.DECREASE -> comparator =
                compareByDescending<Task> { it.difficulty }.then(comparator)

            else -> {}
        }

        // первичный ключ — приоритет
        when (config.priorityOrder) {
            SortType.INCREASE -> comparator = compareBy<Task> { it.priority }.then(comparator)
            SortType.DECREASE -> comparator =
                compareByDescending<Task> { it.priority }.then(comparator)

            else -> {}
        }

        return comparator
    }
}