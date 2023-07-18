package com.eva.reminders.presentation.feature_labels.composabels

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
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
    val focusRequest = remember { FocusRequester() }

    Crossfade(
        targetState = state.isEnabled,
        label = "Create New Label"
    ) { enabled ->
        when {
            enabled -> {
                LaunchedEffect(Unit) {
                    focusRequest.requestFocus()
                }
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .clickable(onClick = onAdd, role = Role.Button),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    IconButton(
                        onClick = onCancel,
                        modifier = Modifier.weight(.1f)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = "Cancel Label Creation"
                        )
                    }
                    Column(
                        modifier = Modifier.weight(.7f)
                    ) {
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
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequest),
                        )

                        state.isError?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    IconButton(
                        onClick = {
                            onDone()
                            focusRequest.freeFocus()
                        },
                        modifier = Modifier.weight(.1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Create Label"
                        )
                    }
                }
            }

            else -> Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable(onClick = onAdd, role = Role.Button),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onAdd,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = "Add label"
                    )
                }
                Text(
                    text = "Create New Label",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                )
            }
        }
    }
}


private class CreateNewLabelPreviewParams
    : PreviewParameterProvider<CreateLabelState> {
    override val values: Sequence<CreateLabelState> = sequenceOf(
        CreateLabelState(isEnabled = false),
        CreateLabelState(isEnabled = true, isError = "Some error"),
        CreateLabelState(isEnabled = true)
    )
}

@Composable
@Preview
private fun CreateNewLabelPreview(
    @PreviewParameter(CreateNewLabelPreviewParams::class) state: CreateLabelState
) {
    Surface(color = MaterialTheme.colorScheme.surface) {
        CreateNewLabel(
            state = state,
            onAdd = { },
            onCancel = { },
            onDone = { },
            onValueChange = {}
        )
    }
}