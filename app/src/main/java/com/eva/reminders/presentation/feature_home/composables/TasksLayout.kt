package com.eva.reminders.presentation.feature_home.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.eva.reminders.domain.models.TaskModel
import com.eva.reminders.presentation.feature_home.utils.TaskArrangementStyle

@Composable
fun TasksLayout(
    modifier: Modifier = Modifier,
    tasks: List<TaskModel>,
    style: TaskArrangementStyle,
    onTaskSelect: (Int) -> Unit
) {
    when (style) {
        TaskArrangementStyle.GRID_STYLE -> TasksGridLayout(
            tasks = tasks,
            modifier = modifier,
            onTaskSelect = onTaskSelect
        )

        TaskArrangementStyle.BLOCK_STYLE -> TasksLinearLayout(
            tasks = tasks,
            modifier = modifier,
            onTaskSelect = onTaskSelect
        )
    }
}