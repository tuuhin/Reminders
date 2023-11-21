package com.eva.reminders.presentation.feature_labels.utils

import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import com.eva.reminders.domain.models.TaskLabelModel

class EditableLabelsPreviewParams : CollectionPreviewParameterProvider<LabelEditableState>(
    listOf(
        LabelEditableState(
            isEdit = false,
            updatedLabel = "Updated label",
            model = TaskLabelModel(0, "Something")
        ),
        LabelEditableState(
            isEdit = true,
            updatedLabel = "Updated Label",
            model = TaskLabelModel(0, "Something")
        ),
        LabelEditableState(
            isEdit = false,
            updatedLabel = "",
            model = TaskLabelModel(0, "Previous")
        )
    )
)

class LabelsSortOrderPreviewParams :
    CollectionPreviewParameterProvider<LabelSortOrder>(LabelSortOrder.entries.toList())

class CreateNewLabelPreviewParams : CollectionPreviewParameterProvider<CreateLabelState>(
    listOf(
        CreateLabelState(label = "", isError = "Some error"),
        CreateLabelState(label = "This is correct")
    )
)
