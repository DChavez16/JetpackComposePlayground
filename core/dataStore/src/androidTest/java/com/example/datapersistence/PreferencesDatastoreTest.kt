package com.example.datapersistence

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PreferencesDatastoreTest {

    private lateinit var dataStore: PreferencesDataStore

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        dataStore = PreferencesDataStore(context)
    }



    // Updates the number value in preferences datastore and verifies it was updated correctly
    @Test
    fun `change number preferences datastore value`() = runTest {
        // Obtains the number flow from preferences datastore
        val numberFlow = dataStore.numberFlow

        // Triggers the method to update the number value to 14
        dataStore.setNumber(14)
        advanceUntilIdle()

        // Asserts the number value from the preferences datastore flow is 14
        assertEquals(
            /* expected = */ 14,
            /* actual = */ numberFlow.first()
        )

        // Triggers the method to update the number value to 82
        dataStore.setNumber(82)
        advanceUntilIdle()

        // Asserts the number value from the preferences datastore flow is 82
        assertEquals(
            /* expected = */ 82,
            /* actual = */ numberFlow.first()
        )
    }


    // Updates the color valule in preferences datastore and verifies it was updated correctly
    @Test
    fun `change color preferences datastore value`() = runTest {
        // Obtains the color flow from preferences datastore
        val colorFlow = dataStore.colorFlow

        // Triggers the method to update the color value to Blue as ARGB
        dataStore.setColor(Color.Blue.toArgb())
        advanceUntilIdle()

        // Asserts the value from the number flow is 0
        assertEquals(
            /* expected = */ Color.Blue.toArgb(),
            /* actual = */ colorFlow.first()
        )

        // Triggers the method to update the color value to Red as ARGB
        dataStore.setColor(Color.Red.toArgb())
        advanceUntilIdle()

        // Asserts the color value from the preferences datastore flow is red as ARGB
        assertEquals(
            /* expected = */ Color.Red.toArgb(),
            /* actual = */ colorFlow.first()
        )
    }
}