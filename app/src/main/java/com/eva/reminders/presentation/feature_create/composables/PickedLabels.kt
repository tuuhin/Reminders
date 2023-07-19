package com.eva.reminders.presentation.feature_create.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Label
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.eva.reminders.domain.models.TaskLabelModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PickedLabels(
    onLabelClick: () -> Unit,
    modifier: Modifier = Modifier,
    selectedLabels: List<TaskLabelModel>? = null,
) {
    var isVisible by remember { mutableStateOf(false) }

    if (!selectedLabels.isNullOrEmpty()) {
        AssistChip(
            onClick = { isVisible = !isVisible },
            label = { Text(text = "Labels (${selectedLabels.size})") },
            modifier = modifier,
            colors = AssistChipDefaults.assistChipColors(
                labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                leadingIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            border = AssistChipDefaults.assistChipBorder(
                borderColor = MaterialTheme.colorScheme.surfaceVariant
            )
        )
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically() + expandVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut() + shrinkVertically()
        ) {
            FlowRow(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                selectedLabels.forEach { item ->
                    AssistChip(
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Label,
                                contentDescription = "Selected label ${item.label}"
                            )
                        },
                        onClick = onLabelClick,
                        label = { Text(text = item.label) },
                        modifier = Modifier.padding(horizontal = 2.dp),
                        colors = AssistChipDefaults.assistChipColors(
                            labelColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            leadingIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        ),
                        border = AssistChipDefaults.assistChipBorder(
                            borderColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    )
                }
            }
        }
    }
}