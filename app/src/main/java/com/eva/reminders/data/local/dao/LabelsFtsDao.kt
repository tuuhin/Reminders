package com.eva.reminders.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.eva.reminders.data.local.TableNames
import com.eva.reminders.data.local.entity.LabelFtsEntity

@Dao
interface LabelsFtsDao {

    @Query("SELECT * FROM ${TableNames.TASK_LABEL_FTS_TABLE} WHERE LABEL_FTS MATCH :query")
    suspend fun search(query: String): List<LabelFtsEntity>

    @Query("SELECT * FROM ${TableNames.TASK_LABEL_FTS_TABLE}")
    suspend fun all(): List<LabelFtsEntity>
}