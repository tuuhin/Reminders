package com.eva.reminders.data.mappers

import com.eva.reminders.data.local.entity.TaskEntity
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
)

fun TaskModel.toEntity(): TaskEntity = TaskEntity(
    id = id,
    title = title,
    content = content,
    pinned = pinned,
    color = color,
    time = reminderAt.at,
    isArchived = isArchived,
    updateTime = updatedAt,
    isRepeating = reminderAt.isRepeating
)

