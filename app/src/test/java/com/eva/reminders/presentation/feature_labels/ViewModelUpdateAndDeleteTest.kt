package com.eva.reminders.presentation.feature_labels

import app.cash.turbine.turbineScope
import com.eva.reminders.data.repository.TaskLabelRepoTestImpl
import com.eva.reminders.domain.models.TaskLabelModel
import com.eva.reminders.domain.repository.TaskLabelsRepository
import com.eva.reminders.presentation.feature_labels.utils.CreateLabelEvents
import com.eva.reminders.presentation.feature_labels.utils.EditLabelsActions
import com.eva.reminders.presentation.feature_labels.utils.toEditState
import com.eva.reminders.presentation.utils.UIEvents
import com.eva.reminders.rules.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ViewModelUpdateAndDeleteTest {

    private val _labels = listOf("Test 1", "Other 1", "One 2", "Some 2")

    @get:Rule
    val dispatcherTestRule = MainDispatcherRule()

    private lateinit var _taskRepository: TaskLabelsRepository
    private lateinit var labelsViewModel: LabelsViewModel

    @Before
    fun setup() {
        _taskRepository = TaskLabelRepoTestImpl()
        labelsViewModel = LabelsViewModel(_taskRepository)
    }

    @Test
    fun `check if a label can be deleted`() = runTest {
        turbineScope {
            val labelsEditStates = labelsViewModel.editLabelStates.testIn(this)
            val uiEventsFlow = labelsViewModel.uiEvents.testIn(this)
            // initially reading the expected initial state
            labelsEditStates.expectMostRecentItem()

            _labels.forEach { label ->
                labelsViewModel.onCreateLabelEvent(CreateLabelEvents.OnValueChange(label))
                labelsViewModel.onCreateLabelEvent(CreateLabelEvents.OnSubmit)
            }
            // wait until the labels are created
            advanceUntilIdle()
            // fetch the updated values
            val labelCheck = labelsEditStates.awaitItem()

            val randomLabelToDelete = labelCheck.random()

            labelsViewModel.onLabelAction(EditLabelsActions.OnDelete(randomLabelToDelete))
            advanceUntilIdle()

            val uiEvent = uiEventsFlow.awaitItem()

            assertTrue(message = "Is the Ui event is send on delete") {
                uiEvent is UIEvents.ShowSnackBar
            }

            val shownLabels = labelsEditStates.awaitItem()

            assertEquals(
                expected = shownLabels.size,
                actual = _labels.size - 1,
                message = "The number of labels should reduce by one"
            )

            assertNotEquals(
                illegal = shownLabels,
                actual = labelCheck,
                message = "Both of the list should not match as one contain one lesser than other"
            )

            assertEquals(
                expected = shownLabels,
                actual = buildList {
                    labelCheck.forEach { label -> if (label != randomLabelToDelete) add(label) }
                },
                message = "After delete the other labels should match other than deleted one"
            )

            labelsEditStates.cancelAndIgnoreRemainingEvents()
            uiEventsFlow.cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `check if deleting a label that don't exits is possible or not`() = runTest {
        turbineScope {
            val labelsEditStates = labelsViewModel.editLabelStates.testIn(this)
            val uiEventsFlow = labelsViewModel.uiEvents.testIn(this)
            // initially reading the expected initial state
            labelsEditStates.expectMostRecentItem()

            _labels.forEach { label ->
                labelsViewModel.onCreateLabelEvent(CreateLabelEvents.OnValueChange(label))
                labelsViewModel.onCreateLabelEvent(CreateLabelEvents.OnSubmit)
            }
            // wait until the labels are created
            advanceUntilIdle()
            // fetch the updated values though it's not required
            labelsEditStates.awaitItem()

            val randomLabelToDelete = TaskLabelModel(1, "SSomething").toEditState()

            labelsViewModel.onLabelAction(EditLabelsActions.OnDelete(randomLabelToDelete))
            advanceUntilIdle()

            val uiEvent = uiEventsFlow.awaitItem()

            assertTrue(message = "Is the Ui event that delete is not possible") {
                uiEvent is UIEvents.ShowSnackBar
            }

            // As it's an error no update in the flow
            labelsEditStates.expectNoEvents()

            labelsEditStates.cancelAndIgnoreRemainingEvents()
            uiEventsFlow.cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `check if updating a label with different name`() = runTest {
        turbineScope {
            val labelsEditStates = labelsViewModel.editLabelStates.testIn(this)

            // initially reading the expected initial state
            labelsEditStates.expectMostRecentItem()

            _labels.forEach { label ->
                labelsViewModel.onCreateLabelEvent(CreateLabelEvents.OnValueChange(label))
                labelsViewModel.onCreateLabelEvent(CreateLabelEvents.OnSubmit)
            }
            // wait until the labels are created
            advanceUntilIdle()
            // fetch the updated values
            val labelCheck = labelsEditStates.awaitItem()

            val randomLabelToUpdate = labelCheck.random()
            val updatedLabelText = "Updated New Label"


            labelsViewModel.onLabelAction(
                EditLabelsActions.OnUpdate(randomLabelToUpdate.copy(updatedLabel = updatedLabelText))
            )
            advanceUntilIdle()

            val labelsAfterUpdate = labelsEditStates.awaitItem()

            assertEquals(
                expected = labelsAfterUpdate.size,
                actual = _labels.size,
                message = "The number of labels is should be same"
            )

            assertNotEquals(
                illegal = labelsAfterUpdate,
                actual = labelCheck,
                message = "Both of the list should not match as one contain one lesser than other"
            )

            assertEquals(
                expected = labelsAfterUpdate.map { it.previousLabel }.contains(updatedLabelText),
                actual = true,
                message = "After delete the other labels should match other than deleted one"
            )

            labelsEditStates.cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `check update to a blank value is possible or not`() {
        // An empty label cannot be added
        testValidationForUpdateLabels("")
    }

    @Test
    fun `check update to a existing label name is possible to not`() {
        // A random label to simulate label update with same name that already exits
        testValidationForUpdateLabels(_labels.random())
    }




    private fun testValidationForUpdateLabels(label: String) = runTest {
        turbineScope {

            val labelsEditStates = labelsViewModel.editLabelStates.testIn(this)
            val uiEventsFlow = labelsViewModel.uiEvents.testIn(this)
            // initially reading the expected initial state
            labelsEditStates.expectMostRecentItem()

            _labels.forEach { label ->
                labelsViewModel.onCreateLabelEvent(CreateLabelEvents.OnValueChange(label))
                labelsViewModel.onCreateLabelEvent(CreateLabelEvents.OnSubmit)
            }
            // wait until the labels are created
            advanceUntilIdle()
            // fetch the updated values
            val labelCheck = labelsEditStates.awaitItem()

            val randomLabelToUpdate = labelCheck.random()
            println(randomLabelToUpdate.copy(updatedLabel = label))

            labelsViewModel.onLabelAction(
                EditLabelsActions.OnUpdate(randomLabelToUpdate.copy(updatedLabel = label))
            )
            advanceUntilIdle()

            val event = uiEventsFlow.awaitItem()

            assertTrue(
                message = "Snack bar event showing its not possible to update value to blank string"
            ) {
                event is UIEvents.ShowSnackBar
            }

            // no events are emitted as blank string update is not possible
            labelsEditStates.expectNoEvents()

            // Cancellation of flows
            uiEventsFlow.cancelAndIgnoreRemainingEvents()
            labelsEditStates.cancelAndIgnoreRemainingEvents()
        }
    }
}

