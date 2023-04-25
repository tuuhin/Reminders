package com.eva.reminders.presentation.feature_create

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.eva.reminders.presentation.feature_create.utils.TaskReminderState
import com.eva.reminders.presentation.feature_create.utils.TaskRemindersEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDateTime
import javax.inject.Inject


@HiltViewModel
class CreateTaskViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val newTaskState = MutableStateFlow(CreateTaskState())
    val task = newTaskState.asStateFlow()

    private val _reminderState = MutableStateFlow(TaskReminderState())
    val reminder = _reminderState.asStateFlow()

    fun onReminderEvents(event: TaskRemindersEvents) {
        when (event) {
            is TaskRemindersEvents.OnDateChanged -> _reminderState
                .update {
                    val isTimeInvalid =
                        LocalDateTime.now() > event.date.schedule.atTime(it.time.schedule)
                    it.copy(
                        date = event.date,
                        invalidTime = if (isTimeInvalid) "This time has already passed" else null
                    )
                }

            is TaskRemindersEvents.OnReminderChanged -> _reminderState
                .update {
                    it.copy(frequency = event.frequency)
                }

            is TaskRemindersEvents.OnTimeChanged -> _reminderState
                .update {
                    val isTimeInvalid =
                        LocalDateTime.now() > it.date.schedule.atTime(event.time.schedule)
                    it.copy(
                        time = event.time,
                        invalidTime = if (isTimeInvalid) "This time has already passed" else null
                    )
                }
        }
    }

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

            CreateTaskEvents.ReminderPicked -> newTaskState
                .update {
                    it.copy(isReminderSelected = true)
                }

            CreateTaskEvents.ReminderCanceled -> newTaskState
                .update {
                    it.copy(isReminderSelected = false)
                }

            is CreateTaskEvents.OnColorChanged -> newTaskState
                .update {
                    it.copy(color = event.taskColor)
                }

            CreateTaskEvents.OnSubmit -> onSubmit()
        }
    }

    private fun onSubmit() {

    }

}