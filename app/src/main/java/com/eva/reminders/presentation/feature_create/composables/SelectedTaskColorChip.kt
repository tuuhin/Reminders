package com.eva.reminders.presentation.feature_create.composables

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import com.eva.reminders.domain.enums.TaskColorEnum
import com.eva.reminders.ui.theme.RemindersTheme

@Composable
fun SelectedTaskColorChip(
    taskColor: TaskColorEnum?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium
) {
    val colorText by remember(taskColor) {
        derivedStateOf { taskColor?.toText() ?: "" }
    }

    AnimatedVisibility(
        visible = taskColor != null && taskColor != TaskColorEnum.TRANSPARENT,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutVertically() + fadeOut(),
    ) {

        taskColor?.let {
            val colorVal by animateColorAsState(
                targetValue = colorResource(id = taskColor.color),
                label = "",
                animationSpec = tween(800, easing = EaseInOutCubic)
            )

            val overlayColor = MaterialTheme.colorScheme.onSurfaceVariant

            AssistChip(
                modifier = modifier,
                onClick = onClick,
                label = { Text(text = colorText) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.WaterDrop,
                        contentDescription = colorText
                    )
                },
                colors = AssistChipDefaults
                    .assistChipColors(
                        containerColor = colorVal,
                        labelColor = overlayColor,
                        leadingIconContentColor = overlayColor
                    ),
                border = AssistChipDefaults.assistChipBorder(borderColor = overlayColor),
                shape = shape
            )
        }
    }
}


class ColorPreviewParams : CollectionPreviewParameterProvider<TaskColorEnum>(
    listOf(
        TaskColorEnum.BLUE,
        TaskColorEnum.AMBER,
        TaskColorEnum.GREEN
    )
)

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun SelectedColorChipPreview(
    @PreviewParameter(ColorPreviewParams::class)
    color: TaskColorEnum
) = RemindersTheme {
    SelectedTaskColorChip(
        taskColor = color,
        onClick = { }
    )
}