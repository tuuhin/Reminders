package com.eva.reminders.presentation.feature_create.utils

import com.eva.reminders.domain.models.TaskLabelModel

data class SelectLabelState(
    val idx: Int,
    val label: String,
    val isSelected: Boolean = false
) {
    fun toModel(): TaskLabelModel = TaskLabelModel(idx, label)
}
