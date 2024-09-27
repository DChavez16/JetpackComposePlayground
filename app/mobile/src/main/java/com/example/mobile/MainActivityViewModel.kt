package com.example.mobile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.theme.ThemeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val themeRepository: ThemeRepository
) : ViewModel() {

    // Backing property of the darkTheme to avoid state updates from other classes
    private val _darkTheme = MutableStateFlow(false)
    // The UI collects from this StateFlow to get its state updates
    val darkTheme: StateFlow<Boolean> = _darkTheme

    // Backing property of the dynamicTheme to avoid state updates from other classes
    private val _dynamicTheme = MutableStateFlow(false)
    // The UI collects from this StateFlow to get its state updates
    val dynamicTheme: StateFlow<Boolean> = _dynamicTheme

    init {
        // Collect the darkTheme value from the repository
        getDarkTheme()

        // Collect the dynamicTheme value from the repository
        getDynamicTheme()
    }

    private fun getDarkTheme() {
        // Collect Flow for darkTheme from the ThemeRepository
        viewModelScope.launch {
            themeRepository.getDarkThemePreferences()
                .collect { collectedDarkTheme ->
                    _darkTheme.value = collectedDarkTheme
                }
        }
    }

    private fun getDynamicTheme() {
        // Collect Flow for dynamicTheme from the ThemeRepository
        viewModelScope.launch {
            themeRepository.getDynamicThemePreferences()
                .collect { collectedDynamicTheme ->
                    _dynamicTheme.value = collectedDynamicTheme
                }
        }
    }
}