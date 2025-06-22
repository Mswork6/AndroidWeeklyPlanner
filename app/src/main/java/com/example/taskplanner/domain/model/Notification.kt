package com.example.taskplanner.domain.model

import java.time.LocalDateTime
import java.util.UUID

data class Notification(
    val taskId: UUID,
    val scheduledTime: LocalDateTime
)