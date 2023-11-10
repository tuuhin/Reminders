package com.eva.reminders.presentation.feature_labels.composabels

import android.content.res.Configuration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Label
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.eva.reminders.R
import com.eva.reminders.ui.theme.RemindersTheme

@Composable
fun TaskLabelsReadOnly(
    label: String,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    ListItem(
        headlineContent = {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        leadingContent = {
            Icon(
                imageVector = Icons.Outlined.Label,
                contentDescription = stringResource(id = R.string.icon_label_desc),
            )
        },
        trailingContent = {
            IconButton(
                onClick = onEdit,

                colors = IconButtonDefaults
                    .iconButtonColors(contentColor = MaterialTheme.colorScheme.secondary)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = stringResource(id = R.string.edit_icon_desc)
                )
            }
        },
        modifier = modifier
    )
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun TaskLabelReadOnlyPreview() = RemindersTheme {
    Surface(color = MaterialTheme.colorScheme.background) {
        TaskLabelsReadOnly(
            label = "This Is The One",
            onEdit = {},
        )
    }
}