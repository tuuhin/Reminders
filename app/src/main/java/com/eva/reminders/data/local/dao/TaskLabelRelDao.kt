package com.eva.reminders.data.local.dao

import androidx.annotation.VisibleForTesting
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.eva.reminders.data.local.TableNames
import com.eva.reminders.data.local.entity.LabelEntity
import com.eva.reminders.data.local.entity.TaskEntity
import com.eva.reminders.data.local.entity.TaskLabelRel
import com.eva.reminders.data.local.relations.LabelWithTaskRelation
import com.eva.reminders.data.local.relations.TaskWithLabelRelation
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskLabelRelDao {

    @Transaction
    @Query("SELECT * FROM ${TableNames.TASK_TABLE} ORDER BY TASK_ID DESC")
    fun getAllTasksWithLabels(): Flow<List<TaskWithLabelRelation>>

    @Transaction
    @Query("SELECT * FROM ${TableNames.TASK_TABLE} ORDER BY TASK_ID DESC")
    fun getTaskWithLabelsAsList(): List<TaskWithLabelRelation>

    @Transaction
    @Query("SELECT * FROM ${TableNames.TASK_LABEL_TABLE}")
    fun getAllLabelsWithTasks(): Flow<List<LabelWithTaskRelation>>

    @Query("DELETE FROM ${TableNames.TASK_LABEL_REL} WHERE TASK_ID=:taskId ")
    suspend fun deleteLabelsByTaskId(taskId: Long)

    @Transaction
    @Query("SELECT * FROM ${TableNames.TASK_TABLE} WHERE TASK_ID=:taskId")
    suspend fun getTaskWithLabels(taskId: Long): TaskWithLabelRelation?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNewTask(task: TaskEntity): Long

    @Update
    suspend fun updateOldTask(entity: TaskEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLabels(labels: List<LabelEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTaskLabelsRel(rel: List<TaskLabelRel>)

    /**
     * A database transaction which inserts tasks and labels with there relation
     * [task] if already exists nothing is done.Make sure to turn [createLabelIfNotExits] flag on
     * if you also want to create the tasks. In the scope of the app the labels will be already
     * added in the database before inserting them
     */
    @Transaction
    suspend fun insertTaskWithLabels(
        task: TaskEntity,
        labels: List<LabelEntity>,
        createLabelIfNotExits: Boolean = false
    ): Long? {
        // tasks primary key is not found then inset otherwise update
        val taskId = insertNewTask(task)
        // If the taskId is -1 then the data is ignored
        if (taskId == -1L) return null
        // If the labels are already present they are ignored
        val labelsRelations = if (createLabelIfNotExits) {
            // filtering ensures that ignored labels are not considered
            val toBeCreated = labels.filter { it.id == null }
            val alreadyPresent = labels.filter { it.id != null }
            val newlyAddedLabelIds = insertLabels(toBeCreated).filter { it != -1L }
            // converting them into task-label-relations
            val newlyAddedLabelsRelations = newlyAddedLabelIds.map { id ->
                TaskLabelRel(
                    taskId = taskId.toInt(),
                    labelId = id.toInt()
                )
            }
            val alreadyPresentDataRelation = alreadyPresent.mapNotNull { entity ->
                entity.id?.let { id -> TaskLabelRel(taskId = taskId.toInt(), labelId = id) }
            }
            newlyAddedLabelsRelations + alreadyPresentDataRelation
        } else labels.mapNotNull { entity ->
            entity.id?.let { id -> TaskLabelRel(taskId = taskId.toInt(), labelId = id) }
        }
        // Add task Labels relations
        addTaskLabelsRel(labelsRelations)
        // Return the results task with labels from task id.
        return taskId
    }

    /**
     * A database transaction in which the task entity and label entities are added make sure
     * [task] already exits.[labels] which are to be added if already present then only the relation
     * is added otherwise label is created then relation is added.
     */
    @Transaction
    suspend fun updateTaskWithLabels(
        task: TaskEntity,
        labels: List<LabelEntity>,
    ): Long? {
        val taskId = task.id ?: return null
        // update this task
        updateOldTask(entity = task)
        val toBeCreated = labels.filter { it.id == null }
        val alreadyPresent = labels.filter { it.id != null }
        // filtering ensures that ignored labels are not considered
        val newlyAddedLabelIds = insertLabels(toBeCreated).filter { it != -1L }
        // converting them into task-label-relations
        val newlyCreateLabelTaskRel = newlyAddedLabelIds.map { id ->
            TaskLabelRel(taskId = taskId, labelId = id.toInt())
        }
        val oldLabelsTaskRelations = alreadyPresent.mapNotNull { entity ->
            entity.id?.let { id -> TaskLabelRel(taskId = taskId, labelId = id) }
        }
        val labelsRelations = newlyCreateLabelTaskRel + oldLabelsTaskRelations
        //delete the previously added task relations
        deleteLabelsByTaskId(taskId.toLong())
        // Add task Labels relations
        addTaskLabelsRel(labelsRelations)
        // Return the results task with labels from task id.
        return taskId.toLong()
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    @Query("SELECT * FROM ${TableNames.TASK_LABEL_REL} WHERE TASK_ID=:taskId")
    suspend fun getTaskLabelRelations(taskId: Long): List<TaskLabelRel>
}
