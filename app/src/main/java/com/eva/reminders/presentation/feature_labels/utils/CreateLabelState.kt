package com.eva.reminders.presentation.feature_labels.utils

data class CreateLabelState(
    val isEnabled: Boolean = false,
    val label: String = "",
    val isError: String? = null
)

sealed class CreateLabelEvents {
    object ToggleEnabled : CreateLabelEvents()
    data class OnValueChange(val text: String) : CreateLabelEvents()
    object OnSubmit : CreateLabelEvents()
}