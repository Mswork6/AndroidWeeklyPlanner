package com.example.androidweeklyplanner.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.androidweeklyplanner.data.entity.TaskEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.util.UUID

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks")
    fun getTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTask(id: UUID): TaskEntity?

    @Query("""
      SELECT * 
      FROM tasks 
      WHERE deadline_day BETWEEN :startDate AND :endDate
    """)
    fun getTasksForDateRange(
        startDate: Long,
        endDate: Long
    ): Flow<List<TaskEntity>>

    @Query(
        """
  SELECT * FROM tasks
  WHERE deadline_day = :date
  """
    )
    fun getTasksByDate(date: Long): Flow<List<TaskEntity>>

    @Insert(entity = TaskEntity::class, onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTask(entity: TaskEntity)

    @Update(entity = TaskEntity::class, onConflict = OnConflictStrategy.ABORT)
    suspend fun updateTask(entity: TaskEntity)

    @Delete(entity = TaskEntity::class)
    suspend fun deleteTask(entity: TaskEntity)

    @Query("SELECT COUNT(*) FROM tasks WHERE deadline_day = :date " +
            "AND difficulty = :difficulty " +
            "AND (:excludeID IS NULL OR id != :excludeID)")
    suspend fun countTasksByDateAndDifficulty(
        date: Long,
        difficulty: String,
        excludeID: UUID?): Int
}
