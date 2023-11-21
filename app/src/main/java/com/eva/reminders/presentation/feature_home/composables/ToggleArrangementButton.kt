package com.eva.reminders.presentation.feature_home.composables

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PlainTooltipBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.eva.reminders.R
import com.eva.reminders.domain.models.ArrangementStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToggleArrangementButton(
    modifier: Modifier = Modifier,
    style: ArrangementStyle, onChange: (ArrangementStyle) -> Unit
) {
    PlainTooltipBox(
        tooltip = { Text(text = stringResource(id = R.string.reminder_arrangement_style)) }
    ) {
        IconButton(
            onClick = {
                val newStyle = when (style) {
                    ArrangementStyle.GRID_STYLE -> ArrangementStyle.BLOCK_STYLE
                    ArrangementStyle.BLOCK_STYLE -> ArrangementStyle.GRID_STYLE
                }
                onChange(newStyle)
            },
            modifier = modifier.tooltipAnchor()
        ) {
            when (style) {
                ArrangementStyle.GRID_STYLE -> Icon(
                    painter = painterResource(id = R.drawable.ic_listview),
                    contentDescription = stringResource(id = R.string.list_icon_desc)
                )

                ArrangementStyle.BLOCK_STYLE -> Icon(
                    painter = painterResource(id = R.drawable.ic_gridview),
                    contentDescription = stringResource(id = R.string.grid_icon_desc)
                )
            }

        }
    }
}