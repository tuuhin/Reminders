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
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderTimePicker(
    time: LocalTime,
    onTimePicked: (LocalTime) -> Unit,
    modifier: Modifier = Modifier,
) {

    var isDialogOpen by remember { mutableStateOf(false) }

    var isExpanded by remember { mutableStateOf(false) }

    var dropdownOffset by remember { mutableStateOf(DpOffset.Zero) }

    val selectedTimeText by remember(time) {
        derivedStateOf {
            time.format(DateTimeFormatter.ofPattern("hh:mm a"))
        }
    }

    if (isDialogOpen) {
        val timePickerState = rememberTimePickerState()

        LaunchedEffect(key1 = timePickerState) {
            val localtime = LocalTime.of(timePickerState.hour, timePickerState.minute)
            onTimePicked(localtime)
        }

        DatePickerDialog(
            modifier = Modifier.padding(horizontal = 10.dp),
            onDismissRequest = { isDialogOpen = !isDialogOpen },
            confirmButton = {
                Button(onClick = { isDialogOpen = !isDialogOpen }) {
                    Text(text = "Done")
                }
            },
            dismissButton = {
                TextButton(onClick = { isDialogOpen = !isDialogOpen }) {
                    Text(text = "Cancel")
                }
            },
            properties = DialogProperties(usePlatformDefaultWidth = false)
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
            Text(text = selectedTimeText)
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
                enabled = LocalTime.now() < LocalTime.of(8, 0),
                text = { Text(text = "Morning") },
                trailingIcon = {
                    Text(
                        text = LocalTime.of(8, 0).format(DateTimeFormatter.ofPattern("hh:mm"))
                    )
                },
                onClick = {
                    onTimePicked(LocalTime.of(8, 0))
                },
            )
        }
    }

}
