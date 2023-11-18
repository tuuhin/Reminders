package com.eva.reminders.data.repository

import android.database.sqlite.SQLiteConstraintException
import com.eva.reminders.data.local.dao.TaskDao
import com.eva.reminders.data.local.dao.TaskLabelRelDao
import com.eva.reminders.data.mappers.toEntity
import com.eva.reminders.data.mappers.toModel
import com.eva.reminders.domain.models.CreateTaskModel
import com.eva.reminders.domain.models.TaskModel
import com.eva.reminders.domain.repository.TaskRepository
import com.eva.reminders.utils.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TaskRepoInstTestImpl @Inject constructor(
    private val taskDao: TaskDao,
    private val taskLabelRel: TaskLabelRelDao,
    private val dispatcher: CoroutineDispatcher = StandardTestDispatcher(),
) : TaskRepository {
    override suspend fun createTask(model: CreateTaskModel): Resource<TaskModel?> {
        return withContext(dispatcher) {
            try {
                val taskId = taskLabelRel.insertTaskWithLabels(
                    task = model.toEntity(),
                    labels = model.labels.map { it.toEntity() }
                ) ?: return@withContext Resource.Error(message = "Failed to create the task")

                val newlyCreatedTask = taskLabelRel.getTaskWithLabels(taskId)
                    ?: return@withContext Resource.Error(message = "Failed to find the task")

                val taskModel = newlyCreatedTask.toModel()
                Resource.Success(taskModel)

            } catch (e: SQLiteConstraintException) {
                e.printStackTrace()
                Resource.Error(message = e.message ?: "Constraint Exception")
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Error(message = e.message ?: "Exception Occurred")
            }
        }
    }

    override suspend fun deleteTask(task: TaskModel): Resource<Boolean> {
        return withContext(dispatcher) {
            try {
                val entity = task.toEntity()
                taskDao.deleteTask(entity)
                Resource.Success(true)
            } catch (e: SQLiteConstraintException) {
                e.printStackTrace()
                Resource.Error(message = e.message ?: "SQLITE EXCEPTION")
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Error(message = e.message ?: "Exception Occurred")
            }
        }
    }

    override suspend fun getAllTasks(): Flow<Resource<List<TaskModel>>> {
        return flow {
            try {
                val savedTasks = taskLabelRel
                    .getAllTasksWithLabels()
                    .map { relations -> Resource.Success(relations.map { it.toModel() }) }
                    .flowOn(dispatcher)
                emitAll(savedTasks)
            } catch (e: SQLiteConstraintException) {
                e.printStackTrace()
                emit(Resource.Error(message = e.message ?: "SQLITE EXCEPTION"))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(message = e.message ?: "Exception Occurred"))
            }
        }
    }

    override suspend fun getTaskById(task: Long): Resource<TaskModel?> {
        return withContext(dispatcher) {
            try {
                val taskEntity = taskLabelRel.getTaskWithLabels(task)
                Resource.Success(taskEntity?.toModel())
            } catch (e: SQLiteConstraintException) {
                e.printStackTrace()
                Resource.Error(message = e.message ?: "SQLITE EXCEPTION")
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Error(message = e.message ?: "Exception Occurred")
            }
        }
    }

    override suspend fun updateTask(task: TaskModel): Resource<TaskModel?> {
        return withContext(dispatcher) {
            try {
                val labels = task.labels.map { it.toEntity() }
                taskLabelRel.updateTaskWithLabels(task = task.toEntity(), labels = labels)

                val updatedTask = taskLabelRel.getTaskWithLabels(task.id.toLong())
                    ?: return@withContext Resource.Error(message = "Cannot find resource ")

                val taskModel = updatedTask.toModel()
                Resource.Success(taskModel)

            } catch (e: SQLiteConstraintException) {
                e.printStackTrace()
                Resource.Error(message = e.message ?: "SQLITE EXCEPTION")
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.Error(message = e.message ?: "Exception Occurred")
            }
        }
    }
}