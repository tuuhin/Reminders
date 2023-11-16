package com.eva.reminders.presentation.navigation

import android.net.Uri
import androidx.core.net.toUri

object NavigationDeepLinks {

    private val baseUri = "app://com.eva.reminders".toUri()

    val taskUriPattern =
        "$baseUri${NavRoutes.UpdateTask.route + NavConstants.TASK_ID_AS_PATH_PARAMS}"

    val homeUri = baseUri

    val homeUriAsString = baseUri.toString()

    fun taskUriFromTaskId(taskId: Int): Uri =
        "$baseUri${NavRoutes.UpdateTask.route}/$taskId".toUri()

}


