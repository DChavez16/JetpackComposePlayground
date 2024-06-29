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


    // Backing property and StateFlow for end x coord slider position (DrawLine)
    private val _drawLineEndXCoordSliderPosition = MutableStateFlow(0.5f)
    val drawLineEndXCoordSliderPosition: StateFlow<Float> = _drawLineEndXCoordSliderPosition
    
    // Backing property and StateFlow for end y coord slider position (DrawLine)
    private val _drawLineEndYCoordSliderPosition = MutableStateFlow(0.5f)
    val drawLineEndYCoordSliderPosition: StateFlow<Float> = _drawLineEndYCoordSliderPosition


    // Backing property and StateFlow for oval start x coord slider position (DrawOval)
    private val _drawOvalStartXCoordSliderPosition = MutableStateFlow(0f)
    val drawOvalStartXCoordSliderPosition: StateFlow<Float> = _drawOvalStartXCoordSliderPosition

    // Backing property and StateFlow for oval start y coord slider position (DrawOval)
    private val _drawOvalStartYCoordSliderPosition = MutableStateFlow(0.25f)
    val drawOvalStartYCoordSliderPosition: StateFlow<Float> = _drawOvalStartYCoordSliderPosition

    // Backing property and StateFlow for oval size x slider position (DrawOval)
    private val _drawOvalSizeXSliderPosition = MutableStateFlow(1f)
    val drawOvalSizeXSliderPosition: StateFlow<Float> = _drawOvalSizeXSliderPosition

    // Backing property and StateFlow for oval size y slider position (DrawOval)
    private val _drawOvalSizeYSliderPosition = MutableStateFlow(0.5f)
    val drawOvalSizeYSliderPosition: StateFlow<Float> = _drawOvalSizeYSliderPosition



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


    // Methods to change end x coord slider value (DrawLine)
    fun changeDrawLineEndXCoordSliderPosition(newValue: Float) {
        _drawLineEndXCoordSliderPosition.value = newValue
    }

    // Methods to change end y coord slider value (DrawLine)
    fun changeDrawLineEndYCoordSliderPosition(newValue: Float) {
        _drawLineEndYCoordSliderPosition.value = newValue
    }


    // Methods to change oval start x coord slider value (DrawOval)
    fun changeDrawOvalStartXCoordSliderPosition(newValue: Float) {
        _drawOvalStartXCoordSliderPosition.value = newValue
    }

    // Methods to change oval start y coord slider value (DrawOval)
    fun changeDrawOvalStartYCoordSliderPosition(newValue: Float) {
        _drawOvalStartYCoordSliderPosition.value = newValue
    }

    // Methods to change oval size x slider value (DrawOval)
    fun changeDrawOvalSizeXSliderPosition(newValue: Float) {
        _drawOvalSizeXSliderPosition.value = newValue
    }

    // Methods to change oval size y slider value (DrawOval)
    fun changeDrawOvalSizeYSliderPosition(newValue: Float) {
        _drawOvalSizeYSliderPosition.value = newValue
    }
}