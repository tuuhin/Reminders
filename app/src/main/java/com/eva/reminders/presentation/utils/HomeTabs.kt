package com.eva.reminders.presentation.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.ui.graphics.vector.ImageVector

sealed class HomeTabs(
    val icon: ImageVector,
    val filledIcon: ImageVector,
    val text: String,
) {
    object AllReminders : HomeTabs(
        icon = Icons.Outlined.Apps,
        filledIcon = Icons.Filled.Apps,
        text = "All Reminders",
    )

    object NonScheduled : HomeTabs(
        icon = Icons.Outlined.Lightbulb,
        filledIcon = Icons.Filled.Lightbulb,
        text = "Non Scheduled",
    )

    object Scheduled : HomeTabs(
        icon = Icons.Outlined.Notifications,
        filledIcon = Icons.Filled.Notifications,
        text = "Scheduled",
    )

    object Archived : HomeTabs(
        icon = Icons.Outlined.Archive,
        filledIcon = Icons.Filled.Archive,
        text = "Archived",
    )
}