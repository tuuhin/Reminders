package com.eva.reminders.domain.repository

import com.eva.reminders.domain.models.TaskLabelModel
import com.eva.reminders.utils.Resource
import kotlinx.coroutines.flow.Flow

interface TaskLabelsRepository {
    suspend fun createLabel(label: String): Resource<TaskLabelModel>
    suspend fun updateLabel(label: TaskLabelModel): Resource<TaskLabelModel>
    suspend fun deleteLabel(label: TaskLabelModel): Resource<Boolean>
    suspend fun getLabels(): Flow<List<TaskLabelModel>>
    suspend fun searchLabels(query: String): Flow<List<TaskLabelModel>>
}