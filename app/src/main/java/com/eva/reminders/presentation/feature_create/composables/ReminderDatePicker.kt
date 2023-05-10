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
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderDatePicker(
    option: ReminderDateOptions,
    onDatePicked: (ReminderDateOptions) -> Unit,
    modifier: Modifier = Modifier,
) {



    var isDialogOpen by remember { mutableStateOf(false) }

    var isExpanded by remember { mutableStateOf(false) }

    var dropdownOffset by remember { mutableStateOf(DpOffset.Zero) }

    val dateText by remember(option.schedule) {
        derivedStateOf { option.schedule.format(DateTimeFormatter.ofPattern("dd MMMM")) }
    }

    val dateOptions = remember {
        listOf(
            ReminderDateOptions.Today(),
            ReminderDateOptions.Tomorrow(),
            ReminderDateOptions.NextWeek()
        )
    }

    val datePickerState = rememberDatePickerState(
        initialDisplayMode = DisplayMode.Picker
    )

    if (isDialogOpen) {
        DatePickerDialog(
            onDismissRequest = { isDialogOpen = !isDialogOpen },
            dismissButton = {
                TextButton(
                    onClick = { isDialogOpen = !isDialogOpen }
                ) {
                    Text(text = "Cancel")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        isDialogOpen = !isDialogOpen
                        val mills = datePickerState.selectedDateMillis
                        if (mills != null) {
                            val pickedDate =
                                Instant.ofEpochMilli(mills).atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                            onDatePicked(ReminderDateOptions.Custom(date = pickedDate))
                        }
                    }
                ) { Text(text = "Done") }
            },
            modifier = Modifier.padding(40.dp),
            properties = DialogProperties(
                usePlatformDefaultWidth = true,
                dismissOnClickOutside = false
            )
        ) {
            DatePicker(
                state = datePickerState,
                dateValidator = {
                    Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault())
                        .toLocalDate() >= LocalDate.now()
                }
            )
        }
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
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
                        }
                    )
                }
                .padding(vertical = 8.dp)
                .clickable(
                    onClick = { isExpanded = !isExpanded },
                    role = Role.Button
                ),
        ) {
            Text(text = dateText, style = MaterialTheme.typography.bodyMedium)
            Icon(
                imageVector = Icons.Outlined.ExpandMore,
                contentDescription = "Options for dates"
            )
        }
        Divider()
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = !isExpanded },
            offset = dropdownOffset,
            modifier = Modifier.fillMaxWidth(.5f)
        ) {
            dateOptions.forEach { date ->
                DropdownMenuItem(
                    text = { Text(text = date.text) },
                    onClick = {
                        onDatePicked(date)
                        isExpanded = !isExpanded
                    }
                )
            }
            DropdownMenuItem(
                text = { Text(text = "Select a date..") },
                onClick = {
                    isExpanded = !isExpanded
                    isDialogOpen = !isDialogOpen
                }
            )
        }
    }
}