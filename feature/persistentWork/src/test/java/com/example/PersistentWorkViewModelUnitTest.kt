package com.example

import com.example.persistentWork.PersistentWorkViewModel
import com.example.testing.repositories.FakeNotificationRepository
import com.example.testing.rules.MainCoroutineRule
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class PersistentWorkViewModelUnitTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeNotificationRepository: FakeNotificationRepository
    private lateinit var persistentWorkViewModel: PersistentWorkViewModel

    @Before
    fun setup() {
        fakeNotificationRepository = FakeNotificationRepository()

        persistentWorkViewModel = PersistentWorkViewModel(
            notificationRepository = fakeNotificationRepository
        )
    }


    @Test
    fun `isRunning value behavior at starting and cancelling the work`() = runTest {
        // Asserts the isRunning initial value of isRunning is false
        assertEquals(
            /* expected = */ false,
            /* actual = */ persistentWorkViewModel.isWorkRunning.value
        )

        // Executes the function to start the notification work
        persistentWorkViewModel.showNotification()
        advanceUntilIdle()

        // Asserts the isRunning value changed to true
        assertEquals(
            /* expected = */ true,
            /* actual = */ persistentWorkViewModel.isWorkRunning.value
        )

        // Executes the function to stop the notification work
        persistentWorkViewModel.cancelNotification()
        advanceUntilIdle()

        // Asserts the isRunning value changed to false
        assertEquals(
            /* expected = */ false,
            /* actual = */ persistentWorkViewModel.isWorkRunning.value
        )
    }


    @Test
    fun `isRunning value behavior at starting the work and letting it finish`() = runTest {

        // Asserts the isRunning initial value of isRunning is false
        assertEquals(
            /* expected = */ false,
            /* actual = */ persistentWorkViewModel.isWorkRunning.value
        )

        // Executes the function to start the notification work
        persistentWorkViewModel.showNotification()
        advanceUntilIdle()

        // Asserts the isRunning value is currently true
        assertEquals(
            /* expected = */ true,
            /* actual = */ persistentWorkViewModel.isWorkRunning.value
        )

        // Simulate work completion from the repository
        fakeNotificationRepository.simulateWorkCompletion()
        advanceUntilIdle()

        // Asserts the isRunning value changed to false
        assertEquals(
            /* expected = */ false,
            /* actual = */ persistentWorkViewModel.isWorkRunning.value
        )
    }
}