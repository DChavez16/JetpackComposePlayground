package com.example.testing.repositories

import com.example.data.theme.ThemeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeLocalThemeRepository: ThemeRepository {
    // Fake flows
    private val darkThemeFlow = MutableStateFlow(false)
    private val dynamicThemeFlow = MutableStateFlow(false)

    override fun getDarkThemePreferences(): Flow<Boolean> =
        darkThemeFlow

    override fun getDynamicThemePreferences(): Flow<Boolean> =
        dynamicThemeFlow

    override suspend fun setDarkThemePreferences(darkTheme: Boolean) =
        darkThemeFlow.emit(darkTheme)

    override suspend fun setDynamicThemePreferences(dynamicTheme: Boolean) =
        dynamicThemeFlow.emit(dynamicTheme)
}