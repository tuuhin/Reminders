package com.eva.reminders.presentation.feature_labels

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.test.espresso.Espresso
import com.eva.reminders.domain.repository.TaskLabelsRepository
import com.eva.reminders.presentation.feature_labels.utils.FeatureLabelsTestTags
import com.eva.reminders.presentation.feature_labels.utils.LabelSortOrder
import com.eva.reminders.presentation.navigation.NavigationTestTags
import com.eva.reminders.ui.theme.RemindersTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class EditLabelsRouteTest {

    @get:Rule(order = 1)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    val composeRule = createComposeRule()

    @Inject
    lateinit var labelsRepository: TaskLabelsRepository

    @Before
    fun setup() {
        hiltRule.inject()

        val labelsViewModel = LabelsViewModel(labelsRepository)

        composeRule.setContent {

            val createState by labelsViewModel.newLabelState.collectAsStateWithLifecycle()
            val editState by labelsViewModel.editLabelStates.collectAsStateWithLifecycle()
            val sortDialogState by labelsViewModel.labelsSortOrder.collectAsStateWithLifecycle()

            RemindersTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.fillMaxSize()
                ) {
                    EditLabelRoute(
                        labelsSortOrder = sortDialogState,
                        createLabelState = createState,
                        editLabelState = editState,
                        onCreateLabelEvent = labelsViewModel::onCreateLabelEvent,
                        onEditLabelEvent = labelsViewModel::onUpdateLabelEvent,
                        onEditActions = labelsViewModel::onLabelAction,
                        onSortOrderChange = labelsViewModel::onSortOderChange,
                    )
                }
            }
        }

        composeRule.onRoot().printToLog("ROUTE_LOGS")
        // ensures this is the edit label route
        composeRule.onNode(hasTestTag(NavigationTestTags.EDIT_LABEL_ROUTE)).assertExists()
    }

    @Test
    fun checking_if_adding_a_single_label_works() = createLabelWithProvidedText("Some label")

    private fun createLabelWithProvidedText(testText: String = "Sample Test") {

        // check if the placeholder is displayed then perform click and then assert if it exits
        composeRule.onNode(hasTestTag(FeatureLabelsTestTags.CREATE_LABEL_PLACEHOLDER))
            .assertExists()
            .performClick()
            .assertDoesNotExist()

        //checking if the field is in focus add some label text
        composeRule.onNode(hasTestTag(FeatureLabelsTestTags.CREATE_NEW_LABEL_TEXT_FIELD))
            .assertIsDisplayed()
            .assertIsFocused()
            .performTextInput(text = testText)

        // perform create click on the button with icon with action create
        composeRule.onNode(hasTestTag(FeatureLabelsTestTags.CREATE_NEW_LABEL_ACTION_CREATE))
            .assertHasClickAction()
            .performClick()

        // check if the label field is still in focus which it's should not
        composeRule.onNode(hasTestTag(FeatureLabelsTestTags.CREATE_LABEL_PLACEHOLDER))
            .assertIsDisplayed()
    }


    @Test
    fun check_sort_dialog_shows_up_correctly() {
        //check if the dialog is don't exist in the tree
        composeRule.onNode(hasTestTag(FeatureLabelsTestTags.SORT_OPTIONS_DIALOG))
            .assertDoesNotExist()
        // Click the sort button in the top-bar
        composeRule.onNode(hasTestTag(FeatureLabelsTestTags.TOGGLE_SORT_OPTIONS_DIALOG))
            .performClick()
        // check if the dialog is visible
        composeRule.onNode(hasTestTag(FeatureLabelsTestTags.SORT_OPTIONS_DIALOG))
            .assertExists()
    }


    @Test
    fun check_if_changing_the_sort_order_changes_the_sequence_of_labels() {
        val labelsToTest = listOf("Other", "One", "Test", "Some")

        val labelsToTestInAscOrder = labelsToTest.sorted()
        val labelsToTestInDescOrder = labelsToTest.sortedDescending()

        // Creates 4 new labels
        labelsToTest.forEach(::createLabelWithProvidedText)
        // check the child at 0 is One
        composeRule.onNode(hasTestTag(FeatureLabelsTestTags.LOADED_LABELS_TEST_TAG))
            .onChildAt(0)
            .onChild()
            .assert(hasText(text = labelsToTest.first()))
        // let's change the sort order to desc order
        // open the sort dialog
        composeRule.onNode(hasTestTag(FeatureLabelsTestTags.TOGGLE_SORT_OPTIONS_DIALOG))
            .performClick()
        // pick the desc order
        composeRule.onNode(hasTestTag(FeatureLabelsTestTags.sortTestTagFromOrder(LabelSortOrder.ALPHABETICALLY_DESC)))
            .performClick()
        // close the dialog
        Espresso.pressBack()
        // check the lazy column again as the labels are sorted not
        // test should be the childAt 0
        composeRule.onNode(hasTestTag(FeatureLabelsTestTags.LOADED_LABELS_TEST_TAG))
            .onChildAt(0)
            .onChild()
            .assert(hasText(text = labelsToTestInDescOrder.first()))
        // changing the order to asc order again
        composeRule.onNode(hasTestTag(FeatureLabelsTestTags.TOGGLE_SORT_OPTIONS_DIALOG))
            .performClick()
        // pick the desc order
        composeRule.onNode(hasTestTag(FeatureLabelsTestTags.sortTestTagFromOrder(LabelSortOrder.ALPHABETICALLY_ASC)))
            .performClick()

        // check the lazy column again as the labels are sorted not
        // One should be the childAt 0
        composeRule.onNode(hasTestTag(FeatureLabelsTestTags.LOADED_LABELS_TEST_TAG))
            .onChildAt(0)
            .onChild()
            .assert(hasText(text = labelsToTestInAscOrder.first()))
    }

    @Test
    fun check_if_loaded_labels_are_scrollable_and_if_no_labels_then_no_label_should_be_shown() {
        //Initially there is no labels show no labels should exist
        composeRule.onNode(hasTestTag(FeatureLabelsTestTags.NO_LABELS_FOUND_TEST_TAG))
            .assertExists()
        // and show labels should not exist
        composeRule.onNode(hasTestTag(FeatureLabelsTestTags.LOADED_LABELS_TEST_TAG))
            .assertDoesNotExist()
        // Add some labels to check both lazy column
        repeat(10) { idx ->
            createLabelWithProvidedText("Label $idx")
        }
        // Now no labels should not exist anymore
        composeRule.onNode(hasTestTag(FeatureLabelsTestTags.NO_LABELS_FOUND_TEST_TAG))
            .assertDoesNotExist()
        //and lazy col should exist checking scroll too
        composeRule.onNode(hasTestTag(FeatureLabelsTestTags.LOADED_LABELS_TEST_TAG))
            .assertExists()
            .performScrollToIndex(9)

        composeRule.onNode(hasTestTag(FeatureLabelsTestTags.LOADED_LABELS_TEST_TAG))
            .onChildAt(9)
    }

    @Test
    fun workflow_of_adding_a_single_label_and_then_deleting_it() {
        // Check there are no labels initially
        composeRule.onNode(hasTestTag(FeatureLabelsTestTags.LOADED_LABELS_TEST_TAG))
            .assertDoesNotExist()
        // create a new label
        createLabelWithProvidedText("New Label")
        // now a label is added
        composeRule.onNode(hasTestTag(FeatureLabelsTestTags.LOADED_LABELS_TEST_TAG))
            .assertExists()

        composeRule.onNode(hasTestTag(FeatureLabelsTestTags.LOADED_LABELS_TEST_TAG))
            .onChildAt(0)
            .onChild()
            .assert(hasText("New Label"))
        // press edit button
        composeRule.onNode(hasTestTag(FeatureLabelsTestTags.EDIT_LABEL_ACTION_TEST_TAG))
            .assertExists()
            .performClick()
        // the placeholder goes to edit mode hiding the edit button
        composeRule.onNode(hasTestTag(FeatureLabelsTestTags.EDIT_LABEL_ACTION_TEST_TAG))
            .assertDoesNotExist()
        //press delete button
        composeRule.onNode(hasTestTag(FeatureLabelsTestTags.DELETE_LABEL_ACTION_TEST_TAG))
            .assertExists()
            .performClick()
        // labels should be deleted and its again lazy column is removed from the
        // tree.Here as we add a single label it works this way, otherwise only this
        // label will be removed
        composeRule.onNode(hasTestTag(FeatureLabelsTestTags.LOADED_LABELS_TEST_TAG))
            .assertDoesNotExist()
    }

    @Test
    fun workflow_of_adding_a_single_label_then_updating_the_value_and_check_if_its_updated() {

        composeRule.onNode(hasTestTag(FeatureLabelsTestTags.LOADED_LABELS_TEST_TAG))
            .assertDoesNotExist()
        // create a new label
        createLabelWithProvidedText("New Label")
        // now a label is added
        composeRule.onNode(hasTestTag(FeatureLabelsTestTags.LOADED_LABELS_TEST_TAG))
            .assertExists()

        composeRule.onNode(hasTestTag(FeatureLabelsTestTags.LOADED_LABELS_TEST_TAG))
            .onChildAt(0)
            .onChild()
            .assert(hasText("New Label"))
        // press edit button
        composeRule.onNode(hasTestTag(FeatureLabelsTestTags.EDIT_LABEL_ACTION_TEST_TAG))
            .assertExists()
            .performClick()
        // the placeholder goes to edit mode hiding the edit button
        composeRule.onNode(hasTestTag(FeatureLabelsTestTags.EDIT_LABEL_ACTION_TEST_TAG))
            .assertDoesNotExist()

        composeRule.onNode(hasTestTag(FeatureLabelsTestTags.UPDATE_LABEL_TEXT_FIELD))
            .assertExists()
            .performTextInput("Updated Label")

        //press done button
        composeRule.onNode(hasTestTag(FeatureLabelsTestTags.UPDATE_LABEL_ACTION_TEST_TAG))
            .assertExists()
            .performClick()
        // runs the coroutine to update the task label


        composeRule.onNode(hasTestTag(FeatureLabelsTestTags.LOADED_LABELS_TEST_TAG))
            .assertExists()
            .onChildAt(0)
            .onChild()
            .assert(hasText("Updated Label"))
    }

}