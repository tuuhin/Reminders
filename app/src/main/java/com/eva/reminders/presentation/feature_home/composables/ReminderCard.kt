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
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.eva.reminders.domain.enums.TaskColorEnum
import com.eva.reminders.domain.models.TaskModel
import com.eva.reminders.presentation.feature_home.utils.taskModelList

@Composable
fun ReminderCard(
    taskModel: TaskModel,
    onTap: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .padding(vertical = 4.dp)
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onTap)
            .animateContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = if (taskModel.color != TaskColorEnum.TRANSPARENT)
                colorResource(id = taskModel.color.color)
            else
                MaterialTheme.colorScheme.surfaceVariant
        ), shape = MaterialTheme.shapes.medium
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

class ReminderCardPreviewParams : PreviewParameterProvider<TaskModel> {
    override val values: Sequence<TaskModel> = taskModelList
}

@Composable
@Preview
private fun ReminderCardPreview(
    @PreviewParameter(ReminderCardPreviewParams::class) task: TaskModel
) {
    ReminderCard(task, onTap = {})
}
