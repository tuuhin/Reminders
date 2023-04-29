package com.eva.reminders.data.repository

import android.database.sqlite.SQLiteConstraintException
import com.eva.reminders.data.local.dao.TaskDao
import com.eva.reminders.data.local.dao.TaskLabelRelDao
import com.eva.reminders.data.local.entity.TaskEntity
import com.eva.reminders.data.local.entity.TaskLabelRel
import com.eva.reminders.data.mappers.toEntity
import com.eva.reminders.data.mappers.toModel
import com.eva.reminders.domain.enums.TaskColorEnum
import com.eva.reminders.domain.models.TaskLabelModel
import com.eva.reminders.domain.models.TaskModel
import com.eva.reminders.domain.models.TaskReminderModel
import com.eva.reminders.domain.repository.TaskRepository
import com.eva.reminders.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TaskRepoImpl @Inject constructor(
    private val taskDao: TaskDao,
    private val taskLabelRel: TaskLabelRelDao
) : TaskRepository {

    override suspend fun createTask(
        title: String,
        content: String,
        isPinned: Boolean,
        isArchive: Boolean,
        time: TaskReminderModel?,
        labels: List<TaskLabelModel>,
        colorEnum: TaskColorEnum
    ): Resource<TaskModel?> {
        try {
            val taskEntity =
                TaskEntity(
                    title = title,
                    content = content,
                    pinned = isPinned,
                    isArchived = isArchive,
                    time = time?.at,
                    isRepeating = time?.isRepeating ?: false,
                    color = colorEnum,
                )
            val newEntityId = taskDao.insertTask(taskEntity).toInt()
            taskLabelRel.addTaskLabelsRel(labels.map { TaskLabelRel(taskId = newEntityId, it.id) })
            val fetchNewTask = taskDao.getTaskWithLabels(newEntityId)
            return Resource.Success(fetchNewTask.toModel())
        } catch (e: SQLiteConstraintException) {
            e.printStackTrace()
            return Resource.Error(message = e.message ?: "Constraint Exception")
        } catch (e: Exception) {
            e.printStackTrace()
            return Resource.Error(message = e.message ?: "Exception Occurred")
        }
    }

    override suspend fun deleteTask(task: TaskModel): Resource<Boolean> {
        return try {
            taskDao.deleteTask(task.toEntity())
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

    override suspend fun updateTask(task: TaskModel): Resource<TaskModel?> {
        return try {
            taskLabelRel.deleteTaskLabelRel(
                task.labels.map {
                    TaskLabelRel(
                        taskId = task.id,
                        labelId = it.id
                    )
                }
            )
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