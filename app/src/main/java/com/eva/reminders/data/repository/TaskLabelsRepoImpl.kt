package com.eva.reminders.data.repository

import android.database.sqlite.SQLiteConstraintException
import com.eva.reminders.data.local.dao.LabelsDao
import com.eva.reminders.data.local.dao.LabelsFtsDao
import com.eva.reminders.data.local.entity.LabelEntity
import com.eva.reminders.data.mappers.toEntity
import com.eva.reminders.data.mappers.toModel
import com.eva.reminders.data.mappers.toModels
import com.eva.reminders.domain.models.TaskLabelModel
import com.eva.reminders.domain.repository.TaskLabelsRepository
import com.eva.reminders.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskLabelsRepoImpl(
    private val labelDao: LabelsDao,
    private val labelFts: LabelsFtsDao
) : TaskLabelsRepository {
    override suspend fun createLabel(label: String): Resource<TaskLabelModel> {
        return try {
            val entity = LabelEntity(label = label)
            val id = labelDao.insertUpdateLabel(entity)
            val model = labelDao.getLabelFromId(id).toModel()
            Resource.Success(data = model)
        } catch (e: SQLiteConstraintException) {
            Resource.Error(message = e.message ?: "Constraint Exception")
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: "Exception Occurred")
        }
    }

    override suspend fun updateLabel(label: TaskLabelModel): Resource<TaskLabelModel> {
        return try {
            labelDao.insertUpdateLabel(label.toEntity())
            val model = labelDao.getLabelFromId(label.id.toLong()).toModel()
            Resource.Success(data = model)
        } catch (e: SQLiteConstraintException) {
            Resource.Error(message = e.message ?: "Constraint Exception")
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: "Exception Occurred")
        }
    }

    override suspend fun deleteLabel(label: TaskLabelModel): Resource<Boolean> {
        return try {
            labelDao.deleteLabel(label.toEntity())
            Resource.Success(data = true)
        } catch (e: SQLiteConstraintException) {
            Resource.Error(message = e.message ?: "Constraint Exception")
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: "Exception Occurred")
        }
    }

    override suspend fun getLabels(): Flow<List<TaskLabelModel>> {
        return labelDao.getAllLabels().map { it.toModels() }
    }

    override suspend fun searchLabels(query: String): Flow<List<TaskLabelModel>> {
        val ftsResults = when {
            query.isEmpty() -> labelFts.all()
            else -> labelFts.search(query)
        }
        return ftsResults.map { it.toModels() }

    }
}