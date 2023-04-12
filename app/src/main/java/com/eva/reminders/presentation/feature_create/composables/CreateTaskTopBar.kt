package com.eva.reminders.presentation.feature_create.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.NotificationAdd
import androidx.compose.material.icons.outlined.Pin
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
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
    TopAppBar(title = {}, navigationIcon = {
        if (navController.currentBackStackEntry != null) IconButton(onClick = { navController.navigateUp() }) {
            Icon(
                imageVector = Icons.Default.ArrowBack, contentDescription = "Back Button"
            )
        }
    }, actions = {
        IconButton(onClick = onPinClick) {
            Icon(
                imageVector = if (isPinned) Icons.Filled.Pin else Icons.Outlined.Pin,
                contentDescription = "Pinned"
            )
        }
        IconButton(onClick = onReminderClick) {
            Icon(
                imageVector = if (isReminder) Icons.Filled.Notifications else Icons.Outlined.NotificationAdd,
                contentDescription = "Add a Reminder"
            )
        }
        IconButton(onClick = onArchiveClick) {
            Icon(
                imageVector = if (isArchived) Icons.Filled.Archive else Icons.Outlined.Archive,
                contentDescription = "Archived"
            )
        }
    }, modifier = modifier
    )
}