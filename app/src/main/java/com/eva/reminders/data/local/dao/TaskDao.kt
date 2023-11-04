package com.eva.reminders.data.local.dao

import androidx.room.*
import com.eva.reminders.data.local.TableNames
import com.eva.reminders.data.local.entity.TaskEntity
import com.eva.reminders.data.local.relations.LabelWithTaskRelation
import com.eva.reminders.data.local.relations.TaskWithLabelRelation
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity): Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateTask(task: TaskEntity)

    @Query("SELECT * FROM ${TableNames.TASK_TABLE}")
    suspend fun getTasks(): List<TaskEntity>

    @Query("SELECT * FROM ${TableNames.TASK_TABLE} WHERE TIME IS NOT NULL")
    suspend fun getTasksWithReminderTime(): List<TaskEntity>

    @Transaction
    @Query("SELECT * FROM ${TableNames.TASK_TABLE} ORDER BY TASK_ID DESC")
    fun getAllTasksWithLabels(): Flow<List<TaskWithLabelRelation>>

    @Transaction
    @Query("SELECT * FROM ${TableNames.TASK_TABLE} WHERE TASK_ID=:taskId")
    suspend fun getTaskWithLabels(taskId: Int): TaskWithLabelRelation

    @Transaction
    @Query("SELECT * FROM ${TableNames.TASK_LABEL_TABLE}")
    fun getAllLabelsWithTasks(): Flow<List<LabelWithTaskRelation>>

    @Delete
    suspend fun deleteTask(task: TaskEntity)
}