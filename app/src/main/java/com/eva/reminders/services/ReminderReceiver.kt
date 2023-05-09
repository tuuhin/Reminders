package com.eva.reminders.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import com.eva.reminders.R
import com.eva.reminders.utils.NotificationConstants
import com.eva.reminders.utils.getFirstSentence
import kotlin.random.Random

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        val notificationManager = context.getSystemService<NotificationManager>()

        val title = intent.getStringExtra("TITLE") ?: "Blank"
        val content = intent.getStringExtra("CONTENT")
        val taskId = intent.getIntExtra("ID", -1)
        if (taskId != -1) {
            val readIntent =
                PendingIntent.getBroadcast(
                    context,
                    -1 * Random(100).nextInt(),
                    Intent(context, RemoveNotificationReceiver::class.java)
                        .apply {
                            putExtra("TASK_ID", taskId)
                        },
                    PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
                )

            val notification =
                NotificationCompat.Builder(context, NotificationConstants.NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.notification_logo)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setOngoing(true)
                    .setContentTitle(title)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .apply {
                        if (!content.isNullOrEmpty())
                            setContentText(content.getFirstSentence())
                    }
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setVibrate(longArrayOf(1L, 2L, 1L))
                    .addAction(R.drawable.notification_logo, "Read", readIntent)
                    .build()

            notificationManager?.notify(taskId, notification)
        }
    }
}