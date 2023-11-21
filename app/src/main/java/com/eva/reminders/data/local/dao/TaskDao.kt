package com.eva.reminders.data.local.dao

import androidx.annotation.VisibleForTesting
import androidx.room.*
import com.eva.reminders.data.local.TableNames
import com.eva.reminders.data.local.entity.TaskEntity

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMultipleTasks(tasks: List<TaskEntity>)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateTask(task: TaskEntity)

    @Query("SELECT * FROM ${TableNames.TASK_TABLE}")
    suspend fun getTasks(): List<TaskEntity>

    @Query("SELECT * FROM ${TableNames.TASK_TABLE} WHERE TIME IS NOT NULL")
    suspend fun getTasksWithReminderTime(): List<TaskEntity>

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Delete
    suspend fun deleteMultipleTasks(tasks: List<TaskEntity>)

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    @Query("SELECT * FROM ${TableNames.TASK_TABLE} WHERE TASK_ID=:taskId")
    suspend fun getTaskWithId(taskId: Long): TaskEntity?

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    @Query("SELECT COUNT(*) FROM ${TableNames.TASK_TABLE}")
    suspend fun tasksCount(): Long
}