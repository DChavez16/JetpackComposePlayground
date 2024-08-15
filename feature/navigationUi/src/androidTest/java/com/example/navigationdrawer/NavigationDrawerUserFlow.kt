package com.example.navigationdrawer

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.example.testing.repositories.FakeLocalThemeRepository
import com.example.util.RootNavigationDestination
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class NavigationDrawerUserFlow {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var navigationDrawerViewModel: NavigationUIViewModel

    @Before
    fun setup() {
        // Initialize NavigationDrawerViewModel with FakeLocalThemeRepository
        navigationDrawerViewModel = NavigationUIViewModel(
            themeRepository = FakeLocalThemeRepository()
        )
    }


    @Test
    fun `theme icon changing depending on current theme`() {
        // Start the NavigationDrawerContent
        composeTestRule.setContent {
            // Shows the NavigationDrawerContent screen using the ViewModel with fake repository
            CustomNavigationDrawer(
                currentSelectedItem = { RootNavigationDestination.LazyLayouts },
                onDrawerItemClick = { },
                onConfigurationButtonClick = { },
                navigationDrawerViewModel = navigationDrawerViewModel
            )
        }

        // Asserts the icon is currently set as light mode
        composeTestRule
            .onNodeWithTag("ChangeThemeIconButton")
            .assertContentDescriptionEquals("Change to dark theme")

        // Clicks on the change theme icon
        composeTestRule
            .onNodeWithTag("ChangeThemeIconButton")
            .performClick()

        // Asserts the icon is currently set as dark mode
        composeTestRule
            .onNodeWithTag("ChangeThemeIconButton")
            .assertContentDescriptionEquals("Change to light theme")
    }
}