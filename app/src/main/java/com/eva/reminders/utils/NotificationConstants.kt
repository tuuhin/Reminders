package com.eva.reminders.utils

object NotificationConstants {
    const val NOTIFICATION_CHANNEL = "ReminderChannelId"
    const val NOTIFICATION_CHANNEL_ID = "ReminderChannel"
    const val NOTIFICATION_CHANNEL_DESC = "A notification channel to show reminder notifications"

    // Returns a random notification read_Code for a notification
    const val NOTIFICATION_READ = -100
    const val ACTIVITY_INTENT = -200
    const val NOTIFICATION_INTENT_ACTION = "com.eva.reminders.CLOSE_NOTIFICATION"
    const val NOTIFICATION_READ_ACTION = "com.eva.reminders.RECEIVE_NOTIFICATION"
}