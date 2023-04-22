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
    suspend fun insertTask(task: TaskEntity)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateTask(task: TaskEntity)

    @Query("SELECT * FROM ${TableNames.TASK_TABLE}")
    fun getTasks(): List<TaskEntity>

    @Transaction
    @Query("SELECT * FROM ${TableNames.TASK_TABLE}")
    fun getAllTasksWithLabels(): Flow<List<TaskWithLabelRelation>>

    @Transaction
    @Query("SELECT * FROM ${TableNames.TASK_LABEL_TABLE}")
    fun getAllLabelsWithTasks(): Flow<List<LabelWithTaskRelation>>

    @Delete
    suspend fun deleteTask(task: TaskEntity)
}