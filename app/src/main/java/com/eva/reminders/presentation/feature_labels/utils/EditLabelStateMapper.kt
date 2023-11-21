package com.eva.reminders.presentation.feature_labels.utils

import com.eva.reminders.domain.models.TaskLabelModel

/**
 * Maps a [TaskLabelModel] instance to a [LabelEditableState]
 */
fun TaskLabelModel.toEditState(): LabelEditableState =
    LabelEditableState(model = this)


/**
 * Helper function for [toEditStates] to work on [List]
 */
fun List<TaskLabelModel>.toEditStates(): List<LabelEditableState> = map { it.toEditState() }


/** Converts the [LabelEditableState] to a [TaskLabelModel] just changing the [LabelEditableState.updatedLabel]
 * to [TaskLabelModel.label] before updating
 */
fun LabelEditableState.toUpdateModel(): TaskLabelModel = model.copy(label = updatedLabel)