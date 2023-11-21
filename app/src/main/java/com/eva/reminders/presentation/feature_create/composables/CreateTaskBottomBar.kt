package com.eva.reminders.presentation.feature_create.composables

import android.content.res.Configuration
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ColorLens
import androidx.compose.material.icons.outlined.CopyAll
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Update
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltipBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eva.reminders.R
import com.eva.reminders.ui.theme.RemindersTheme
import com.eva.reminders.utils.formatToDayMonthShort
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskBottomBar(
    isUpdate: Boolean,
    isCopyEnabled: Boolean,
    isDeleteEnabled: Boolean,
    onActionClick: () -> Unit,
    onAddColor: () -> Unit,
    onDelete: () -> Unit,
    onCopy: () -> Unit,
    modifier: Modifier = Modifier,
    editedAt: LocalDateTime? = null,
    iconColor: Color = MaterialTheme.colorScheme.secondary,
    floatingActionBarColor: Color = MaterialTheme.colorScheme.primaryContainer,
    floatingActionBarContentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    timeColor: Color = MaterialTheme.colorScheme.secondary,
    windowInsets: WindowInsets = BottomAppBarDefaults.windowInsets
) {
    val currentTime by remember(editedAt) {
        derivedStateOf {
            editedAt?.toLocalDate()?.formatToDayMonthShort()?.let { time -> "Edited: $time" }
        }
    }
    BottomAppBar(
        modifier = modifier.fillMaxWidth(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = onActionClick,
                elevation = FloatingActionButtonDefaults.elevation(),
                containerColor = floatingActionBarColor,
                contentColor = floatingActionBarContentColor
            ) {
                Icon(
                    imageVector = if (isUpdate) Icons.Outlined.Update else Icons.Outlined.Add,
                    contentDescription = stringResource(id = R.string.add_or_update_icon)
                )
            }
        },
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        actions = {
            PlainTooltipBox(
                tooltip = { Text(text = stringResource(id = R.string.add_color_text)) }
            ) {
                IconButton(
                    onClick = onAddColor,
                    colors = IconButtonDefaults.iconButtonColors(contentColor = iconColor),
                    modifier = Modifier
                        .tooltipAnchor()
                        .align(Alignment.CenterVertically)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ColorLens,
                        contentDescription = stringResource(id = R.string.icon_color_desc)
                    )
                }
            }
            PlainTooltipBox(
                tooltip = { Text(text = stringResource(id = R.string.delete_action)) }
            ) {
                IconButton(
                    enabled = isDeleteEnabled,
                    onClick = onDelete,
                    colors = IconButtonDefaults.iconButtonColors(contentColor = iconColor),
                    modifier = Modifier
                        .tooltipAnchor()
                        .align(Alignment.CenterVertically)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = stringResource(id = R.string.delete_icon_desc)
                    )
                }
            }
            PlainTooltipBox(
                tooltip = { Text(text = stringResource(id = R.string.make_copy_action)) }
            ) {
                IconButton(
                    enabled = isCopyEnabled,
                    onClick = onCopy,
                    colors = IconButtonDefaults.iconButtonColors(contentColor = iconColor),
                    modifier = Modifier
                        .tooltipAnchor()
                        .align(Alignment.CenterVertically)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CopyAll,
                        contentDescription = stringResource(id = R.string.copy_icon_desc)
                    )
                }
            }

            currentTime?.let {
                Text(
                    text = it,
                    color = timeColor,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
        },
        windowInsets = windowInsets
    )
}


@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun CreateTaskBottomBarPreview() = RemindersTheme {
    CreateTaskBottomBar(
        isUpdate = true,
        isDeleteEnabled = true,
        isCopyEnabled = true,
        onActionClick = {},
        onCopy = {},
        onDelete = {},
        onAddColor = {},
    )
}