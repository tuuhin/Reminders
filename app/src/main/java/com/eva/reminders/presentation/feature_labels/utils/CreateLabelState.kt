package com.eva.reminders.presentation.feature_labels.utils

/**
 * The state for the new label to be created
 * @property label text value fot the new label
 * @property isError if there is an error in evaluation
 */
data class CreateLabelState(
    val label: String = "",
    val isError: String? = null
)

/**
 * Events triggered during creation of a new label
 */
sealed interface CreateLabelEvents {

    /**
     * Send when the value of the label is changes
     */
    data class OnValueChange(val text: String) : CreateLabelEvents

    /**
     * Trigger by the user when the label is to be submitted
     */
    data object OnSubmit : CreateLabelEvents
}