package com.eva.reminders.presentation.feature_labels.composabels

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eva.reminders.R
import com.eva.reminders.presentation.feature_labels.utils.FeatureLabelsTestTags
import com.eva.reminders.presentation.utils.noColor
import com.eva.reminders.ui.theme.RemindersTheme

@Composable
fun TaskLabelsEditable(
    previous: String,
    edited: String,
    onValueChange: (String) -> Unit,
    onDelete: () -> Unit,
    onCancel: () -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onDelete,
            modifier = Modifier
                .weight(.1f)
                .testTag(FeatureLabelsTestTags.DELETE_LABEL_ACTION_TEST_TAG),
            colors = IconButtonDefaults
                .iconButtonColors(contentColor = MaterialTheme.colorScheme.onSurface)
        ) {
            Icon(
                imageVector = Icons.Outlined.DeleteOutline,
                contentDescription = stringResource(id = R.string.icon_delete_desc)
            )
        }
        TextField(
            value = edited,
            onValueChange = onValueChange,
            singleLine = true,
            keyboardActions = KeyboardActions(onDone = { onDone() }),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            textStyle = MaterialTheme.typography.bodyMedium,
            placeholder = {
                Text(
                    text = previous,
                    color = MaterialTheme.colorScheme.outline,
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
            colors = TextFieldDefaults.noColor(),
            modifier = Modifier
                .weight(.7f)
                .testTag(FeatureLabelsTestTags.UPDATE_LABEL_TEXT_FIELD)
        )
        IconButton(
            onClick = onCancel,
            modifier = Modifier
                .weight(.1f)
                .testTag(FeatureLabelsTestTags.CANCEL_UPDATE_ACTION_TEST_TAG),
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
        ) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = stringResource(id = R.string.close_icon_desc)
            )
        }
        IconButton(
            onClick = onDone,
            modifier = Modifier
                .weight(.1f)
                .testTag(FeatureLabelsTestTags.UPDATE_LABEL_ACTION_TEST_TAG),
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = stringResource(id = R.string.icon_check_desc)
            )
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun TaskLabelEditablePreview() = RemindersTheme {
    Surface(color = MaterialTheme.colorScheme.background) {
        TaskLabelsEditable(
            previous = "Previous",
            edited = "New",
            onValueChange = {},
            onDelete = {},
            onCancel = {},
            onDone = {},
        )
    }
}