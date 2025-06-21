package com.example.androidweeklyplanner.domain.usecase

import com.example.androidweeklyplanner.domain.model.FilterConfig
import com.example.androidweeklyplanner.domain.model.Task
import javax.inject.Inject

class FilterTasksUseCase @Inject constructor() {
    operator fun invoke(tasks: List<Task>, cfg: FilterConfig): List<Task> =
        tasks.filter { task ->
            // Диапазон дат
            (cfg.startDate == null || !task.date.isBefore(cfg.startDate)) &&
                    (cfg.endDate   == null || !task.date.isAfter(cfg.endDate))  &&
                    // Приоритет
                    (cfg.priorityFilter.isEmpty()  || task.priority  in cfg.priorityFilter)  &&
                    // Сложность
                    (cfg.difficultyFilter.isEmpty()|| task.difficulty in cfg.difficultyFilter)&&
                    // Категории
                    (cfg.categoryFilter.isEmpty()  || task.category   in cfg.categoryFilter)
        }
}