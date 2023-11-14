package com.eva.reminders.presentation.feature_create.composables

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Keyboard
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.eva.reminders.R
import com.eva.reminders.ui.theme.RemindersTheme
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    showTextInput: Boolean,
    state: TimePickerState,
    onDismissRequest: () -> Unit,
    onCancel: () -> Unit,
    onConfirm: (LocalTime) -> Unit,
    onToggleMode: () -> Unit,
    modifier: Modifier = Modifier,
    properties: DialogProperties = DialogProperties(
        dismissOnClickOutside = false,
        usePlatformDefaultWidth = true
    )
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        properties = properties,
        modifier = modifier
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
            ),
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Column(
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.dialogs_internal_padding),
                    vertical = dimensionResource(id = R.dimen.dialogs_internal_padding_secondary)
                )
            ) {
                Text(
                    text = if (!showTextInput) stringResource(id = R.string.enter_time_text)
                    else stringResource(id = R.string.select_time_text),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dialogs_internal_padding)))
                when {
                    showTextInput -> TimePicker(
                        state = state,
                        layoutType = TimePickerLayoutType.Vertical
                    )

                    else -> TimeInput(state = state)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    IconButton(
                        onClick = onToggleMode,
                        colors = IconButtonDefaults
                            .iconButtonColors(contentColor = MaterialTheme.colorScheme.onSurface)
                    ) {
                        Icon(
                            if (showTextInput) Icons.Outlined.Keyboard
                            else Icons.Outlined.Schedule,
                            contentDescription = null
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TextButton(
                            onClick = onCancel,
                            colors = ButtonDefaults
                                .textButtonColors(contentColor = MaterialTheme.colorScheme.secondary)
                        ) {
                            Text(text = stringResource(id = R.string.dialog_cancel_button_text))
                        }
                        Button(
                            onClick = {
                                onDismissRequest()
                                val localtime = LocalTime.of(state.hour, state.minute)
                                onConfirm(localtime)
                            },
                            colors = ButtonDefaults.buttonColors(
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(text = stringResource(id = R.string.dialog_done_button_text))
                        }
                    }
                }
            }
        }
    }
}


private class BooleanPreviewParams :
    CollectionPreviewParameterProvider<Boolean>(listOf(true, false))

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun TimePickerDialogPreview(
    @PreviewParameter(BooleanPreviewParams::class)
    isTimeInputModePicker: Boolean
) = RemindersTheme {
    TimePickerDialog(
        showTextInput = isTimeInputModePicker,
        state = rememberTimePickerState(),
        onDismissRequest = { },
        onCancel = { },
        onConfirm = {},
        onToggleMode = {}
    )
}