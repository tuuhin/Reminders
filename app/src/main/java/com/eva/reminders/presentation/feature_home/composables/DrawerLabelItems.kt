package com.eva.reminders.presentation.feature_home.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Label
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.eva.reminders.domain.models.TaskLabelModel
import com.eva.reminders.presentation.feature_home.utils.PreviewTaskModels

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerLabelItems(
    modifier: Modifier = Modifier,
    onEdit: () -> Unit,
    labels: List<TaskLabelModel>,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = "Labels ",
            style = MaterialTheme.typography.titleMedium
        )
        Badge(
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ) {
            Text(
                text = "${labels.size}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        item {
            NavigationDrawerItem(
                label = { Text(text = "Create New Label") },
                selected = false,
                onClick = onEdit,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add New Label"
                    )
                }
            )
        }
        itemsIndexed(labels) { _, item ->
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
                        contentDescription = "Item Label"
                    )
                },
            )
        }
    }
}


class DrawerLabelItemsPreViewParams : CollectionPreviewParameterProvider<List<TaskLabelModel>>(
    listOf(
        emptyList(),
        PreviewTaskModels.taskLabelModelList
    )
)

@Preview
@Composable
fun DrawerLabelItemsPreview(
    @PreviewParameter(DrawerLabelItemsPreViewParams::class)
    items: List<TaskLabelModel>
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
    ) {
        DrawerLabelItems(onEdit = {}, labels = items)
    }
}