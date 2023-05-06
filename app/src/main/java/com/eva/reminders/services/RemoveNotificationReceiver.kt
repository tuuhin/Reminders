package com.eva.reminders.services

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.getSystemService

class RemoveNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val notificationId = intent.getIntExtra("TASK_ID", -1)
        if (notificationId != -1) {
            val notificationManager = context.getSystemService<NotificationManager>()
            notificationManager?.cancel(notificationId)
        }
    }
}