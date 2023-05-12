package com.eva.reminders.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import com.eva.reminders.MainActivity
import com.eva.reminders.R
import com.eva.reminders.utils.NotificationConstants
import com.eva.reminders.utils.getFirstSentence
import kotlin.random.Random

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action == NotificationConstants.NOTIFICATION_READ_ACTION) {

            val title = intent.getStringExtra("TITLE") ?: "Blank"
            val content = intent.getStringExtra("CONTENT")
            val taskId = intent.getIntExtra("ID", -1)

            val notificationReadRequestCode = - (1 * Random(taskId).nextInt())
            val activityRequestCode = - (2 * Random(taskId).nextInt())

            if (taskId != -1) {

                val notificationManager = context.getSystemService<NotificationManager>()

                val readIntent =
                    PendingIntent.getBroadcast(
                        context,
                        notificationReadRequestCode,
                        Intent(context, RemoveNotificationReceiver::class.java)
                            .apply {
                                putExtra("TASK_ID", taskId)
                                action = NotificationConstants.NOTIFICATION_INTENT_ACTION
                            },
                        PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )

                val activityIntent = PendingIntent.getActivity(
                    context,
                    activityRequestCode,
                    Intent(context, MainActivity::class.java).apply {
                        putExtra("TASK_ID", taskId)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        action = NotificationConstants.NOTIFICATION_INTENT_ACTION
                    },
                    PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )

                val notification =
                    NotificationCompat.Builder(
                        context,
                        NotificationConstants.NOTIFICATION_CHANNEL_ID
                    )
                        .setSmallIcon(R.drawable.notification_logo)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_REMINDER)
                        .setOngoing(true)
                        .setContentTitle(title)
                        .apply {
                            if (!content.isNullOrEmpty())
                                setContentText(content.getFirstSentence())
                        }
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setVibrate(longArrayOf(0L, 400L, 200L, 400L))
                        .setContentIntent(activityIntent)
                        .setFullScreenIntent(activityIntent, true)
                        .addAction(R.drawable.notification_logo, "Read", readIntent)
                        .build()

                notificationManager?.notify(taskId, notification)
            }
        }
    }
}