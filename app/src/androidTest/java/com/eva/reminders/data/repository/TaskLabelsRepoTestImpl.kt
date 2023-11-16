package com.eva.reminders.data.repository

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.eva.reminders.data.local.dao.LabelsDao
import com.eva.reminders.data.local.entity.LabelEntity
import com.eva.reminders.data.mappers.toEntity
import com.eva.reminders.data.mappers.toModel
import com.eva.reminders.data.mappers.toModels
import com.eva.reminders.domain.models.TaskLabelModel
import com.eva.reminders.domain.repository.TaskLabelsRepository
import com.eva.reminders.utils.Resource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TaskLabelsRepoTestImpl @Inject constructor(
    private val labelDao: LabelsDao,
    private val dispatcher: CoroutineDispatcher = StandardTestDispatcher(),
) : TaskLabelsRepository {

    override suspend fun createLabel(label: String): Resource<TaskLabelModel> {
        return withContext(dispatcher) {
            println("KNOCKING  HERE")
            try {
                val entity = LabelEntity(label = label)
                val id = labelDao.insertUpdateLabel(entity)
                val taskLabelModel = labelDao.getLabelFromId(id)
                    ?: return@withContext Resource.Error(message = "No such labels exists")
                Resource.Success(data = taskLabelModel.toModel())
            } catch (e: SQLiteConstraintException) {
                Resource.Error(message = e.message ?: "Constraint Exception")
            } catch (e: Exception) {
                if (e is CancellationException) {
                    Log.i("CANCELLATION", e.localizedMessage ?: "")
                    throw e
                }
                Resource.Error(message = e.message ?: "Exception Occurred")
            }
        }
    }

    override suspend fun updateLabel(label: TaskLabelModel): Resource<TaskLabelModel> {
        return withContext(dispatcher) {
            try {
                val labelId = labelDao.insertUpdateLabel(label.toEntity())
                labelDao.getLabelFromId(labelId)?.toModel()
                    ?.let { model -> Resource.Success(data = model) }
                    ?: Resource.Error(message = "No such labels exists")
            } catch (e: SQLiteConstraintException) {
                Resource.Error(message = e.message ?: "Constraint Exception")
            } catch (e: Exception) {
                Resource.Error(message = e.message ?: "Exception Occurred")
            }
        }
    }

    override suspend fun deleteLabel(label: TaskLabelModel): Resource<Boolean> {
        return withContext(dispatcher) {
            try {
                labelDao.deleteLabel(label.toEntity())
                Resource.Success(data = true)
            } catch (e: SQLiteConstraintException) {
                Resource.Error(message = e.message ?: "Constraint Exception")
            } catch (e: Exception) {
                Resource.Error(message = e.message ?: "Exception Occurred")
            }
        }
    }

    override suspend fun getLabels(): Flow<List<TaskLabelModel>> {
        return labelDao.getAllLabels()
            .map { it.toModels() }
            .flowOn(dispatcher)

    }

    override suspend fun searchLabels(query: String): Flow<List<TaskLabelModel>> = emptyFlow()
}