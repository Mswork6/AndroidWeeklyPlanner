package com.example.courseworkandroidweeklyplanner.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: UUID,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "description")
    val description: String?,

    @ColumnInfo(name = "deadline_day")
    val date: Long,

    @ColumnInfo(name = "deadline_time")
    val time: Long,

    @ColumnInfo(name = "priority")
    val priority: String,

    @ColumnInfo(name = "difficulty")
    val difficulty: String,

    @ColumnInfo(name = "category")
    val category: String,

    @ColumnInfo(name = "notification_time")
    val notificationTime: String,

    @ColumnInfo(name = "is_done")
    val isDone: Boolean
)
