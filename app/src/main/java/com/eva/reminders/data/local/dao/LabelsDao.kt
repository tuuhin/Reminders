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

    @Query("SELECT * FROM ${TableNames.TASK_LABEL_TABLE} WHERE LABEL_ID=:id")
    fun getLabelFromId(id: Long): LabelEntity

    @Delete
    suspend fun deleteLabel(labelEntity: LabelEntity)
}