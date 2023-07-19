package com.eva.reminders.presentation.feature_home.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.eva.reminders.R
import com.eva.reminders.domain.models.TaskModel
import com.eva.reminders.presentation.feature_home.utils.TaskArrangementStyle

@Composable
fun TaskSearchResults(
    tasks: List<TaskModel>,
    arrangement: TaskArrangementStyle,
    onTaskSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        tasks.isEmpty() -> Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_no_search_results),
                contentDescription = "No associated notes are found",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surfaceTint)
            )
            Text(
                text = "Nothing matching found",
                modifier = Modifier.padding(4.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary
            )
        }

        else -> TasksLayout(
            tasks = tasks,
            style = arrangement,
            onTaskSelect = onTaskSelect,
            modifier = modifier.fillMaxSize()
        )
    }

}