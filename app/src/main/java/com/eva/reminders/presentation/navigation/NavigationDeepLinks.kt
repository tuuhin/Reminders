package com.eva.reminders.presentation.navigation

import android.net.Uri
import androidx.core.net.toUri

object NavigationDeepLinks {

    private val baseUri = "app://com.eva.reminders".toUri()

    val taskUriPattern = "$baseUri${NavRoutes.AddTask.route + NavConstants.TASK_ID_QUERY_PARAMS}"

    val homeUri = baseUri

    fun taskUriFromTaskId(taskId: Int): Uri {
        return "$baseUri${NavRoutes.AddTask.route}?${NavConstants.TASK_ID}=$taskId".toUri()
    }

}


