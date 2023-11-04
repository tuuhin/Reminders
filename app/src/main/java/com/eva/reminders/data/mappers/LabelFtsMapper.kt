package com.eva.reminders.data.mappers

import com.eva.reminders.data.local.entity.LabelFtsEntity
import com.eva.reminders.domain.models.TaskLabelModel

fun LabelFtsEntity.toModel(): TaskLabelModel = TaskLabelModel(id, label)

fun List<LabelFtsEntity>.toModels(): List<TaskLabelModel> = map { it.toModel() }
