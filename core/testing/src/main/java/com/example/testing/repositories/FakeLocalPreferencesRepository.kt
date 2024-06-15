package com.example.testing.repositories

import com.example.preferences.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeLocalPreferencesRepository: PreferencesRepository {
    // Fake flows
    private val numberPreferencesFlow = MutableStateFlow(0)
    private val colorPreferencesFlow = MutableStateFlow(0)

    override fun getNumberPreferences(): Flow<Int> =
        numberPreferencesFlow

    override fun getColorPreferences(): Flow<Int> =
        colorPreferencesFlow

    override suspend fun setNumberPreferences(number: Int) =
        numberPreferencesFlow.emit(number)

    override suspend fun setColorPreferences(color: Int) =
        colorPreferencesFlow.emit(color)

}