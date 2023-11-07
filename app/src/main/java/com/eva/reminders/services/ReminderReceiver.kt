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
import com.eva.reminders.presentation.navigation.NavigationDeepLinks
import com.eva.reminders.utils.IntentsExtra
import com.eva.reminders.utils.NotificationConstants
import com.eva.reminders.utils.getFirstSentence
import kotlin.random.Random

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action != NotificationConstants.NOTIFICATION_READ_ACTION) return

        val title = intent.getStringExtra(IntentsExtra.TASK_TITLE) ?: ""
        val content = intent.getStringExtra(IntentsExtra.TASK_CONTENT)
        val taskId = intent.getIntExtra(IntentsExtra.TASK_ID, -1)

        val randomValue = Random(taskId).nextInt()

        val seenPendingIntentRequestCode = -1 * randomValue
        val homePendingIntentRequestCode = -2 * randomValue
        val openPendingIntentRequestCode = -3 * randomValue

        if (taskId == -1) return

        val notificationManager by lazy { context.getSystemService<NotificationManager>() }

        val seenNotificationIntent = Intent(context, RemoveNotificationReceiver::class.java)
            .apply {
                putExtra(IntentsExtra.TASK_ID, taskId)
                flags = Intent.FLAG_RECEIVER_REPLACE_PENDING
                action = NotificationConstants.NOTIFICATION_INTENT_ACTION
            }

        val readIntent = PendingIntent.getBroadcast(
            context,
            seenPendingIntentRequestCode,
            seenNotificationIntent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val taskLinkIntent = Intent(context, MainActivity::class.java).apply {
            data = NavigationDeepLinks.taskUriFromTaskId(taskId)
            action = Intent.ACTION_VIEW
        }

        val openTaskPendingIntent = PendingIntent.getActivity(
            context,
            openPendingIntentRequestCode,
            taskLinkIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val homeLinkIntent = Intent(context, MainActivity::class.java).apply {
            data = NavigationDeepLinks.homeUri
            action = Intent.ACTION_VIEW
        }

        val activityIntent = PendingIntent.getActivity(
            context,
            homePendingIntentRequestCode,
            homeLinkIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val seenAction =
            NotificationCompat.Action
                .Builder(0, context.getString(R.string.notification_seen_action), readIntent)
                .setAuthenticationRequired(true)
                .build()

        val openAction = NotificationCompat.Action
            .Builder(1, context.getString(R.string.notification_open_action), openTaskPendingIntent)
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
                .setOngoing(true)
                .setContentTitle(title)
                .setContentIntent(activityIntent)
                .apply {
                    if (!content.isNullOrEmpty()) {
                        setContentText(content.getFirstSentence())
                        setStyle(NotificationCompat.BigTextStyle().bigText(content))
                    }
                }
                .addAction(seenAction)
                .addAction(openAction)
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .setPublicVersion(publicNotification)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setAutoCancel(true)
                .build()

        notificationManager?.notify(taskId, notification)
    }
}