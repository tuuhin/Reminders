package com.eva.reminders.presentation.feature_create.composables

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Label
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eva.reminders.domain.models.TaskLabelModel
import com.eva.reminders.presentation.feature_home.utils.PreviewTaskModels
import com.eva.reminders.ui.theme.RemindersTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SelectTaskLabels(
    onLabelClick: () -> Unit,
    modifier: Modifier = Modifier,
    selectedLabels: List<TaskLabelModel>? = null,
    labelChipContainerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    labelChipContentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    labelContainerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    labelContentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer
) {
    var isVisible by rememberSaveable { mutableStateOf(true) }

    val labelsCountText by remember {
        derivedStateOf {
            "Labels (${selectedLabels?.size ?: 0})"
        }
    }

    if (selectedLabels.isNullOrEmpty()) return

    Column {

        AssistChip(
            onClick = { isVisible = !isVisible },
            label = { Text(text = labelsCountText) },
            modifier = modifier,
            colors = AssistChipDefaults.assistChipColors(
                labelColor = labelChipContentColor,
                leadingIconContentColor = labelChipContentColor,
                containerColor = labelChipContainerColor
            ),
            border = AssistChipDefaults.assistChipBorder(
                borderColor = labelChipContentColor
            )
        )
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut()
        ) {
            FlowRow(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                selectedLabels.forEach { item ->


                    AssistChip(
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Label,
                                contentDescription = item.label
                            )
                        },
                        onClick = onLabelClick,
                        label = { Text(text = item.label) },
                        colors = AssistChipDefaults.assistChipColors(
                            labelColor = labelContentColor,
                            leadingIconContentColor = labelContentColor,
                            containerColor = labelContainerColor
                        ),
                        border = AssistChipDefaults.assistChipBorder(
                            borderColor = labelContentColor
                        )
                    )
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
fun SelectTasksLabelsPreview() = RemindersTheme {
    SelectTaskLabels(
        selectedLabels = PreviewTaskModels.taskLabelModelList,
        onLabelClick = { },
    )
}