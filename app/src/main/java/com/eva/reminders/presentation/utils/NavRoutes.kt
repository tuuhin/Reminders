package com.eva.reminders.presentation.utils

object NavConstants {
    const val TASK_ID_PARAM = "{taskId}"
    const val TASK_ID = "taskId"
    const val TASK_ID_QUERY_PARAMS = "?${TASK_ID}=${TASK_ID_PARAM}"
}

sealed class NavRoutes(val route: String) {
    object Home : NavRoutes("/")
    object AddTask : NavRoutes("/add-task")
    object EditLabels : NavRoutes("/edit-labels")
}
