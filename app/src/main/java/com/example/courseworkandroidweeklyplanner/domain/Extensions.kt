package com.example.courseworkandroidweeklyplanner.domain

import android.os.Bundle
import com.example.courseworkandroidweeklyplanner.domain.model.Notification
import com.example.courseworkandroidweeklyplanner.domain.model.Task
import com.example.courseworkandroidweeklyplanner.domain.model.Week
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.util.UUID

private const val milliCoefficient: Long = 1_000_000

fun LocalTime.toMilliOfDay(): Long {
    return this.toNanoOfDay() / milliCoefficient
}

fun localTimeOfMilliOfDay(millis: Long): LocalTime {
    return LocalTime.ofNanoOfDay(millis * milliCoefficient)
}

fun instantFromDateAndTime(date: LocalDate, time: LocalTime): Instant {
    return date.atTime(time).atZone(ZoneId.systemDefault()).toInstant()
}

val Task.notification: Notification?
    get() = when (time) {
        null -> null
        else -> Notification(
            taskId = id,
            scheduledTime = instantFromDateAndTime(date, time)
        )
    }

fun Task.assignedToWeek(week: Week): Boolean {
    return this.date.isAfter(week.start) || this.date.isEqual(week.start)
            || this.date.isBefore(week.end) || this.date.isEqual(week.end)
}

const val TASK_ID_KEY = "UUID"

@Throws(IllegalArgumentException::class)
fun getTaskId(arguments: Bundle?): UUID {
    val uuid = arguments?.getString(TASK_ID_KEY) ?: throw IllegalArgumentException("Null UUID!")
    return UUID.fromString(uuid)
}