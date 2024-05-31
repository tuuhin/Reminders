package com.eva.reminders.data.repository

import com.eva.reminders.data.mappers.toEntity
import com.eva.reminders.data.mappers.toModel
import com.eva.reminders.domain.models.CreateTaskModel
import com.eva.reminders.domain.models.TaskModel
import com.eva.reminders.domain.repository.TaskRepository
import com.eva.reminders.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update


class TaskRepoTestImpl : TaskRepository {

    private val _inMemoryDb = MutableStateFlow(emptyList<TaskModel>())

    override suspend fun createTask(model: CreateTaskModel): Resource<TaskModel?> {
        val taskIndex = _inMemoryDb.value.size + 1
        val fakeModel = model.toEntity().copy(id = taskIndex).toModel()
        _inMemoryDb.update { it + fakeModel }
        return Resource.Success(data = fakeModel)
    }

    override suspend fun deleteTask(task: TaskModel): Resource<Boolean> {
        val isTaskPresent = _inMemoryDb.value.contains(task)
        if (!isTaskPresent) return Resource.Error(message = "Cannot find label")
        val filteredTask = _inMemoryDb.value.filter { it.id != task.id }
        _inMemoryDb.update { filteredTask }
        return Resource.Success(true)
    }

    override suspend fun getAllTasks(): Flow<Resource<List<TaskModel>>> {
        return _inMemoryDb.map { Resource.Success(data = it) }
    }

    override suspend fun getTaskById(task: Long): Resource<TaskModel?> {
        return _inMemoryDb.value.find { it.id.toLong() == task }?.let {
            Resource.Success(data = it)
        } ?: Resource.Error(message = "Task With Id $task not found")
    }

    override suspend fun updateTask(task: TaskModel): Resource<TaskModel?> {
        if (task.title.isEmpty()) return Resource.Error(message = "Task Cannot have an empty title")
        val isAbsent = _inMemoryDb.value.all { it.id != task.id }
        if (isAbsent) return Resource.Error(message = "Label missing")
        _inMemoryDb.update { models ->
            models.map { model ->
                if (task.id == model.id) task
                else model
            }
        }
        return _inMemoryDb.value.find { it.id == task.id }?.let { Resource.Success(data = it) }
            ?: Resource.Error(message = "Task Model not found")
    }

}