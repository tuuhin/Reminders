package com.eva.reminders

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eva.reminders.presentation.feature_create.CreateReminderRoute
import com.eva.reminders.presentation.feature_create.CreateTaskViewModel
import com.eva.reminders.presentation.feature_home.HomeRoute
import com.eva.reminders.presentation.utils.NavRoutes
import com.eva.reminders.ui.theme.RemindersTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RemindersTheme {
                // A surface container using the 'background' color from the theme
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
                            HomeRoute(navController = navHost)
                        }
                        composable(NavRoutes.AddReminder.route) {
                            val viewModel = viewModel<CreateTaskViewModel>()
                            val state = viewModel.task.collectAsState()
                            CreateReminderRoute(
                                navController = navHost,
                                state = state.value,
                                onCreateTaskEvents = viewModel::onCreateTaskEvent
                            )
                        }
                    }
                }
            }
        }
    }
}
