package com.eva.reminders.presentation.feature_labels

import app.cash.turbine.turbineScope
import com.eva.reminders.data.repository.TaskLabelRepoTestImpl
import com.eva.reminders.domain.repository.TaskLabelsRepository
import com.eva.reminders.presentation.feature_labels.utils.CreateLabelEvents
import com.eva.reminders.presentation.feature_labels.utils.LabelSortOrder
import com.eva.reminders.rules.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ViewModelSortLabelsTest {

    private val _labels = listOf("Test 1", "Other 1", "One 2", "Some 2")

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
    fun `check sort order for labels ascending provides correct results`() =
        testSortOrder(LabelSortOrder.ALPHABETICALLY_ASC)

    @Test
    fun `check sort order for labels descending provides correct results`() =
        testSortOrder(LabelSortOrder.ALPHABETICALLY_DESC)

    private fun testSortOrder(order: LabelSortOrder) = runTest {
        turbineScope {
            val labelsEditStates = labelsViewModel.editLabelStates.testIn(this)
            // initially reading the expected initial state
            labelsEditStates.expectMostRecentItem()

            _labels.forEach { label ->
                labelsViewModel.onCreateLabelEvent(CreateLabelEvents.OnValueChange(label))
                labelsViewModel.onCreateLabelEvent(CreateLabelEvents.OnSubmit)
            }

            advanceUntilIdle()

            val labelCheck = labelsEditStates.awaitItem()

            assertEquals(
                expected = labelCheck.size,
                actual = _labels.size,
                message = "The number of labels added and the number of labels fetched should be same"
            )

            //changing the sort order
            labelsViewModel.onSortOderChange(LabelSortOrder.ALPHABETICALLY_ASC)
            // again receiving the updated flow
            val labelsAfterSort = labelsEditStates.awaitItem()
            // sorting label text ascending manner
            val actualSortedLabels = when (order) {
                LabelSortOrder.REGULAR -> _labels
                LabelSortOrder.ALPHABETICALLY_ASC -> _labels.sorted()
                LabelSortOrder.ALPHABETICALLY_DESC -> _labels.sortedDescending()
            }
            // sorting the labels received as others are of no concern
            val expectedLabels = labelsAfterSort.map { it.previousLabel }

            val expectedSortedLabels = when (order) {
                LabelSortOrder.REGULAR -> expectedLabels
                LabelSortOrder.ALPHABETICALLY_ASC -> expectedLabels.sorted()
                LabelSortOrder.ALPHABETICALLY_DESC -> expectedLabels.sortedDescending()
            }
            assertEquals(
                expected = expectedSortedLabels,
                actual = actualSortedLabels,
                message = "Labels should be sorted in a ascending manner"
            )

            // Cancellation of the flow
            labelsEditStates.cancelAndIgnoreRemainingEvents()
        }
    }
}