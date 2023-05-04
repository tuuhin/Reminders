package com.eva.reminders.presentation.feature_create.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Timelapse
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.eva.reminders.presentation.feature_create.utils.ReminderFrequency
import com.eva.reminders.presentation.feature_create.utils.TaskReminderState
import java.time.format.DateTimeFormatter

@Composable
fun PickedReminder(
    show: Boolean,
    state: TaskReminderState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = show,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutVertically() + fadeOut()
    ) {
        val data by remember(state) {
            derivedStateOf {
                val formattedTime = state.time.schedule
                    .format(DateTimeFormatter.ofPattern("h:mm a"))
                val repeat = when (state.frequency) {
                    ReminderFrequency.DO_NOT_REPEAT -> "Once"
                    ReminderFrequency.DAILY -> "Daily"
                }
                "${state.date.text}, $formattedTime ( $repeat )"
            }
        }
        AssistChip(
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Timelapse,
                    contentDescription = "Reminder Time"
                )
            },
            onClick = onClick,
            label = { Text(text = data) },
            modifier = modifier
        )
    }
}