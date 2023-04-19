package com.eva.reminders.presentation.feature_create.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.NotificationAdd
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

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
            IconButton(
                onClick = onPinClick
            ) {
                Icon(
                    imageVector = if (isPinned)
                        Icons.Filled.PushPin
                    else
                        Icons.Outlined.PushPin,
                    contentDescription = "Pinned"
                )
            }
            IconButton(
                onClick = onReminderClick
            ) {
                Icon(
                    imageVector = if (isReminder)
                        Icons.Filled.Notifications
                    else
                        Icons.Outlined.NotificationAdd,
                    contentDescription = "Add a Reminder"
                )
            }
            IconButton(
                onClick = onArchiveClick
            ) {
                Icon(
                    imageVector = if (isArchived)
                        Icons.Filled.Archive
                    else
                        Icons.Outlined.Archive,
                    contentDescription = "Archived"
                )
            }
        },
        modifier = modifier,
    )
}