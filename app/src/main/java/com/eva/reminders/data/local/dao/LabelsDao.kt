package com.eva.reminders.data.local.dao

import androidx.annotation.VisibleForTesting
import androidx.room.*
import com.eva.reminders.data.local.TableNames
import com.eva.reminders.data.local.entity.LabelEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LabelsDao {


    @Upsert
    suspend fun insertUpdateLabel(labelEntity: LabelEntity): Long

    @Insert
    suspend fun insertMultipleLabels(labels: List<LabelEntity>): List<Long>

    @Query("SELECT * FROM ${TableNames.TASK_LABEL_TABLE} WHERE LABEL_ID=:id")
    suspend fun getLabelFromId(id: Long): LabelEntity?

    @Query("SELECT * FROM ${TableNames.TASK_LABEL_TABLE}")
    fun getAllLabels(): Flow<List<LabelEntity>>

    @Query("SELECT * FROM ${TableNames.TASK_LABEL_TABLE}")
    suspend fun getAllLabelsAsList(): List<LabelEntity>

    @Delete
    suspend fun deleteLabel(labelEntity: LabelEntity)

    @Delete
    suspend fun deleteMultiples(labels: List<LabelEntity>)

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    @Query("SELECT COUNT(*) FROM ${TableNames.TASK_LABEL_TABLE}")
    suspend fun totalNumberOfLabels(): Int

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    @Query("SELECT * FROM ${TableNames.TASK_LABEL_TABLE} WHERE LABEL_ID in (:ids)")
    suspend fun getLabelFromIds(ids: List<Long>): List<LabelEntity>

}