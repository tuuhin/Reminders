package com.eva.reminders.presentation.feature_create.composables

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.ColorLens
import androidx.compose.material.icons.outlined.Label
import androidx.compose.material.icons.outlined.NotificationAdd
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.eva.reminders.R
import com.eva.reminders.presentation.feature_create.utils.checkNotificationPermissions
import com.eva.reminders.ui.theme.RemindersTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskTopBar(
    isPinned: Boolean,
    isReminder: Boolean,
    isArchived: Boolean,
    onPinClick: () -> Unit,
    onReminderClick: () -> Unit,
    onArchiveClick: () -> Unit,
    onAddLabels: () -> Unit,
    onAddColor: () -> Unit,
    modifier: Modifier = Modifier,
    navigation: @Composable () -> Unit = {},
) {

    val permission = checkNotificationPermissions()

    var isExpanded by rememberSaveable { mutableStateOf(false) }

    TopAppBar(
        title = {},
        navigationIcon = navigation,
        actions = {
            PlainTooltipBox(
                tooltip = { Text(text = stringResource(id = R.string.pin_icon_desc)) },
            ) {
                IconButton(
                    onClick = onPinClick,
                    modifier = Modifier.tooltipAnchor()
                ) {
                    Icon(
                        imageVector = if (isPinned) Icons.Filled.PushPin else Icons.Outlined.PushPin,
                        contentDescription = stringResource(id = R.string.pin_icon_desc)
                    )
                }
            }
            AnimatedVisibility(
                visible = permission,
                enter = slideInHorizontally() + fadeIn()
            ) {
                PlainTooltipBox(
                    tooltip = { Text(text = stringResource(id = R.string.notification_action_desc)) }
                ) {
                    IconButton(
                        onClick = onReminderClick,
                        modifier = Modifier.tooltipAnchor()
                    ) {
                        Icon(
                            imageVector = if (isReminder) Icons.Filled.Notifications else Icons.Outlined.NotificationAdd,
                            contentDescription = stringResource(id = R.string.notification_action_desc)
                        )
                    }
                }
            }
            PlainTooltipBox(
                tooltip = { Text(text = stringResource(id = R.string.archive_action_desc)) }
            ) {
                IconButton(
                    onClick = onArchiveClick,
                    modifier = Modifier.tooltipAnchor()
                ) {
                    Icon(
                        imageVector = if (isArchived) Icons.Filled.Archive else Icons.Outlined.Archive,
                        contentDescription = stringResource(id = R.string.archive_action_desc)
                    )
                }
            }
            AppBarMoreActions(
                isDropDownExpanded = isExpanded,
                onToggleDropDown = { isExpanded = !isExpanded },
            ) {
                DropdownMenuItem(
                    text = { Text(text = stringResource(id = R.string.add_labels_text)) },
                    onClick = onAddLabels,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Label,
                            contentDescription = stringResource(id = R.string.icon_label_desc)
                        )
                    },
                )
                Divider(color = MaterialTheme.colorScheme.outlineVariant)
                DropdownMenuItem(
                    text = { Text(text = stringResource(id = R.string.add_color_text)) },
                    onClick = onAddColor,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.ColorLens,
                            contentDescription = stringResource(id = R.string.icon_color_desc)
                        )
                    },
                )
            }
        },
        modifier = modifier,
    )
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun CreateTaskTopBarPreview() = RemindersTheme {
    CreateTaskTopBar(
        isPinned = true,
        isReminder = true,
        isArchived = false,
        onPinClick = { },
        onReminderClick = { },
        onArchiveClick = {},
        onAddColor = {},
        onAddLabels = {},
        navigation = {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null
            )
        }
    )
}