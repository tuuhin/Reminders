package com.eva.reminders.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import com.eva.reminders.data.local.TableNames

@Entity(tableName = TableNames.TASK_LABEL_FTS_TABLE)
@Fts4(contentEntity = LabelEntity::class)
data class LabelFtsEntity(

    @ColumnInfo(name = "LABEL")
    val label: String,

    @ColumnInfo(name = "LABEL_ID")
    val id: Int
)
