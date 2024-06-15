package com.example.configuration

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.example.testing.repositories.FakeLocalThemeRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class ConfigurationScreenUserFlow {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var configurationViewModel: ConfigurationViewModel

    @Before
    fun setup() {
        // Initialize ConfigurationViewModel using FakeLocalThemeRepository
        configurationViewModel = ConfigurationViewModel(
            themeRepository = FakeLocalThemeRepository()
        )
    }



    // Changing dynamic theme value using the switch widget
    @Test
    fun `use switch to change dynamic theme value`() {
        // Start the ConfigurationScreen
        composeTestRule.setContent {
            // Shows the Configuration screen using the ViewModel with fake repository
            ConfigurationScreen(
                onBackButtonClick = {},
                configurationViewModel = configurationViewModel
            )
        }

        // Assert the dynamic theme switch is off by default
        composeTestRule
            .onNodeWithTag("DynamicThemeSwitch")
            .assertIsOff()

        // Perform click on the dynamic theme switch
        composeTestRule
            .onNodeWithTag("DynamicThemeOption")
            .performClick()

        // Assert if the dynamic theme switch is on
        composeTestRule
            .onNodeWithTag("DynamicThemeSwitch")
            .assertIsOn()
    }
}