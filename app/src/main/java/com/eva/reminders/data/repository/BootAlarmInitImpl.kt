package com.eva.reminders.data.repository

import com.eva.reminders.data.local.dao.TaskDao
import com.eva.reminders.data.mappers.toModel
import com.eva.reminders.domain.models.TaskModel
import com.eva.reminders.domain.repository.BootAlarmInitRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BootAlarmInitImpl(
    private val tasksDao: TaskDao
) : BootAlarmInitRepo {
    override suspend fun initializeTasks(): List<TaskModel> =
        withContext(Dispatchers.IO) {
            tasksDao.getTasks().filter { it.time != null }.map { it.toModel() }
        }

}