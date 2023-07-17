package com.eva.reminders.presentation.feature_home.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eva.reminders.R
import com.eva.reminders.domain.models.TaskLabelModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TaskReminderLabelChips(
    labels: List<TaskLabelModel>,
    modifier: Modifier = Modifier
) {
    var showLabels by remember { mutableStateOf(false) }

    when {
        labels.isNotEmpty() -> Column(
            modifier = modifier.animateContentSize()
        ) {
            AssistChip(
                onClick = { showLabels = !showLabels },
                label = {
                    Text(
                        text = "Labels (${labels.size})",
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
                    when {
                        labels.size <= 3 -> labels.forEach {
                            SuggestionChip(
                                onClick = {},
                                label = {
                                    Text(
                                        text = it.label,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                },
                                colors = SuggestionChipDefaults.suggestionChipColors(
                                    containerColor = colorResource(id = R.color.white_overlay)
                                )
                            )
                        }

                        else -> {
                            labels.subList(0, 3).forEach {
                                SuggestionChip(
                                    onClick = {},
                                    label = {
                                        Text(
                                            text = it.label,
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    },
                                    colors = SuggestionChipDefaults.suggestionChipColors(
                                        containerColor = colorResource(id = R.color.white_overlay)
                                    )
                                )
                            }
                            SuggestionChip(
                                onClick = {},
                                label = {
                                    Text(
                                        text = "+${labels.size - 3}",
                                        style = MaterialTheme.typography.labelSmall,
                                        letterSpacing = 8.sp
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