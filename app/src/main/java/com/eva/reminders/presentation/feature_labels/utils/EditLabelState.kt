package com.eva.reminders.presentation.feature_labels.utils

import com.eva.reminders.domain.models.TaskLabelModel

data class EditLabelState(
    val model: TaskLabelModel?= null,
    val isEdit: Boolean = false,
    val text: String = "",
    val prevText: String = ""
) {
    companion object {
        fun fromLabel(label: TaskLabelModel): EditLabelState {
            return EditLabelState(prevText = label.label, model = label)
        }
    }

    fun toModel(): TaskLabelModel? {
        return model?.copy(label = text)
    }
}

sealed class EditLabelEvents {
    data class ToggleEnabled(val item: EditLabelState) : EditLabelEvents()
    data class OnValueChange(val text: String, val item: EditLabelState) : EditLabelEvents()
    data class OnUpdate(val item: EditLabelState) : EditLabelEvents()
    data class OnDelete(val item: EditLabelState) : EditLabelEvents()
}