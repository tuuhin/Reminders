package com.eva.reminders.presentation.feature_home.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
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
import com.eva.reminders.presentation.feature_home.utils.PreviewTaskModels

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TasksGridLayout(
    tasks: List<TaskModel>,
    onTaskSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val isPinned by remember {
        derivedStateOf { tasks.any { it.pinned } }
    }
    val allNotPinned by remember {
        derivedStateOf { !tasks.all { it.pinned } }
    }

    val pinnedTasks by remember(tasks) {
        derivedStateOf { tasks.filter { it.pinned } }
    }

    val unPinnedTask by remember(tasks) {
        derivedStateOf { tasks.filter { !it.pinned } }
    }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 10.dp),
        verticalItemSpacing = 4.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        if (isPinned && allNotPinned) {
            item(span = StaggeredGridItemSpan.FullLine) {
                Text(
                    text = "Pinned",
                    modifier = Modifier.padding(vertical = 2.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
            itemsIndexed(pinnedTasks, key = { _, item -> item.id }) { _, item ->
                ReminderCard(
                    taskModel = item,
                    onTap = { onTaskSelect(item.id) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item(span = StaggeredGridItemSpan.FullLine) {
                Text(
                    text = "Others",
                    modifier = Modifier.padding(vertical = 2.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
            itemsIndexed(unPinnedTask, key = { _, item -> item.id }) { _, item ->
                ReminderCard(
                    taskModel = item,
                    onTap = { onTaskSelect(item.id) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        } else {
            itemsIndexed(tasks, key = { _, item -> item.id }) { _, item ->
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
fun TaskGridLayoutPreview() {
    TasksGridLayout(
        tasks = PreviewTaskModels.taskModelsList,
        onTaskSelect = {}
    )
}