package com.eva.reminders.data.local

import com.eva.reminders.data.local.dao.LabelsDao
import com.eva.reminders.data.local.dao.LabelsFtsDao
import com.eva.reminders.data.local.entity.LabelEntity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
class LabelsFtsDaoTest {

    private val fakeRawLabels =
        listOf("Test 1", "Test 2", "Test 3", "Other 1", "Other 3", "Some 2", "One 2")

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var labelsFtsDao: LabelsFtsDao

    @Inject
    lateinit var labelsDao: LabelsDao

    @Before
    fun setup() = runTest {
        hiltRule.inject()

        val fakeLabelEntities = fakeRawLabels.map { labels -> LabelEntity(label = labels) }

        labelsDao.insertMultipleLabels(fakeLabelEntities)

        advanceUntilIdle()

    }

    @Test
    fun match_for_no_matcher_should_return_everything() {
        checkForMatchedResults(fakeRawLabels)
    }


    @Test
    fun match_with_text_one() {
        val result = listOf("Test 1", "Other 1")
        checkForMatchedResults(result, "1")
    }

    @Test
    fun match_with_text_as_test() {
        val results = listOf("Test 1", "Test 2", "Test 3")
        checkForMatchedResults(results, "Test")
    }

    private fun checkForMatchedResults(expected: List<String>, matcher: String = "") = runTest {
        val matcherFlow = when {
            matcher.isEmpty() -> labelsFtsDao.all()
            else -> labelsFtsDao.search(matcher)
        }
        Assert.assertEquals(
            "These labels should match",
            expected,
            matcherFlow.map { it.label }
        )
    }

}
