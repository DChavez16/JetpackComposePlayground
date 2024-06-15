package com.example.datastore

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.preferences.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DataPersistenceViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository
): ViewModel() {

    // Define a Flow for number value and its backing property
    private val _numberFlow = MutableStateFlow(0)
    val numberFlow: StateFlow<Int> = _numberFlow

    // Define a Flow color value and its backing property
    private val _colorFlow = MutableStateFlow(Color.Unspecified)
    val colorFlow: StateFlow<Color> = _colorFlow

    init {
        // Collect Flow for number value from PreferencesRepository
        viewModelScope.launch {
            preferencesRepository.getNumberPreferences()
                .collect { newNumberValue ->
                    _numberFlow.value = newNumberValue
                }
        }

        // Collect Flow for color value from PreferencesRepository
        viewModelScope.launch {
            preferencesRepository.getColorPreferences()
                .collect { newColorValue ->
                    _colorFlow.value = Color(newColorValue)
                }
        }
    }

    // Function to update number value
    fun updateNumberPreference(newValueString: String) {
        // Parse the new value string to an integer in a temporal value
        val numberAsInt = try {
            newValueString.toInt()
        } catch (
            e: NumberFormatException
        ) {
            0
        }

        viewModelScope.launch {
            if(numberAsInt in 0..99) preferencesRepository.setNumberPreferences(numberAsInt)
        }
    }

    // Function to update color value, changes the Color value to ARGB before updating it
    fun updateColorPreference(newColor: Color) {
        // Change Color to Argb value
        val colorArgb = newColor.toArgb()

        // Update color value
        viewModelScope.launch {
            preferencesRepository.setColorPreferences(colorArgb)
        }
    }
}