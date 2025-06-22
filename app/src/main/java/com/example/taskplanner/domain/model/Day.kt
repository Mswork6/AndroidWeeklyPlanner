package com.example.taskplanner.domain.model

import androidx.compose.runtime.Immutable
import java.time.LocalDate

@Immutable
data class Day(
    val id: Int,
    val type: DayType,
    val date: LocalDate,
    val tasks: List<Task>
)
