package com.eva.reminders.data.repository

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.eva.reminders.data.local.dao.LabelsDao
import com.eva.reminders.data.local.dao.LabelsFtsDao
import com.eva.reminders.data.local.entity.LabelEntity
import com.eva.reminders.data.mappers.toEntity
import com.eva.reminders.data.mappers.toModel
import com.eva.reminders.data.mappers.toModels
import com.eva.reminders.domain.models.TaskLabelModel
import com.eva.reminders.domain.repository.TaskLabelsRepository
import com.eva.reminders.utils.Resource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class TaskLabelsRepoImpl(
    private val labelDao: LabelsDao,
    private val labelFts: LabelsFtsDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : TaskLabelsRepository {
    override suspend fun createLabel(label: String): Resource<TaskLabelModel> {
        return withContext(dispatcher) {
            try {
                val entity = LabelEntity(label = label)
                val id = labelDao.insertUpdateLabel(entity)
                labelDao.getLabelFromId(id)?.toModel()
                    ?.let { model -> Resource.Success(data = model) }
                    ?: Resource.Error(message = "No such labels exists")
            } catch (e: SQLiteConstraintException) {
                Resource.Error(message = e.message ?: "Constraint Exception")
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                Resource.Error(message = e.message ?: "Exception Occurred")
            }
        }
    }

    override suspend fun updateLabel(label: TaskLabelModel): Resource<TaskLabelModel> {
        return withContext(dispatcher) {
            try {
                labelDao.insertUpdateLabel(label.toEntity())
                val labelId = label.id.toLong()
                labelDao.getLabelFromId(labelId)?.toModel()
                    ?.let { model -> Resource.Success(data = model) }
                    ?: Resource.Error(message = "No such labels exists")
            } catch (e: SQLiteConstraintException) {
                Resource.Error(message = e.message ?: "Constraint Exception")
            } catch (e: Exception) {
                if (e is CancellationException) throw e
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
                if (e is CancellationException) throw e
                Resource.Error(message = e.message ?: "Exception Occurred")
            }
        }
    }

    override suspend fun getLabels(): Flow<Resource<List<TaskLabelModel>>> {
        return withContext(dispatcher) {
            flow {
                try {
                    val results = labelDao.getAllLabels()
                        .map { Resource.Success(data = it.toModels()) }
                    emitAll(results)
                } catch (e: SQLiteConstraintException) {
                    emit(Resource.Error(message = e.message ?: "Constraint Exception"))
                } catch (e: Exception) {
                    if (e is CancellationException) throw e
                    emit(Resource.Error(message = e.message ?: "Exception Occurred"))
                }
            }
        }
    }

    override suspend fun searchLabels(query: String): Resource<List<TaskLabelModel>> {
        return withContext(dispatcher) {
            try {
                val results = when {
                    query.isEmpty() -> labelFts.all()
                    else -> labelFts.search(query)
                }
                Resource.Success(data = results.toModels())
            } catch (e: SQLiteConstraintException) {
                Resource.Error(message = e.message ?: "Constraint Exception")
            } catch (e: Exception) {
                if (e is CancellationException) {
                    Log.d("Cancellation", "cancelled")
                    throw e
                }
                Resource.Error(message = e.message ?: "Exception Occurred")
            }
        }
    }
}