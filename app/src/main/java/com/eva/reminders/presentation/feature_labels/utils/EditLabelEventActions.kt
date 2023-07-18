package com.eva.reminders.presentation.feature_labels.utils

sealed interface EditLabelEvents {
    object ShowAllLabels : EditLabelEvents
    data class ToggleEnabled(val item: EditLabelState) : EditLabelEvents
    data class OnValueChange(val text: String, val item: EditLabelState) : EditLabelEvents
}

sealed interface EditLabelsActions {
    data class OnUpdate(val item: EditLabelState) : EditLabelsActions
    data class OnDelete(val item: EditLabelState) : EditLabelsActions
}