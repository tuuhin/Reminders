package com.eva.reminders.presentation.feature_create.utils

import com.eva.reminders.domain.enums.TaskColorEnum

data class AddTaskState(
    val id: Int? = null,
    val title: String = "",
    val content: String = "",
    val isPinned: Boolean = false,
    val isArchived: Boolean = false,
    val color: TaskColorEnum? = null,
    val reminderState: TaskReminderState = TaskReminderState(),
    val isReminderPresent: Boolean = false,
    val isCreate: Boolean = true
)
