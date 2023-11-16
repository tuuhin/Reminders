package com.eva.reminders.presentation.navigation.screens_ext

import android.content.Intent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
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

fun NavGraphBuilder.updateTaskRoute(navHost: NavHostController) = navigation(
    startDestination = NavConstants.TASK_ID_AS_PATH_PARAMS,
    route = NavRoutes.UpdateTask.route + NavConstants.TASK_ID_AS_PATH_PARAMS,
    arguments = listOf(
        navArgument(NavConstants.TASK_ID) {
            type = NavType.IntType
            defaultValue = -1
        }
    ),
) {
    composable(
        route = NavConstants.TASK_ID_AS_PATH_PARAMS,
        deepLinks = listOf(
            navDeepLink {
                uriPattern = NavigationDeepLinks.taskUriPattern
                action = Intent.ACTION_VIEW
            },
        ),
    ) { entry ->

        val taskId = entry.arguments?.getInt(NavConstants.TASK_ID) ?: -1

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

        Crossfade(
            targetState = !content.isLoading,
            label = "Cross fading the loading state",
            animationSpec = tween(easing = FastOutLinearInEasing)
        ) { isReady ->
            when {
                isReady -> CreateTaskRoute(
                    state = content.content,
                    onAddTaskEvents = viewModel::onAddTaskEvents,
                    selectedLabels = selectedLabels,
                    onLabelPickerDialog = {

                        val labelPickerRoute = "/$taskId" + NavRoutes.TaskLabelsRoute.route

                        navHost.navigate(labelPickerRoute)
                    },
                    navigation = {
                        if (navHost.previousBackStackEntry != null)
                            IconButton(
                                onClick = {
                                    navHost.popBackStack(
                                        route = NavRoutes.UpdateTask.route,
                                        inclusive = true
                                    )
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back Button"
                                )
                            }
                    },
                )

                else -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                    content = { CircularProgressIndicator() }
                )
            }
        }
    }
    dialog(
        route = NavConstants.TASK_ID_AS_PATH_PARAMS + NavRoutes.TaskLabelsRoute.route,
        dialogProperties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        ),
        arguments = listOf(
            navArgument(NavConstants.TASK_ID) {
                type = NavType.IntType
                defaultValue = -1
            }
        ),
    ) { entry ->

        val viewModel = entry.sharedViewModel<AddTaskViewModel>(controller = navHost)

        val labels by viewModel.labelsSelectorStates.collectAsStateWithLifecycle()
        val query by viewModel.labelQuery.collectAsStateWithLifecycle()

        SelectLabelsRoute(
            query = query,
            labels = labels,
            onQueryChanged = viewModel::searchLabels,
            onCreateNew = viewModel::createNewLabel,
            onSelect = viewModel::onSelect,
            onDismissRequest = navHost::navigateUp
        )
    }
}

