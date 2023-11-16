package com.eva.reminders.presentation.feature_create.composables

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Label
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.eva.reminders.domain.models.TaskLabelModel
import com.eva.reminders.presentation.feature_create.utils.SelectLabelState
import com.eva.reminders.ui.theme.RemindersTheme

@Composable
fun SelectTaskLabelOption(
    item: SelectLabelState,
    onSelect: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    checkedColor: Color = MaterialTheme.colorScheme.primary,
    checkMarkColor: Color = MaterialTheme.colorScheme.onPrimary,
    textColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    leadingIconColor: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    Row(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .clickable(onClick = { onSelect(!item.isSelected) })
            .padding(start = 12.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.Label,
            contentDescription = item.model.label,
            tint = leadingIconColor,
        )
        Spacer(modifier = Modifier.weight(.1f))
        Text(
            text = item.model.label,
            style = MaterialTheme.typography.bodyLarge,
            color = textColor,
            modifier = Modifier.weight(.7f),
        )
        Checkbox(
            checked = item.isSelected,
            onCheckedChange = onSelect,
            colors = CheckboxDefaults.colors(
                checkedColor = checkedColor,
                checkmarkColor = checkMarkColor,
            )
        )
    }
}


class CheckLabelItemPreviewParams : CollectionPreviewParameterProvider<SelectLabelState>(
    listOf(
        SelectLabelState(model = TaskLabelModel(1, "One"), isSelected = true),
        SelectLabelState(model = TaskLabelModel(1, "One"), isSelected = false)
    )
)

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun SelectTaskLabelOptionPreview(
    @PreviewParameter(CheckLabelItemPreviewParams::class)
    item: SelectLabelState
) = RemindersTheme {
    Surface(color = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)) {
        SelectTaskLabelOption(
            item = item,
            onSelect = {}
        )
    }
}