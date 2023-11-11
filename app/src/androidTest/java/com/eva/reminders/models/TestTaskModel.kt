package com.eva.reminders.models

import com.eva.reminders.domain.enums.TaskColorEnum
import com.eva.reminders.domain.models.TaskReminderModel

data class TestTaskModel(
    val id: Int,
    val title: String,
    val content: String,
    val pinned: Boolean,
    val color: TaskColorEnum,
    val reminderAt: TaskReminderModel,
    val isArchived: Boolean,
    val isExact:Boolean,
)
