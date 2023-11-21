package com.eva.reminders.presentation.feature_create.composables

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eva.reminders.R
import com.eva.reminders.presentation.feature_create.utils.ReminderFrequency
import com.eva.reminders.ui.theme.RemindersTheme

@Composable
fun ReminderFrequencyPicker(
    isExact: Boolean,
    frequency: ReminderFrequency,
    modifier: Modifier = Modifier,
    onReminderFrequency: (ReminderFrequency) -> Unit,
    onIsExactChange: (Boolean) -> Unit,
    optionTextStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    optionColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {

    var isExpanded by remember { mutableStateOf(false) }

    val reminderFrequenciesOptions by remember {
        derivedStateOf { ReminderFrequency.entries.reversed() }
    }

    Column {
        PickerWithOptions(
            isExpanded = isExpanded,
            onToggleExpanded = { isExpanded = !isExpanded },
            selectedOptionTile = {
                Text(
                    text = when (frequency) {
                        ReminderFrequency.DAILY -> stringResource(id = R.string.reminder_repeat_daily)
                        ReminderFrequency.DO_NOT_REPEAT -> stringResource(id = R.string.reminder_do_not_repeat)
                    },
                    style = optionTextStyle,
                    color = optionColor
                )
            },
            dropDownContent = {
                reminderFrequenciesOptions.forEachIndexed { idx, freq ->
                    if (idx != 0) Divider()
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = when (freq) {
                                    ReminderFrequency.DAILY -> stringResource(id = R.string.reminder_repeat_daily)
                                    ReminderFrequency.DO_NOT_REPEAT -> stringResource(id = R.string.reminder_do_not_repeat)
                                },
                            )
                        },
                        onClick = {
                            onReminderFrequency(freq)
                            isExpanded = !isExpanded
                        }
                    )
                }
            },
            modifier = modifier
        )

        AnimatedVisibility(
            visible = frequency == ReminderFrequency.DAILY,
            enter = slideInVertically(),
            exit = slideOutVertically(),
            modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.reminder_pickers_option_outer_padding))
        ) {
            Row(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .fillMaxWidth()
                    .clickable(
                        onClick = { onIsExactChange(!isExact) },
                        role = Role.Checkbox
                    )
                    .padding(start = dimensionResource(R.dimen.reminder_pickers_option_horizontal_padding)),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(id = R.string.reminder_schedule_time_exact),
                    style = optionTextStyle,
                    color = optionColor
                )
                Checkbox(
                    checked = isExact,
                    onCheckedChange = onIsExactChange,
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.secondary,
                        checkmarkColor = MaterialTheme.colorScheme.onSecondary
                    )
                )
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
fun ReminderFrequencyPickerPreview() = RemindersTheme {
    Surface(
        color = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
    ) {
        ReminderFrequencyPicker(
            frequency = ReminderFrequency.DAILY,
            isExact = true,
            onReminderFrequency = {},
            onIsExactChange = {}
        )
    }
}