package com.eva.reminders.presentation.feature_home.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    onTap: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showLabels by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .padding(vertical = 4.dp)
            .clickable(onClick = onTap)
            .animateContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = if (taskModel.color != TaskColorEnum.TRANSPARENT)
                colorResource(id = taskModel.color.color)
            else
                MaterialTheme.colorScheme.surfaceVariant
        ),
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = taskModel.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = taskModel.content,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(4.dp))
            taskModel.reminderAt.at?.let { time ->
                val reminderTime = remember(time) {
                    derivedStateOf {
                        val pattern = DateTimeFormatter.ofPattern("dd MMMM,hh:mm a")
                        if (taskModel.reminderAt.isRepeating && taskModel.reminderAt.at < LocalDateTime.now()) {
                            val daysDiff =
                                LocalDateTime.now().dayOfYear - taskModel.reminderAt.at.dayOfYear
                            return@derivedStateOf time.plusDays(daysDiff.toLong()).format(pattern)
                        }
                        time.format(pattern)
                    }
                }
                val isCrossed = remember(taskModel.reminderAt) {
                    derivedStateOf {
                        !taskModel.reminderAt.isRepeating && taskModel.reminderAt.at < LocalDateTime.now()
                    }
                }
                AssistChip(
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Timer, contentDescription = "Timer"
                        )
                    },
                    onClick = {},
                    label = {
                        Text(
                            text = reminderTime.value,
                            textDecoration = if (isCrossed.value) TextDecoration.LineThrough
                            else null,
                            fontStyle = if (isCrossed.value) FontStyle.Italic
                            else null,
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = colorResource(id = R.color.white_overlay),
                        leadingIconContentColor = MaterialTheme.colorScheme.onBackground,
                        labelColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
            if (taskModel.labels.isNotEmpty()) {
                AssistChip(
                    onClick = { showLabels = !showLabels }, label = {
                        Text(
                            text = "Labels (${taskModel.labels.size})",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                )
                AnimatedVisibility(
                    visible = showLabels,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically(),
                    label = "Show labels for the correspond task"
                ) {
                    FlowRow(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(0.dp)
                    ) {
                        if (taskModel.labels.size <= 3) {
                            taskModel.labels.forEach {
                                SuggestionChip(
                                    onClick = {},
                                    label = {
                                        Text(
                                            text = it.label,
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    }, colors = SuggestionChipDefaults.suggestionChipColors(
                                        containerColor = colorResource(id = R.color.white_overlay)
                                    )
                                )
                            }
                        } else {
                            taskModel.labels.subList(0, 3).forEach {
                                SuggestionChip(
                                    onClick = {},
                                    label = {
                                        Text(
                                            text = it.label,
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    }, colors = SuggestionChipDefaults.suggestionChipColors(
                                        containerColor = colorResource(id = R.color.white_overlay)
                                    )
                                )
                            }
                            SuggestionChip(
                                onClick = {},
                                label = {
                                    Text(
                                        text = "+${taskModel.labels.size - 3}",
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                },
                                colors = SuggestionChipDefaults.suggestionChipColors(
                                    containerColor = colorResource(id = R.color.white_overlay)
                                )
                            )
                        }
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
            color = TaskColorEnum.TRANSPARENT,
            reminderAt = TaskReminderModel(),
            isArchived = false,
            updatedAt = LocalDateTime.now(),
            isExact = true, labels = emptyList()

        ), TaskModel(
            id = 1,
            title = "Something",
            content = "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).",
            pinned = true,
            color = TaskColorEnum.PURPLE,
            reminderAt = TaskReminderModel(at = LocalDateTime.now()),
            isArchived = false,
            updatedAt = LocalDateTime.now().plusDays(1),
            isExact = false,
            labels = emptyList()
        ), TaskModel(
            id = 2,
            title = "Something",
            content = "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).",
            pinned = false,
            color = TaskColorEnum.GREEN,
            reminderAt = TaskReminderModel(
                at = LocalDateTime.of(2023, 4, 28, 20, 40), isRepeating = true
            ),
            isArchived = false,
            updatedAt = LocalDateTime.now(),
            isExact = false,
            labels = listOf(TaskLabelModel(0, "One"), TaskLabelModel(1, "Two"))
        ), TaskModel(
            id = 4,
            title = "Something",
            content = "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).",
            pinned = true,
            color = TaskColorEnum.ROSE,
            reminderAt = TaskReminderModel(at = LocalDateTime.now()),
            isArchived = false,
            updatedAt = LocalDateTime.of(2023, 6, 1, 10, 40),
            isExact = true,
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
    ReminderCard(task, onTap = {})
}
