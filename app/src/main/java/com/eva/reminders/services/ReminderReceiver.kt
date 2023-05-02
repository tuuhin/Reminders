package com.eva.reminders.services

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import com.eva.reminders.R
import com.eva.reminders.utils.NotificationConstants
import com.eva.reminders.utils.getFirstSentence

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        val notificationManager = context.getSystemService<NotificationManager>()

        val title = intent.extras?.getString("TITLE", "") ?: ""
        val content = (intent.extras?.getString("CONTENT", "") ?: "").getFirstSentence()
        val taskId = intent.extras?.getInt("ID", 0) ?: 0

        val notification =
            NotificationCompat.Builder(context, NotificationConstants.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_logo)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(false)
                .setContentTitle(title)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .apply {
                    if (content.isNotEmpty())
                        setContentText(content)
                }
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .build()

        notificationManager?.notify(taskId, notification)
    }
}