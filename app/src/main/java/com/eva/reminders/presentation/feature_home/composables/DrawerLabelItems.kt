package com.eva.reminders.presentation.feature_home.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Label
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DrawerLabelItems(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Labels", style = MaterialTheme.typography.bodyLarge)
            TextButton(onClick = {}) {
                Text(text = "Edit", style = MaterialTheme.typography.bodySmall)
            }
        }
        LazyColumn {
            items(10) { _ ->
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp)) {
                    Icon(imageVector = Icons.Outlined.Label, contentDescription = "Item Label")
                }
            }
        }

        TextButton(onClick = {}) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add New Label")
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "Create New Label")
        }
    }
}

@Composable
@Preview
private fun DrawerLabelItemsPreview() {
    DrawerLabelItems()
}