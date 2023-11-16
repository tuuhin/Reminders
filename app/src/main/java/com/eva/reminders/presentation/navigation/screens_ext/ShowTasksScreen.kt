package com.eva.reminders.presentation.navigation.screens_ext

import android.content.Intent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.eva.reminders.presentation.feature_home.HomeRoute
import com.eva.reminders.presentation.feature_home.HomeViewModel
import com.eva.reminders.presentation.feature_labels.LabelsViewModel
import com.eva.reminders.presentation.navigation.NavRoutes
import com.eva.reminders.presentation.navigation.NavigationDeepLinks
import com.eva.reminders.presentation.utils.LocalArrangementStyle
import com.eva.reminders.presentation.utils.LocalSnackBarHostProvider
import com.eva.reminders.presentation.utils.UIEvents

fun NavGraphBuilder.showTasksRoute(
    navHost: NavHostController,
) {
    composable(
        route = NavRoutes.Home.route,
        deepLinks = listOf(
            navDeepLink {
                uriPattern = NavigationDeepLinks.homeUriAsString
                action = Intent.ACTION_VIEW
            },
        ),
    ) {

        val lifecycleOwner = LocalLifecycleOwner.current
        val snackBarHostState = LocalSnackBarHostProvider.current

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
                onTaskSelect = { taskId -> navHost.navigate(NavRoutes.UpdateTask.route + "/$taskId") }
            )
        }
    }
}