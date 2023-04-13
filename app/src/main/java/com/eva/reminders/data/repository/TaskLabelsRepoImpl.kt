package com.eva.reminders.data.repository

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import com.eva.reminders.data.local.dao.LabelsDao
import com.eva.reminders.data.local.entity.LabelEntity
import com.eva.reminders.data.mappers.toEntity
import com.eva.reminders.data.mappers.toModel
import com.eva.reminders.domain.models.TaskLabelModel
import com.eva.reminders.domain.repository.TaskLabelsRepository
import com.eva.reminders.utils.Resource
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class TaskLabelsRepoImpl @Inject constructor(
    private val labelDao: LabelsDao
) : TaskLabelsRepository {
    override suspend fun createLabel(label: String): Resource<Boolean> {
        return try {
            val entity = LabelEntity(label = label)
            labelDao.insertUpdateLabel(entity)
            Resource.Success(data = true)
        } catch (e: SQLiteConstraintException) {
            Resource.Error(message = e.message ?: "Constraint Exception")
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: "Exception Occurred")
        }
    }

    override suspend fun updateLabel(label: TaskLabelModel): Resource<Boolean> {
        return try {
            labelDao.insertUpdateLabel(label.toEntity())
            Resource.Success(data = true)
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
        return labelDao.getAllLabels().map { entities ->
                entities.map { label -> label.toModel() }
            }
    }
}