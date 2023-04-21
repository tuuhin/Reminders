package com.eva.reminders.services

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import com.eva.reminders.R
import com.eva.reminders.utils.NotificationConstants

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val notificationManager = context.getSystemService<NotificationManager>()

        val title = intent.extras?.getString("TITLE", "")
        val taskId = intent.extras?.getInt("ID", 0) ?: 0
        val channelId = NotificationConstants.NOTIFICATION_CHANNEL_ID

        val notification =
            NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.notification_logo)
                .setContentTitle("APP_NAME")
                .setContentText(title)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(true)
                .build()

        notificationManager?.notify(taskId, notification)
    }
}