package com.eva.reminders.presentation.feature_create.composables

import android.content.Context
import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eva.reminders.R
import com.eva.reminders.presentation.feature_create.utils.ReminderFrequency
import com.eva.reminders.presentation.feature_create.utils.TaskReminderState
import com.eva.reminders.ui.theme.RemindersTheme
import com.eva.reminders.utils.toCurrentDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectedReminderTimeChip(
    showReminder: Boolean,
    state: TaskReminderState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    labelColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    iconColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
) {
    AnimatedVisibility(
        visible = showReminder,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutVertically() + fadeOut()
    ) {
        val data by remember(state) {
            derivedStateOf {
                val formattedTime = state.time.schedule.toCurrentDateTime()
                val repeat = when (state.frequency) {
                    ReminderFrequency.DO_NOT_REPEAT -> context.getString(R.string.reminder_do_not_repeat)
                    ReminderFrequency.DAILY -> context.getString(R.string.reminder_repeat_daily)
                }
                "${state.date.text}, $formattedTime ( $repeat )"
            }
        }
        FilterChip(
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Timer,
                    contentDescription = stringResource(id = R.string.timer_icon_desc),
                )
            },
            onClick = onClick,
            label = { Text(text = data, style = MaterialTheme.typography.bodyLarge) },
            selected = true,
            modifier = modifier,
            colors = FilterChipDefaults.filterChipColors(
                containerColor = containerColor,
                labelColor = labelColor,
                iconColor = iconColor
            ),
            border = FilterChipDefaults.filterChipBorder(
                borderColor = MaterialTheme.colorScheme.onSurface,
                borderWidth = 1.dp
            )
        )
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun SelectedReminderTimeChipPreview() = RemindersTheme {
    SelectedReminderTimeChip(
        showReminder = true,
        state = TaskReminderState(),
        onClick = {}
    )
}