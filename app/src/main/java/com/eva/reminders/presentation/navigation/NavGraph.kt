package com.eva.reminders.presentation.navigation

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.eva.reminders.presentation.feature_create.AddTaskViewModel
import com.eva.reminders.presentation.feature_create.CreateTaskRoute
import com.eva.reminders.presentation.feature_home.HomeRoute
import com.eva.reminders.presentation.feature_home.HomeViewModel
import com.eva.reminders.presentation.feature_labels.EditLabelRoute
import com.eva.reminders.presentation.feature_labels.LabelsViewModel
import com.eva.reminders.presentation.utils.LocalArrangementStyle
import com.eva.reminders.presentation.utils.LocalSnackBarHostProvider
import com.eva.reminders.presentation.utils.UIEvents

@Composable
fun NavigationGraph(
    navHost: NavHostController,
    modifier: Modifier = Modifier,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    snackBarHostState: SnackbarHostState = LocalSnackBarHostProvider.current
) {

    NavHost(
        navController = navHost,
        startDestination = NavRoutes.Home.route,
        modifier = modifier
    ) {
        composable(
            route = NavRoutes.Home.route,
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = NavigationDeepLinks.homeUri.toString()
                    action = Intent.ACTION_VIEW
                },
            ),
        ) {
            val labelsViewModel = hiltViewModel<LabelsViewModel>()
            val labels by labelsViewModel.allLabels.collectAsStateWithLifecycle()

            val homeViewModel = hiltViewModel<HomeViewModel>()
            val tasks by homeViewModel.tasks.collectAsStateWithLifecycle()
            val colorOptions by homeViewModel.colorOptions.collectAsStateWithLifecycle()
            val currentTab by homeViewModel.currentTab.collectAsStateWithLifecycle()
            val style by homeViewModel.arrangement.collectAsStateWithLifecycle()
            val searchResults by homeViewModel.searchedTasks.collectAsStateWithLifecycle()
            val homSearchBarState by homeViewModel.homeSearchBarState.collectAsStateWithLifecycle()

            LaunchedEffect(homeViewModel.uiEvents, lifecycleOwner.lifecycle) {
                // ensures all the events are collected when the activity is in foreground
                lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    homeViewModel.uiEvents.collect { uiEvents ->
                        when (uiEvents) {
                            is UIEvents.ShowSnackBar -> snackBarHostState.showSnackbar(uiEvents.message)
                            else -> {}
                        }
                    }
                }
            }

            CompositionLocalProvider(LocalArrangementStyle provides style) {
                HomeRoute(
                    labels = labels,
                    tasks = tasks,
                    tab = currentTab,
                    searchBarState = homSearchBarState,
                    searchResultsType = searchResults,
                    colorOptions = colorOptions,
                    onArrangementChange = homeViewModel::onArrangementChange,
                    onTabChange = homeViewModel::changeCurrentTab,
                    onSearchType = homeViewModel::onSearchType,
                    onSearchBarEvents = homeViewModel::onSearchBarEvents,
                    onAdd = { navHost.navigate(NavRoutes.AddTask.route) },
                    onEditRoute = { navHost.navigate(NavRoutes.EditLabels.route) },
                    onTaskSelect = { taskId ->
                        navHost.navigate(NavRoutes.AddTask.route + "?${NavConstants.TASK_ID}=$taskId")
                    }
                )
            }
        }
        composable(
            NavRoutes.AddTask.route + NavConstants.TASK_ID_QUERY_PARAMS,
            arguments = listOf(
                navArgument(NavConstants.TASK_ID) {
                    type = NavType.IntType
                    defaultValue = -1
                }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = NavigationDeepLinks.taskUriPattern
                    action = Intent.ACTION_VIEW
                },
            ),
        ) {
            val viewModel = hiltViewModel<AddTaskViewModel>()

            val queriedLabels by viewModel.labelsSelectorStates.collectAsStateWithLifecycle()
            val query by viewModel.labelQuery.collectAsStateWithLifecycle()
            val content by viewModel.taskState.collectAsStateWithLifecycle()
            val selectedLabels by viewModel.selectedLabels.collectAsStateWithLifecycle()


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

            if (content.isLoading)
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                    content = { CircularProgressIndicator() }
                )
            AnimatedVisibility(
                visible = !content.isLoading,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                CreateTaskRoute(
                    state = content.content,
                    labelSearchQuery = query,
                    onLabelSearchQuery = viewModel::searchLabels,
                    onNewLabelCreate = viewModel::createNewLabel,
                    onAddTaskEvents = viewModel::onAddTaskEvents,
                    queriedLabels = queriedLabels,
                    pickedLabels = selectedLabels,
                    onLabelSelect = viewModel::onSelect,
                    navigation = {
                        if (navHost.previousBackStackEntry != null)
                            IconButton(onClick = navHost::navigateUp) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back Button"
                                )
                            }
                    },
                )
            }
        }
        composable(NavRoutes.EditLabels.route) {

            val viewModel = hiltViewModel<LabelsViewModel>()
            val createState = viewModel.newLabelState.collectAsStateWithLifecycle()
            val editState = viewModel.editLabelStates.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                viewModel.uiEvents.collect { uiEvents ->
                    when (uiEvents) {
                        is UIEvents.ShowSnackBar -> snackBarHostState
                            .showSnackbar(uiEvents.message)

                        else -> {}
                    }
                }
            }

            EditLabelRoute(
                createLabelState = createState.value,
                editLabelState = editState.value,
                onCreateLabelEvent = viewModel::onCreateLabelEvent,
                onEditLabelEvent = viewModel::onUpdateLabelEvent,
                onEditActions = viewModel::onLabelAction,
                navigation = {
                    if (navHost.previousBackStackEntry != null)
                        IconButton(onClick = navHost::navigateUp) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back Button"
                            )
                        }
                },
            )
        }
    }
}
