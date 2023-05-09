package com.eva.reminders.presentation.feature_create.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.eva.reminders.presentation.feature_create.utils.TaskReminderState
import com.eva.reminders.presentation.feature_create.utils.TaskRemindersEvents

@Composable
fun TaskReminderPicker(
    state: TaskReminderState,
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    onRemindersEvents: (TaskRemindersEvents) -> Unit
) {

    if (showDialog)
        Dialog(
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(
                dismissOnClickOutside = false,
                dismissOnBackPress = false
            )
        ) {

            Card(
                modifier = modifier,
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Edit Reminder",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "Pick the time for the reminder",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Divider(
                        modifier = Modifier
                            .padding(vertical = 2.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ReminderDatePicker(
                        option = state.date,
                        onDatePicked = {
                            onRemindersEvents(TaskRemindersEvents.OnDateChanged(it))
                        },
                    )
                    ReminderTimePicker(
                        reminderTimeOption = state.time,
                        reminderDate = state.date,
                        errorText = state.invalidTime,
                        onTimePicked = {
                            onRemindersEvents(TaskRemindersEvents.OnTimeChanged(it))
                        }
                    )
                    ReminderFrequencyPicker(
                        current = state.frequency,
                        onReminderFrequency = {
                            onRemindersEvents(TaskRemindersEvents.OnReminderChanged(it))
                        },
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(onClick = onDismissRequest) { Text(text = "Cancel") }
                        TextButton(onClick = onDelete) { Text(text = "Delete") }
                        Button(onClick = onSave) { Text(text = "Save") }
                    }
                }
            }
        }
}




