package com.eva.reminders.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.content.getSystemService
import com.eva.reminders.domain.models.TaskModel
import com.eva.reminders.presentation.navigation.NavigationDeepLinks
import com.eva.reminders.utils.IntentsExtra
import com.eva.reminders.utils.NotificationConstants
import com.eva.reminders.utils.millisToLocalDateTime
import com.eva.reminders.utils.nextAlarmTimeInMillis
import java.time.LocalDateTime

class AlarmManagerImpl(
    private val context: Context
) : AlarmManagerRepo {

    private val alarmTag = "ALARM_TAG"

    private val alarmManager by lazy { context.getSystemService<AlarmManager>() }

    private val alarmType = AlarmManager.RTC_WAKEUP

    override fun createAlarm(taskModel: TaskModel) {

        if (taskModel.reminderAt.at == null) return

        val intent = Intent(context, ReminderReceiver::class.java)
            .apply {
                putExtra(IntentsExtra.TASK_TITLE, taskModel.title)
                putExtra(IntentsExtra.TASK_CONTENT, taskModel.content)
                putExtra(IntentsExtra.TASK_ID, taskModel.id)
                /** data is not used but to help in [Intent.filterEquals] ,uri is sent
                 * This intent data can be used in the notification for the open intent
                 * deep link uri
                 * */
                data = NavigationDeepLinks.taskUriFromTaskId(taskModel.id)
                action = NotificationConstants.NOTIFICATION_READ_ACTION
            }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            taskModel.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )


        if (taskModel.reminderAt.isRepeating) {
            setRepeatingAlarms(
                alarmTime = taskModel.reminderAt.at,
                pendingIntent = pendingIntent,
                isExact = taskModel.isExact
            )
            return
        }

        setNonRepeatingAlarm(
            alarmTime = taskModel.reminderAt.at,
            pendingIntent = pendingIntent,
        )

    }


    override fun stopAlarm(taskModel: TaskModel) {

        val intent = Intent(context, ReminderReceiver::class.java)
            .apply {
                /** data is not used but to help in [Intent.filterEquals] ,uri is sent*/
                data = NavigationDeepLinks.taskUriFromTaskId(taskModel.id)
                action = NotificationConstants.NOTIFICATION_READ_ACTION
            }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            taskModel.id,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE
        )

        if (pendingIntent != null) {
            Log.i(alarmTag, "Canceling the previous alarm ${taskModel.reminderAt.at}")
            alarmManager?.cancel(pendingIntent)
        }
    }

    override fun cancelOrCreateAlarm(taskModel: TaskModel) {
        if (taskModel.reminderAt.at == null) {
            stopAlarm(taskModel)
            return
        }
        createAlarm(taskModel)
    }

    private fun setNonRepeatingAlarm(
        alarmTime: LocalDateTime,
        pendingIntent: PendingIntent
    ) {
        //Checking if the time has already passed for the non-repeating alarm
        if (alarmTime < LocalDateTime.now()) return

        val canScheduleAlarm = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                && alarmManager?.canScheduleExactAlarms() == true

        /** although its calling [nextAlarmTimeInMillis] but as the [alarmTime] is
         *in future it will return that time only without adding or subtracting seconds
         */
        val triggerTime = alarmTime.nextAlarmTimeInMillis()
        val alarmTimeLog = millisToLocalDateTime(triggerTime)

        if (canScheduleAlarm) {
            alarmManager?.setExactAndAllowWhileIdle(
                /* type = */ AlarmManager.RTC_WAKEUP,
                /* triggerAtMillis = */ triggerTime,
                /* operation = */ pendingIntent
            )
            Log.i(alarmTag, "SET EXACT AND ALLOW IDLE : $alarmTimeLog")
            return
        }
        alarmManager?.setAndAllowWhileIdle(
            /* type = */ AlarmManager.RTC_WAKEUP,
            /* triggerAtMillis = */ triggerTime,
            /* operation = */ pendingIntent
        )
        Log.d(alarmTag, "SET AND ALLOW IDLE : $alarmTimeLog")
    }

    private fun setRepeatingAlarms(
        isExact: Boolean,
        alarmTime: LocalDateTime,
        pendingIntent: PendingIntent,
        intervalInMillis: Long = AlarmManager.INTERVAL_DAY
    ) {
        // Adding the extra days to make the alarm work properly
        val triggerTime = alarmTime.nextAlarmTimeInMillis()

        val alarmTimeLog = millisToLocalDateTime(triggerTime)

        if (isExact) {
            alarmManager?.setRepeating(
                /* type = */ alarmType,
                /* triggerAtMillis = */ triggerTime,
                /* intervalMillis = */ intervalInMillis,
                /* operation = */ pendingIntent
            )
            Log.d(alarmTag, "REPEATING AT : $alarmTimeLog")
            return
        }
        alarmManager?.setInexactRepeating(
            /* type = */ alarmType,
            /* triggerAtMillis = */ triggerTime,
            /* intervalMillis = */ intervalInMillis,
            /* operation = */ pendingIntent
        )
        Log.d(alarmTag, "IN EXACT REPEATING AT : $alarmTimeLog")

    }
}
