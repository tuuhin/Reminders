package com.eva.reminders.presentation.feature_labels

import app.cash.turbine.turbineScope
import com.eva.reminders.data.repository.TaskLabelRepoTestImpl
import com.eva.reminders.domain.models.TaskLabelModel
import com.eva.reminders.domain.repository.TaskLabelsRepository
import com.eva.reminders.presentation.feature_labels.utils.CreateLabelEvents
import com.eva.reminders.rules.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ViewModelCreateLabelTest {

    @get:Rule
    val mainDispatcherTestRule = MainDispatcherRule()

    private lateinit var _taskRepository: TaskLabelsRepository
    private lateinit var labelsViewModel: LabelsViewModel

    @Before
    fun setup() {
        _taskRepository = TaskLabelRepoTestImpl()
        labelsViewModel = LabelsViewModel(_taskRepository)
    }

    @Test
    fun `try to create one label and check if the loaded list updates`() = runTest {
        turbineScope {
            val allLabelsFlow = labelsViewModel.allLabels.testIn(this)
            // the initial state is already received
            allLabelsFlow.expectMostRecentItem()
            // this will update the flow state for new label state
            labelsViewModel.onCreateLabelEvent(CreateLabelEvents.OnValueChange("New Label"))
            labelsViewModel.onCreateLabelEvent(CreateLabelEvents.OnSubmit)

            // labels should be created lets check
            val labelsAfterAddingNewLabel = allLabelsFlow.awaitItem()

            assertNotEquals(
                illegal = emptyList(),
                actual = labelsAfterAddingNewLabel,
                message = "THe list should not be blank"
            )

            assertEquals(
                expected = listOf(TaskLabelModel(id = 1, label = "New Label")),
                actual = labelsAfterAddingNewLabel,
                message = "Newly added label should be found on the list"
            )
            // Cancellation of flows
            allLabelsFlow.cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `trying to add a blank label it should not be added`() = runTest {
        turbineScope {
            val labelsFlow = labelsViewModel.allLabels.testIn(this)
            val createLabelState = labelsViewModel.newLabelState.testIn(this)
            // trying to add a blank label
            labelsViewModel.onCreateLabelEvent(CreateLabelEvents.OnValueChange(""))
            labelsViewModel.onCreateLabelEvent(CreateLabelEvents.OnSubmit)
            // expected validate will invalidate it
            val labelState = createLabelState.expectMostRecentItem()

            assertEquals(
                expected = true,
                actual = labelState.isError != null,
                message = "As this is an empty text label cannot be created."
            )
            // as the labels didn't suppose change lets get the recent event
            val labelsAfterTryingToInsert = labelsFlow.awaitItem()
            assertEquals(
                expected = emptyList(),
                actual = labelsAfterTryingToInsert,
                message = "THe list should be blank as label creation is not successful"
            )
            //Cancellation of flows
            createLabelState.cancelAndIgnoreRemainingEvents()
            labelsFlow.cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `trying to add label which already exits it should not create such labels`() = runTest {
        turbineScope {
            val labelsFlow = labelsViewModel.allLabels.testIn(this)
            val createLabelState = labelsViewModel.newLabelState.testIn(this)
            // reading the initial state without it await item will provide the initial state
            labelsFlow.expectMostRecentItem()

            // Checking if labels with similar names can be added
            labelsViewModel.onCreateLabelEvent(CreateLabelEvents.OnValueChange("One"))
            labelsViewModel.onCreateLabelEvent(CreateLabelEvents.OnSubmit)

            // provides the newly updated labelsFlow emission
            val labelsAfterInsert = labelsFlow.awaitItem()

            assertEquals(
                expected = listOf(TaskLabelModel(id = 1, label = "One")),
                actual = labelsAfterInsert,
                message = "Newly added label should be found on the list"
            )

            // again try to add the same label with same text
            labelsViewModel.onCreateLabelEvent(CreateLabelEvents.OnValueChange("One"))
            labelsViewModel.onCreateLabelEvent(CreateLabelEvents.OnSubmit)

            //Validator will invalidate it
            val labelState = createLabelState.expectMostRecentItem()
            assertEquals(
                expected = true,
                actual = labelState.isError != null,
                message = "As this is an empty text label cannot be created."
            )
            // As the same label is invalidated there is no events to be expected
            labelsFlow.expectNoEvents()

            //Cancellation of flows
            createLabelState.cancelAndIgnoreRemainingEvents()
            labelsFlow.cancelAndIgnoreRemainingEvents()
        }
    }

}