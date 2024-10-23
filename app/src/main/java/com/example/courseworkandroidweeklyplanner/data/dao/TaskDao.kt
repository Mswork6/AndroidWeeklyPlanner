package com.example.courseworkandroidweeklyplanner.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.courseworkandroidweeklyplanner.data.entity.TaskEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks")
    fun getTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTask(id: UUID): TaskEntity?

    @Insert(entity = TaskEntity::class, onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTask(entity: TaskEntity)

    @Update(entity = TaskEntity::class, onConflict = OnConflictStrategy.ABORT)
    suspend fun updateTask(entity: TaskEntity)

    @Delete(entity = TaskEntity::class)
    suspend fun deleteTask(entity: TaskEntity)
}
