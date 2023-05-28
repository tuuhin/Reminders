package com.eva.reminders.domain.models

import com.eva.reminders.domain.enums.TaskColorEnum

data class CreateTaskModel(
    val title: String,
    val content: String,
    val isPinned: Boolean,
    val isArchive: Boolean,
    val time: TaskReminderModel?,
    val labels: List<TaskLabelModel>,
    val colorEnum: TaskColorEnum,
    val isExact: Boolean,
)
