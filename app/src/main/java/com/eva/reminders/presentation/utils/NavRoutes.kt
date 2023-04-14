package com.eva.reminders.presentation.utils

sealed class NavRoutes(val route: String) {
    object Home : NavRoutes("/")
    object AddTask : NavRoutes("/add-task")
    object EditLabels:NavRoutes("/add-labels")
}
