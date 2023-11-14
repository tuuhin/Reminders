package com.eva.reminders.presentation.feature_create.composables

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.eva.reminders.R
import com.eva.reminders.presentation.feature_create.utils.ReminderDateOptions
import com.eva.reminders.ui.theme.RemindersTheme
import com.eva.reminders.utils.formatToDayMonth
import com.eva.reminders.utils.formatToDayMonthShort
import com.eva.reminders.utils.millisToLocalDateTime
import java.time.LocalDate
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderDatePicker(
    option: ReminderDateOptions,
    onDatePicked: (ReminderDateOptions) -> Unit,
    modifier: Modifier = Modifier,
    properties: DialogProperties = DialogProperties(
        usePlatformDefaultWidth = false,
        dismissOnClickOutside = false,
        dismissOnBackPress = false
    ),
) {

    var isDialogOpen by rememberSaveable { mutableStateOf(false) }

    var isDropDownExpanded by rememberSaveable { mutableStateOf(false) }


    val dateText by remember(option.schedule) { derivedStateOf { option.schedule.formatToDayMonth() } }

    val dateOptions = remember {
        listOf(
            ReminderDateOptions.Today(),
            ReminderDateOptions.Tomorrow(),
            ReminderDateOptions.NextWeek()
        )
    }

    fun dateValidator(millis: Long): Boolean =
        millisToLocalDateTime(millis).toLocalDate() >= LocalDate.now()

    val datePickerState = rememberDatePickerState(
        initialDisplayMode = DisplayMode.Picker,
    )

    if (isDialogOpen) {

        DatePickerDialog(
            onDismissRequest = { isDialogOpen = !isDialogOpen },
            dismissButton = {
                TextButton(
                    onClick = { isDialogOpen = !isDialogOpen },
                    colors = ButtonDefaults
                        .textButtonColors(contentColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text(
                        text = stringResource(id = R.string.dialog_cancel_button_text)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        isDialogOpen = !isDialogOpen
                        val millis = datePickerState.selectedDateMillis
                        if (millis != null) {
                            val pickedDate = millisToLocalDateTime(millis).toLocalDate()
                            onDatePicked(ReminderDateOptions.Custom(date = pickedDate))
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.dialog_done_button_text)
                    )
                }
            },
            properties = properties,
            shape = MaterialTheme.shapes.large,
            tonalElevation = 4.dp
        ) {
            DatePicker(
                state = datePickerState,
                dateValidator = ::dateValidator,
                showModeToggle = true,
                title = {
                    Text(
                        text = stringResource(id = R.string.pick_reminder_date_title),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(
                            PaddingValues(start = 24.dp, end = 12.dp, top = 16.dp)
                        )
                    )
                },
                colors = DatePickerDefaults
                    .colors(titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer),
            )
        }
    }

    PickerWithOptions(
        isExpanded = isDropDownExpanded,
        onToggleExpanded = { isDropDownExpanded = !isDropDownExpanded },
        selectedOptionTile = {
            Text(text = dateText, style = MaterialTheme.typography.bodyMedium)
        },
        dropDownContent = {
            dateOptions.forEachIndexed { idx, date ->
                if (idx != 0) Divider()
                DropdownMenuItem(
                    text = { Text(text = date.text) },
                    onClick = {
                        onDatePicked(date)
                        isDropDownExpanded = !isDropDownExpanded
                    },
                    trailingIcon = {
                        Text(
                            text = date.schedule.formatToDayMonthShort(),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                )
            }
            Divider()
            DropdownMenuItem(
                onClick = {
                    isDropDownExpanded = !isDropDownExpanded
                    isDialogOpen = !isDialogOpen
                },
                text = {
                    Text(text = stringResource(id = R.string.pick_custom_date_text))
                },
            )
        },
        modifier = modifier
    )
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun ReminderDatePickerPreview() = RemindersTheme {
    Surface(color = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)) {
        ReminderDatePicker(
            option = ReminderDateOptions.Today(),
            onDatePicked = {},
        )
    }
}