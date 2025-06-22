package com.example.taskplanner.domain.model

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
    val category: Category,
    val date: LocalDate,
    val time: LocalTime,
    val notificationTimeOffset: Long?,
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

enum class Category(val emoji: String) {
    WORK("\uD83D\uDCBC"),
    STUDY("\uD83D\uDCDA"),
    SPORT("\uD83C\uDFC3"),
    HOUSEHOLD_CHORES("\uD83C\uDFE0"),
    VACATION("\uD83C\uDF34"),
    NONE(" ")
}


enum class NotificationTime(val offsetMinutes: Long?) {
    MINUTES_120_BEFORE(-120),
    MINUTES_90_BEFORE(-90),
    MINUTES_60_BEFORE(-60),
    MINUTES_30_BEFORE(-30),
    MINUTES_15_BEFORE(-15),
    TASK_TIME(0),
    NONE(null)
}



data class TaskSchema(
    val id: UUID? = null,
    val name: String = "",
    val description: String? = null,
    val priority: Priority = Priority.BASIC,
    val difficulty: Difficulty = Difficulty.MEDIUM,
    val category: Category = Category.NONE,
    val day: LocalDate = if (LocalDateTime.now().plusHours(1).toLocalDate()
            .equals(LocalDate.now())) LocalDate.now() else LocalDate.now().plusDays(1),
    val time: LocalTime = LocalTime.now().plusHours(1),
    val notificationTimeOffset: Long? = null,
    val isDone: Boolean? = null,
)
