package com.eva.reminders.presentation.feature_create

data class CreateTaskState(
    val title: String = "",
    val content: String = "",
    val isPinned: Boolean = false,
    val isReminder: Boolean = false,
    val isArchived: Boolean = false,
)

sealed class CreateTaskEvents {
    data class OnTitleChange(val title: String) : CreateTaskEvents()
    data class OnContentChange(val content: String) : CreateTaskEvents()
    object TogglePinned : CreateTaskEvents()
    object ToggleArchive : CreateTaskEvents()
    object ToggleReminder :CreateTaskEvents()
}