package com.eva.reminders.presentation.feature_home.composables

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.eva.reminders.R
import com.eva.reminders.domain.enums.TaskColorEnum
import com.eva.reminders.domain.models.TaskModel
import com.eva.reminders.presentation.feature_home.utils.TaskModelPreviewParameter
import com.eva.reminders.ui.theme.RemindersTheme

@Composable
fun ReminderCard(
    taskModel: TaskModel,
    onTap: () -> Unit,
    modifier: Modifier = Modifier,
    defaultContainerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    defaultContentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {

    var showLabels by rememberSaveable { mutableStateOf(false) }

    val containerColor = if (taskModel.color != TaskColorEnum.TRANSPARENT)
        colorResource(id = taskModel.color.color)
    else defaultContainerColor


    Card(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onTap),
        colors = CardDefaults.cardColors(
            containerColor = containerColor, contentColor = defaultContentColor
        ),
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(
            modifier = Modifier
                .padding(all = dimensionResource(id = R.dimen.reminder_card_inter_padding)),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = taskModel.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onBackground
            )
            if (taskModel.content.isNotBlank()) {
                Text(
                    text = taskModel.content,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            taskModel.reminderAt.at?.let { time ->
                Spacer(modifier = Modifier.height(2.dp))
                TaskReminderChip(
                    time = time,
                    isRepeating = taskModel.reminderAt.isRepeating,
                    isExact = taskModel.isExact,

                    )
            }
            if (taskModel.labels.isNotEmpty()) {
                TaskReminderLabelChips(
                    showLabels = showLabels,
                    labels = taskModel.labels,
                    onClick = { showLabels = !showLabels },
                )
            }
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun ReminderCardPreview(
    @PreviewParameter(TaskModelPreviewParameter::class)
    task: TaskModel
) = RemindersTheme {
    ReminderCard(task, onTap = {})
}
