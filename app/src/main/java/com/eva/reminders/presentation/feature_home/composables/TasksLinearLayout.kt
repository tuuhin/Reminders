package com.eva.reminders.presentation.feature_home.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eva.reminders.domain.enums.TaskColorEnum
import com.eva.reminders.domain.models.TaskLabelModel
import com.eva.reminders.domain.models.TaskModel
import com.eva.reminders.domain.models.TaskReminderModel
import java.time.LocalDateTime

@Composable
fun TasksLinearLayout(
    tasks: List<TaskModel>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        if (tasks.any { it.pinned }) {
            item {
                Text(
                    text = "Pinned",
                    modifier = Modifier.padding(vertical = 2.dp),
                    style = MaterialTheme.typography.labelMedium,
                )
            }
            itemsIndexed(tasks.filter { it.pinned }) { _, item ->
                ReminderCard(
                    taskModel = item,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                Text(
                    text = "Others",
                    modifier = Modifier.padding(vertical = 2.dp),
                    style = MaterialTheme.typography.labelMedium,
                )
            }
            itemsIndexed(tasks.filter { !it.pinned }) { _, item ->
                ReminderCard(
                    taskModel = item,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        } else {
            itemsIndexed(tasks) { _, item ->
                ReminderCard(
                    taskModel = item,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
@Preview
fun TasksLinearLayoutPreview() {
    Surface(color = MaterialTheme.colorScheme.surface) {
        TasksLinearLayout(
            tasks = listOf(
                TaskModel(
                    id = 0,
                    title = "Something",
                    content = "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).",
                    pinned = true,
                    color = TaskColorEnum.AMBER,
                    reminderAt = TaskReminderModel(),
                    isArchived = false,
                    updatedAt = LocalDateTime.now(),
                ), TaskModel(
                    id = 1,
                    title = "Something",
                    content = "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).",
                    pinned = false,
                    color = TaskColorEnum.PURPLE,
                    reminderAt = TaskReminderModel(at = LocalDateTime.now()),
                    isArchived = false,
                    updatedAt = LocalDateTime.now().plusDays(1),
                ), TaskModel(
                    id = 2,
                    title = "Something",
                    content = "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).",
                    pinned = false,
                    color = TaskColorEnum.GREEN,
                    reminderAt = TaskReminderModel(
                        at = LocalDateTime.now().minusDays(1), isRepeating = true
                    ),
                    isArchived = false,
                    updatedAt = LocalDateTime.now(),
                    labels = listOf(TaskLabelModel(0, "One"), TaskLabelModel(1, "Two"))
                ), TaskModel(
                    id = 4,
                    title = "Something",
                    content = "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).",
                    pinned = false,
                    color = TaskColorEnum.ROSE,
                    reminderAt = TaskReminderModel(at = LocalDateTime.now()),
                    isArchived = false,
                    updatedAt = LocalDateTime.now(),
                    labels = listOf(
                        TaskLabelModel(0, "One"),
                        TaskLabelModel(1, "Two"),
                        TaskLabelModel(2, "Three"),
                        TaskLabelModel(3, "Four"),
                        TaskLabelModel(4, "Five"),
                        TaskLabelModel(5, "Six")
                    )

                )
            )
        )
    }
}