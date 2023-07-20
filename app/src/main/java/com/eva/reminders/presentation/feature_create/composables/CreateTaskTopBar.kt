package com.eva.reminders.presentation.feature_create.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.NotificationAdd
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.eva.reminders.presentation.feature_create.utils.checkNotificationPermissions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskTopBar(
    isPinned: Boolean,
    isReminder: Boolean,
    isArchived: Boolean,
    onPinClick: () -> Unit,
    onReminderClick: () -> Unit,
    onArchiveClick: () -> Unit,
    modifier: Modifier = Modifier,
    navigation: @Composable () -> Unit = {},
) {

    val permission = checkNotificationPermissions()

    TopAppBar(
        title = {},
        navigationIcon = navigation,
        actions = {
            PlainTooltipBox(
                tooltip = { Text(text = "Pinned") },
            ) {
                IconButton(
                    onClick = onPinClick,
                    modifier = Modifier.tooltipAnchor()
                ) {
                    Icon(
                        imageVector = if (isPinned)
                            Icons.Filled.PushPin
                        else
                            Icons.Outlined.PushPin,
                        contentDescription = "Pinned"
                    )
                }
            }
            AnimatedVisibility(
                visible = permission,
                enter = slideInHorizontally() + fadeIn()
            ) {
                PlainTooltipBox(
                    tooltip = { Text(text = "Reminder") }
                ) {
                    IconButton(
                        onClick = onReminderClick,
                        modifier = Modifier.tooltipAnchor()
                    ) {
                        Icon(
                            imageVector = if (isReminder)
                                Icons.Filled.Notifications
                            else
                                Icons.Outlined.NotificationAdd,
                            contentDescription = "Add a Reminder"
                        )
                    }
                }
            }
            when {
                isArchived -> PlainTooltipBox(
                    tooltip = { Text(text = "Archive") }
                ) {
                    IconButton(
                        onClick = onArchiveClick,
                        modifier = Modifier.tooltipAnchor()
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Archive,
                            contentDescription = "Archived"
                        )
                    }
                }

                else -> PlainTooltipBox(
                    tooltip = { Text(text = "Un Archived") },
                ) {
                    IconButton(
                        onClick = onArchiveClick,
                        modifier = Modifier.tooltipAnchor()
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Archive,
                            contentDescription = "Un archived"
                        )
                    }
                }
            }
        },
        modifier = modifier,
    )
}