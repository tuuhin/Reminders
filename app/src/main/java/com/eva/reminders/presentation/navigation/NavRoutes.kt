package com.eva.reminders.presentation.navigation


sealed class NavRoutes(val route: String) {
    data object Home : NavRoutes("/")
    data object AddTask : NavRoutes("/add-task")
    data object EditLabels : NavRoutes("/edit-labels")
}
