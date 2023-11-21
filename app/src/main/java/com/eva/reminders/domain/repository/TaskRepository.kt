package com.eva.reminders.domain.repository

import com.eva.reminders.domain.models.CreateTaskModel
import com.eva.reminders.domain.models.TaskModel
import com.eva.reminders.utils.Resource
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    suspend fun createTask(model: CreateTaskModel): Resource<TaskModel?>
    suspend fun deleteTask(task: TaskModel): Resource<Boolean>
    suspend fun getAllTasks(): Flow<Resource<List<TaskModel>>>
    suspend fun getTaskById(task: Long): Resource<TaskModel?>
    suspend fun updateTask(task: TaskModel): Resource<TaskModel?>
}