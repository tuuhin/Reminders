package com.eva.reminders.domain.models

import com.eva.reminders.domain.enums.TaskColorEnum
import java.time.LocalDateTime

data class TaskModel(
    val id: Int,
    val title: String,
    val content: String,
    val pinned: Boolean,
    val color: TaskColorEnum,
    val reminderAt: TaskReminderModel,
    val isArchived: Boolean,
    val updatedAt: LocalDateTime,
    val labels: List<TaskLabelModel>,
    val isExact:Boolean ,
)
