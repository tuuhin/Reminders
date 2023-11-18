package com.eva.reminders.presentation.navigation.screens_ext

import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.navigation
import androidx.navigation.navDeepLink
import com.eva.reminders.presentation.feature_create.AddTaskViewModel
import com.eva.reminders.presentation.feature_create.CreateTaskRoute
import com.eva.reminders.presentation.feature_create.SelectLabelsRoute
import com.eva.reminders.presentation.navigation.NavConstants
import com.eva.reminders.presentation.navigation.NavRoutes
import com.eva.reminders.presentation.navigation.NavigationDeepLinks
import com.eva.reminders.presentation.navigation.sharedViewModel
import com.eva.reminders.presentation.utils.LocalSnackBarHostProvider
import com.eva.reminders.presentation.utils.UIEvents

fun NavGraphBuilder.createTaskRoute(navHost: NavHostController) = navigation(
    startDestination = NavConstants.BLANK_ROUTE,
    route = NavRoutes.AddTask.route,
    deepLinks = listOf(
        navDeepLink {
            uriPattern = NavigationDeepLinks.createNewTaskUriPattern
            action = Intent.ACTION_VIEW
        },
    ),
) {
    composable(route = NavConstants.BLANK_ROUTE) { entry ->
        val lifecycleOwner = LocalLifecycleOwner.current
        val snackBarHostState = LocalSnackBarHostProvider.current

        val viewModel = entry.sharedViewModel<AddTaskViewModel>(controller = navHost)

        val content by viewModel.taskState.collectAsStateWithLifecycle()
        val selectedLabels by viewModel.labelsForTask.collectAsStateWithLifecycle()

        LaunchedEffect(viewModel.uiEvents, lifecycleOwner.lifecycle) {
            lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiEvents.collect { event ->
                    when (event) {
                        is UIEvents.ShowSnackBar -> snackBarHostState.showSnackbar(event.message)
                        UIEvents.NavigateBack -> navHost.navigateUp()
                    }
                }
            }
        }
        CreateTaskRoute(
            state = content.content,
            onAddTaskEvents = viewModel::onAddTaskEvents,
            selectedLabels = selectedLabels,
            onLabelPickerDialog = { navHost.navigate(NavRoutes.TaskLabelsRoute.route) },
            navigation = {
                if (navHost.previousBackStackEntry != null)
                    IconButton(
                        onClick = {
                            navHost.popBackStack(route = NavRoutes.AddTask.route, inclusive = true)
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                        )
                    }
            },
        )
    }
    dialog(
        route = NavRoutes.TaskLabelsRoute.route,
        dialogProperties = DialogProperties(usePlatformDefaultWidth = false)
    ) { entry ->

        val viewModel = entry.sharedViewModel<AddTaskViewModel>(controller = navHost)

        val queriedLabels by viewModel.labelsSelectorStates.collectAsStateWithLifecycle()
        val query by viewModel.labelQuery.collectAsStateWithLifecycle()

        SelectLabelsRoute(
            query = query,
            onQueryChanged = viewModel::searchLabels,
            onCreateNew = viewModel::createNewLabel,
            labels = queriedLabels,
            onSelect = viewModel::onSelect,
            onDismissRequest = navHost::navigateUp
        )
    }
}

