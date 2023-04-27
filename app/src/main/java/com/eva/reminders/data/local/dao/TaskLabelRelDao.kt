package com.eva.reminders.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.eva.reminders.data.local.TableNames
import com.eva.reminders.data.local.entity.TaskLabelRel

@Dao
interface TaskLabelRelDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTaskLabelsRel(rel: List<TaskLabelRel>)

    @Delete
    suspend fun deleteTaskLabelRel(rel: List<TaskLabelRel>)

    @Query("DELETE FROM ${TableNames.TASK_LABEL_REL} WHERE TASK_ID=:taskId ")
    suspend fun deleteAllRelatedLabels(taskId: Int)
}
