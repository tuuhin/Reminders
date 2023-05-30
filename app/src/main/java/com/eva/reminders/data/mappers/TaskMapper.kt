package com.eva.reminders.data.mappers

import com.eva.reminders.data.local.entity.TaskEntity
import com.eva.reminders.domain.models.CreateTaskModel
import com.eva.reminders.domain.models.TaskModel
import com.eva.reminders.domain.models.TaskReminderModel

fun TaskEntity.toModel(): TaskModel = TaskModel(
    id = id ?: 0,
    title = title,
    content = content,
    pinned = pinned,
    color = color,
    reminderAt = TaskReminderModel(time, isRepeating),
    isArchived = isArchived,
    updatedAt = updateTime,
    isExact = exact,
    labels = emptyList()
)

fun TaskModel.toEntity(): TaskEntity = TaskEntity(
    id = id,
    title = title,
    content = content,
    pinned = pinned,
    color = color,
    time = reminderAt.at,
    isArchived = isArchived,
    isRepeating = reminderAt.isRepeating,
    exact = isExact
)

fun CreateTaskModel.toEntity(): TaskEntity = TaskEntity(
    title = title,
    content = content,
    pinned = isPinned,
    color = colorEnum,
    time = time?.at,
    isArchived = isArchive,
    isRepeating = time?.isRepeating ?: false,
    exact = isExact
)