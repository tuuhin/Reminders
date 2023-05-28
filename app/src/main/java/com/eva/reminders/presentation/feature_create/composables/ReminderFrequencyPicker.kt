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
import com.eva.reminders.presentation.feature_create.utils.ReminderFrequency

@Composable
fun ReminderFrequencyPicker(
    current: ReminderFrequency,
    modifier: Modifier = Modifier,
    onReminderFrequency: (ReminderFrequency) -> Unit,
) {
    var dropdownOffset by remember { mutableStateOf(DpOffset.Zero) }

    var isExpanded by remember { mutableStateOf(false) }

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
                .fillMaxWidth()
                .pointerInput(true) {
                    detectTapGestures(
                        onTap = { tapOffset ->
                            dropdownOffset = DpOffset(tapOffset.x.toDp(), tapOffset.y.toDp())
                        }
                    )
                }
                .padding(vertical = 8.dp)
                .clickable(
                    onClick = { isExpanded = !isExpanded }, role = Role.Button
                ),
        ) {
            Text(
                text = when (current) {
                    ReminderFrequency.DAILY -> "Daily"
                    ReminderFrequency.DO_NOT_REPEAT -> "Do not repeat"
                },
                style = MaterialTheme.typography.bodyMedium
            )
            Icon(
                imageVector = Icons.Outlined.ExpandMore, contentDescription = "Options for dates"
            )
        }
        Divider()
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = !isExpanded },
            offset = dropdownOffset,
            modifier = Modifier.fillMaxWidth(.5f)
        ) {
            ReminderFrequency.values().forEach { freq ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = when (freq) {
                                ReminderFrequency.DAILY -> "Daily"
                                ReminderFrequency.DO_NOT_REPEAT -> "Do not repeat"
                            }
                        )
                    },
                    onClick = {
                        onReminderFrequency(freq)
                        isExpanded = !isExpanded
                    }
                )
                Divider()
            }
        }

    }
}