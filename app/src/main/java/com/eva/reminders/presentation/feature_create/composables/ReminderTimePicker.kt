package com.eva.reminders.presentation.feature_create.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.eva.reminders.R
import com.eva.reminders.presentation.feature_create.utils.ReminderTimeOptions
import com.eva.reminders.utils.toCurrentDateTime
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderTimePicker(
    scheduledDate: LocalDate,
    reminderTimeOption: ReminderTimeOptions,
    onTimePicked: (ReminderTimeOptions) -> Unit,
    modifier: Modifier = Modifier,
    errorText: String? = null,
    optionTextStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    optionColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {

    var isTimePickerDialogOpen by rememberSaveable { mutableStateOf(false) }

    var isExpanded by rememberSaveable { mutableStateOf(false) }

    var showTextInputPicker by rememberSaveable { mutableStateOf(false) }

    val reminderOptions by remember(scheduledDate) {
        derivedStateOf {
            ReminderTimeOptions.allOptionsExceptCustom(scheduledDate)
        }
    }


    val selectedTimeText by remember(reminderTimeOption) {
        derivedStateOf { reminderTimeOption.schedule.toCurrentDateTime() }
    }

    val currentTime by remember { derivedStateOf { LocalTime.now() } }

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.hour,
        initialMinute = currentTime.hour
    )

    if (isTimePickerDialogOpen) {

        TimePickerDialog(
            showTextInput = showTextInputPicker,
            state = timePickerState,
            onDismissRequest = { isTimePickerDialogOpen = !isTimePickerDialogOpen },
            onCancel = { isTimePickerDialogOpen = !isTimePickerDialogOpen },
            onConfirm = { localtime -> onTimePicked(ReminderTimeOptions.Custom(localtime)) },
            onToggleMode = { showTextInputPicker = !showTextInputPicker }
        )
    }

    PickerWithOptions(
        isExpanded = isExpanded,
        onToggleExpanded = { isExpanded = !isExpanded },
        selectedOptionTile = {
            Text(
                text = selectedTimeText,
                style = optionTextStyle,
                color = optionColor
            )
        },
        dropDownContent = {
            reminderOptions.forEachIndexed { idx, opt ->
                if (idx != 0) Divider()
                DropdownMenuItem(
                    enabled = opt.enable,
                    text = { Text(text = opt.text) },
                    trailingIcon = {
                        Text(
                            text = opt.schedule.toCurrentDateTime(),
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    onClick = {
                        onTimePicked(opt)
                        isExpanded = !isExpanded
                    },
                )
            }
            Divider()
            DropdownMenuItem(
                text = { Text(text = stringResource(id = R.string.pick_custom_time_text)) },
                onClick = {
                    isExpanded = !isExpanded
                    isTimePickerDialogOpen = !isTimePickerDialogOpen
                },
            )
        },
        errorText = {
            errorText?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        },
        modifier = modifier
    )
}
