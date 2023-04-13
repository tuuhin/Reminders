package com.eva.reminders.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.eva.reminders.data.local.TableNames

@Entity(
    primaryKeys = ["TASK_ID", "LABEL_ID"],
    tableName = TableNames.TASK_LABEL_REL
)
data class TaskLabelRel(

    @ColumnInfo(name = "TASK_ID", index = true)
    val taskId: Int,

    @ColumnInfo(name = "LABEL_ID", index = true)
    val labelId: Int,
)
