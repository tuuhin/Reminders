package com.eva.reminders.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.eva.reminders.data.local.TableNames
import com.eva.reminders.data.local.entity.LabelFtsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LabelsFtsDao {
    @Query("SELECT * FROM ${TableNames.TASK_LABEL_FTS_TABLE} WHERE LABEL_FTS MATCH :query")
    fun search(query: String): Flow<List<LabelFtsEntity>>

    @Query("SELECT * FROM ${TableNames.TASK_LABEL_FTS_TABLE}")
    fun all(): Flow<List<LabelFtsEntity>>
}