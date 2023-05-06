package com.eva.reminders.services

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.core.content.getSystemService
import com.eva.reminders.domain.models.TaskModel
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.time.Duration.Companion.days
import kotlin.time.DurationUnit

class AlarmManagerImpl(
    private val context: Context
) : AlarmManagerRepo {

    private val alarmTag = "ALARM_TAG"

    private val alarmManager = context.getSystemService<AlarmManager>()

    override fun createAlarm(taskModel: TaskModel) {
        if (taskModel.reminderAt.at != null && taskModel.reminderAt.at > LocalDateTime.now()) {
            val intent = Intent(context, ReminderReceiver::class.java)
                .apply {
                    putExtra("TITLE", taskModel.title)
                    putExtra("CONTENT", taskModel.content)
                    putExtra("ID", taskModel.id)
                }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                taskModel.id,
                intent,
                PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val alarmTime =
                taskModel.reminderAt.at
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()

            if (taskModel.reminderAt.isRepeating) {
                Log.d(alarmTag, "Settings the Repeating alarm at ${taskModel.reminderAt.at}")
                alarmManager?.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    alarmTime,
                    1.days.toLong(DurationUnit.MILLISECONDS),
                    pendingIntent
                )
                return
            }
            Log.d(alarmTag, "Settings the non Repeating alarm at ${taskModel.reminderAt.at}")
            if (
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.SCHEDULE_EXACT_ALARM
                ) == PermissionChecker.PERMISSION_GRANTED
            ) {
                alarmManager?.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    alarmTime,
                    pendingIntent
                )
                return
            }
            alarmManager?.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                alarmTime,
                pendingIntent
            )
        }
    }

    override fun stopAlarm(taskModel: TaskModel) {
        val intent = Intent(context, ReminderReceiver::class.java)
        val alarmExists = PendingIntent.getBroadcast(
            context,
            taskModel.id,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (alarmExists != null) {
            val pendingIntent =
                PendingIntent.getBroadcast(
                    context,
                    taskModel.id,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT
                )
            Log.d(alarmTag, "Canceling the alarm ${taskModel.reminderAt.at}")

            alarmManager?.cancel(pendingIntent)
        }
    }

    override fun updateAlarm(taskModel: TaskModel) {
        stopAlarm(taskModel)
        createAlarm(taskModel)
    }
}