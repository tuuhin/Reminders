package com.eva.reminders.data.preferences

import app.cash.turbine.turbineScope
import com.eva.reminders.domain.facades.PreferencesFacade
import com.eva.reminders.domain.models.ArrangementStyle
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class PreferencesTests {


    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var preferencesFacade: PreferencesFacade

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun gets_the_first_arrangement_style_returned() = runTest {
        val initial = preferencesFacade.arrangementStyle.first()
        assertEquals(
            "The initial style should be grid style",
            ArrangementStyle.GRID_STYLE,
            initial
        )
    }


    @Test
    fun changing_the_style_emits_a_new_value() = runTest {

        val initial = preferencesFacade.arrangementStyle.first()
        assertEquals(
            "The initial style should be grid style",
            ArrangementStyle.GRID_STYLE,
            initial
        )
        // updates the style
        preferencesFacade.updateStyle(ArrangementStyle.BLOCK_STYLE)

        val updated = preferencesFacade.arrangementStyle.first()
        assertEquals(
            "The updated style should be block style",
            ArrangementStyle.BLOCK_STYLE,
            updated
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun check_if_the_flow_is_working_properly() = runTest {
        turbineScope {
            val style = preferencesFacade.arrangementStyle.testIn(this)

            // the update style will trigger the flow
            preferencesFacade.updateStyle(ArrangementStyle.BLOCK_STYLE)
            runCurrent()

            assertEquals(
                "After the update the state is block",
                ArrangementStyle.BLOCK_STYLE,
                style.expectMostRecentItem()
            )

            preferencesFacade.updateStyle(ArrangementStyle.GRID_STYLE)
            advanceUntilIdle()

            assertEquals(
                "Again after the update the style is grid",
                ArrangementStyle.GRID_STYLE,
                style.expectMostRecentItem()
            )

            style.cancelAndIgnoreRemainingEvents()
        }
    }


    @After
    fun tearDown() = runTest {
        (preferencesFacade as? PreferencesFacadeTestImpl)
            ?.clearTestPreferences()
    }

}