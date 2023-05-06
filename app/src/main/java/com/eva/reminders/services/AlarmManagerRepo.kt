package com.eva.reminders.services

import com.eva.reminders.domain.models.TaskModel

interface AlarmManagerRepo {
    fun createAlarm(taskModel: TaskModel)
    fun stopAlarm(taskModel: TaskModel)
    fun updateAlarm(taskModel: TaskModel)
}