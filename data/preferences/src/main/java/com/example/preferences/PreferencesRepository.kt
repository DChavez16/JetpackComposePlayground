package com.example.preferences

import com.example.datastore.PreferencesDataStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface PreferencesRepository {
    fun getNumberPreferences(): Flow<Int>
    fun getColorPreferences(): Flow<Int>
    suspend fun setNumberPreferences(number: Int)
    suspend fun setColorPreferences(color: Int)
}


class LocalPreferencesRepository @Inject constructor(
    private val preferencesDataStore: PreferencesDataStore
): PreferencesRepository {

    // Returns a number flow obtained from DataStore
    override fun getNumberPreferences(): Flow<Int> =
        preferencesDataStore.numberFlow

    // Returns a color flow obtained from DataStore
    override fun getColorPreferences(): Flow<Int> =
        preferencesDataStore.colorFlow

    // Store the number in DataStore
    override suspend fun setNumberPreferences(number: Int) =
        preferencesDataStore.setNumber(number)

    // Store the color in DataStore
    override suspend fun setColorPreferences(color: Int) =
        preferencesDataStore.setColor(color)
}