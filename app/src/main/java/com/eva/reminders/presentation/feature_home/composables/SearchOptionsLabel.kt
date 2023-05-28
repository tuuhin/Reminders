package com.eva.reminders.presentation.feature_home.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Label
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.eva.reminders.domain.models.TaskLabelModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchOptionLabels(
    labels: List<TaskLabelModel>,
    modifier: Modifier = Modifier,
    onLabelClick: (TaskLabelModel) -> Unit,
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        labels.forEach { label ->
            AssistChip(
                onClick = { onLabelClick(label) },
                label = { Text(text = label.label) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Label,
                        contentDescription = "Label: ${label.label}"
                    )
                }
            )
        }
    }
}
