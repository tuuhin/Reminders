package com.eva.reminders.presentation.feature_labels.composabels

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.eva.reminders.presentation.feature_labels.utils.CreateLabelState
import com.eva.reminders.presentation.utils.noColor

@Composable
fun CreateNewLabel(
    state: CreateLabelState,
    modifier: Modifier = Modifier,
    onAdd: () -> Unit,
    onCancel: () -> Unit,
    onDone: () -> Unit,
    onValueChange: (String) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onAdd, role = Role.Button),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!state.isEnabled) {
            IconButton(
                onClick = onAdd,
                modifier = Modifier.weight(.1f)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "Add label"
                )
            }
            Spacer(modifier = Modifier.weight(.1f))
            Text(
                text = "Create New Label",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(.8f)
            )
        } else {
            IconButton(
                onClick = onCancel,
                modifier = Modifier.weight(.1f)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "Cancel Label Creation"
                )
            }
            TextField(
                value = state.label,
                onValueChange = onValueChange,
                colors = TextFieldDefaults.noColor(),
                keyboardActions = KeyboardActions(onDone = { onDone() }),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                textStyle = MaterialTheme.typography.bodyMedium,
                placeholder = {
                    Text(
                        text = "New Label",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            )
            IconButton(
                onClick = onDone,
                modifier = Modifier.weight(.1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Create Label"
                )
            }
        }
    }
}