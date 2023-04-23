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
import com.eva.reminders.data.parcelable.LabelParceler
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

                            val labels =
                                navHost.currentBackStackEntry?.savedStateHandle?.get<List<LabelParceler>>(
                                    "SELECTED_LABELS",
                                )?.map { it.toModel() }

                            CreateReminderRoute(
                                navController = navHost,
                                state = state,
                                reminderState = reminderState,
                                onCreateTaskEvents = viewModel::onCreateTaskEvent,
                                onRemindersEvents = viewModel::onReminderEvents,
                                selectedLabels = labels
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
                        composable(NavRoutes.AddLabels.route) {
                            val viewModel = hiltViewModel<AddLabelViewModel>()
                            val labels by viewModel.labelSelector.collectAsStateWithLifecycle()
                            val query by viewModel.query.collectAsStateWithLifecycle()

                            AddLabelsRoute(
                                navController = navHost,
                                labels = labels,
                                query = query,
                                onSearch = viewModel::searchLabels,
                                onSelect = viewModel::onSelect,
                                onCreateNew = viewModel::createLabel,
                                uiEvents = viewModel.uiEvents,
                            )
                        }
                    }
                }
            }
        }
    }
}
