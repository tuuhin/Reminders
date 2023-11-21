package com.eva.reminders

import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.content.getSystemService
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.eva.reminders.presentation.navigation.RootNavGraph
import com.eva.reminders.presentation.utils.LocalSnackBarHostProvider
import com.eva.reminders.ui.theme.RemindersTheme
import com.eva.reminders.utils.IntentsExtra
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navHostController: NavHostController

    private val notificationManager by lazy { getSystemService<NotificationManager>() }

    override fun onCreate(savedInstanceState: Bundle?) {

        WindowCompat.setDecorFitsSystemWindows(window, false)

        installSplashScreen()
        super.onCreate(savedInstanceState)

        //if the user clicks open when the activity is in [Lifecycle.State.CREATED]
        intent?.getIntExtra(IntentsExtra.TASK_ID, -1)?.let { taskId ->
            if (taskId != -1) {
                notificationManager?.cancel(taskId)
            }
        }

        setContent {
            RemindersTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    navHostController = rememberNavController()
                    val snackBarHostState = remember { SnackbarHostState() }
                    CompositionLocalProvider(
                        LocalSnackBarHostProvider provides snackBarHostState
                    ) {
                        RootNavGraph(navHost = navHostController)

                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navHostController.handleDeepLink(intent)
        //if the user clicks open when the activity is in [Lifecycle.State.STARTED]
        intent?.getIntExtra(IntentsExtra.TASK_ID, -1)?.let { taskId ->
            if (taskId != -1) {
                notificationManager?.cancel(taskId)
            }
        }
    }
}
