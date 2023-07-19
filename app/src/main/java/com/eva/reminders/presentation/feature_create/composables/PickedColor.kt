package com.eva.reminders.presentation.feature_create.composables

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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import com.eva.reminders.domain.enums.TaskColorEnum

@Composable
fun PickedColor(
    color: TaskColorEnum?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colorText by remember(color) {
        derivedStateOf { color?.toText() ?: "" }
    }

    AnimatedVisibility(
        visible = color != null && color != TaskColorEnum.TRANSPARENT,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutVertically() + fadeOut(),
    ) {

        if (color != null) {
            val colorVal by animateColorAsState(
                targetValue = colorResource(id = color.color),
                animationSpec = tween(800, easing = EaseInOutCubic)
            )
            AssistChip(
                modifier = modifier,
                onClick = onClick,
                label = { Text(text = colorText) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.WaterDrop,
                        contentDescription = "Color"
                    )
                },
                colors = AssistChipDefaults
                    .assistChipColors(
                        containerColor = colorVal,
                        labelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .75f),
                        leadingIconContentColor = MaterialTheme.colorScheme.onSurface
                            .copy(alpha = .75f)
                    ),
                border = AssistChipDefaults.assistChipBorder(
                    borderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .75f)
                )
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

@Preview
@Composable
fun PickedColorPreview(
    @PreviewParameter(ColorPreviewParams::class)
    color: TaskColorEnum
) {
    PickedColor(
        color = color,
        onClick = {  }
    )
}