package com.eva.reminders.services

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.getSystemService
import com.eva.reminders.utils.NotificationConstants

class RemoveNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != NotificationConstants.NOTIFICATION_INTENT_ACTION) return

        val notificationId = intent.getIntExtra("TASK_ID", -1)

        if (notificationId == -1) return

        val notificationManager = context.getSystemService<NotificationManager>()
        notificationManager?.cancel(notificationId)

    }

}