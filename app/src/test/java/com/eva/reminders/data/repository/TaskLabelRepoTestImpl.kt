package com.eva.reminders.data.repository

import com.eva.reminders.domain.models.TaskLabelModel
import com.eva.reminders.domain.repository.TaskLabelsRepository
import com.eva.reminders.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class TaskLabelRepoTestImpl : TaskLabelsRepository {

    private val _inMemoryDb = MutableStateFlow(emptyList<TaskLabelModel>())

    override suspend fun createLabel(label: String): Resource<TaskLabelModel> {
        val labelIndex = _inMemoryDb.value.size + 1
        val fakeModel = TaskLabelModel(labelIndex, label)
        _inMemoryDb.update { it + fakeModel }
        return Resource.Success(data = fakeModel)
    }

    override suspend fun updateLabel(label: TaskLabelModel): Resource<TaskLabelModel> {
        if (label.label.isEmpty()) return Resource.Error(message = "Cannot have empty strings")
        val isAbsent = _inMemoryDb.value.all { it.id != label.id }
        if (isAbsent) return Resource.Error(message = "Label missing")
        _inMemoryDb.update { models ->
            models.map { model ->
                if (label.id == model.id) label
                else model
            }
        }
        return _inMemoryDb.value.find { it.id == label.id }?.let { Resource.Success(data = it) }
            ?: Resource.Error(message = "Label Model is not found")
    }

    override suspend fun deleteLabel(label: TaskLabelModel): Resource<Boolean> {
        val labelPresent = _inMemoryDb.value.contains(label)
        if (!labelPresent) return Resource.Error(message = "Cannot find label")
        val filterListWithoutLabel = _inMemoryDb.value.filter { it.id != label.id }
        _inMemoryDb.update { filterListWithoutLabel }
        return Resource.Success(true)
    }

    override suspend fun getLabels(): Flow<Resource<List<TaskLabelModel>>> {
        return _inMemoryDb.map { Resource.Success(it) }
    }

    override suspend fun searchLabels(query: String): Resource<List<TaskLabelModel>> {

        val results = when (query) {
            "" -> _inMemoryDb.value
            else -> _inMemoryDb.value.filter { it.label.contains(query.trim()) }
        }

        return Resource.Success(data = results)
    }
}