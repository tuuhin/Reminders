package com.eva.reminders.presentation.feature_create.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.DialogProperties
import com.eva.reminders.R

@Composable
fun DeleteReminderDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = AlertDialogDefaults.shape,
    containerColor: Color = AlertDialogDefaults.containerColor,
    tonalElevation: Dp = AlertDialogDefaults.TonalElevation,
    properties: DialogProperties = DialogProperties()
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(text = stringResource(id = R.string.dialog_delete_button_text))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onCancel,
                colors = ButtonDefaults
                    .textButtonColors(contentColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text(text = stringResource(id = R.string.dialog_cancel_button_text))
            }
        },
        title = { Text(text = stringResource(id = R.string.dialog_title_are_you_sure)) },
        text = { Text(text = stringResource(id = R.string.delete_dialog_text)) },
        modifier = modifier,
        shape = shape,
        containerColor = containerColor,
        tonalElevation = tonalElevation,
        properties = properties,
    )
}