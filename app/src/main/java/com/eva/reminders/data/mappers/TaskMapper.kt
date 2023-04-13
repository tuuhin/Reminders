package com.eva.reminders.data.mappers

import com.eva.reminders.data.local.entity.TaskEntity
import com.eva.reminders.domain.enums.TaskColorEnum
import com.eva.reminders.domain.models.TaskModel

fun TaskEntity.toModel(): TaskModel = TaskModel(
    id = id ?: 0,
    title = title,
    content = content,
    pinned = pinned,
    color = color ?: TaskColorEnum.BLUE,
    time = time,
    isArchived = isArchived
)
