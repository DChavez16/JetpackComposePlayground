@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.mobile

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test


class RootNavigationTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()


    // Navigates to the Animations screen from the lazy layouts screen
    @Test
    fun `navigate the to animations screen`() {
        // Starts in Lazy Layouts screen

        // Open the navigation drawer menu
        composeTestRule.onNodeWithContentDescription("Open Menu").performClick()

        // Click on the Animation option to navigate to the Animations screen
        composeTestRule.onNodeWithContentDescription("Animations").performClick()

        // Verify that the screen top app bar title is Animations
        composeTestRule.onNodeWithTag("TopAppBarTitle").assertTextEquals("Animations")
    }


    // Navigates to the Draw Scopes screen from the lazy layouts screen
    @Test
    fun `navigate to the draw scopes screen`() {
        // Starts in Lazy Layouts screen

        // Open the navigation drawer menu
        composeTestRule.onNodeWithContentDescription("Open Menu").performClick()

        // Click on the Draw Scopes option to navigate to the Draw Scopes screen
        composeTestRule.onNodeWithContentDescription("Draw Scopes").performClick()

        // Verify that the screen top app bar title is Draw Scopes
        composeTestRule.onNodeWithTag("TopAppBarTitle").assertTextEquals("Draw Scopes")
    }


    // Navigates to the Themes screen from the lazy layouts screen
    @Test
    fun `navigate to the themes screen`() {
        // Starts in Lazy Layouts screen

        // Open the navigation drawer menu
        composeTestRule.onNodeWithContentDescription("Open Menu").performClick()

        // Click on the Themes option to navigate to the Themes screen
        composeTestRule.onNodeWithContentDescription("Themes").performClick()

        // Verify that the screen top app bar title is Themes
        composeTestRule.onNodeWithTag("TopAppBarTitle").assertTextEquals("Themes")
    }


    // Navigates to the Dependency Injection screen from the lazy layouts screen
    @Test
    fun `navigate to the dependency injection screen`() {
        // Starts in Lazy Layouts screen

        // Open the navigation drawer menu
        composeTestRule.onNodeWithContentDescription("Open Menu").performClick()

        // Click on the Dependency Injection option to navigate to the Dependency Injection screen
        composeTestRule.onNodeWithContentDescription("Dependency Injection").performClick()

        // Verify that the screen top app bar title is Dependency Injection
        composeTestRule.onNodeWithTag("TopAppBarTitle").assertTextEquals("Dependency Injection")
    }


    // Navigates to the Local Database screen from the lazy layouts screen
    @Test
    fun `navigate to the local database screen`() {
        // Starts in Lazy Layouts screen

        // Open the navigation drawer menu
        composeTestRule.onNodeWithContentDescription("Open Menu").performClick()

        // Click on the Local Database option to navigate to the Local Database screen
        composeTestRule.onNodeWithContentDescription("Local Database").performClick()

        // Verify that the screen top app bar title is Local Database
        composeTestRule.onNodeWithTag("TopAppBarTitle").assertTextEquals("Products")
    }


    // Navigates to the Data Persistence screen from the lazy layouts screen
    @Test
    fun `navigate to the data persistence screen`() {
        // Starts in Lazy Layouts screen

        // Open the navigation drawer menu
        composeTestRule.onNodeWithContentDescription("Open Menu").performClick()

        // Click on the Data Persistence option to navigate to the Data Persistence screen
        composeTestRule.onNodeWithContentDescription("Data Persistence").performClick()

        // Verify that the screen top app bar title is Data Persistence
        composeTestRule.onNodeWithTag("TopAppBarTitle").assertTextEquals("Data Persistence")
    }


    // Navigates to the Persistent Work screen from the lazy layouts screen
    @Test
    fun `navigate to the persistence work screen`() {
        // Starts in Lazy Layouts screen

        // Open the navigation drawer menu
        composeTestRule.onNodeWithContentDescription("Open Menu").performClick()

        // Click on the Persistent Work option to navigate to the Persistent Work screen
        composeTestRule.onNodeWithContentDescription("Persistent Work").performClick()

        // Verify that the screen top app bar title is Persistent Work
        composeTestRule.onNodeWithTag("TopAppBarTitle").assertTextEquals("Persistent Work")
    }


    // Navigates to the Configuration screen from the lazy layouts screen
    @Test
    fun `navigate to the configuration screen and get back to the starting screen`() {
        // Starts in Lazy Layouts screen

        // Open the navigation drawer menu
        composeTestRule.onNodeWithContentDescription("Open Menu").performClick()

        // Click on the configuration icon to navigate to the Configuration screen
        composeTestRule.onNodeWithContentDescription("Configuration").performClick()

        // Verify that the screen top app bar title is Configuration
        composeTestRule.onNodeWithTag("TopAppBarTitle").assertTextEquals("Configuration")

        // Click on the back button to get back to the starting screen
        composeTestRule.onNodeWithContentDescription("Return to the previous screen").performClick()

        // Verify that the screen top app bar title is Lazy Layouts
        composeTestRule.onNodeWithTag("TopAppBarTitle").assertTextEquals("Lazy Layouts")
    }
}