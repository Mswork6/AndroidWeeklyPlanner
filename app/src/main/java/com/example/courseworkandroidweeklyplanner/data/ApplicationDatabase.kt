package com.example.courseworkandroidweeklyplanner.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.courseworkandroidweeklyplanner.data.dao.TaskDao
import com.example.courseworkandroidweeklyplanner.data.entity.TaskEntity

@Database(
    entities = [TaskEntity::class],
    version = 6,
    exportSchema = false
)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
