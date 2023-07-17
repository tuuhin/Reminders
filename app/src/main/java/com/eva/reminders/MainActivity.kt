package com.eva.reminders

import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.getSystemService
import com.eva.reminders.presentation.NavigationGraph
import com.eva.reminders.ui.theme.RemindersTheme
import com.eva.reminders.utils.NotificationConstants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val notificationManager by lazy { getSystemService<NotificationManager>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RemindersTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationGraph()
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent?.action != NotificationConstants.NOTIFICATION_INTENT_ACTION) return
        // Canceling the requested notification if this is requested via a notification
        val extra = intent.getIntExtra(NotificationConstants.INTENT_EXTRA_ID, -1)
        if (extra == -1) return
        notificationManager?.cancel(extra)
    }
}
