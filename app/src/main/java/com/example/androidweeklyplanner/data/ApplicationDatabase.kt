package com.example.androidweeklyplanner.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.androidweeklyplanner.data.dao.TaskDao
import com.example.androidweeklyplanner.data.entity.TaskEntity

@Database(
    entities = [TaskEntity::class],
    version = 8,
    exportSchema = false
)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
