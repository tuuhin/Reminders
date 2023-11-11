package com.eva.reminders.data.local

import com.eva.reminders.data.local.dao.TaskDao
import com.eva.reminders.data.local.entity.TaskEntity
import com.eva.reminders.data.mapper.toTestModel
import com.eva.reminders.utils.localDateTimeWithoutMillis
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

@HiltAndroidTest
@OptIn(ExperimentalCoroutinesApi::class)
class TaskDaoTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var taskDao: TaskDao

    @Before
    fun setup() = hiltRule.inject()

    @Test
    fun check_if_the_table_is_empty_initially() = runTest {
        val taskCount = taskDao.tasksCount()
        advanceUntilIdle()

        Assert.assertEquals(
            "Initially there was no content in the table",
            0,
            taskCount
        )
    }

    @Test
    fun check_if_insert_is_working_properly() = runTest {

        val newTaskOne = TaskEntity(title = "Task 1", content = "Some content")
        // Save the new Task
        val savedTaskId = taskDao.insertTask(newTaskOne)
        // Count the number of saved tasks
        val taskCount = taskDao.tasksCount()
        advanceUntilIdle()

        Assert.assertEquals(
            "After saving one  task count should be one",
            1,
            taskCount
        )
        // Fetch the saved entity
        val savedEntity = taskDao.getTaskWithId(savedTaskId)
        runCurrent()

        Assert.assertNotNull(
            "Fetched task is not null with labelId $savedTaskId",
            savedEntity
        )
        // The update time will now allow the check == thus converting updateTime as same for both
        Assert.assertEquals(
            "Is the new task and the saved tasks are same",
            newTaskOne.toTestModel(),
            savedEntity?.toTestModel()
        )
    }


    @Test
    fun check_if_updating_data_of_a_given_task() = runTest {
        val taskEntity = TaskEntity(title = "Test 1", content = "Content for test 1")
        //Creating a new task
        val labelId = taskDao.insertTask(taskEntity)
        // Fetching the newly created task
        val savedTask = taskDao.getTaskWithId(labelId)
        advanceUntilIdle()

        Assert.assertNotNull(
            "Fetched task is not null with labelId $labelId",
            savedTask
        )

        savedTask?.let { entity ->
            // Checking if the task is same or not
            Assert.assertEquals(
                "Check if the title for the task is same",
                savedTask.title,
                "Test 1"
            )

            Assert.assertEquals(
                "Check if the content for the task is same",
                savedTask.content,
                "Content for test 1"
            )

            // Updating the task with title and pinned changed
            taskDao.updateTask(entity.copy(title = "Updated Title", pinned = true))
            // Fetching the updated label
            val updatedTask = taskDao.getTaskWithId(labelId)
            advanceUntilIdle()

            Assert.assertEquals(
                "Checking if the task title is updated or not",
                updatedTask?.title,
                "Updated Title"
            )

            Assert.assertEquals(
                "Checking if the task is-pinned property is set to true",
                updatedTask?.pinned,
                true,
            )
        }
    }

    @Test
    fun adding_multiple_tasks_and_filtering_out_tasks_with_reminders() = runTest {
        val tasksEntity = List(10) { idx ->
            TaskEntity(
                title = "Task$idx",
                content = "Content for $idx",
                time = if (idx % 2 == 0) localDateTimeWithoutMillis() else null
            )
        }
        // Insert multiple tasks
        taskDao.insertMultipleTasks(tasksEntity)
        // Fetch tasks Count
        val taskCount = taskDao.tasksCount()
        advanceUntilIdle()

        Assert.assertEquals(
            "After saving task 10 count should be 10",
            10,
            taskCount
        )
        // fetch tasks with reminders
        val tasksWithReminder = taskDao.getTasksWithReminderTime()
        runCurrent()

        Assert.assertEquals(
            "THere are 5 tasks with reminder",
            5,
            tasksWithReminder.size
        )

        val entitiesTitleHavingReminderTime = tasksEntity.filter { it.time != null }
            .map { it.title }

        val expectedEntitiesTitle = tasksWithReminder.map { it.title }

        Assert.assertEquals(
            "The task title should be same",
            expectedEntitiesTitle,
            entitiesTitleHavingReminderTime,
        )
    }

    @Test
    fun check_if_delete_single_is_working() = runTest {
        val taskEntity = TaskEntity(title = "Test 1", content = "Content for test 1")
        //Creating a new task
        val labelId = taskDao.insertTask(taskEntity)
        // Fetching the newly created task
        val savedTask = taskDao.getTaskWithId(labelId)
        advanceUntilIdle()

        Assert.assertEquals(
            "Check if the entry fetched is the correct one",
            savedTask?.toTestModel(),
            taskEntity.toTestModel(),
        )

        savedTask?.let { entity ->
            // Deleting the tasks
            taskDao.deleteTask(entity)
            // Fetching the task from the id
            val deletedTask = taskDao.getTaskWithId(labelId)
            advanceUntilIdle()

            Assert.assertNull(
                "As the task is already being deleted its null",
                deletedTask
            )
        }

    }

    @Test
    fun check_if_delete_multiple_is_working() = runTest {
        val tasksEntity = List(10) { idx ->
            TaskEntity(
                title = "Task$idx",
                content = "Content for $idx",
            )
        }
        // Inserting tasks
        taskDao.insertMultipleTasks(tasksEntity)
        // Fetching the tasks
        val getInsertTasks = taskDao.getTasks()
        advanceUntilIdle()

        Assert.assertEquals(
            "As 10 tasks are being added we can fetch only 10 tasks",
            10,
            getInsertTasks.size
        )

        val someTasks = getInsertTasks.take(4)
        // Deleting 4 tasks
        taskDao.deleteMultipleTasks(someTasks)
        // Again fetching tasks
        val tasksAfterDelete = taskDao.getTasks()

        Assert.assertEquals(
            "As 4 tasks are deleted then 6 task remain",
            6,
            tasksAfterDelete.size
        )
    }
}