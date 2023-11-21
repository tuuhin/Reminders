package com.eva.reminders.presentation.feature_home.utils

import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import com.eva.reminders.domain.models.ArrangementStyle
import com.eva.reminders.domain.models.TaskModel

class ArrangementStyleParameter : CollectionPreviewParameterProvider<ArrangementStyle>(
    listOf(
        ArrangementStyle.GRID_STYLE,
        ArrangementStyle.BLOCK_STYLE
    )
)

class TaskModelPreviewParameter : CollectionPreviewParameterProvider<TaskModel>(
    PreviewTaskModels.taskModelsList
)