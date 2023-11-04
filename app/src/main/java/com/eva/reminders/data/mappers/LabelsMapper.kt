package com.eva.reminders.data.mappers

import com.eva.reminders.data.local.entity.LabelEntity
import com.eva.reminders.domain.models.TaskLabelModel

fun LabelEntity.toModel(): TaskLabelModel =
    TaskLabelModel(id = id ?: 0, label = label)

fun List<LabelEntity>.toModels() = map { it.toModel() }

fun TaskLabelModel.toEntity(): LabelEntity = LabelEntity(id, label)