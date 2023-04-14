package com.eva.reminders

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eva.reminders.presentation.feature_create.CreateReminderRoute
import com.eva.reminders.presentation.feature_create.CreateTaskViewModel
import com.eva.reminders.presentation.feature_home.HomeRoute
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
                        startDestination = NavRoutes.EditLabels.route
                    ) {
                        composable(NavRoutes.Home.route) {
                            HomeRoute(navController = navHost)
                        }
                        composable(NavRoutes.AddTask.route) {
                            val viewModel = viewModel<CreateTaskViewModel>()
                            val state = viewModel.task.collectAsStateWithLifecycle()
                            CreateReminderRoute(
                                navController = navHost,
                                state = state.value,
                                onCreateTaskEvents = viewModel::onCreateTaskEvent
                            )
                        }
                        composable(NavRoutes.EditLabels.route) {
                            val viewModel = hiltViewModel<LabelsViewModel>()
                            val createState =
                                viewModel.createLabelState.collectAsStateWithLifecycle()
                            val editState =
                                viewModel.updatedLabels.collectAsStateWithLifecycle()
                            val uiEvents = viewModel.uiEvents
                            EditLabelRoute(
                                navController = navHost,
                                createLabelState = createState.value,
                                editLabelState = editState.value,
                                onCreateLabelEvent = viewModel::onCreateLabelEvent,
                                onEditLabelEvent = viewModel::onUpdateLabelEvent,
                                uiEvents = uiEvents
                            )
                        }
                    }
                }
            }
        }
    }
}
