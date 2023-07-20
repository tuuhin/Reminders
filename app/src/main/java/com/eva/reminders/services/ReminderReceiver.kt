package com.eva.reminders.services

import android.app.Notification
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

        if (intent.action != NotificationConstants.NOTIFICATION_READ_ACTION) return

        val title = intent.getStringExtra("TITLE") ?: "Blank"
        val content = intent.getStringExtra("CONTENT")
        val taskId = intent.getIntExtra("ID", -1)

        val notificationReadRequestCode = -(1 * Random(taskId).nextInt())
        val activityRequestCode = -(2 * Random(taskId).nextInt())

        if (taskId == -1) return

        val notificationManager by lazy { context.getSystemService<NotificationManager>() }

        val readIntent = PendingIntent.getBroadcast(
            context, notificationReadRequestCode,
            Intent(context, RemoveNotificationReceiver::class.java).apply {
                putExtra("TASK_ID", taskId)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                action = NotificationConstants.NOTIFICATION_INTENT_ACTION
            },
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val activityIntent = PendingIntent.getActivity(
            context, activityRequestCode,
            Intent(context, MainActivity::class.java).apply {
                putExtra("TASK_ID", taskId)
                action = NotificationConstants.NOTIFICATION_INTENT_ACTION
            },
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val readAction =
            NotificationCompat.Action
                .Builder(R.drawable.ic_notification_bell, "Read", readIntent)
                .setAuthenticationRequired(true)
                .build()


        val publicNotification =
            NotificationCompat.Builder(context, NotificationConstants.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification_bell)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setContentTitle(title)
                .setOngoing(true)
                .build()


        val notification =
            NotificationCompat
                .Builder(context, NotificationConstants.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification_bell)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setOngoing(false)
                .setContentTitle(title)
                .setContentIntent(activityIntent)
                .apply {
                    if (!content.isNullOrEmpty()) {
                        setContentText(content.getFirstSentence())
                        setStyle(NotificationCompat.BigTextStyle().bigText(content))
                    }
                }
                .addAction(readAction)
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .setPublicVersion(publicNotification)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .build()


        notificationManager?.notify(taskId, notification)
    }
}