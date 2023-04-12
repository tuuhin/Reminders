package com.eva.reminders.presentation.feature_create

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CreateTaskViewModel : ViewModel() {

    private val newTaskState = MutableStateFlow(CreateTaskState())
    val task = newTaskState.asStateFlow()

    fun onCreateTaskEvent(event: CreateTaskEvents) {
        when (event) {
            is CreateTaskEvents.OnContentChange -> newTaskState
                .update {
                    it.copy(content = event.content)
                }
            is CreateTaskEvents.OnTitleChange -> newTaskState
                .update {
                    it.copy(title = event.title)
                }
            CreateTaskEvents.ToggleArchive -> newTaskState
                .update {
                    it.copy(isArchived = !it.isArchived)
                }
            CreateTaskEvents.TogglePinned -> newTaskState
                .update {
                    it.copy(isPinned = !it.isPinned)
                }
            CreateTaskEvents.ToggleReminder -> newTaskState
                .update {
                    it.copy(isReminder = !it.isReminder)
                }
        }
    }

}