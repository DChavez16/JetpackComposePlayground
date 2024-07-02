package com.example.drawscope

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ClipOp
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


    // Backing property and StateFlow for rect start x coord slider position (DrawRect)
    private val _drawRectStartXCoordSliderPosition = MutableStateFlow(0f)
    val drawRectStartXCoordSliderPosition: StateFlow<Float> = _drawRectStartXCoordSliderPosition

    // Backing property and StateFlow for rect start y coord slider position (DrawRect)
    private val _drawRectStartYCoordSliderPosition = MutableStateFlow(0.25f)
    val drawRectStartYCoordSliderPosition: StateFlow<Float> = _drawRectStartYCoordSliderPosition

    // Backing property and StateFlow for rect size x slider position (DrawRect)
    private val _drawRectSizeXSliderPosition = MutableStateFlow(1f)
    val drawRectSizeXSliderPosition: StateFlow<Float> = _drawRectSizeXSliderPosition

    // Backing property and StateFlow for rect size y slider position (DrawRect)
    private val _drawRectSizeYSliderPosition = MutableStateFlow(0.5f)
    val drawRectSizeYSliderPosition: StateFlow<Float> = _drawRectSizeYSliderPosition


    // Backing property and StateFlow for round rect x size slider position (DrawRoundRect)
    private val _drawRoundRectXSizeSliderPosition = MutableStateFlow(0.8f)
    val drawRoundRectXSizeSliderPosition: StateFlow<Float> = _drawRoundRectXSizeSliderPosition

    // Backing property and StateFlow for round rect y size slider position (DrawRoundRect)
    private val _drawRoundRectYSizeSliderPosition = MutableStateFlow(0.5f)
    val drawRoundRectYSizeSliderPosition: StateFlow<Float> = _drawRoundRectYSizeSliderPosition

    // Backing property and StateFlow for round rect x radius slider position (DrawRoundRect)
    private val _drawRoundRectXRadiusSliderPosition = MutableStateFlow(0.2f)
    val drawRoundRectXRadiusSliderPosition: StateFlow<Float> = _drawRoundRectXRadiusSliderPosition

    // Backing property and StateFlow for round rect y radius slider position (DrawRoundRect)
    private val _drawRoundRectYRadiusSliderPosition = MutableStateFlow(0.2f)
    val drawRoundRectYRadiusSliderPosition: StateFlow<Float> = _drawRoundRectYRadiusSliderPosition


    // Backing property and StateFlow for outline height slider position (DrawOutline)
    private val _drawOutlineHeightSliderPosition = MutableStateFlow(1f)
    val drawOutlineHeightSliderPosition: StateFlow<Float> = _drawOutlineHeightSliderPosition

    // Backing property and StateFlow for outline width slider position (DrawOutline)
    private val _drawOutlineWidthSliderPosition = MutableStateFlow(1f)
    val drawOutlineWidthSliderPosition: StateFlow<Float> = _drawOutlineWidthSliderPosition


    // Backing property and StateFlow for text max width slider position (DrawText)
    private val _drawTextMaxWidthSliderPosition = MutableStateFlow(.75f)
    val drawTextMaxWidthSliderPosition: StateFlow<Float> = _drawTextMaxWidthSliderPosition
    
    // Backing property and StateFlow for text max height slider position (DrawText)
    private val _drawTextMaxHeightSliderPosition = MutableStateFlow(.75f)
    val drawTextMaxHeightSliderPosition: StateFlow<Float> = _drawTextMaxHeightSliderPosition

    // Backing property and StateFlow for text example (DrawText)
    private val _drawTextExampleText = MutableStateFlow(getExampleText())
    val drawTextExampleText: StateFlow<String> = _drawTextExampleText


    // Backing property and StateFlow for image width slider position (DrawImage)
    private val _drawImageWidthSliderPosition = MutableStateFlow(1f)
    val drawImageWidthSliderPosition: StateFlow<Float> = _drawImageWidthSliderPosition

    // Backing property and StateFlow for image height slider position (DrawImage)
    private val _drawImageHeightSliderPosition = MutableStateFlow(1f)
    val drawImageHeightSliderPosition: StateFlow<Float> = _drawImageHeightSliderPosition

    // Backing property and StateFlow for image position x slider position (DrawImage)
    private val _drawImagePositionXSliderPosition = MutableStateFlow(0f)
    val drawImagePositionXSliderPosition: StateFlow<Float> = _drawImagePositionXSliderPosition

    // Backing property and StateFlow for image position y slider position (DrawImage)
    private val _drawImagePositionYSliderPosition = MutableStateFlow(0f)
    val drawImagePositionYSliderPosition: StateFlow<Float> = _drawImagePositionYSliderPosition


    // Backing property and StateFlow for clip position slider position (ClipPath)
    private val _clipPositionSliderPosition = MutableStateFlow(0f)
    val clipPositionSliderPosition: StateFlow<Float> = _clipPositionSliderPosition

    // Backing property and StateFlow for clip selected operation (ClipPath)
    private val _selectedClipPathOperation = MutableStateFlow(ClipOperation.Intersect)
    val selectedClipPathOperation: StateFlow<ClipOperation> = _selectedClipPathOperation


    // Backing property and StateFlow for clip scale slider position (ClipRect)
    private val _clipScaleSliderPosition = MutableStateFlow(1f)
    val clipScaleSliderPosition: StateFlow<Float> = _clipScaleSliderPosition

    // Backing property and StateFlow for clip selected operation (ClipRect)
    private val _selectedClipRectOperation = MutableStateFlow(ClipOperation.Intersect)
    val selectedClipRectOperation: StateFlow<ClipOperation> = _selectedClipRectOperation


    // Backing property and StateFlow for inset vertical state slider position (Inset)
    private val _insetVerticalScaleSliderPosition = MutableStateFlow(0f)
    val insetVerticalScaleSliderPosition: StateFlow<Float> = _insetVerticalScaleSliderPosition

    // Backing property and StateFlow for inset horizontal state slider position (Inset)
    private val _insetHorizontalScaleSliderPosition = MutableStateFlow(0f)
    val insetHorizontalScaleSliderPosition: StateFlow<Float> = _insetHorizontalScaleSliderPosition



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


    // Methods to change rect start x coord slider value (DrawRect)
    fun changeDrawRectStartXCoordSliderPosition(newValue: Float) {
        _drawRectStartXCoordSliderPosition.value = newValue
    }

    // Methods to change rect start y coord slider value (DrawRect)
    fun changeDrawRectStartYCoordSliderPosition(newValue: Float) {
        _drawRectStartYCoordSliderPosition.value = newValue
    }

    // Methods to change rect size x slider value (DrawRect)
    fun changeDrawRectSizeXSliderPosition(newValue: Float) {
        _drawRectSizeXSliderPosition.value = newValue
    }

    // Methods to change rect size y slider value (DrawRect)
    fun changeDrawRectSizeYSliderPosition(newValue: Float) {
        _drawRectSizeYSliderPosition.value = newValue
    }


    // Methods to change round rect x size slider value (DrawRoundRect)
    fun changeDrawRoundRectXSizeSliderPosition(newValue: Float) {
        _drawRoundRectXSizeSliderPosition.value = newValue
    }

    // Methods to change round rect y size slider value (DrawRoundRect)
    fun changeDrawRoundRectYSizeSliderPosition(newValue: Float) {
        _drawRoundRectYSizeSliderPosition.value = newValue
    }

    // Methods to change round rect x radius slider value (DrawRoundRect)
    fun changeDrawRoundRectXRadiusSliderPosition(newValue: Float) {
        _drawRoundRectXRadiusSliderPosition.value = newValue
    }

    // Methods to change round rect y radius slider value (DrawRoundRect)
    fun changeDrawRoundRectYRadiusSliderPosition(newValue: Float) {
        _drawRoundRectYRadiusSliderPosition.value = newValue
    }


    // Methods to change outline height slider value (DrawOutline)
    fun changeOutlineHeightSliderPosition(newValue: Float) {
        _drawOutlineHeightSliderPosition.value = newValue
    }

    // Methods to change outline width slider value (DrawOutline)
    fun changeOutlineWidthSliderPosition(newValue: Float) {
        _drawOutlineWidthSliderPosition.value = newValue
    }


    // Methods to change text max width slider value (DrawText)
    fun changeTextMaxWidthSliderPosition(newValue: Float) {
        _drawTextMaxWidthSliderPosition.value = newValue
    }

    // Methods to change text max height slider value (DrawText)
    fun changeTextMaxHeightSliderPosition(newValue: Float) {
        _drawTextMaxHeightSliderPosition.value = newValue
    }

    // Methods to regenerate text example (DrawText)
    fun regenerateTextExample() {
        _drawTextExampleText.value = getExampleText()
    }


    // Methods to change image width slider value (DrawImage)
    fun changeDrawImageWidthSliderPosition(newValue: Float) {
        _drawImageWidthSliderPosition.value = newValue
    }

    // Methods to change image height slider value (DrawImage)
    fun changeDrawImageHeightSliderPosition(newValue: Float) {
        _drawImageHeightSliderPosition.value = newValue
    }

    // Methods to change image position x slider value (DrawImage)
    fun changeDrawImagePositionXSliderPosition(newValue: Float) {
        _drawImagePositionXSliderPosition.value = newValue
    }

    // Methods to change image position y slider value (DrawImage)
    fun changeDrawImagePositionYSliderPosition(newValue: Float) {
        _drawImagePositionYSliderPosition.value = newValue
    }


    // Methods to change clip position slider value (ClipPath)
    fun changeClipPositionSliderPosition(newValue: Float) {
        _clipPositionSliderPosition.value = newValue
    }

    // Methods to change selected clip path operation (ClipPath)
    fun changeSelectedClipPathOperation(newValue: ClipOperation) {
        _selectedClipPathOperation.value = newValue
    }


    // Methods to change clip scale slider value (ClipRect)
    fun changeClipScaleSliderPosition(newValue: Float) {
        _clipScaleSliderPosition.value = newValue
    }

    // Methods to change selected clip rect operation (ClipRect)
    fun changeSelectedClipRectOperation(newValue: ClipOperation) {
        _selectedClipRectOperation.value = newValue
    }


    // Methods to change inset vertical state slider value (Inset)
    fun changeInsetVerticalScaleSliderPosition(newValue: Float) {
        _insetVerticalScaleSliderPosition.value = newValue
    }

    // Methos to change inset horizontal state slider value (Inset)
    fun changeInsetHorizontalScaleSliderPosition(newValue: Float) {
        _insetHorizontalScaleSliderPosition.value = newValue
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


internal fun getExampleText(): String {
    val numDeCaracteres = Random.nextInt(0, exampleString.length)

    return exampleString.substring(0, numDeCaracteres)
}

private const val exampleString =
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec quis ipsum non sapien vulputate aliquet pharetra elementum tortor. Phasellus consectetur posuere erat eu sollicitudin. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam erat volutpat. In rutrum cursus tincidunt. Ut lectus erat, dignissim sed turpis quis, varius aliquet eros. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus."


internal enum class ClipOperation(val clipOperationName: String, val clipOperation: ClipOp) {
    Intersect("Intersect", ClipOp.Intersect),
    Difference("Difference", ClipOp.Difference);
}