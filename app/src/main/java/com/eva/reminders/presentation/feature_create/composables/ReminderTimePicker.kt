package com.eva.reminders.presentation.feature_create.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.eva.reminders.presentation.feature_create.utils.ReminderDateOptions
import com.eva.reminders.presentation.feature_create.utils.ReminderTimeOptions
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderTimePicker(
    reminderDate: ReminderDateOptions,
    reminderTimeOption: ReminderTimeOptions,
    onTimePicked: (ReminderTimeOptions) -> Unit,
    modifier: Modifier = Modifier,
    errorText: String? = null,
) {

    var isDialogOpen by remember { mutableStateOf(false) }

    var isExpanded by remember { mutableStateOf(false) }

    var dropdownOffset by remember { mutableStateOf(DpOffset.Zero) }

    val timeFormat = remember { DateTimeFormatter.ofPattern("h:mm a") }

    val selectedTimeText by remember(reminderTimeOption) {
        derivedStateOf { reminderTimeOption.schedule.format(timeFormat) }
    }

    val timePickerState = rememberTimePickerState(
        initialHour = LocalTime.now().hour,
        initialMinute = LocalTime.now().minute
    )


    if (isDialogOpen) {

        DatePickerDialog(
            modifier = Modifier.padding(horizontal = 10.dp),
            onDismissRequest = { isDialogOpen = !isDialogOpen },
            confirmButton = {
                Button(
                    onClick = {
                        isDialogOpen = !isDialogOpen
                        val localtime = LocalTime.of(timePickerState.hour, timePickerState.minute)
                        onTimePicked(ReminderTimeOptions.Custom(localtime))
                    }
                ) {
                    Text(text = "Done")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { isDialogOpen = !isDialogOpen }
                ) {
                    Text(text = "Cancel")
                }
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnClickOutside = false
            )
        ) {
            TimePicker(
                modifier = modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally),
                state = timePickerState,
                layoutType = TimePickerLayoutType.Vertical
            )
        }
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(true) {
                    detectTapGestures(
                        onTap = { tapOffset ->
                            dropdownOffset =
                                DpOffset(tapOffset.x.toDp(), tapOffset.y.toDp())
                        },
                    )
                }
                .clickable(
                    onClick = { isExpanded = !isExpanded },
                    role = Role.Button
                )
                .padding(vertical = 8.dp)
        ) {
            Text(text = selectedTimeText, style = MaterialTheme.typography.bodyMedium)
            Icon(
                imageVector = Icons.Outlined.ExpandMore,
                contentDescription = "Options for dates",

                )
        }
        Divider()
        errorText?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(vertical = 2.dp)
            )
        }
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = !isExpanded },
            offset = dropdownOffset,
            modifier = Modifier.width(300.dp)
        ) {
            ReminderTimeOptions.allOptions(reminderDate.schedule).forEach { opt ->
                DropdownMenuItem(
                    enabled = opt.enable,
                    text = { Text(text = opt.text) },
                    trailingIcon = { Text(text = opt.schedule.format(timeFormat)) },
                    onClick = {
                        onTimePicked(opt)
                        isExpanded = !isExpanded
                    },
                )
                Divider()
            }
            DropdownMenuItem(
                text = { Text(text = "Select a time...") },
                onClick = {
                    isExpanded = !isExpanded
                    isDialogOpen = !isDialogOpen
                },
            )
            Divider()
        }
    }

}
