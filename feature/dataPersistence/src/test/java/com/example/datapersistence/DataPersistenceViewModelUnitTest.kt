package com.example.datapersistence

import androidx.compose.ui.graphics.Color
import com.example.testing.repositories.FakeLocalPreferencesRepository
import com.example.testing.rules.MainCoroutineRule
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DataPersistenceViewModelUnitTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: DataPersistenceViewModel

    @Before
    fun setup() {
        viewModel = DataPersistenceViewModel(
            preferencesRepository = FakeLocalPreferencesRepository()
        )
    }

    @Test
    fun `update number value`() = runTest {
        // Observe the number flow, the starting value is 0
        val numberFlow = viewModel.numberFlow

        // Change the number to 57
        viewModel.updateNumberPreference("57")
        advanceUntilIdle()

        // Verify the number in the flow is 57
        assertEquals(57, numberFlow.value)
    }

    @Test
    fun `update color value`() = runTest {
        // Observe the color flow, the starting value is Color.Unspecified
        val colorFlow = viewModel.colorFlow

        // Change the color to Color.Red
        viewModel.updateColorPreference(Color.Red)
        advanceUntilIdle()

        // Verify the color in the flow is Color.Red
        assertEquals(Color.Red, colorFlow.value)
    }
}