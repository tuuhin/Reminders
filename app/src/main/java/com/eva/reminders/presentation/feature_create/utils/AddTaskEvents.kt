package com.eva.reminders.presentation.feature_create.utils

import com.eva.reminders.domain.enums.TaskColorEnum

sealed interface AddTaskEvents {
    data class OnTitleChange(val title: String) : AddTaskEvents
    data class OnContentChange(val content: String) : AddTaskEvents
    data class OnColorChanged(val taskColor: TaskColorEnum) : AddTaskEvents
    object TogglePinned : AddTaskEvents
    object ToggleArchive : AddTaskEvents
    object ReminderStatePicked : AddTaskEvents
    object ReminderStateUnpicked : AddTaskEvents
    data class OnReminderEvent(val event: TaskRemindersEvents) : AddTaskEvents
    object OnSubmit : AddTaskEvents
    object OnDelete : AddTaskEvents
    object MakeCopy : AddTaskEvents
}