package com.example.drawscope

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@HiltViewModel
internal class DrawScopeViewModel @Inject constructor(): ViewModel() {

    // Backing property and StateFlow for slider position (DrawArc)
    private val _drawArcSliderPosition = MutableStateFlow(0f)
    val drawArcSliderPosition: StateFlow<Float> = _drawArcSliderPosition



    // Methods to change slider value (DrawArc)
    fun changeDrawArcSliderPosition(newValue: Float) {
        _drawArcSliderPosition.value = newValue
    }
}