package com.eva.reminders.presentation.feature_labels.composabels

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Label
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.eva.reminders.presentation.feature_labels.utils.SelectLabelState

@Composable
fun CheckLabelItem(
    item: SelectLabelState,
    onSelect: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.Label,
            contentDescription = "Label for ${item.label}",
            modifier = Modifier.weight(.1f)
        )
        Text(
            text = item.label,
            modifier = Modifier.weight(.7f),
            style = MaterialTheme.typography.bodyMedium
        )
        Checkbox(
            checked = item.isSelected,
            onCheckedChange = onSelect,
            modifier = Modifier.weight(.2f)
        )
    }
}

@Composable
@Preview
private fun CheckLabelItemPreview() {
    Surface(
        color = MaterialTheme.colorScheme.surface
    ) {
        CheckLabelItem(
            item = SelectLabelState(label = "SOMETHING", idx = 0),
            onSelect = {}
        )
    }
}