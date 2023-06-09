package com.eva.reminders.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.content.getSystemService
import com.eva.reminders.domain.models.TaskModel
import com.eva.reminders.utils.NotificationConstants
import java.time.Instant
import java.time.LocalDate
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

        if (taskModel.reminderAt.at == null) return

        val intent = Intent(context, ReminderReceiver::class.java)
            .apply {
                putExtra("TITLE", taskModel.title)
                putExtra("CONTENT", taskModel.content)
                putExtra("ID", taskModel.id)

                action = NotificationConstants.NOTIFICATION_READ_ACTION
            }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            taskModel.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmTime =
            taskModel.reminderAt.at
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()

        if (taskModel.reminderAt.isRepeating) {
            setRepeatingAlarms(
                taskModel = taskModel,
                alarmTime = alarmTime,
                pendingIntent = pendingIntent,
            )
            return
        }

        setNonRepeatingAlarm(
            alarmTime = alarmTime,
            taskModel = taskModel,
            pendingIntent = pendingIntent
        )

    }


    override fun stopAlarm(taskModel: TaskModel) {

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
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        if (pendingIntent != null) {
            Log.i(alarmTag, "Canceling the previous alarm ${taskModel.reminderAt.at}")
            alarmManager?.cancel(pendingIntent)
        }
    }

    override fun updateAlarm(taskModel: TaskModel) {
        if (taskModel.reminderAt.at == null)
            stopAlarm(taskModel)
        createAlarm(taskModel)
    }

    private fun setNonRepeatingAlarm(
        alarmTime: Long,
        taskModel: TaskModel,
        pendingIntent: PendingIntent
    ) {
        if (taskModel.reminderAt.at == null) return
        //Checking if the time has already passed for the non repeating alarm
        if (taskModel.reminderAt.at < LocalDateTime.now() && !taskModel.reminderAt.isRepeating) {
            Log.w(alarmTag, "Cannot set the non repeating alarm as the time had already passed")
            return
        }

        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
            && alarmManager?.canScheduleExactAlarms() == true
        ) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                alarmTime,
                pendingIntent
            )
            Log.i(alarmTag, "setExactAndAllowWhileIdle : ${taskModel.reminderAt.at}")
            return
        }
        alarmManager?.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarmTime,
            pendingIntent
        )
        Log.d(alarmTag, "setAndAllowWhileIdle : ${taskModel.reminderAt.at} ")
    }

    private fun setRepeatingAlarms(
        taskModel: TaskModel,
        alarmTime: Long,
        pendingIntent: PendingIntent,
        intervalInMillis: Long = AlarmManager.INTERVAL_DAY
    ) {
        if (taskModel.reminderAt.at == null) return

        // Adding the extra days to make the alarm work properly
        val daysDifference = if (taskModel.reminderAt.at < LocalDateTime.now()) {
            val extra =
                LocalDate.now().dayOfYear - taskModel.reminderAt.at.toLocalDate().dayOfYear
            if (extra != 0) extra else 1
        } else 0

        val extraMillis = daysDifference.days.toInt(DurationUnit.MILLISECONDS)
        if (taskModel.isExact) {
            alarmManager?.setRepeating(
                AlarmManager.RTC_WAKEUP,
                alarmTime + extraMillis,
                intervalInMillis,
                pendingIntent
            )
        } else {
            alarmManager?.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                alarmTime + extraMillis,
                intervalInMillis,
                pendingIntent
            )
        }

        val alarmTimeLog =
            Instant.ofEpochMilli(alarmTime + extraMillis)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()

        Log.d(alarmTag, "setInexactRepeating : $alarmTimeLog")

        return
    }

}