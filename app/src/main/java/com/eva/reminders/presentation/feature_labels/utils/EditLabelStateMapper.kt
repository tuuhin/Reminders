package com.eva.reminders.presentation.feature_labels.utils

import com.eva.reminders.domain.models.TaskLabelModel

fun TaskLabelModel.toEditState(): EditLabelState =
    EditLabelState(previousLabel = label, model = this, labelId = id)

fun EditLabelState.toUpdateModel():TaskLabelModel? =  model?.copy(label = updatedLabel)