package com.eva.reminders.presentation.feature_labels.utils

import com.eva.reminders.data.parcelable.LabelParceler

data class SelectLabelState(
    val idx: Int,
    val label: String,
    val isSelected: Boolean = false
) {
    fun parcelize(): LabelParceler = LabelParceler(id = idx, label = label)
}


sealed class SelectLabelsEvents {
    data class OnSelect(val state: SelectLabelState) : SelectLabelsEvents()
}