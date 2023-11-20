package com.eva.reminders.presentation.feature_labels.utils

object FeatureLabelsTestTags {
    //Create New Label Test Tags
    const val CREATE_LABEL_PLACEHOLDER = "CREATE_LABEL_PLACEHOLDER"
    const val CREATE_NEW_LABEL_TEXT_FIELD = "CREATE_NEW_LABEL_ACTUAL_TEXT_FIELD"
    const val CREATE_NEW_LABEL_ACTION_CREATE = "CREATE_NEW_LABEL_ACTION_CREATE"
    const val CREATE_NEW_LABEL_ACTION_CANCEL = "CREATE_NEW_LABEL_ACTION_CANCEL"

    // Dialog Test Tags
    const val TOGGLE_SORT_OPTIONS_DIALOG = "TOGGLE_SORT_OPTIONS_DIALOG"
    const val SORT_OPTIONS_DIALOG = "SORT_OPTIONS_DIALOG"
    fun sortTestTagFromOrder(sortOrder: LabelSortOrder): String =
        "CHANGE_SORT_ORDER_TO_${sortOrder.name}"

    const val NO_LABELS_ADDED_TAG = "NO_LABELS_FOUND"
    const val LOADED_LABELS_LAZY_COL = "LOADED LABELS LAZY COL"
}