package com.eva.reminders.services

import com.eva.reminders.domain.models.TaskModel
import java.time.LocalDateTime

interface AlarmManagerRepo {

    /**
     * Creates an Alarm
     */
    fun createAlarm(taskModel: TaskModel)

    /**
     * Stops the Alarm
     */
    fun stopAlarm(taskModel: TaskModel)

    /**
     * Cancels or Create Alarm if [LocalDateTime] is provided its created otherwise cancelled
     */
    fun cancelOrCreateAlarm(taskModel: TaskModel)
}