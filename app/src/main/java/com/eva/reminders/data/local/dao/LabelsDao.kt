package com.eva.reminders.data.local.dao

import androidx.room.*
import com.eva.reminders.data.local.TableNames
import com.eva.reminders.data.local.entity.LabelEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LabelsDao {

    @Query("SELECT * FROM ${TableNames.TASK_LABEL_TABLE}")
    fun getAllLabels(): Flow<List<LabelEntity>>

    @Upsert
    suspend fun insertUpdateLabel(labelEntity: LabelEntity): Long

    @Delete
    suspend fun deleteLabel(labelEntity: LabelEntity)
}