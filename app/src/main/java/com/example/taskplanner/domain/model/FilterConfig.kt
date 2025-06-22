package com.example.taskplanner.domain.model

import java.time.LocalDate

data class FilterConfig(
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val priorityFilter: Set<Priority> = emptySet(),
    val difficultyFilter: Set<Difficulty> = emptySet(),
    val categoryFilter: Set<Category> = emptySet()
)
