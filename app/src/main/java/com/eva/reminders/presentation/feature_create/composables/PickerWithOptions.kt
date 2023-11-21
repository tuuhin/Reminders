package com.eva.reminders.presentation.feature_create.composables

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.DpOffset
import com.eva.reminders.R
import com.eva.reminders.utils.toDpOffset

@Composable
fun PickerWithOptions(
    isExpanded: Boolean,
    onToggleExpanded: () -> Unit,
    selectedOptionTile: @Composable RowScope.() -> Unit,
    dropDownContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    errorText: (@Composable BoxScope.() -> Unit)? = null,
) {
    var dropdownOffset by remember { mutableStateOf(DpOffset.Zero) }

    val iconRotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = "Animated Expand More Icon",
        animationSpec = tween(durationMillis = 400, easing = EaseInOutCubic)
    )

    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .pointerInput(true) {
                detectTapGestures(
                    onTap = { tapOffset -> dropdownOffset = tapOffset.toDpOffset() }
                )
            }
            .clickable(
                onClick = onToggleExpanded,
                role = Role.Button,
            )
            .padding(vertical = dimensionResource(id = R.dimen.reminder_pickers_option_outer_padding))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = dimensionResource(R.dimen.reminder_pickers_option_vertical_padding),
                    horizontal = dimensionResource(R.dimen.reminder_pickers_option_horizontal_padding)
                ),
        ) {

            selectedOptionTile()
            Icon(
                imageVector = Icons.Outlined.ExpandMore,
                contentDescription = stringResource(id = R.string.drop_down_icon_desc),
                tint = MaterialTheme.colorScheme.surfaceTint,
                modifier = modifier.graphicsLayer {
                    rotationZ = iconRotation
                }
            )
        }
        errorText?.invoke(this)
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = onToggleExpanded,
            offset = dropdownOffset,
            modifier = Modifier.fillMaxWidth(.5f),
            content = dropDownContent
        )
    }
}