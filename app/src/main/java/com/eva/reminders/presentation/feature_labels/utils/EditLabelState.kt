package com.eva.reminders.presentation.feature_labels.utils

import com.eva.reminders.domain.models.TaskLabelModel

data class EditLabelState(
    val labelId: Int? = null,
    val model: TaskLabelModel? = null,
    val isEdit: Boolean = false,
    val updatedLabel: String = "",
    val previousLabel: String = "",
)
