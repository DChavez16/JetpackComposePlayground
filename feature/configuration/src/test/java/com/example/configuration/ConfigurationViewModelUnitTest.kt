package com.example.configuration

import com.example.testing.repositories.FakeLocalThemeRepository
import com.example.testing.rules.MainCoroutineRule
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ConfigurationViewModelUnitTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: ConfigurationViewModel

    @Before
    fun setup() {
        viewModel = ConfigurationViewModel(
            themeRepository = FakeLocalThemeRepository()
        )
    }

    @Test
    fun `update dynamic theme value`() = runTest {
        // Observe the dynamic theme option flow, the starting value is false
        val dynamicThemeFlow = viewModel.dynamicThemeFlow

        // Change dynamic theme value to true
        viewModel.updateDynamicTheme(!dynamicThemeFlow.value)
        advanceUntilIdle()

        // Assert dynamic theme value as true
        assertEquals(true, dynamicThemeFlow.value)

        // Change dynamic theme value to false
        viewModel.updateDynamicTheme(!dynamicThemeFlow.value)
        advanceUntilIdle()

        // Assert dynamic theme value as false
        assertEquals(false, dynamicThemeFlow.value)
    }
}