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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.eva.reminders.domain.models.TaskLabelModel

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PickedLabels(
    onLabelClick: () -> Unit,
    modifier: Modifier = Modifier,
    selectedLabels: List<TaskLabelModel>? = null,
) {
    var isVisible by remember { mutableStateOf(false) }

    if (!selectedLabels.isNullOrEmpty()) {
        SuggestionChip(
            onClick = { isVisible = !isVisible },
            label = { Text(text = "Labels (${selectedLabels.size})") },
            modifier = modifier
        )
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically() + expandVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut() + shrinkVertically()
        ) {
            FlowRow(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                selectedLabels.forEach { item ->
                    FilterChip(
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Label,
                                contentDescription = "Selected label ${item.label}"
                            )
                        },
                        onClick = onLabelClick,
                        label = { Text(text = item.label) },
                        modifier = Modifier.padding(horizontal = 2.dp),
                        selected = true
                    )
                }
            }
        }
    }
}