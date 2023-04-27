package com.eva.reminders.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.eva.reminders.data.local.TableNames
import com.eva.reminders.domain.enums.TaskColorEnum
import java.time.LocalDateTime

@Entity(tableName = TableNames.TASK_TABLE)
data class TaskEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "TASK_ID")
    val id: Int? = null,

    @ColumnInfo(name = "TITLE")
    val title: String,

    @ColumnInfo(name = "CONTENT")
    val content: String,

    @ColumnInfo(name = "IS_PINNED")
    val pinned: Boolean = false,

    @ColumnInfo(name = "COLOR")
    val color: TaskColorEnum = TaskColorEnum.TRANSPARENT,

    @ColumnInfo(name = "TIME")
    val time: LocalDateTime? = null,

    @ColumnInfo(name = "IS_REPEATING")
    val isRepeating: Boolean = false,

    @ColumnInfo(name = "IS_ARCHIVED")
    val isArchived: Boolean = false,

    @ColumnInfo(name = "UPDATED AT")
    val updateTime: LocalDateTime = LocalDateTime.now(),

)
