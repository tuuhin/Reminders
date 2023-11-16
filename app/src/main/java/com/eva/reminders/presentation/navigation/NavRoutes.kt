package com.eva.reminders.presentation.navigation


sealed class NavRoutes(val route: String) {

    data object Home : NavRoutes("/")

    data object AddTask : NavRoutes("/create")

    data object UpdateTask : NavRoutes("/update")

    data object EditLabels : NavRoutes("/edit-labels")

    data object TaskLabelsRoute : NavRoutes("/task-labels")
}
