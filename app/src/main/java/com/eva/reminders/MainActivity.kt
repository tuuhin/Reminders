package com.eva.reminders

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eva.reminders.presentation.feature_create.CreateReminderRoute
import com.eva.reminders.presentation.feature_create.CreateTaskViewModel
import com.eva.reminders.presentation.feature_home.HomeRoute
import com.eva.reminders.presentation.feature_home.HomeViewModel
import com.eva.reminders.presentation.feature_labels.EditLabelRoute
import com.eva.reminders.presentation.feature_labels.LabelsViewModel
import com.eva.reminders.presentation.utils.NavRoutes
import com.eva.reminders.ui.theme.RemindersTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RemindersTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navHost = rememberNavController()
                    NavHost(
                        navController = navHost,
                        startDestination = NavRoutes.Home.route
                    ) {
                        composable(NavRoutes.Home.route) {
                            val labelsViewModel = hiltViewModel<LabelsViewModel>()
                            val labels by labelsViewModel.allLabels.collectAsStateWithLifecycle()

                            val homeViewModel = hiltViewModel<HomeViewModel>()
                            val tasks by homeViewModel.tasks.collectAsStateWithLifecycle()

                            val currentTab by homeViewModel.currentTab.collectAsStateWithLifecycle()

                            HomeRoute(
                                navController = navHost,
                                labels = labels,
                                tasks = tasks,
                                tab = currentTab,
                                onTabChange = homeViewModel::changeCurrentTab
                            )
                        }
                        composable(NavRoutes.AddTask.route) {

                            val viewModel = hiltViewModel<CreateTaskViewModel>()
                            val state by viewModel.task.collectAsStateWithLifecycle()
                            val reminderState by viewModel.reminder.collectAsStateWithLifecycle()

                            val addLabelViewModel = viewModel.addLabelViewModel
                            val queriedLabels by addLabelViewModel.labelSelector.collectAsStateWithLifecycle()
                            val query by addLabelViewModel.query.collectAsStateWithLifecycle()

                            CreateReminderRoute(
                                navController = navHost,
                                state = state,
                                reminderState = reminderState,
                                labelSearchQuery = query,
                                onLabelSearchQuery = addLabelViewModel::searchLabels,
                                onNewLabelCreate = addLabelViewModel::createLabel,
                                onCreateTaskEvents = viewModel::onCreateTaskEvent,
                                onRemindersEvents = viewModel::onReminderEvents,
                                queriedLabels = queriedLabels,
                                pickedLabels = addLabelViewModel.pickedLabels.map { it.toModel() },
                                onLabelSelect = addLabelViewModel::onSelect
                            )
                        }
                        composable(NavRoutes.EditLabels.route) {
                            val viewModel = hiltViewModel<LabelsViewModel>()
                            val createState =
                                viewModel.createLabelState.collectAsStateWithLifecycle()
                            val editState =
                                viewModel.updatedLabels.collectAsStateWithLifecycle()
                            EditLabelRoute(
                                navController = navHost,
                                createLabelState = createState.value,
                                editLabelState = editState.value,
                                onCreateLabelEvent = viewModel::onCreateLabelEvent,
                                onEditLabelEvent = viewModel::onUpdateLabelEvent,
                                uiEvents = viewModel.uiEvents
                            )
                        }

                    }
                }
            }
        }
    }
}
