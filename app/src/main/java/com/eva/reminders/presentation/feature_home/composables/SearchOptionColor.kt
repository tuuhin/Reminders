package com.eva.reminders.presentation.feature_home.composables

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FormatColorReset
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eva.reminders.domain.enums.TaskColorEnum
import com.eva.reminders.ui.theme.RemindersTheme

@Composable
fun SearchOptionColor(
    colors: List<TaskColorEnum>,
    onColorSelect: (TaskColorEnum) -> Unit,
    modifier: Modifier = Modifier,
    optionShape: RoundedCornerShape = CircleShape,
    borderStroke: BorderStroke = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
) {

    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(all = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(colors, key = { _, color -> color.color }) { _, color ->

            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(optionShape)
                        .border(borderStroke,optionShape)
                        .background(color = colorResource(id = color.color))
                        .clickable(onClick = { onColorSelect(color) }),
                    contentAlignment = Alignment.Center
                ) {
                    if (color == TaskColorEnum.TRANSPARENT) {
                        Icon(
                            imageVector = Icons.Outlined.FormatColorReset,
                            contentDescription = null
                        )
                    }
                }
                if (color != TaskColorEnum.TRANSPARENT)
                    Text(
                        text = color.name,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline,
                    )
            }
        }
    }
}


@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun SearchColorOptionsPreview() = RemindersTheme {
    Surface(color = MaterialTheme.colorScheme.background) {
        SearchOptionColor(
            colors = TaskColorEnum.values().toList(),
            onColorSelect = {}
        )
    }
}