package com.example.navigationdrawer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.theme.ThemeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NavigationUIViewModel @Inject constructor(
    private val themeRepository: ThemeRepository
) : ViewModel() {

    // Define a Flow for darkTheme value and its backing property
    private val _darkThemeFlow = MutableStateFlow(false)
    val darkThemeFlow: StateFlow<Boolean> = _darkThemeFlow

    init {
        // Collect Flow for darkTheme value from ThemeRepository
        viewModelScope.launch {
            themeRepository.getDarkThemePreferences()
                .collect { newDarkThemeValue ->
                    _darkThemeFlow.value = newDarkThemeValue
                }
        }
    }

    // Function to update the darkTheme value
    fun updateDarkTheme(newDarkThemeValue: Boolean) {
        viewModelScope.launch {
            themeRepository.setDarkThemePreferences(newDarkThemeValue)
        }
    }
}