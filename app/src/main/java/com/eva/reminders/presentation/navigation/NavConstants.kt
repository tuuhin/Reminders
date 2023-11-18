package com.eva.reminders.presentation.navigation

object NavConstants {
    const val BLANK_ROUTE = "/"
    const val TASK_ID = "taskId"
    const val TASK_ID_AS_PATH_PARAMS = "/{$TASK_ID}"
    const val TASK_ID_AS_QUERY_PARAMS = "?${TASK_ID}={${TASK_ID}}"
}