package com.eva.reminders.presentation.feature_create.composables


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun TaskReminderPicker(
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {

    var pickedDate by remember { mutableStateOf(LocalDate.now()) }
    var pickedTime by remember { mutableStateOf(LocalTime.now()) }

    if (showDialog)
        Dialog(
            onDismissRequest = onDismissRequest
        ) {
            Card(
                modifier = modifier,
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(text = "Edit Reminder", style = MaterialTheme.typography.titleLarge)
                    Text(
                        text = "Pick the time for the reminder",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(
                        modifier = Modifier
                            .padding(vertical = 2.dp)
                            .height(2.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                    ReminderDatePicker(
                        date = pickedDate,
                        onDatePicked = { pickedDate = it },
                    )
                    Divider()
                    ReminderTimePicker(
                        time = LocalTime.now(),
                        onTimePicked = {
                            pickedTime = it
                        }
                    )
                    Divider()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        TextButton(onClick = { }) {
                            Text(text = "Cancel")
                        }
                        TextButton(onClick = {}) {
                            Text(text = "Delete")
                        }
                        Button(onClick = {}) {
                            Text(text = "Save")
                        }
                    }
                }
            }
        }
}




