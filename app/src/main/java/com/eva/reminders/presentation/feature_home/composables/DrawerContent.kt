package com.eva.reminders.presentation.feature_home.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eva.reminders.R

@Composable
fun DrawerContent(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.titleLarge
            )
        }
        NavigationDrawerItem(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Lightbulb,
                    contentDescription = "Notes"
                )
            },
            label = { Text(text = "Notes") },
            selected = true,
            onClick = { /*TODO*/ }
        )
        NavigationDrawerItem(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Reminders"
                )
            },
            label = { Text(text = "Reminders") },
            selected = false,
            onClick = { /*TODO*/ }
        )
        Divider()
        DrawerLabelItems(modifier = Modifier.weight(1f))
        Divider()
        NavigationDrawerItem(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Archive,
                    contentDescription = "Archived"
                )
            },
            label = { Text(text = "Archived") },
            selected = false,
            onClick = { /*TODO*/ }
        )
        NavigationDrawerItem(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.DeleteOutline,
                    contentDescription = "Deleted"
                )
            },
            label = { Text(text = "Deleted") },
            selected = false,
            onClick = { /*TODO*/ }
        )
    }
}

@Composable
@Preview
private fun DrawerItemsPreview() {
    DrawerContent()
}