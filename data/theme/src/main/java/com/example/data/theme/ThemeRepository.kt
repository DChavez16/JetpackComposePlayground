package com.example.data.theme

import com.example.datapersistence.PreferencesDataStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ThemeRepository {
    fun getDarkThemePreferences(): Flow<Boolean>
    fun getDynamicThemePreferences(): Flow<Boolean>
    suspend fun setDarkThemePreferences(darkTheme: Boolean)
    suspend fun setDynamicThemePreferences(dynamicTheme: Boolean)
}


class LocalThemeRepository @Inject constructor(
    private val preferencesDataStore: PreferencesDataStore
): ThemeRepository {

    // Return the dark theme preferences value
    override fun getDarkThemePreferences(): Flow<Boolean> =
        preferencesDataStore.darkThemeFlow

    // Return the dynamic theme preferences value
    override fun getDynamicThemePreferences(): Flow<Boolean> =
        preferencesDataStore.dynamicThemeFlow

    // Store the dark theme preferences in the DataStore
    override suspend fun setDarkThemePreferences(darkTheme: Boolean) =
        preferencesDataStore.setDarkTheme(darkTheme)

    // Store the dynamic theme preferences in the DataStore
    override suspend fun setDynamicThemePreferences(dynamicTheme: Boolean) =
        preferencesDataStore.setDynamicTheme(dynamicTheme)

}