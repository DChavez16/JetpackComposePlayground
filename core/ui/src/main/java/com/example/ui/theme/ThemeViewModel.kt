package com.example.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.theme.ThemeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val themeRepository: ThemeRepository
): ViewModel() {

    // Define a Flow for darkTheme value and its backing property
    private val _darkThemeFlow = MutableStateFlow(false)
    val darkThemeFlow: StateFlow<Boolean> = _darkThemeFlow

    // Define a Flor for dynamicTheme value and its backing property
    private val _dynamicThemeFlow = MutableStateFlow(false)
    val dynamicThemeFlow: StateFlow<Boolean> = _dynamicThemeFlow

    init {
        // Collect Flow for darkTheme value from ThemeRepository
        viewModelScope.launch {
            themeRepository.getDarkThemePreferences()
                .collect { newDarkThemeValue ->
                    _darkThemeFlow.value = newDarkThemeValue
                }
        }

        // Collect Flow for dynamicTheme value from ThemeRepository
        viewModelScope.launch {
            themeRepository.getDynamicThemePreferences()
                .collect { newDynamicThemeValue ->
                    _dynamicThemeFlow.value = newDynamicThemeValue
                }
        }
    }
}