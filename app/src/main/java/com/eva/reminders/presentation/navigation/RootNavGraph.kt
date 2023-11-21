package com.eva.reminders.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.eva.reminders.presentation.navigation.screens_ext.createTaskRoute
import com.eva.reminders.presentation.navigation.screens_ext.editLabelsScreen
import com.eva.reminders.presentation.navigation.screens_ext.showTasksRoute
import com.eva.reminders.presentation.navigation.screens_ext.updateTaskRoute

@Composable
fun RootNavGraph(
    navHost: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navHost,
        startDestination = NavRoutes.Home.route,
        modifier = modifier
    ) {
        //show tasks route
        showTasksRoute(navHost = navHost)
        // update Task
        updateTaskRoute(navHost = navHost)
        //create Task
        createTaskRoute(navHost = navHost)
        // edit labels
        editLabelsScreen(navHost = navHost)
    }
}
