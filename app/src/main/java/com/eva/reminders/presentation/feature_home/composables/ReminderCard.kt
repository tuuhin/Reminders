package com.eva.reminders.presentation.feature_home.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.eva.reminders.R
import com.eva.reminders.domain.enums.TaskColorEnum
import com.eva.reminders.domain.models.TaskLabelModel
import com.eva.reminders.domain.models.TaskModel
import com.eva.reminders.domain.models.TaskReminderModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ReminderCard(
    taskModel: TaskModel,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.padding(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = taskModel.color.color)
        ),
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = taskModel.title,
                    style = MaterialTheme.typography.titleLarge
                )
                Icon(
                    imageVector = Icons.Outlined.PushPin,
                    contentDescription = "Its pinned",
                    modifier = Modifier.graphicsLayer { rotationZ = 45f })
            }
            Text(
                text = taskModel.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(4.dp))
            taskModel.reminderAt.at?.let { time ->
                val reminderTime = remember {
                    derivedStateOf {
                        val pattern = DateTimeFormatter.ofPattern("dd MMMM,hh:mm a")
                        time.format(pattern)
                    }
                }
                AssistChip(
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Timer,
                            contentDescription = "Timer"
                        )
                    },
                    onClick = {},
                    label = {
                        Text(
                            text = reminderTime.value,
                            textDecoration = if (taskModel.reminderAt.at < LocalDateTime.now())
                                TextDecoration.LineThrough
                            else
                                null,
                            fontStyle = if (taskModel.reminderAt.at < LocalDateTime.now())
                                FontStyle.Italic
                            else
                                null,
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    colors = AssistChipDefaults
                        .assistChipColors(
                            containerColor = colorResource(id = R.color.white_overlay),
                            leadingIconContentColor = MaterialTheme.colorScheme.onBackground,
                            labelColor = MaterialTheme.colorScheme.onSurface
                        )
                )
            }
            if (taskModel.labels.isNotEmpty()) {
                Text(
                    text = "Labels (${taskModel.labels.size})",
                    style = MaterialTheme.typography.bodyMedium
                )
                FlowRow(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(0.dp)
                ) {
                    if (taskModel.labels.size <= 6) {
                        taskModel.labels.forEach {
                            SuggestionChip(
                                onClick = {},
                                label = {
                                    Text(
                                        text = it.label,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                },
                                colors = SuggestionChipDefaults
                                    .suggestionChipColors(
                                        containerColor = colorResource(id = R.color.white_overlay)
                                    )
                            )
                        }
                    } else {
                        taskModel.labels.subList(0, 6).forEach {
                            SuggestionChip(
                                onClick = {},
                                label = {
                                    Text(
                                        text = it.label,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                },
                                colors = SuggestionChipDefaults
                                    .suggestionChipColors(
                                        containerColor = colorResource(id = R.color.white_overlay)
                                    )
                            )
                        }
                        SuggestionChip(
                            onClick = {},
                            label = {
                                Text(
                                    text = "+ ${taskModel.labels.size - 4}..",
                                    style = MaterialTheme.typography.labelSmall
                                )
                            },
                            colors = SuggestionChipDefaults
                                .suggestionChipColors(
                                    containerColor = colorResource(id = R.color.white_overlay)
                                )
                        )
                    }
                }
            }
        }
    }
}


private class ReminderCardPreviewParams : PreviewParameterProvider<TaskModel> {
    override val values: Sequence<TaskModel> = sequenceOf(
        TaskModel(
            id = 0,
            title = "Something",
            content = "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).",
            pinned = false,
            color = TaskColorEnum.AMBER,
            reminderAt = TaskReminderModel(),
            isArchived = false,
            updatedAt = LocalDateTime.now(),
        ), TaskModel(
            id = 1,
            title = "Something",
            content = "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).",
            pinned = true,
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
            reminderAt = TaskReminderModel(),
            isArchived = false,
            updatedAt = LocalDateTime.now(),
            labels = listOf(TaskLabelModel(0, "One"), TaskLabelModel(1, "Two"))
        ), TaskModel(
            id = 4,
            title = "Something",
            content = "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).",
            pinned = true,
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

}

@Composable
@Preview
private fun ReminderCardPreview(
    @PreviewParameter(ReminderCardPreviewParams::class) task: TaskModel
) {
    ReminderCard(task)
}
