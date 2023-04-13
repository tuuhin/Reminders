package com.eva.reminders.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.eva.reminders.data.local.TableNames

@Entity(tableName = TableNames.TASK_LABEL_TABLE)
data class LabelEntity(

    @PrimaryKey
    @ColumnInfo(name = "LABEL_ID")
    val id: Int? = null,

    @ColumnInfo(name = "LABEL")
    val label: String
)
