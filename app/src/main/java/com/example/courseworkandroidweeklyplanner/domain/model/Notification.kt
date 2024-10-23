package com.example.courseworkandroidweeklyplanner.domain.model

import java.time.Instant
import java.time.LocalTime
import java.util.UUID

data class Notification(
    val taskId: UUID,
    val scheduledTime: Instant
)