package com.eva.reminders.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.eva.reminders.data.local.TableNames
import com.eva.reminders.domain.enums.TaskColorEnum
import java.time.LocalDateTime

@Entity(tableName = TableNames.TASK_TABLE)
data class TaskEntity(

    @PrimaryKey
    @ColumnInfo(name = "TASK_ID")
    val id: Int? = null,

    @ColumnInfo(name = "TITLE")
    val title: String,

    @ColumnInfo(name = "CONTENT")
    val content: String,

    @ColumnInfo(name = "IS_PINNED", index = true)
    val pinned: Boolean = false,

    @ColumnInfo(name = "COLOR")
    val color: TaskColorEnum? ,

    @ColumnInfo(name = "TIME")
    val time: LocalDateTime? ,

    @ColumnInfo(name = "IS_ARCHIVED", index = true)
    val isArchived: Boolean,

    @ColumnInfo(name = "CREATED_AT")
    val createTime: LocalDateTime,

    @ColumnInfo(name = "UPDATED AT")
    val updateTime: LocalDateTime,

    @ColumnInfo(name = "IS_TRASHED")
    val trashed: Boolean = false
)
