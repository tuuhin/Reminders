package com.eva.reminders.presentation.feature_create.composables

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.eva.reminders.R
import com.eva.reminders.presentation.feature_create.utils.TaskReminderState
import com.eva.reminders.presentation.feature_create.utils.TaskRemindersEvents
import com.eva.reminders.ui.theme.RemindersTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskReminderPickerDialog(
    state: TaskReminderState,
    showDelete: Boolean,
    onDismissRequest: () -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit,
    onRemindersEvents: (TaskRemindersEvents) -> Unit,
    modifier: Modifier = Modifier,
    properties: DialogProperties = DialogProperties(
        dismissOnClickOutside = false,
        dismissOnBackPress = true
    ),
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        properties = properties,
    ) {
        Card(
            modifier = modifier,
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp),
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(all = dimensionResource(id = R.dimen.dialogs_internal_padding))
            ) {
                Text(
                    text = stringResource(id = R.string.date_time_picker_dialog_title),
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = stringResource(id = R.string.date_time_picker_dialog_subtitle),
                    style = MaterialTheme.typography.labelMedium,
                )
                Divider(
                    modifier = Modifier.padding(vertical = 2.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                ReminderDatePicker(
                    option = state.date,
                    onDatePicked = {
                        onRemindersEvents(TaskRemindersEvents.OnDateChanged(it))
                    },
                )
                ReminderTimePicker(
                    reminderTimeOption = state.time,
                    scheduledDate = state.date.schedule,
                    errorText = state.invalidTime,
                    onTimePicked = {
                        onRemindersEvents(TaskRemindersEvents.OnTimeChanged(it))
                    }
                )
                ReminderFrequencyPicker(
                    isExact = state.isExact,
                    frequency = state.frequency,
                    onReminderFrequency = {
                        onRemindersEvents(TaskRemindersEvents.OnReminderChanged(it))
                    },
                    onIsExactChange = {
                        onRemindersEvents(TaskRemindersEvents.OnIsExactChange(it))
                    }
                )
                Divider(
                    color = MaterialTheme.colorScheme.outlineVariant,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (showDelete) Arrangement.SpaceEvenly else Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = onDismissRequest,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text(text = stringResource(id = R.string.dialog_cancel_button_text))
                    }
                    if (showDelete) {
                        TextButton(
                            onClick = onDelete,
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            Text(text = stringResource(id = R.string.dialog_delete_button_text))
                        }
                    }
                    Button(
                        onClick = onSave,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(text = stringResource(id = R.string.dialog_save_button_text))
                    }
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
fun TaskReminderPickerDialogPreview() = RemindersTheme {
    TaskReminderPickerDialog(
        state = TaskReminderState(isExact = true),
        onDismissRequest = { },
        showDelete = false,
        onSave = { },
        onDelete = { },
        onRemindersEvents = {}
    )
}