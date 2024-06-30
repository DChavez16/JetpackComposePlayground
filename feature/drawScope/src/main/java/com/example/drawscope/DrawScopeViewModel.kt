package com.example.drawscope

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PointMode
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import kotlin.random.Random


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


    // Backing property and StateFlow for path values list (DrawPath)
    private val _drawPathValuesList = MutableStateFlow(generateRandomGraphValues())
    val drawPathValuesList: StateFlow<List<Int>> = _drawPathValuesList


    // Backing property and StateFlow for points list (DrawPoints)
    private val _drawPointsList = MutableStateFlow(emptyList<Offset>())
    val drawPointsList: StateFlow<List<Offset>> = _drawPointsList

    // Backing property and StateFlow for point mode (DrawPoints)
    private val _drawPointsPointMode = MutableStateFlow(PointModeOption.Points)
    val drawPointsPointMode: StateFlow<PointModeOption> = _drawPointsPointMode



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


    // Methods to update path values list (DrawPath)
    fun updateDrawPathValuesList() {
        _drawPathValuesList.value = generateRandomGraphValues()
    }


    // Methods to add an offset to the points list (DrawPoints)
    fun addOffsetToPointsList(newOffset: Offset) {
        _drawPointsList.value = _drawPointsList.value.addOffset(newOffset)
    }

    // Methods to delete the last offset from the points list (DrawPoints)
    fun deleteLastOffsetFromPointsList() {
        _drawPointsList.value = _drawPointsList.value.deleteLastOffset()
    }

    // Methods to clean the points list (DrawPoints)
    fun cleanPointsList() {
        _drawPointsList.value = emptyList()
    }

    // Methods to change the point mode (DrawPoints)
    fun changePointMode(newPointMode: PointModeOption) {
        _drawPointsPointMode.value = newPointMode
    }
}




private fun generateRandomGraphValues(): List<Int> {
    return List(7) { Random.nextInt(1000) }
}


private fun List<Offset>.addOffset(newOffset: Offset): List<Offset> {
    return this + newOffset
}

private fun List<Offset>.deleteLastOffset(): List<Offset> {
    return dropLast(1)
}

internal enum class PointModeOption(val pointModeName: String, val pointMode: PointMode) {
    Points("Points", PointMode.Points),
    Lines("Lines", PointMode.Lines),
    Polygon("Polygon", PointMode.Polygon)
}