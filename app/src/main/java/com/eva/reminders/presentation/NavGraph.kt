package com.eva.reminders.presentation

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.eva.reminders.presentation.feature_create.AddTaskViewModel
import com.eva.reminders.presentation.feature_create.CreateTaskRoute
import com.eva.reminders.presentation.feature_home.HomeRoute
import com.eva.reminders.presentation.feature_home.HomeViewModel
import com.eva.reminders.presentation.feature_labels.EditLabelRoute
import com.eva.reminders.presentation.feature_labels.LabelsViewModel
import com.eva.reminders.presentation.utils.LocalArrangementStyle
import com.eva.reminders.presentation.utils.NavConstants
import com.eva.reminders.presentation.utils.NavRoutes
import com.eva.reminders.presentation.utils.LocalSnackBarHostProvider
import com.eva.reminders.presentation.utils.UIEvents

@Composable
fun NavigationGraph(
    modifier: Modifier = Modifier
) {
    val navHost = rememberNavController()
    val snackBarHostState = remember { SnackbarHostState() }
    CompositionLocalProvider(
        LocalSnackBarHostProvider provides snackBarHostState
    ) {
        NavHost(
            navController = navHost,
            startDestination = NavRoutes.Home.route,
            modifier = modifier
        ) {
            composable(NavRoutes.Home.route) {
                val labelsViewModel = hiltViewModel<LabelsViewModel>()
                val labels by labelsViewModel.allLabels.collectAsStateWithLifecycle()

                val homeViewModel = hiltViewModel<HomeViewModel>()
                val tasks by homeViewModel.tasks.collectAsStateWithLifecycle()
                val colorOptions by homeViewModel.colorOptions.collectAsStateWithLifecycle()
                val currentTab by homeViewModel.currentTab.collectAsStateWithLifecycle()
                val style by homeViewModel.arrangement.collectAsStateWithLifecycle()
                val searchResults by homeViewModel.searchedTasks.collectAsStateWithLifecycle()
                val homSearchBarState by homeViewModel.homeSearchBarState.collectAsStateWithLifecycle()

                LaunchedEffect(Unit) {
                    homeViewModel.uiEvents.collect { uiEvents ->
                        when (uiEvents) {
                            is UIEvents.ShowSnackBar -> snackBarHostState
                                .showSnackbar(uiEvents.message)

                            else -> {}
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
                )
            ) {
                val viewModel = hiltViewModel<AddTaskViewModel>()

                val queriedLabels by viewModel.labelsSelectorStates.collectAsStateWithLifecycle()
                val query by viewModel.labelQuery.collectAsStateWithLifecycle()
                val content by viewModel.taskState.collectAsStateWithLifecycle()
                val selectedLabels by viewModel.selectedLabels.collectAsStateWithLifecycle()


                LaunchedEffect(key1 = Unit) {
                    viewModel.uiEvents.collect { event ->
                        when (event) {
                            is UIEvents.ShowSnackBar -> snackBarHostState
                                .showSnackbar(event.message)

                            UIEvents.NavigateBack -> navHost.navigateUp()
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
}