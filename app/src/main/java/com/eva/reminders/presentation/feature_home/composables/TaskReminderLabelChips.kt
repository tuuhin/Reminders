package com.eva.reminders.presentation.feature_home.composables

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eva.reminders.domain.models.TaskLabelModel
import com.eva.reminders.presentation.feature_home.utils.PreviewTaskModels
import com.eva.reminders.ui.theme.RemindersTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TaskReminderLabelChips(
    showLabels: Boolean,
    labels: List<TaskLabelModel>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val labelsSublist by remember(labels) {
        derivedStateOf {
            val firstThree = (if (labels.size <= 3) labels else labels.subList(0, 3))
                .map { it.label }

            if (labels.size <= 3) firstThree
            else firstThree + "+${labels.size - firstThree.size}"
        }
    }

    val labelsTotalSizeText = remember(labels) {
        "Labels (${labels.size})"
    }

    Column(
        modifier = modifier
    ) {
        SuggestionChip(
            onClick = onClick,
            label = {
                Text(
                    text = labelsTotalSizeText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        )
        AnimatedVisibility(
            visible = showLabels,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically(),
        ) {
            FlowRow(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                labelsSublist.forEach { label ->
                    ReminderLabelChip(label = label, modifier = Modifier.wrapContentWidth())
                }
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
fun TasksReminderLabelChipsPreview() = RemindersTheme {
    Surface(color = MaterialTheme.colorScheme.surface) {
        TaskReminderLabelChips(
            showLabels = true,
            labels = PreviewTaskModels.taskLabelModelList,
            onClick = {},
        )
    }
}