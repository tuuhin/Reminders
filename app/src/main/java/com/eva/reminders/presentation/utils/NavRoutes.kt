package com.eva.reminders.presentation.utils

sealed class NavRoutes(val route: String) {
    object Home : NavRoutes("/")
    object AddReminder : NavRoutes("/add-task")
}
