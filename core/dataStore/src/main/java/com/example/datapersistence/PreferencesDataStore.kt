package com.example.datapersistence

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.preferencesDataStore: DataStore<Preferences> by preferencesDataStore(name = "preferencesExample")


@Singleton
class PreferencesDataStore @Inject constructor(
    @ApplicationContext context: Context
) {
    // Instance of the Preferences DataStore "preferencesExample"
    private val preferencesDataStore = context.preferencesDataStore

    // Object that contains all the keys that represent the values inside preferencesDataStore
    private object PreferencesKeys {
        // Keys for the number and color for the dataPersistence feature
        val NUMBER_KEY = intPreferencesKey("number")
        val COLOR_KEY = intPreferencesKey("color")

        // Keys for darkTheme and dynamicTheme for the app's theme
        val DARK_THEME_KEY = booleanPreferencesKey("darkTheme")
        val DYNAMIC_THEME_KEY = booleanPreferencesKey("dynamicTheme")
    }

    // Gets the value with the key "number" inside preferencesDataStore
    val numberFlow: Flow<Int> = preferencesDataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.NUMBER_KEY] ?: 0
        }

    // Gets the value with the key "color" inside preferencesDataStore
    val colorFlow: Flow<Int> = preferencesDataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.COLOR_KEY] ?: 0
        }

    // Gets the value with the key "darkTheme" inside preferencesDataStore
    val darkThemeFlow: Flow<Boolean> = preferencesDataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.DARK_THEME_KEY] ?: false
        }

    // Gets the value with the key "dynamicTheme" inside preferencesDataStore
    val dynamicThemeFlow: Flow<Boolean> = preferencesDataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.DYNAMIC_THEME_KEY] ?: false
        }

    // Set the value with the key "number" inside preferencesDataStore
    suspend fun setNumber(newNumber: Int) {
        preferencesDataStore.edit { preferences ->
            preferences[PreferencesKeys.NUMBER_KEY] = newNumber
        }
    }

    // Set the value with the key "color" inside preferencesDataStore
    suspend fun setColor(newColorInt: Int) {
        preferencesDataStore.edit { preferences ->
            preferences[PreferencesKeys.COLOR_KEY] = newColorInt
        }
    }

    // Set the value with the key "darkTheme" inside preferencesDataStore
    suspend fun setDarkTheme(newDarkTheme: Boolean) {
        preferencesDataStore.edit { preferences ->
            preferences[PreferencesKeys.DARK_THEME_KEY] = newDarkTheme
        }
    }

    // Set the value with the key "dynamicTheme" inside preferencesDataStore
    suspend fun setDynamicTheme(newDynamicTheme: Boolean) {
        preferencesDataStore.edit { preferences ->
            preferences[PreferencesKeys.DYNAMIC_THEME_KEY] = newDynamicTheme
        }
    }
}