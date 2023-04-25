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
import com.eva.reminders.presentation.feature_labels.AddLabelViewModel
import com.eva.reminders.presentation.feature_labels.AddLabelsRoute
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
                    // Keeping this viewModel in the global scope as its accessed by two routes
                    // This maybe a bit problematic as the selected state doesn't changes for any
                    // Not thinking much for now
                    val addLabelViewModel = hiltViewModel<AddLabelViewModel>()
                    val selectedLabels by addLabelViewModel.labelSelector.collectAsStateWithLifecycle()
                    NavHost(
                        navController = navHost,
                        startDestination = NavRoutes.Home.route
                    ) {
                        composable(NavRoutes.Home.route) {
                            val labelsViewModel = hiltViewModel<LabelsViewModel>()
                            val labels by labelsViewModel.allLabels.collectAsStateWithLifecycle()
                            HomeRoute(navController = navHost, labels = labels)
                        }
                        composable(NavRoutes.AddTask.route) {
                            val viewModel = hiltViewModel<CreateTaskViewModel>()
                            val state by viewModel.task.collectAsStateWithLifecycle()
                            val reminderState by viewModel.reminder.collectAsStateWithLifecycle()
                            CreateReminderRoute(
                                navController = navHost,
                                state = state,
                                reminderState = reminderState,
                                onCreateTaskEvents = viewModel::onCreateTaskEvent,
                                onRemindersEvents = viewModel::onReminderEvents,
                                selectedLabels = addLabelViewModel.pickedLabels.filter { it.isSelected }
                                    .map { it.toModel() }
                            )
                        }
                        composable(NavRoutes.AddLabels.route) {

                            val query by addLabelViewModel.query.collectAsStateWithLifecycle()
                            AddLabelsRoute(
                                navController = navHost,
                                labels = selectedLabels,
                                query = query,
                                onSearch = addLabelViewModel::searchLabels,
                                onSelect = addLabelViewModel::onSelect,
                                onCreateNew = addLabelViewModel::createLabel,
                                uiEvents = addLabelViewModel.uiEvents,
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
