package com.eva.reminders.data.mappers

import com.eva.reminders.data.local.relations.TaskWithLabelRelation
import com.eva.reminders.domain.models.TaskModel
import com.eva.reminders.domain.models.TaskReminderModel

fun TaskWithLabelRelation.toModel(): TaskModel = TaskModel(
    id = task.id ?: 0,
    title = task.title,
    content = task.content,
    pinned = task.pinned,
    color = task.color ,
    reminderAt = TaskReminderModel(task.time,task.isRepeating),
    isArchived = task.isArchived,
    updatedAt = task.updateTime,
    labels = labels.map { it.toModel() }
)