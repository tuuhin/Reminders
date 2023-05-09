package com.eva.reminders

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.core.content.getSystemService
import com.eva.reminders.utils.NotificationConstants
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val channel = NotificationChannel(
            NotificationConstants.NOTIFICATION_CHANNEL_ID,
            NotificationConstants.NOTIFICATION_CHANNEL,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = NotificationConstants.NOTIFICATION_CHANNEL_DESC
            setShowBadge(true)
        }
        val notificationManager = getSystemService<NotificationManager>()
        notificationManager?.createNotificationChannel(channel)
    }
}
