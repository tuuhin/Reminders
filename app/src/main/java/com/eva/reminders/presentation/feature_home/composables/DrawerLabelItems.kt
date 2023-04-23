package com.eva.reminders.presentation.feature_home.composables

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.eva.reminders.domain.models.TaskLabelModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DrawerLabelItems(
    modifier: Modifier = Modifier,
    onEdit: () -> Unit,
    labels: List<TaskLabelModel>,
) {
    Divider()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Labels")
        TextButton(onClick = onEdit) {
            Text(
                text = "Edit",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
    LazyColumn(
        modifier = modifier
            .wrapContentHeight()
    ) {
        stickyHeader {
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
                label = { Text(text = item.label) },
                selected = false,
                onClick = onEdit,
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.Label,
                        contentDescription = "Item Label"
                    )
                }
            )
        }
    }
}


class DrawerLabelItemsPreViewParams : PreviewParameterProvider<List<TaskLabelModel>> {
    override val values: Sequence<List<TaskLabelModel>> = sequenceOf(
        emptyList(),
        listOf(
            TaskLabelModel(0, "One"),
            TaskLabelModel(1, "Two"),
            TaskLabelModel(2, "Three")
        )
    )
}


@Composable
@Preview(name = "Drawer Labels")
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