package com.eva.reminders.presentation.feature_create.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.eva.reminders.domain.enums.TaskColorEnum

@Composable
fun PickedColor(
    color: TaskColorEnum?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val colorText by remember(color) {
        derivedStateOf {
            when (color) {
                TaskColorEnum.TRANSPARENT -> ""
                TaskColorEnum.RED -> "Red"
                TaskColorEnum.ORANGE -> "Orange"
                TaskColorEnum.AMBER -> "Amber"
                TaskColorEnum.YELLOW -> "Yellow"
                TaskColorEnum.LIME -> "Lime"
                TaskColorEnum.GREEN -> "Green"
                TaskColorEnum.EMERALD -> "Emerald"
                TaskColorEnum.TEAL -> "Teal"
                TaskColorEnum.CYAN -> "Cyan"
                TaskColorEnum.SKY -> "Sky"
                TaskColorEnum.BLUE -> "Blue"
                TaskColorEnum.INDIGO -> "Indigo"
                TaskColorEnum.VIOLET -> "Violet"
                TaskColorEnum.PURPLE -> "Purple"
                TaskColorEnum.FUCHSIA -> "Fuchsia"
                TaskColorEnum.PINK -> "Pink"
                TaskColorEnum.ROSE -> "Rose"
                else -> ""
            }
        }
    }

    AnimatedVisibility(
        visible = color != null && color != TaskColorEnum.TRANSPARENT,
        enter = slideInVertically() + expandVertically() + fadeIn(),
        exit = slideOutVertically() + fadeOut() + shrinkVertically(),
    ) {

        if (color != null) {
            val colorVal by animateColorAsState(
                targetValue = colorResource(id = color.color),
                animationSpec = tween(800, easing = EaseInOutCubic)
            )
            AssistChip(
                onClick = onClick,
                label = { Text(text = colorText) },
                colors = AssistChipDefaults.assistChipColors(containerColor = colorVal),
                modifier = modifier
            )
        }
    }
}