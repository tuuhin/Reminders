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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
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
    when {
        state.isEdit -> Column {
            Divider(color = MaterialTheme.colorScheme.outline)
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.weight(.1f),
                    colors = IconButtonDefaults
                        .iconButtonColors(contentColor = MaterialTheme.colorScheme.onSurface)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.DeleteOutline,
                        contentDescription = "Delete the label"
                    )
                }
                TextField(
                    value = state.updatedLabel,
                    onValueChange = onValueChange,
                    colors = TextFieldDefaults.noColor(),
                    keyboardActions = KeyboardActions(onDone = { onDone() }),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier.weight(.7f),
                    placeholder = {
                        Text(
                            text = state.previousLabel,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                )
                IconButton(
                    onClick = onCancel,
                    modifier = Modifier.weight(.1f),
                    colors = IconButtonDefaults
                        .iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        )
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
                        .iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Create Label"
                    )
                }
            }
            Divider(color = MaterialTheme.colorScheme.outline)
        }

        else -> Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Label,
                contentDescription = "Icon Labels",
                modifier = Modifier.weight(.1f)
            )
            Spacer(modifier = Modifier.weight(.1f))
            Text(
                text = state.previousLabel,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(.7f)
            )
            IconButton(
                onClick = onEdit,
                modifier = Modifier.weight(.1f),
                colors = IconButtonDefaults
                    .iconButtonColors(contentColor = MaterialTheme.colorScheme.secondary)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Edit Label"
                )
            }
        }
    }
}

private class EditableLabelsPreviewParams
    : CollectionPreviewParameterProvider<EditLabelState>(
    listOf(
        EditLabelState(isEdit = false, updatedLabel = "Updated label", previousLabel = "Old label"),
        EditLabelState(isEdit = true, updatedLabel = "Updated Label", previousLabel = "Old Label")
    )
)

@Preview(showBackground = true)
@Composable
private fun EditableLabelPreview(
    @PreviewParameter(EditableLabelsPreviewParams::class)
    state: EditLabelState
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