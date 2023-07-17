package com.eva.reminders.presentation.feature_home.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FormatColorReset
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
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
        contentPadding = PaddingValues(4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(colors) { _, color ->
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(color = colorResource(id = color.color))
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.outline,
                            shape = CircleShape
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
                Text(
                    text = if (color != TaskColorEnum.TRANSPARENT)
                        color.name
                    else "CLEAR",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline,
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SearchColorOptionsPreview() {
    SearchOptionColor(
        colors = TaskColorEnum.values().toList(),
        onColorSelect = {}
    )
}