package com.eva.reminders.presentation.feature_create.composables

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.window.PopupProperties
import com.eva.reminders.R
import com.eva.reminders.utils.toDpOffset

@Composable
fun AppBarMoreActions(
    isDropDownExpanded: Boolean,
    onToggleDropDown: () -> Unit,
    modifier: Modifier = Modifier,
    properties: PopupProperties = PopupProperties(),
    actions: @Composable ColumnScope.() -> Unit,
) {

    var dpOffset by remember {
        mutableStateOf(DpOffset.Zero)
    }

    Box(modifier = modifier) {

        IconButton(
            onClick = onToggleDropDown,
            modifier = Modifier.pointerInput(Unit) {
                detectTapGestures(
                    onTap = { offset -> dpOffset = offset.toDpOffset() }
                )
            }
        ) {
            Icon(
                imageVector = Icons.Outlined.MoreVert,
                contentDescription = stringResource(id = R.string.more_options_icon_desc)
            )
        }

        DropdownMenu(
            expanded = isDropDownExpanded,
            onDismissRequest = onToggleDropDown,
            offset = dpOffset,
            content = actions,
            properties = properties
        )
    }
}