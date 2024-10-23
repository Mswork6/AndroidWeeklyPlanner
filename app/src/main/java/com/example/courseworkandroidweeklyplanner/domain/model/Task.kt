package com.example.courseworkandroidweeklyplanner.domain.model

import androidx.compose.runtime.Immutable
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

@Immutable
data class Task(
    val id: UUID,
    val name: String,
    val description: String?,
    val priority: Priority,
    val date: LocalDate,
    val time: LocalTime?,
    val isDone: Boolean,
)

enum class Priority {
    HIGH,
    BASIC,
    LOW
}

data class TaskSchema(
    val id: UUID? = null,
    val name: String = "",
    val description: String? = null,
    val priority: Priority = Priority.BASIC,
    val day: LocalDate = LocalDate.now(),
    val time: LocalTime? = null,
    val isDone: Boolean? = null,
)
