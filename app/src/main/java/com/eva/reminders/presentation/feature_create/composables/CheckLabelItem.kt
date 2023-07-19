package com.eva.reminders.presentation.feature_create.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Label
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import com.eva.reminders.presentation.feature_create.utils.SelectLabelState

@Composable
fun CheckLabelItem(
    item: SelectLabelState,
    onSelect: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    checkedColor: Color = MaterialTheme.colorScheme.primary,
    checkMarkColor: Color = MaterialTheme.colorScheme.onPrimary,
    textColor: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.Label,
            contentDescription = "Label for ${item.label}",
            modifier = Modifier.weight(.1f),
            tint = MaterialTheme.colorScheme.onSecondaryContainer
        )
        Spacer(modifier = Modifier.weight(.1f))
        Text(
            text = item.label,
            modifier = Modifier.weight(.6f),
            style = MaterialTheme.typography.bodyMedium,
            color = textColor
        )
        Checkbox(
            checked = item.isSelected,
            onCheckedChange = onSelect,
            modifier = Modifier.weight(.2f),
            colors = CheckboxDefaults.colors(
                checkedColor = checkedColor,
                checkmarkColor = checkMarkColor,
            )
        )
    }
}


class CheckLabelItemPreviewParams : CollectionPreviewParameterProvider<SelectLabelState>(
    listOf(
        SelectLabelState(label = "Something", idx = 0),
        SelectLabelState(label = "Something", idx = 0, isSelected = true)
    )
)

@Composable
@Preview
private fun CheckLabelItemPreview(
    @PreviewParameter(CheckLabelItemPreviewParams::class)
    item: SelectLabelState
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        CheckLabelItem(
            item = item,
            onSelect = {}
        )
    }
}