package com.eva.reminders.presentation.navigation

import android.net.Uri
import androidx.core.net.toUri

object NavigationDeepLinks {

    private const val baseUri = "app://com.eva.reminders"

    // Update task Route the path parameters are not working thus using query params
    // not sure but maybe as taskUri is in a nested nav-graph
    // thus it's not able to find the route
    // but when replaced with query params it's not a complete route anymore but a
    //  route with additional properties
    val taskUriPattern = baseUri + NavRoutes.UpdateTask.route + NavConstants.TASK_ID_AS_QUERY_PARAMS

    fun taskUriFromTaskId(taskId: Int): Uri =
        "${baseUri + NavRoutes.UpdateTask.route}?${NavConstants.TASK_ID}=$taskId".toUri()

    // Create Task Route
    val createNewTaskUriPattern = baseUri + NavRoutes.AddTask.route
    val createNewTaskUri = createNewTaskUriPattern.toUri()

    //EditLabel Route
    val editLabelsUriPattern = baseUri + NavRoutes.EditLabels.route
    val editLabelsUri = editLabelsUriPattern.toUri()

    //home route
    val homeUriPattern = baseUri + NavRoutes.Home.route
    val homeUri = homeUriPattern.toUri()

}


