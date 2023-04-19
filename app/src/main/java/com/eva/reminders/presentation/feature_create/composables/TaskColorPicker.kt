package com.eva.reminders.presentation.feature_create.composables

import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.FormatColorReset
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eva.reminders.domain.enums.TaskColorEnum
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskColorPicker(
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    state: SheetState,
    onColorChange: (TaskColorEnum) -> Unit,
    selectedColor: TaskColorEnum? = null
) {
    val scope = rememberCoroutineScope()

    if (isVisible)
        ModalBottomSheet(
            modifier = modifier,
            onDismissRequest = {
                if (state.isVisible)
                    scope.launch { state.hide() }
            },
            sheetState = state
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "Color",
                    modifier = Modifier.align(Alignment.Start),
                    style = MaterialTheme.typography.titleSmall,
                )
                ColorValues(
                    colors = TaskColorEnum.values(),
                    selectedColor = selectedColor,
                    onColorChange = onColorChange
                )
            }
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ColorValues(
    colors: Array<TaskColorEnum>,
    selectedColor: TaskColorEnum? = null,
    onColorChange: (TaskColorEnum) -> Unit,
) {
    LazyRow(
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(colors.size) { idx ->
            val color = colors[idx]
            Box(
                modifier = Modifier

                    .padding(horizontal = 4.dp)
                    .size(50.dp)
                    .background(
                        color = colorResource(id = color.color),
                        shape = RoundedCornerShape(50.dp)
                    )
                    .clickable(
                        onClick = { onColorChange(color) },
                        role = Role.Image,
                        onClickLabel = color.name
                    )
                    .run {
                        if (color == selectedColor)
                            border(
                                1.5.dp,
                                MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(50.dp)
                            )
                        else
                            border(
                                1.dp,
                                Color.DarkGray,
                                shape = RoundedCornerShape(50.dp)
                            )
                    }, contentAlignment = Alignment.Center
            ) {
                if (color == TaskColorEnum.TRANSPARENT) Icon(
                    imageVector = if (selectedColor == color)
                        Icons.Outlined.Check
                    else
                        Icons.Outlined.FormatColorReset,
                    contentDescription = "No color to be added",
                    tint = if (selectedColor == color)
                        MaterialTheme.colorScheme.surfaceTint
                    else
                        LocalContentColor.current
                )
                if (color == selectedColor) Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = "No color to be added",
                    tint = MaterialTheme.colorScheme.surfaceTint
                )
            }
        }
    }
}


@Composable
@Preview(backgroundColor = 0xffffffff, uiMode = UI_MODE_NIGHT_MASK)
private fun ColorValuesPreview() {
    ColorValues(
        colors = TaskColorEnum.values(),
        onColorChange = {
        }
    )
}