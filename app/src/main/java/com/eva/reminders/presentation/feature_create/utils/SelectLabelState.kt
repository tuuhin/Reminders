package com.eva.reminders.presentation.feature_create.utils

import com.eva.reminders.domain.models.TaskLabelModel

data class SelectLabelState(
    val model: TaskLabelModel,
    val isSelected: Boolean = false
)

fun List<TaskLabelModel>.toSelectLabelStates(): List<SelectLabelState> =
    map { it.toSelectLabelState() }

fun TaskLabelModel.toSelectLabelState(selected: Boolean = false): SelectLabelState =
    SelectLabelState(
        model = this,
        isSelected = selected

    )