package com.example.drawscope

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@HiltViewModel
internal class DrawScopeViewModel @Inject constructor(): ViewModel() {

    // Backing property and StateFlow for angle slider position (DrawArc)
    private val _drawArcSliderPosition = MutableStateFlow(0f)
    val drawArcSliderPosition: StateFlow<Float> = _drawArcSliderPosition


    // Backing property and StateFlow for radius slider position (DrawCircle)
    private val _drawCircleRadiusSliderPosition = MutableStateFlow(.25f)
    val drawCircleRadiusSliderPosition: StateFlow<Float> = _drawCircleRadiusSliderPosition

    // Backing property and StateFlow for x coord slider position (DrawCircle)
    private val _drawCircleXCoordSliderPosition = MutableStateFlow(.5f)
    val drawCircleXCoordSliderPosition: StateFlow<Float> = _drawCircleXCoordSliderPosition

    // Backing property and StateFlow for y coord slider position (DrawCircle)
    private val _drawCircleYCoordSliderPosition = MutableStateFlow(.5f)
    val drawCircleYCoordSliderPosition: StateFlow<Float> = _drawCircleYCoordSliderPosition



    // Methods to change slider value (DrawArc)
    fun changeDrawArcSliderPosition(newValue: Float) {
        _drawArcSliderPosition.value = newValue
    }


    // Methods to change radius slider value (DrawCircle)
    fun changeDrawCircleRadiusSliderPosition(newValue: Float) {
        _drawCircleRadiusSliderPosition.value = newValue
    }

    // Methods to change x coord slider value (DrawCircle)
    fun changeDrawCircleXCoordSliderPosition(newValue: Float) {
        _drawCircleXCoordSliderPosition.value = newValue
    }

    // Methods to change y coord slider value (DrawCircle)
    fun changeDrawCircleYCoordSliderPosition(newValue: Float) {
        _drawCircleYCoordSliderPosition.value = newValue
    }

}