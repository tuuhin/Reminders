package com.eva.reminders.presentation.feature_create.utils

import com.eva.reminders.domain.enums.TaskColorEnum
import com.eva.reminders.domain.models.CreateTaskModel
import com.eva.reminders.domain.models.TaskLabelModel
import com.eva.reminders.domain.models.TaskModel
import java.time.LocalDateTime

fun TaskModel.toUpdateState(): AddTaskState = AddTaskState(
    id = id,
    title = title,
    content = content,
    isPinned = pinned,
    isArchived = isArchived,
    color = color,
    reminderState = reminderAt.toState(isExact = isExact),
    isReminderPresent = reminderAt.at != null,
    editedAt = updatedAt
)

fun AddTaskState.toCreateModel(labels: List<TaskLabelModel>): CreateTaskModel = CreateTaskModel(
    title = title,
    content = content,
    isPinned = isPinned,
    isArchive = isArchived,
    colorEnum = color ?: TaskColorEnum.TRANSPARENT,
    labels = labels,
    time = reminderState.toModel(isReminderPresent),
    isExact = reminderState.isExact
)

fun AddTaskState.toUpdateModel(labels: List<TaskLabelModel>): TaskModel = TaskModel(
    id = id!!,
    title = title,
    content = content,
    pinned = isPinned,
    isArchived = isArchived,
    color = color ?: TaskColorEnum.TRANSPARENT,
    labels = labels,
    reminderAt = reminderState.toModel(isReminderPresent),
    updatedAt = LocalDateTime.now(),
    isExact = reminderState.isExact
)