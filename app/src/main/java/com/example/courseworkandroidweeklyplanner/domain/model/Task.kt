package com.example.courseworkandroidweeklyplanner.domain.model

import androidx.compose.runtime.Immutable
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID

@Immutable
data class Task(
    val id: UUID,
    val name: String,
    val description: String?,
    val priority: Priority,
    val difficulty: Difficulty,
    val date: LocalDate,
    val time: LocalTime,
    val isDone: Boolean,
)

enum class Priority {
    HIGH,
    BASIC,
    LOW
}

enum class Difficulty {
    HARD,
    MEDIUM,
    EASY
}

data class TaskSchema(
    val id: UUID? = null,
    val name: String = "",
    val description: String? = null,
    val priority: Priority = Priority.BASIC,
    val difficulty: Difficulty = Difficulty.MEDIUM,
    val day: LocalDate = if (LocalDateTime.now().plusHours(1).toLocalDate()
            .equals(LocalDate.now())) LocalDate.now() else LocalDate.now().plusDays(1),
    val time: LocalTime = LocalTime.now().plusHours(1),
    val isDone: Boolean? = null,
)
