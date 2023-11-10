package com.eva.reminders.presentation.feature_labels.utils

/**
 * Represents the state of the sort labels dialog
 * @param isDialogVisible Is the dialog is visible
 * @param order The current [LabelSortOrder] in which labels are ordered
 */
data class SortLabelsDialogState(
    val isDialogVisible: Boolean = false,
    val order: LabelSortOrder = LabelSortOrder.REGULAR
)

/**
 * Events triggered when [SortLabelsDialogState] is edited
 */
sealed interface SortLabelEvents {

    /**
     * Toggles the Sort Dialog
     */
    data object ToggleDialog : SortLabelEvents

    /**
     * Event trigger [SortLabelsDialogState.order] is updated
     */
    data class SelectSortOrder(val order: LabelSortOrder) : SortLabelEvents
}