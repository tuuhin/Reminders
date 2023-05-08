package com.eva.reminders.presentation.feature_home.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FormatColorReset
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.eva.reminders.domain.enums.TaskColorEnum

@Composable
fun SearchOptionColor(
    colors: List<TaskColorEnum>,
    onColorSelect: (TaskColorEnum) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(colors) { _, color ->
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        color = colorResource(id = color.color),
                        shape = MaterialTheme.shapes.large
                    )
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.outline,
                        shape = MaterialTheme.shapes.large
                    )
                    .clickable(onClick = { onColorSelect(color) }),
                contentAlignment = Alignment.Center
            ) {
                if (color == TaskColorEnum.TRANSPARENT) {
                    Icon(
                        imageVector = Icons.Outlined.FormatColorReset,
                        contentDescription = "Label "
                    )
                }
            }
        }
    }
}