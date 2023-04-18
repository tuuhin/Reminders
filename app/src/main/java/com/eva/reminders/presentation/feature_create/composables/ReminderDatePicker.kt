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
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderDatePicker(
    date: LocalDate,
    onDatePicked: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isDialogOpen by remember { mutableStateOf(false) }

    var isExpanded by remember { mutableStateOf(false) }

    var dropdownOffset by remember { mutableStateOf(DpOffset.Zero) }

    val rememberedDate by remember {
        derivedStateOf {
            LocalDate.now().dayOfWeek.name.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.ROOT
                ) else it.toString()
            }
        }
    }

    val dateText by remember(date) {
        derivedStateOf {
            date.format(DateTimeFormatter.ofPattern("dd MMMM"))
        }
    }

    if (isDialogOpen) {
        val datePickerState = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)

        LaunchedEffect(datePickerState.selectedDateMillis) {
            val mills = datePickerState.selectedDateMillis
            if (mills != null) {
                val pickedDate = Instant.ofEpochMilli(mills).atZone(ZoneId.systemDefault())
                    .toLocalDate()
                onDatePicked(pickedDate)
            }
        }

        DatePickerDialog(
            onDismissRequest = { isDialogOpen = !isDialogOpen },
            dismissButton = {
                TextButton(onClick = { isDialogOpen = !isDialogOpen }) { Text(text = "Cancel") }
            },
            confirmButton = {
                TextButton(onClick = { isDialogOpen = !isDialogOpen }) { Text(text = "Done") }
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
        modifier = modifier.fillMaxWidth()
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
            Text(text = dateText)
            Icon(
                imageVector = Icons.Outlined.ExpandMore,
                contentDescription = "Options for dates"
            )
        }
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = !isExpanded },
            offset = dropdownOffset,
            modifier = Modifier.width(300.dp)
        ) {
            DropdownMenuItem(
                text = { Text(text = "Today") },
                onClick = {
                    onDatePicked(LocalDate.now())
                },
            )
            DropdownMenuItem(
                text = { Text(text = "Tomorrow") },
                onClick = {
                    onDatePicked(LocalDate.now().plusDays(1))
                }
            )
            DropdownMenuItem(
                text = {
                    Text(text = "Next $rememberedDate")
                },
                onClick = {
                    onDatePicked(LocalDate.now().plusWeeks(1))
                }
            )
            DropdownMenuItem(
                text = { Text(text = "Select a Date") },
                onClick = {
                    isDialogOpen = !isDialogOpen
                    isExpanded = !isExpanded
                }
            )
        }
    }
}