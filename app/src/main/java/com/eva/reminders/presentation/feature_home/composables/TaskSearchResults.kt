package com.eva.reminders.presentation.feature_home.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.eva.reminders.domain.models.ArrangementStyle
import com.eva.reminders.domain.models.TaskModel

@Composable
fun TaskSearchResults(
    tasks: List<TaskModel>,
    arrangement: ArrangementStyle,
    onTaskSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        tasks.isEmpty() -> NoSearchResults()
        else -> TasksLayout(
            tasks = tasks,
            style = arrangement,
            onTaskSelect = { onTaskSelect(it.id) },
            modifier = modifier.fillMaxSize()
        )
    }

}