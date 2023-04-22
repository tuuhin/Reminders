package com.eva.reminders.data.parcelable

import android.os.Parcelable
import com.eva.reminders.domain.models.TaskLabelModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class LabelParceler(
    val id: Int,
    val label: String
) : Parcelable {

    fun toModel(): TaskLabelModel = TaskLabelModel(id = id, label = label)
}
