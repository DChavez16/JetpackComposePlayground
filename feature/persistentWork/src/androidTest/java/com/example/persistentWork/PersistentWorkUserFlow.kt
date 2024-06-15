package com.example.persistentWork

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.example.testing.repositories.FakeNotificationRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class PersistentWorkUserFlow {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var fakeNotificationRepository: FakeNotificationRepository
    private lateinit var persistentWorkViewModel: PersistentWorkViewModel

    @Before
    fun setup() {
        fakeNotificationRepository = FakeNotificationRepository()

        persistentWorkViewModel = PersistentWorkViewModel(
            notificationRepository = fakeNotificationRepository
        )

        composeTestRule.setContent {
            PersistentWorkScreen(
                onMenuButtonClick = {},
                persistentWorkViewModel = persistentWorkViewModel
            )
        }
    }


    @Test
    fun `buttons state by default`() {
        // Asserts the 'Show Notification' button is enabled
        composeTestRule.onNodeWithTag("ShowNotificationButton").assertIsEnabled()

        // Asserts the 'Cancel Notification' button is disabled
        composeTestRule.onNodeWithTag("CancelNotificationButton").assertIsNotEnabled()
    }


    @Test
    fun `buttons state at work start and finish`() {
        // Clicks on the 'Show Notification' button
        composeTestRule.onNodeWithTag("ShowNotificationButton").performClick()

        // Asserts the 'Show Notification' button is disabled
        composeTestRule.onNodeWithTag("ShowNotificationButton").assertIsNotEnabled()

        // Asserts the 'Cancel Notification' button is enabled
        composeTestRule.onNodeWithTag("CancelNotificationButton").assertIsEnabled()

        // Simulate work completion from the repository
        fakeNotificationRepository.simulateWorkCompletion()

        // Asserts the 'Show Notification' button is enabled
        composeTestRule.onNodeWithTag("ShowNotificationButton").assertIsEnabled()

        // Asserts the 'Cancel Notification' button is disabled
        composeTestRule.onNodeWithTag("CancelNotificationButton").assertIsNotEnabled()
    }


    @Test
    fun `buttons state at work cancel`() {
        // Clicks on the 'Show Notification' button
        composeTestRule.onNodeWithTag("ShowNotificationButton").performClick()

        // Asserts the 'Show Notification' button is disabled
        composeTestRule.onNodeWithTag("ShowNotificationButton").assertIsNotEnabled()

        // Asserts the 'Cancel Notification' button is enabled
        composeTestRule.onNodeWithTag("CancelNotificationButton").assertIsEnabled()

        // Clicks on the 'Cancel Notification' button
        composeTestRule.onNodeWithTag("CancelNotificationButton").performClick()

        // Asserts the 'Show Notification' button is enabled
        composeTestRule.onNodeWithTag("ShowNotificationButton").assertIsEnabled()

        // Asserts the 'Cancel Notification' button is disabled
        composeTestRule.onNodeWithTag("CancelNotificationButton").assertIsNotEnabled()
    }
}