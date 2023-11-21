package com.eva.reminders.data.local

import app.cash.turbine.test
import app.cash.turbine.turbineScope
import com.eva.reminders.data.local.dao.LabelsDao
import com.eva.reminders.data.local.entity.LabelEntity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
class LabelsDaoTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var labelsDao: LabelsDao

    @Before
    fun setup() = hiltRule.inject()

    @Test
    fun is_the_table_empty() = runTest {
        val labelsCount = labelsDao.totalNumberOfLabels()
        Assert.assertEquals("There are no labels", 0, labelsCount)
    }

    @Test
    fun test_adding_labels_to_the_db() = runTest {
        val labelToSave = labelsDao.insertUpdateLabel(LabelEntity(label = "Test1"))
        runCurrent()
        // as we enter a label the label count should be more than one
        val labelsCount = labelsDao.totalNumberOfLabels()
        Assert.assertEquals(
            "After adding Test1 label the number of labels should be 1",
            1,
            labelsCount
        )
        // Fetching the old label from the id
        val labelFromId = labelsDao.getLabelFromId(labelToSave)
        runCurrent()

        Assert.assertEquals(
            "The previously saved label has text Tas1",
            "Test1",
            labelFromId?.label
        )
    }

    @Test
    fun test_updating_labels_to_the_db() = runTest {
        //Creating a new label Test
        val labelId = labelsDao.insertUpdateLabel(LabelEntity(label = "Test1"))
        runCurrent()
        // Fetching the new.y created label
        val labelSaved = labelsDao.getLabelFromId(labelId)
        runCurrent()

        labelSaved?.let { entity ->
            // Checking if the label is the same one
            Assert.assertEquals(
                "Checking if the label retrieved is the correct one",
                labelSaved.label,
                "Test1"
            )
            // Updating the label
            labelsDao.insertUpdateLabel(entity.copy(label = "Test1_Updated"))
            runCurrent()
            // Fetching the updated label
            val labelSavedUpdated = labelsDao.getLabelFromId(labelId)
            runCurrent()

            Assert.assertEquals(
                "Checking if the label retrieved is the updated one with the updated label text",
                labelSavedUpdated?.label,
                "Test1_Updated"
            )

            Assert.assertEquals(
                "Checking if the label has the same id marking it as modified not newly created",
                labelSavedUpdated?.id?.toLong(),
                labelId,
            )
        }
    }

    @Test
    fun test_flow_for_retrieving_labels() = runTest {
        turbineScope {

            // Adding One Item
            labelsDao.insertUpdateLabel(LabelEntity(label = "Test1"))
            runCurrent()

            labelsDao.getAllLabels().test {
                Assert.assertEquals(
                    "After adding an item the number of labels should be 1",
                    listOf(LabelEntity(1, "Test1")),
                    awaitItem()
                )
                cancelAndIgnoreRemainingEvents()
            }
            // Adding 2nd Item
            labelsDao.insertUpdateLabel(LabelEntity(label = "Test2"))
            runCurrent()

            labelsDao.getAllLabels().test {
                Assert.assertEquals(
                    "After adding another item the number of labels should be 2",
                    listOf(LabelEntity(1, "Test1"), LabelEntity(2, "Test2")),
                    awaitItem()
                )
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun test_deleting_labels_singular() = runTest {

        val labelId = labelsDao.insertUpdateLabel(LabelEntity(label = "Test1"))
        runCurrent()
        // Fetching the new.y created label
        val labelSaved = labelsDao.getLabelFromId(labelId)
        runCurrent()

        labelSaved?.let { entity ->
            Assert.assertEquals(
                "Checking if the label retrieved is the correct one",
                entity.label,
                "Test1"
            )

            // Deletes the label
            labelsDao.deleteLabel(entity)
            runCurrent()

            // fetch the label from the labelId
            val updatedLabel = labelsDao.getLabelFromId(labelId)
            runCurrent()

            // make sure there is no such entity
            Assert.assertEquals(
                "Checking if the label retrieved is the correct one",
                updatedLabel,
                null
            )
        }
    }

    @Test
    fun test_adding_multiple_labels_and_delete_multiple_ones() = runTest {
        // test labels to be added
        val createdLabels = List(10) { idx -> LabelEntity(label = "Test_$idx") }
        // Adding the test labels
        val newlyInsertedLabelIds = labelsDao.insertMultipleLabels(createdLabels)
        runCurrent()
        //Counting the number of test labels
        val labelsCount = labelsDao.totalNumberOfLabels()
        runCurrent()
        // Checking if the number satisfy
        Assert.assertEquals(
            "After inserting 10 labels the number of labels should be 10",
            labelsCount,
            10
        )
        // fetching the entries from 0 to 9 ie, labels with id from 1 to 10
        val fetchedEntities = labelsDao.getLabelFromIds(newlyInsertedLabelIds)
        runCurrent()

        val fetchLabels = fetchedEntities.map { it.label }
        // Checking if they are the same labels
        Assert.assertEquals(
            "Checking are the fetch labels are the labels which were created previously",
            fetchLabels,
            createdLabels.map { it.label }
        )
        // Deleting the fetched Entries
        labelsDao.deleteMultiples(fetchedEntities)
        runCurrent()
        //Again fetching the labels after delete
        val labelsCountAfterDelete = labelsDao.totalNumberOfLabels()
        runCurrent()
        // The result should be zero number of labels
        Assert.assertEquals(
            "After deleting the labels created the new count should be again 0",
            labelsCountAfterDelete,
            0
        )

    }
}