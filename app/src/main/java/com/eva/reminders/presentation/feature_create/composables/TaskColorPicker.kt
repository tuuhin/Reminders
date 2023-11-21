package com.eva.reminders.presentation.feature_create.composables

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBarsIgnoringVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.FormatColorReset
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eva.reminders.R
import com.eva.reminders.domain.enums.TaskColorEnum
import com.eva.reminders.ui.theme.RemindersTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun TaskColorPicker(
    modifier: Modifier = Modifier,
    state: SheetState,
    onColorChange: (TaskColorEnum) -> Unit,
    selectedColor: TaskColorEnum? = null
) {
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = {
            if (state.isVisible)
                scope.launch { state.hide() }
        },
        sheetState = state,
        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp),
        windowInsets = WindowInsets.navigationBarsIgnoringVisibility
    ) {
        Column(
            modifier = Modifier.padding(all = 8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.color_picker_title),
                modifier = Modifier.align(Alignment.Start),
                style = MaterialTheme.typography.titleSmall,
            )
            ColorPickerOptions(
                taskColors = TaskColorEnum.entries.toImmutableList(),
                selectedColor = selectedColor,
                onColorChange = onColorChange
            )
        }

    }
}

@Composable
private fun ColorPickerOptions(
    taskColors: ImmutableList<TaskColorEnum>,
    selectedColor: TaskColorEnum? = null,
    onColorChange: (TaskColorEnum) -> Unit,
) {
    val state = rememberLazyListState()

    LazyRow(
        state = state,
        contentPadding = PaddingValues(4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        itemsIndexed(taskColors, key = { _, color -> color.color }) { _, taskColor ->

            ColorOption(
                isSelected = selectedColor == taskColor,
                taskColor = taskColor,
                onColorChange = onColorChange,
            )
        }
    }
}

@Composable
fun ColorOption(
    isSelected: Boolean,
    taskColor: TaskColorEnum,
    onColorChange: (TaskColorEnum) -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape
) {
    val boxColor = colorResource(id = taskColor.color)

    val onBoxColor = contentColorFor(backgroundColor = boxColor)

    val borderModifier = if (isSelected)
        Modifier.border(1.dp, onBoxColor, shape = shape)
    else Modifier.border(1.dp, MaterialTheme.colorScheme.onSurfaceVariant, shape = shape)

    Box(
        modifier = modifier
            .clip(CircleShape)
            .size(size = dimensionResource(id = R.dimen.color_option_size))
            .background(color = boxColor, shape = shape)
            .clickable(
                onClick = { onColorChange(taskColor) },
                role = Role.Image,
            )
            .then(borderModifier),
        contentAlignment = Alignment.Center
    ) {
        if (taskColor == TaskColorEnum.TRANSPARENT && !isSelected)
            Icon(
                imageVector = Icons.Outlined.FormatColorReset,
                contentDescription = null,
                tint = contentColorFor(backgroundColor = boxColor)
            )
        if (isSelected) Icon(
            imageVector = Icons.Outlined.Check,
            contentDescription = null,
            tint = contentColorFor(backgroundColor = boxColor)
        )
    }
}


@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun ColorValuesPreview() = RemindersTheme {
    Surface(color = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)) {
        ColorPickerOptions(
            taskColors = TaskColorEnum.entries.toImmutableList(),
            onColorChange = {}
        )
    }
}