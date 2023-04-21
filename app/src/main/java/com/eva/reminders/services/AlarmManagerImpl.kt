package com.eva.reminders.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.content.getSystemService
import com.eva.reminders.domain.models.TaskModel
import java.time.ZoneId

class AlarmManagerImpl(
    private val context: Context
) : AlarmManagerRepo {

    private val alarmManager = context.getSystemService<AlarmManager>()

    override fun createAlarm(taskModel: TaskModel) {
        val intent = Intent(context, ReminderReceiver::class.java)
            .apply {
                putExtra("TITLE", taskModel.title)
                putExtra("ID", taskModel.id)
            }
        val pendingIntent = PendingIntent
            .getBroadcast(context, taskModel.id, intent, PendingIntent.FLAG_IMMUTABLE)
        val alarmTime = taskModel.createTime
            .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        alarmManager?.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarmTime,
            pendingIntent
        )
    }

    override fun stopAlarm(taskModel: TaskModel) {
        val intent = Intent(context, ReminderReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(context, taskModel.id, intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager?.cancel(pendingIntent)
    }
}