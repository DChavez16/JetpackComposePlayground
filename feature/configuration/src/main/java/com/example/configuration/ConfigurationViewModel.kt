package com.example.configuration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.theme.ThemeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfigurationViewModel @Inject constructor(
    private val themeRepository: ThemeRepository
): ViewModel() {

    // Define a Flow for dynamicTheme value and its backing property
    private val _dynamicThemeFlow = MutableStateFlow(false)
    val dynamicThemeFlow: StateFlow<Boolean> = _dynamicThemeFlow

    init {
        // Collect Flow for dynamicTheme value from ThemeRepository
        viewModelScope.launch {
            themeRepository.getDynamicThemePreferences()
                .collect { newDynamicThemeValue ->
                    _dynamicThemeFlow.value = newDynamicThemeValue
                }
        }
    }

    // Function to update the dynamicTheme value
    fun updateDynamicTheme(newDynamicThemeValue: Boolean) {
        viewModelScope.launch {
            themeRepository.setDynamicThemePreferences(newDynamicThemeValue)
        }
    }
}