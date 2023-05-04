package com.eva.reminders.presentation.utils

object NavConstants {
    const val TASK_ID_PARAM = "{taskId}"
    const val TASK_ID = "taskId"
}

sealed class NavRoutes(val route: String) {
    object Home : NavRoutes("/")
    object AddTask : NavRoutes("/add-task")
    object EditLabels : NavRoutes("/edit-labels")
    object AddLabels : NavRoutes("/add-labels")
}
