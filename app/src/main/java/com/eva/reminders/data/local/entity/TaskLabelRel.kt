package com.eva.reminders.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.eva.reminders.data.local.TableNames

@Entity(
    primaryKeys = ["TASK_ID", "LABEL_ID"],
    tableName = TableNames.TASK_LABEL_REL,
    foreignKeys = [
        ForeignKey(
            TaskEntity::class,
            childColumns = arrayOf("TASK_ID"),
            parentColumns = arrayOf("TASK_ID"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
        ForeignKey(
            LabelEntity::class,
            childColumns = arrayOf("LABEL_ID"),
            parentColumns = arrayOf("LABEL_ID"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        )
    ]
)
data class TaskLabelRel(

    @ColumnInfo(name = "TASK_ID", index = true)
    val taskId: Int,

    @ColumnInfo(name = "LABEL_ID", index = true)
    val labelId: Int,
)
