package com.eva.reminders.presentation.feature_labels.composabels

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Label
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.eva.reminders.presentation.feature_labels.utils.EditLabelState
import com.eva.reminders.presentation.utils.noColor

@Composable
fun EditableLabels(
    state: EditLabelState,
    onEdit: () -> Unit,
    onDone: () -> Unit,
    onDelete: () -> Unit,
    onCancel: () -> Unit,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (!state.isEdit) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Label,
                contentDescription = "Icon Labels",
                modifier = Modifier.weight(.1f)
            )
            Spacer(modifier = Modifier.weight(.1f))
            Text(
                text = state.prevText,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(.7f)
            )
            IconButton(
                onClick = onEdit,
                modifier = Modifier.weight(.1f)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Edit Label"
                )
            }
        }
    } else {
        Column {
            Divider()
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.weight(.1f)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.DeleteOutline,
                        contentDescription = "Delete the label"
                    )
                }
                TextField(
                    value = state.text,
                    onValueChange = onValueChange,
                    colors = TextFieldDefaults.noColor(),
                    keyboardActions = KeyboardActions(onDone = { onDone() }),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        keyboardType = KeyboardType.Text
                    ),
                    modifier = Modifier.weight(.7f),
                    placeholder = { Text(text = state.prevText) }
                )
                IconButton(
                    onClick = onCancel,
                    modifier = Modifier.weight(.1f),
                    colors = IconButtonDefaults
                        .iconButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "Cancel label update"
                    )
                }
                IconButton(
                    onClick = onDone,
                    modifier = Modifier.weight(.1f),
                    colors = IconButtonDefaults
                        .iconButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Create Label"
                    )
                }
            }
            Divider()
        }
    }
}

private class EditableLabelsPreviewParams
    : PreviewParameterProvider<EditLabelState> {
    override val values: Sequence<EditLabelState> = sequenceOf(
        EditLabelState(isEdit = true),
        EditLabelState(isEdit = false)
    )
}

@Composable
@Preview
private fun EditableLabelPreview(
    @PreviewParameter(EditableLabelsPreviewParams::class) state: EditLabelState
) {
    EditableLabels(
        state = state,
        onEdit = { },
        onDone = { },
        onDelete = { },
        onCancel = { },
        onValueChange = {}
    )
}