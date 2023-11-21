package com.eva.reminders.data.local

import android.util.Log
import com.eva.reminders.data.local.dao.LabelsDao
import com.eva.reminders.data.local.dao.TaskDao
import com.eva.reminders.data.local.dao.TaskLabelRelDao
import com.eva.reminders.data.local.entity.LabelEntity
import com.eva.reminders.data.local.entity.TaskEntity
import com.eva.reminders.data.mapper.toTestModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject


@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
class TaskLabelRelTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var taskLabelRel: TaskLabelRelDao

    @Inject
    lateinit var taskDao: TaskDao

    @Inject
    lateinit var labelDao: LabelsDao


    @Before
    fun setup() = hiltRule.inject()


    @Test
    fun check_if_adding_tasks_with_labels_works_labels_are_initially_created() =
        runTest {

            val testLabels = List(4) { idx -> LabelEntity(label = "Test Label $idx") }

            val newlyAddedLabels = taskLabelRel.insertLabels(testLabels)

            val labelsToBeAdded = labelDao.getLabelFromIds(newlyAddedLabels)

            advanceUntilIdle()

            val expectedLabelsText = labelsToBeAdded.map { it.label }
            val actualLabelText = testLabels.map { it.label }

            Assert.assertEquals(
                "After adding labels the fetch labels should be same",
                expectedLabelsText,
                actualLabelText
            )

            val testEntity = TaskEntity(title = "Test 1", content = "Content 1")

            // Adding the task
            val taskId = taskLabelRel.insertTaskWithLabels(
                task = testEntity,
                labels = labelsToBeAdded,
                createLabelIfNotExits = false
            )
            runCurrent()

            Assert.assertNotNull(
                "Ensures the newly added task is Added successfully",
                taskId
            )

            if (taskId == null) return@runTest
            //fetching the new task with labels
            val newlyAddedTaskWithLabels = taskLabelRel.getTaskWithLabels(taskId = taskId)
            // checking the newly created task
            val newlyCreatedTask = taskDao.getTasks()

            advanceUntilIdle()

            Assert.assertEquals(
                "The results should be just one as just a single task is added",
                newlyCreatedTask.map { it.toTestModel() },
                listOf(testEntity.toTestModel())
            )

            Assert.assertEquals(
                "Check if the results contains the same task model",
                newlyAddedTaskWithLabels?.task?.toTestModel(),
                testEntity.toTestModel(taskId = 1)
            )

            val resultsLabelText = newlyAddedTaskWithLabels?.labels?.map { it.label }
            Log.d("RESULTS", taskId.toString())

            Assert.assertEquals(
                "The labels of the results should be same",
                resultsLabelText,
                actualLabelText,
            )
        }

    @Test
    fun check_if_adding_tasks_with_labels_works_labels_are_created_here() = runTest {
        //dummy data
        val testEntity = TaskEntity(title = "Test 1", content = "Content 1")
        val testLabels = List(4) { idx -> LabelEntity(label = "Test Label $idx") }
        // Adding the task
        val taskId = taskLabelRel.insertTaskWithLabels(
            task = testEntity,
            labels = testLabels,
            createLabelIfNotExits = true
        )
        runCurrent()

        Assert.assertNotNull(
            "Ensures the newly added task is Added successfully",
            taskId
        )

        if (taskId == null) return@runTest
        //fetching the new task with labels
        val newlyAddedTask = taskLabelRel.getTaskWithLabels(taskId = taskId)
        // checking the number of labels
        val newlyCreatedLabels = labelDao.getAllLabelsAsList()
        // checking the newly created task
        val newlyCreatedTask = taskDao.getTasks()

        advanceUntilIdle()

        val expectedLabelTexts = newlyCreatedLabels.map { it.label }
        val actualLabelTexts = testLabels.map { it.label }

        Assert.assertEquals(
            "The content for the newly created label should be same",
            expectedLabelTexts,
            actualLabelTexts
        )

        Assert.assertEquals(
            "The results should be just one ",
            newlyCreatedTask.map { it.toTestModel() },
            listOf(testEntity.toTestModel())
        )

        Assert.assertEquals(
            "Check if the results contains the same task model",
            newlyAddedTask?.task?.toTestModel(),
            testEntity.toTestModel(taskId = 1)
        )

        val resultsLabelText = newlyAddedTask?.labels?.map { it.label }
        Log.d("RESULTS", taskId.toString())

        val actualLabels = testLabels.map { it.label }

        Assert.assertEquals(
            "The labels of the results should be same",
            resultsLabelText,
            actualLabels,
        )
    }

    @Test
    fun check_if_updating_a_task_or_label_in_the_relation_works() = runTest {
        val taskEntity = TaskEntity(title = "Test 1", content = "Content 1")
        val labels = List(4) { idx -> LabelEntity(label = "Test Label $idx") }

        val taskId = taskLabelRel.insertTaskWithLabels(
            task = taskEntity,
            labels = labels,
            createLabelIfNotExits = true
        )
        runCurrent()

        Assert.assertNotNull(
            "The insert should work without any problems then taskId should not be null",
            taskId
        )

        if (taskId == null) return@runTest

        // Fetching the newly added task with label from its taskId
        val newlyAddedTask = taskLabelRel.getTaskWithLabels(taskId = taskId)
        // Making sure it's not null
        Assert.assertNotNull(
            "Fetching newly added task should not provide a null value",
            newlyAddedTask
        )

        if (newlyAddedTask == null) return@runTest
        // Fetching the map for label id and taskId
        val taskLabelRelations = taskLabelRel.getTaskLabelRelations(taskId = taskId)
        runCurrent()

        val expectedLabelRelations =
            mapOf(taskId.toInt() to taskLabelRelations.map { it.labelId })
        val actualRelationMap =
            mapOf(taskId.toInt() to newlyAddedTask.labels.map { it.id ?: 0 })
        // Making sure the relations is same as what its suppose to be
        Assert.assertEquals(
            "Is the map for the ids are same or not",
            expectedLabelRelations,
            actualRelationMap
        )
        // Some Update in the task entity
        val updatedEntity = newlyAddedTask.task
            .copy(title = "Updated Title", content = "Updated content")

        val anotherLabel = LabelEntity(label = "Another Label")
        //creating a new label
        val labelId = labelDao.insertUpdateLabel(anotherLabel)
        // fetching the newly created label
        val newlyAddedLabel = labelDao.getLabelFromId(labelId)

        advanceUntilIdle()

        if (newlyAddedLabel == null) return@runTest

        val updatedLabels = labels + newlyAddedLabel

        // After update
        taskLabelRel.updateTaskWithLabels(
            task = updatedEntity,
            labels = updatedLabels,
        )

        // Fetching the task label relationship for taskId
        val relationsAfterUpdate = taskLabelRel.getTaskLabelRelations(taskId = taskId)
        // Fetch tasks with
        val taskWithLabelsAfterUpdate = taskLabelRel.getTaskWithLabels(taskId = taskId)

        advanceUntilIdle()

        val expectedTaskLabelRelationAfterUpdate =
            mapOf(taskId.toInt() to relationsAfterUpdate.map { it.labelId })


        val relationMapAfterUpdate =
            mapOf(taskId.toInt() to taskWithLabelsAfterUpdate?.labels?.mapNotNull { it.id })
        // Making sure the relations is same as what its suppose to be
        Assert.assertEquals(
            "Is the map for the ids are same or not",
            expectedTaskLabelRelationAfterUpdate,
            relationMapAfterUpdate
        )
    }

}

