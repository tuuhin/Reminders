package com.eva.reminders.presentation.feature_labels.utils

import com.eva.reminders.domain.models.TaskLabelModel

/**
 * Label Edit State a state field for showing and updating a label field
 * @param model The associated [TaskLabelModel]
 * @param isEdit Shows if the mode is in EditMode
 * @param updatedLabel The updated Label that is stored temporarily
 * @property labelId The label id of the [model]
 * @property previousLabel The saved label for [model]
 */
data class LabelEditableState(
    val model: TaskLabelModel? = null,
    val isEdit: Boolean = false,
    val updatedLabel: String = "",
) {
    val labelId: Int?
        get() = model?.id

    val previousLabel: String
        get() = model?.label ?: ""
}


/**
 * Events triggered during [LabelEditableState] is edited
 */
sealed interface EditLabelEvents {

    /**
     * Events to show all the labels,triggers mainly after a [EditLabelsActions] is made
     */
    data object ShowAllLabels : EditLabelEvents

    /**
     * Events to trigger to label with [labelId] and toggle its state
     * @param labelId The LabelId of the label to be edited
     */
    data class ToggleEnabled(val labelId: Int) : EditLabelEvents

    /**
     * Event trigger when the new value label is updated
     * @param text The updated label text
     * @param labelId The labelId of the label to be edited
     */
    data class OnLabelValueUpdate(val text: String, val labelId: Int) : EditLabelEvents
}


/**
 * Events That are responsible to update and delete the labels
 */
sealed interface EditLabelsActions {

    /**
     * Updates the label
     * @param item The [LabelEditableState] instance to be saved
     */
    data class OnUpdate(val item: LabelEditableState) : EditLabelsActions

    /**
     * Deletes the label
     * @param item The [LabelEditableState] instance to be removed
     */
    data class OnDelete(val item: LabelEditableState) : EditLabelsActions
}