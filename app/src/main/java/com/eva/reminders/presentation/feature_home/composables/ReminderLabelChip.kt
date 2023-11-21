package com.eva.reminders.presentation.feature_home.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.eva.reminders.R

@Composable
fun ReminderLabelChip(
    label: String,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.small,
    background: Color = colorResource(id = R.color.white_overlay),
    borderColor: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(color = background)
            .border(1.dp, borderColor, shape)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = borderColor,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}