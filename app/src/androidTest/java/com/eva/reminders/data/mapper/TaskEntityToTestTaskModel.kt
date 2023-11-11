package com.eva.reminders.data.mapper

import com.eva.reminders.data.local.entity.TaskEntity
import com.eva.reminders.domain.models.TaskReminderModel
import com.eva.reminders.models.TestTaskModel

fun TaskEntity.toTestModel(taskId: Int? = 1): TestTaskModel = TestTaskModel(
    id = taskId ?: 1,
    title = title,
    content = content,
    pinned = pinned,
    color = color,
    reminderAt = TaskReminderModel(time, isRepeating),
    isArchived = isArchived,
    isExact = exact,
)