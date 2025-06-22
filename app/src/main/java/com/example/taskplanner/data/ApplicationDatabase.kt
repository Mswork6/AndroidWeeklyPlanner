package com.example.taskplanner.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.taskplanner.data.dao.TaskDao
import com.example.taskplanner.data.entity.TaskEntity

@Database(
    entities = [TaskEntity::class],
    version = 8,
    exportSchema = false
)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
