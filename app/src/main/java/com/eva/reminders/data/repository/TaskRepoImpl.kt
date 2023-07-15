package com.eva.reminders.data.repository

import android.database.sqlite.SQLiteConstraintException
import com.eva.reminders.data.local.dao.TaskDao
import com.eva.reminders.data.local.dao.TaskLabelRelDao
import com.eva.reminders.data.local.entity.TaskLabelRel
import com.eva.reminders.data.mappers.toEntity
import com.eva.reminders.data.mappers.toModel
import com.eva.reminders.domain.models.CreateTaskModel
import com.eva.reminders.domain.models.TaskModel
import com.eva.reminders.domain.repository.TaskRepository
import com.eva.reminders.services.AlarmManagerRepo
import com.eva.reminders.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TaskRepoImpl(
    private val taskDao: TaskDao,
    private val taskLabelRel: TaskLabelRelDao,
    private val alarmRepo: AlarmManagerRepo,
) : TaskRepository {
    override suspend fun createTask(model: CreateTaskModel): Resource<TaskModel?> {
        return try {
            val taskEntity = model.toEntity()
            val newEntityId = taskDao.insertTask(taskEntity).toInt()
            val relations = model.labels.map {
                TaskLabelRel(taskId = newEntityId, it.id)
            }
            taskLabelRel.addTaskLabelsRel(relations)
            val fetchNewTask = taskDao.getTaskWithLabels(newEntityId)
            alarmRepo.createAlarm(fetchNewTask.toModel())
            Resource.Success(fetchNewTask.toModel())
        } catch (e: SQLiteConstraintException) {
            e.printStackTrace()
            Resource.Error(message = e.message ?: "Constraint Exception")
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(message = e.message ?: "Exception Occurred")
        }
    }

    override suspend fun deleteTask(task: TaskModel): Resource<Boolean> {
        return try {
            val entity = task.toEntity()
            val labels = task.labels.map { TaskLabelRel(taskId = task.id, it.id) }
            taskLabelRel.deleteTaskLabelRel(labels)
            taskDao.deleteTask(entity)
            alarmRepo.stopAlarm(task)
            Resource.Success(true)
        } catch (e: SQLiteConstraintException) {
            e.printStackTrace()
            Resource.Error(message = e.message ?: "SQLITE EXCEPTION")
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(message = e.message ?: "Exception Occurred")
        }
    }

    override suspend fun getAllTasks(): Flow<Resource<List<TaskModel>>> {
        return flow {
            try {
                taskDao
                    .getAllTasksWithLabels()
                    .collect { relations ->
                        emit(Resource.Success(relations.map { it.toModel() }))
                    }
            } catch (e: SQLiteConstraintException) {
                e.printStackTrace()
                emit(Resource.Error(message = e.message ?: "SQLITE EXCEPTION"))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(message = e.message ?: "Exception Occurred"))
            }
        }
    }

    override suspend fun getTaskById(task: Int): Resource<TaskModel> {
        return try {
            val taskEntity = taskDao.getTaskWithLabels(task)
            Resource.Success(taskEntity.toModel())
        } catch (e: SQLiteConstraintException) {
            e.printStackTrace()
            Resource.Error(message = e.message ?: "SQLITE EXCEPTION")
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(message = e.message ?: "Exception Occurred")
        }
    }

    override suspend fun updateTask(task: TaskModel): Resource<TaskModel?> {
        return try {
            val labels = task.labels.map { TaskLabelRel(taskId = task.id, it.id) }
            taskDao.updateTask(task.toEntity())
            taskLabelRel.deleteLabelsByTaskId(task.id)
            taskLabelRel.addTaskLabelsRel(labels)

            alarmRepo.updateAlarm(task)
            Resource.Success(task)
        } catch (e: SQLiteConstraintException) {
            e.printStackTrace()
            Resource.Error(message = e.message ?: "SQLITE EXCEPTION")
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(message = e.message ?: "Exception Occurred")
        }
    }
}