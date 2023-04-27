package com.eva.reminders.domain.repository

import com.eva.reminders.domain.enums.TaskColorEnum
import com.eva.reminders.domain.models.TaskLabelModel
import com.eva.reminders.domain.models.TaskModel
import com.eva.reminders.domain.models.TaskReminderModel
import com.eva.reminders.utils.Resource
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    suspend fun createTask(
        title: String,
        content: String,
        isPinned: Boolean,
        isArchive: Boolean,
        time: TaskReminderModel?,
        labels: List<TaskLabelModel>,
        colorEnum: TaskColorEnum,
    ): Resource<TaskModel?>

    suspend fun deleteTask(task: TaskModel): Resource<Boolean>

    suspend fun getAllTasks(): Flow<Resource<List<TaskModel>>>

    suspend fun updateTask(task: TaskModel): Resource<TaskModel?>

}