package com.eva.reminders.presentation.feature_create

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eva.reminders.domain.enums.TaskColorEnum
import com.eva.reminders.domain.models.TaskReminderModel
import com.eva.reminders.domain.repository.TaskLabelsRepository
import com.eva.reminders.domain.repository.TaskRepository
import com.eva.reminders.presentation.feature_create.utils.TaskReminderState
import com.eva.reminders.presentation.feature_create.utils.TaskRemindersEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class CreateTaskViewModel @Inject constructor(
    private val repo: TaskRepository,
    labelRepo: TaskLabelsRepository
) : ViewModel() {

    private val newTaskState = MutableStateFlow(CreateTaskState())
    val task = newTaskState.asStateFlow()

    private val _reminderState = MutableStateFlow(TaskReminderState())
    val reminder = _reminderState.asStateFlow()

    val addLabelViewModel = AddLabelToTasksViewModel(labelRepo)

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
        viewModelScope.launch(Dispatchers.IO) {
            val task = repo.createTask(
                title = newTaskState.value.title,
                content = newTaskState.value.content,
                colorEnum = newTaskState.value.color ?: TaskColorEnum.TRANSPARENT,
                isArchive = newTaskState.value.isArchived,
                isPinned = newTaskState.value.isPinned,
                time = if (newTaskState.value.isReminderSelected) TaskReminderModel(
                    at = reminder.value.date.schedule.atTime(reminder.value.time.schedule),
                    isRepeating = reminder.value.frequency.isRepeating
                ) else null,
                labels = addLabelViewModel.pickedLabels.map { it.toModel() }
            )
            Log.d("TASK", task.toString())
        }
    }


}