package com.example.datastore

import androidx.activity.ComponentActivity
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.click
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.performTouchInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.testing.repositories.FakeLocalPreferencesRepository
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DataPersistenceScreenUserFlow {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var dataPersistenceViewModel: DataPersistenceViewModel

    @Before
    fun setup() {
        // Initialize DataPersistenceViewModel with FakeLocalPreferencesRepository
        dataPersistenceViewModel = DataPersistenceViewModel(
            preferencesRepository = FakeLocalPreferencesRepository()
        )
    }


    @Test
    fun `number change`() {
        // Start DataPersistenceScreen
        composeTestRule.setContent {
            // Shows the DataPersistence screen using the ViewModel with fake repository
            DataPersistenceScreen(
                onMenuButtonClick = {},
                dataPersistenceViewModel = dataPersistenceViewModel
            )
        }

        // Sets into the number text field the number 41
        composeTestRule.onNodeWithTag("NumberTextField").performTextReplacement("41")

        // Asserts the number in the number display text is 41
        composeTestRule.onNodeWithTag("NumberDisplay").assertTextEquals("41")
    }


    @Test
    fun `color change alert dialog shows`() {
        // Start DataPersistenceScreen
        composeTestRule.setContent {
            // Shows the DataPersistence screen using the ViewModel with fake repository
            DataPersistenceScreen(
                onMenuButtonClick = {},
                dataPersistenceViewModel = dataPersistenceViewModel
            )
        }

        // Clicks on the color change button
        composeTestRule.onNodeWithContentDescription("Change background color").performClick()

        // Asserts the alert dialog is displayed
        composeTestRule.onNodeWithTag("ColorPickerDialog").isDisplayed()
    }


    @Test
    fun `color change alert dialog dismiss on button click`() {
        // Start DataPersistenceScreen
        composeTestRule.setContent {
            // Shows the DataPersistence screen using the ViewModel with fake repository
            DataPersistenceScreen(
                onMenuButtonClick = {},
                dataPersistenceViewModel = dataPersistenceViewModel
            )
        }

        // Clicks on the color change button
        composeTestRule.onNodeWithContentDescription("Change background color").performClick()

        // Clicks on the Dismiss button
        composeTestRule.onNodeWithText("Dismiss").performClick()

        // Asserts the alert dialog is not displayed
        composeTestRule.onNodeWithTag("ColorPickerDialog").isNotDisplayed()
    }


    @Test
    fun `color change`() {
        // Start DataPersistenceScreen
        composeTestRule.setContent {
            // Shows the DataPersistence screen using the ViewModel with fake repository
            DataPersistenceScreen(
                onMenuButtonClick = {},
                dataPersistenceViewModel = dataPersistenceViewModel
            )
        }

        // Gets the current color
        val startingColor = dataPersistenceViewModel.colorFlow.value

        // Clicks on the color change button
        composeTestRule.onNodeWithContentDescription("Change background color").performClick()

        // Clicks inside the color picker dialog in the position (0.4f, 0.7f) to select a color
        composeTestRule.onNodeWithTag("ColorPicker").performTouchInput {
            click(
                position = Offset(0.4f, 0.7f)
            )
        }

        // Clicks on the Confirm button
        composeTestRule.onNodeWithText("Confirm").performClick()

        // Asserts the starting color is not equal to the starting color
        assertNotEquals(
            /* unexpected = */ startingColor,
            /* actual = */ dataPersistenceViewModel.colorFlow.value
        )
    }
}