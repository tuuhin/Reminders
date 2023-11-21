package com.eva.reminders.presentation.feature_home.composables

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Label
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.eva.reminders.R
import com.eva.reminders.domain.models.TaskLabelModel
import com.eva.reminders.presentation.feature_home.utils.PreviewTaskModels
import com.eva.reminders.ui.theme.RemindersTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerLabelItems(
    modifier: Modifier = Modifier,
    onEdit: () -> Unit,
    labels: List<TaskLabelModel>,
) {

    val labelsCount by remember(labels) {
        derivedStateOf { "${labels.size}" }
    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = stringResource(id = R.string.subheading_labels),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Badge(
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ) {
            Text(
                text = labelsCount,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        item {
            NavigationDrawerItem(
                label = { Text(text = stringResource(id = R.string.create_new_label)) },
                selected = false,
                onClick = onEdit,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                }
            )
        }
        itemsIndexed(labels, key = { _, item -> item.id }) { _, item ->
            NavigationDrawerItem(
                label = {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                selected = false,
                onClick = onEdit,
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.Label,
                        contentDescription = stringResource(id = R.string.icon_label_desc)
                    )
                },
            )
        }
    }
}


class DrawerLabelItemsPreViewParams :
    CollectionPreviewParameterProvider<List<TaskLabelModel>>(
        listOf(
            emptyList(),
            PreviewTaskModels.taskLabelModelList
        )
    )

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun DrawerLabelItemsPreview(
    @PreviewParameter(DrawerLabelItemsPreViewParams::class)
    items: List<TaskLabelModel>
) = RemindersTheme {
    Surface(
        color = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
    ) {
        DrawerLabelItems(
            labels = items,
            onEdit = {},
        )
    }
}