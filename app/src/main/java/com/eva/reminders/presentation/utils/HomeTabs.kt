package com.eva.reminders.presentation.utils

import androidx.annotation.StringRes
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
import com.eva.reminders.R

sealed class HomeTabs(
    val icon: ImageVector,
    val filledIcon: ImageVector,
    @StringRes val textRes: Int,
) {
    data object AllReminders : HomeTabs(
        icon = Icons.Outlined.Apps,
        filledIcon = Icons.Filled.Apps,
        textRes = R.string.drawer_option_all_reminder,
    )

    data object NonScheduled : HomeTabs(
        icon = Icons.Outlined.Lightbulb,
        filledIcon = Icons.Filled.Lightbulb,
        textRes = R.string.drawer_option_not_scheduled,
    )

    data object Scheduled : HomeTabs(
        icon = Icons.Outlined.Notifications,
        filledIcon = Icons.Filled.Notifications,
        textRes = R.string.drawer_option_scheduled,
    )

    data object Archived : HomeTabs(
        icon = Icons.Outlined.Archive,
        filledIcon = Icons.Filled.Archive,
        textRes = R.string.drawer_option_achieved,
    )
}