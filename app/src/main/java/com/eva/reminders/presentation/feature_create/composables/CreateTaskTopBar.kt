package com.eva.reminders.presentation.feature_create.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.NotificationAdd
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.eva.reminders.presentation.feature_create.utils.checkNotificationPermissions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskTopBar(
    navController: NavController,
    isPinned: Boolean,
    isReminder: Boolean,
    isArchived: Boolean,
    onPinClick: () -> Unit,
    onReminderClick: () -> Unit,
    onArchiveClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val permission = checkNotificationPermissions()

    TopAppBar(
        title = {},
        navigationIcon = {
            if (navController.currentBackStackEntry != null)
                IconButton(
                    onClick = { navController.navigateUp() }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back Button"
                    )
                }
        },
        actions = {
            PlainTooltipBox(
                tooltip = { Text(text = "Pinned") },
            ) {
                IconButton(
                    onClick = onPinClick, modifier = Modifier.tooltipAnchor()
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
                visible = permission
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
            PlainTooltipBox(
                tooltip = { Text(text = "Archive") }
            ) {
                IconButton(
                    onClick = onArchiveClick,
                    modifier = Modifier.tooltipAnchor()
                ) {
                    Icon(
                        imageVector = if (isArchived)
                            Icons.Filled.Archive
                        else
                            Icons.Outlined.Archive,
                        contentDescription = "Archived"
                    )
                }
            }
        },
        modifier = modifier,
    )
}