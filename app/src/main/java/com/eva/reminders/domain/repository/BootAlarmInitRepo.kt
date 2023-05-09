package com.eva.reminders.domain.repository

import com.eva.reminders.domain.models.TaskModel

interface BootAlarmInitRepo {
    suspend fun initializeTasks(): List<TaskModel>
}