package com.eva.reminders.presentation.feature_create.utils

import com.eva.reminders.domain.enums.TaskColorEnum
import com.eva.reminders.domain.models.CreateTaskModel
import com.eva.reminders.domain.models.TaskLabelModel
import com.eva.reminders.domain.models.TaskModel
import com.eva.reminders.domain.models.TaskReminderModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

fun TaskModel.toUpdateState(): AddTaskState = AddTaskState(
    id = id,
    title = title,
    content = content,
    isPinned = pinned,
    isArchived = isArchived,
    color = color,
    reminderState = reminderAt.at?.let { dateTime ->
        TaskReminderState(
            frequency = if (reminderAt.isRepeating)
                ReminderFrequency.DAILY
            else
                ReminderFrequency.DO_NOT_REPEAT,
            time = when (dateTime.toLocalTime()) {
                LocalTime.of(6, 0) -> ReminderTimeOptions.Morning()
                LocalTime.of(15, 0) -> ReminderTimeOptions.AfterNoon()
                LocalTime.of(18, 0) -> ReminderTimeOptions.Evening()
                LocalTime.of(21, 0) -> ReminderTimeOptions.Night()
                else -> ReminderTimeOptions.Custom(dateTime.toLocalTime())
            },
            date = when (dateTime.toLocalDate()) {
                LocalDate.now() -> ReminderDateOptions.Today()
                LocalDate.now().plusDays(1) -> ReminderDateOptions.Tomorrow()
                LocalDate.now().plusWeeks(1) -> ReminderDateOptions.NextWeek()
                else -> ReminderDateOptions.Custom(dateTime.toLocalDate())
            },
            isExact = isExact
        )
    } ?: TaskReminderState(),
    isReminderPresent = reminderAt.at != null
)

fun AddTaskState.toCreateModel(labels: List<TaskLabelModel>): CreateTaskModel = CreateTaskModel(
    title = title,
    content = content,
    isPinned = isPinned,
    isArchive = isArchived,
    colorEnum = color ?: TaskColorEnum.TRANSPARENT,
    labels = labels,
    time = TaskReminderModel(
        at = if (isReminderPresent)
            reminderState.date.schedule.atTime(reminderState.time.schedule)
        else null, isRepeating = reminderState.frequency.isRepeating
    ), isExact = reminderState.isExact
)

fun AddTaskState.toUpdateModel(labels: List<TaskLabelModel>): TaskModel = TaskModel(
    id = id!!,
    title = title,
    content = content,
    pinned = isPinned,
    isArchived = isArchived,
    color = color ?: TaskColorEnum.TRANSPARENT,
    labels = labels,
    reminderAt = TaskReminderModel(
        at = if (isReminderPresent)
            reminderState.date.schedule.atTime(reminderState.time.schedule)
        else null, isRepeating = reminderState.frequency.isRepeating
    ),
    updatedAt = LocalDateTime.now(),
    isExact = reminderState.isExact
)