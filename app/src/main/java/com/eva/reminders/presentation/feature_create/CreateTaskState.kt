package com.eva.reminders.presentation.feature_create

import com.eva.reminders.domain.enums.TaskColorEnum

data class CreateTaskState(
    val title: String = "",
    val content: String = "",
    val isPinned: Boolean = false,
    val isReminder: Boolean = true,
    val isArchived: Boolean = false,
    val color: TaskColorEnum? = null,
    val showReminderPicker: Boolean = false
)


sealed class CreateTaskEvents {
    data class OnTitleChange(val title: String) : CreateTaskEvents()
    data class OnContentChange(val content: String) : CreateTaskEvents()
    data class OnColorChanged(val taskColor: TaskColorEnum) : CreateTaskEvents()
    object TogglePinned : CreateTaskEvents()
    object ToggleArchive : CreateTaskEvents()
    object ToggleReminder : CreateTaskEvents()
    object OnSubmit : CreateTaskEvents()
}