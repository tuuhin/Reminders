package com.eva.reminders.presentation.feature_home.composables

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.eva.reminders.domain.enums.TaskColorEnum
import com.eva.reminders.domain.models.TaskModel
import com.eva.reminders.presentation.feature_home.utils.PreviewTaskModels

@Composable
fun ReminderCard(
    taskModel: TaskModel,
    onTap: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onTap)
            .animateContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = if (taskModel.color != TaskColorEnum.TRANSPARENT)
                colorResource(id = taskModel.color.color)
            else
                MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if (taskModel.color != TaskColorEnum.TRANSPARENT)
                MaterialTheme.colorScheme.onSurfaceVariant
            else
                MaterialTheme.colorScheme.onSurface
        ), shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = taskModel.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = taskModel.content,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
            taskModel.reminderAt.at?.let { time ->
                TaskReminderChip(
                    time = time,
                    isRepeating = taskModel.reminderAt.isRepeating,
                    isExact = taskModel.isExact
                )
            }
            TaskReminderLabelChips(labels = taskModel.labels)
        }
    }
}

class ReminderCardPreviewModels :
    CollectionPreviewParameterProvider<TaskModel>(PreviewTaskModels.taskModelsList)

@Composable
@Preview
private fun ReminderCardPreview(

    @PreviewParameter(ReminderCardPreviewModels::class)
    task: TaskModel
) {
    ReminderCard(task, onTap = {})
}
