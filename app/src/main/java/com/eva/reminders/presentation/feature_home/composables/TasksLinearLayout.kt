package com.eva.reminders.presentation.feature_home.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eva.reminders.domain.models.TaskModel
import com.eva.reminders.presentation.feature_home.utils.taskModelList

@Composable
fun TasksLinearLayout(
    tasks: List<TaskModel>,
    onTaskSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val isPinned by remember {
        derivedStateOf { tasks.any { it.pinned } }
    }
    val pinnedTasks by remember(tasks) {
        derivedStateOf { tasks.filter { it.pinned } }
    }

    val unPinnedTask by remember(tasks) {
        derivedStateOf { tasks.filter { !it.pinned } }
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {

        if (isPinned) {
            item {
                Text(
                    text = "Pinned",
                    modifier = Modifier.padding(vertical = 2.dp),
                    style = MaterialTheme.typography.labelLarge,
                )
            }
            itemsIndexed(pinnedTasks) { _, item ->
                ReminderCard(
                    taskModel = item,
                    onTap = { onTaskSelect(item.id) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                Text(
                    text = "Others",
                    modifier = Modifier.padding(vertical = 2.dp),
                    style = MaterialTheme.typography.labelLarge,
                )
            }
            itemsIndexed(unPinnedTask) { _, item ->
                ReminderCard(
                    taskModel = item,
                    onTap = { onTaskSelect(item.id) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        } else {
            itemsIndexed(tasks) { _, item ->
                ReminderCard(
                    taskModel = item,
                    onTap = { onTaskSelect(item.id) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TasksLinearLayoutPreview() {
    TasksLinearLayout(
        tasks = taskModelList.toList(),
        onTaskSelect = {}
    )
}