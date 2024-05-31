package com.eva.reminders.presentation.feature_create

import app.cash.turbine.turbineScope
import com.eva.reminders.data.repository.TaskLabelRepoTestImpl
import com.eva.reminders.domain.repository.TaskLabelsRepository
import com.eva.reminders.rules.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertTrue


@OptIn(ExperimentalCoroutinesApi::class)
class AddLabelsToTaskTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var repository: TaskLabelsRepository

    private lateinit var presenter: AddLabelToTasksPresenter


    @Before
    fun setUp() = runTest {
        repository = TaskLabelRepoTestImpl()

        presenter = AddLabelToTasksPresenter(repository)
        // to initialize the presenter
        presenter.onSearch("")
        runCurrent()
    }

    @Test
    fun `check if the initially loaded list of results is empty or not`() = runTest {
        turbineScope {
            val labelsFlow = presenter.searchedLabels.testIn(this)

            val labels = labelsFlow.expectMostRecentItem()

            assertTrue(message = "No labels should be present") { labels.isEmpty() }

            labelsFlow.cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `check if a label can be added from this presenter`() = runTest {

    }
}