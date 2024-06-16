package com.example.navigationdrawer

import com.example.testing.repositories.FakeLocalThemeRepository
import com.example.testing.rules.MainCoroutineRule
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NavigationUIViewModelUnitTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: NavigationUIViewModel

    @Before
    fun setup() {
        viewModel = NavigationUIViewModel(
            themeRepository = FakeLocalThemeRepository()
        )
    }

    @Test
    fun `update dark theme value`() = runTest {
        // Observe the dark theme flow, the starting value is false
        val darkThemeFlow = viewModel.darkThemeFlow

        // Change dark theme value to true
        viewModel.updateDarkTheme(!darkThemeFlow.value)
        advanceUntilIdle()

        // Assert dark theme value as true
        assertEquals(
            /* expected = */ true,
            /* actual = */ darkThemeFlow.value
        )

        // Change dark theme value to false
        viewModel.updateDarkTheme(!darkThemeFlow.value)
        advanceUntilIdle()

        // Assert dark theme value as false
        assertEquals(
            /* expected = */ false,
            /* actual = */ darkThemeFlow.value
        )
    }
}