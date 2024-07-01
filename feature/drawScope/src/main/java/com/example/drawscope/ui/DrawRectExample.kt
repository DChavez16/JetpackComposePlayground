package com.example.drawscope.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.drawscope.DrawScopeViewModel
import com.example.drawscope.R
import com.example.ui.theme.PreviewAppTheme
import com.example.ui.ui.CompactSizeScreenThemePreview
import com.example.ui.ui.CustomSlider


@Composable
internal fun DrawRectExample(
    drawScopeViewModel: DrawScopeViewModel = hiltViewModel()
) {

    // SliderPositions for positionX, positionY, sizeX, sizeY
    val xCoordSliderPosition by drawScopeViewModel.drawRectStartXCoordSliderPosition.collectAsState()
    val yCoordSliderPosition by drawScopeViewModel.drawRectStartYCoordSliderPosition.collectAsState()
    val xSizeSliderPosition by drawScopeViewModel.drawRectSizeXSliderPosition.collectAsState()
    val ySizeSliderPosition by drawScopeViewModel.drawRectSizeYSliderPosition.collectAsState()

    DrawRectExampleContent(
        xCoordSliderPosition = { xCoordSliderPosition },
        yCoordSliderPosition = { yCoordSliderPosition },
        xSizeSliderPosition = { xSizeSliderPosition },
        ySizeSliderPosition = { ySizeSliderPosition },
        changeXCoordSliderPosition = drawScopeViewModel::changeDrawRectStartXCoordSliderPosition,
        changeYCoordSliderPosition = drawScopeViewModel::changeDrawRectStartYCoordSliderPosition,
        changeXSizeSliderPosition = drawScopeViewModel::changeDrawRectSizeXSliderPosition,
        changeYSizeSliderPosition = drawScopeViewModel::changeDrawRectSizeYSliderPosition
    )
}


@Composable
private fun DrawRectExampleContent(
    xCoordSliderPosition: () -> Float,
    yCoordSliderPosition: () -> Float,
    xSizeSliderPosition: () -> Float,
    ySizeSliderPosition: () -> Float,
    changeXCoordSliderPosition: (Float) -> Unit,
    changeYCoordSliderPosition: (Float) -> Unit,
    changeXSizeSliderPosition: (Float) -> Unit,
    changeYSizeSliderPosition: (Float) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Slider for changing the start X coordinate of the rectangle
        CustomSlider(
            sliderTextLabel = stringResource(R.string.draw_scope_draw_rect_start_x_position),
            sliderValue = xCoordSliderPosition,
            sliderValueRange = -1f..1f,
            onSliderValueChange = changeXCoordSliderPosition,
            onSliderValueReset = { changeXCoordSliderPosition(0f) }
        )

        // Slider for changing the start Y coordinate of the rectangle
        CustomSlider(
            sliderTextLabel = stringResource(R.string.draw_scope_draw_rect_start_y_position),
            sliderValue = yCoordSliderPosition,
            sliderValueRange = 0f..0.5f,
            onSliderValueChange = changeYCoordSliderPosition,
            onSliderValueReset = { changeYCoordSliderPosition(0.25f) }
        )

        // Slider for changing the size X of the rectangle
        CustomSlider(
            sliderTextLabel = stringResource(R.string.draw_scope_draw_rect_size_x),
            sliderValue = xSizeSliderPosition,
            sliderValueRange = 0f..1f,
            onSliderValueChange = changeXSizeSliderPosition,
            onSliderValueReset = { changeXSizeSliderPosition(1f) }
        )

        // Slider for changing the size Y of the rectangle
        CustomSlider(
            sliderTextLabel = stringResource(R.string.draw_scope_draw_rect_size_y),
            sliderValue = ySizeSliderPosition,
            sliderValueRange = 0f..1f,
            onSliderValueChange = changeYSizeSliderPosition,
            onSliderValueReset = { changeYSizeSliderPosition(0.5f) }
        )

        // Rectangle that will be drawn
        RectExample(
            xCoordSliderPosition = xCoordSliderPosition,
            yCoordSliderPosition = yCoordSliderPosition,
            xSizeSliderPosition = xSizeSliderPosition,
            ySizeSliderPosition = ySizeSliderPosition
        )

        // Button to reset the four values
        OutlinedButton(
            colors = ButtonDefaults.elevatedButtonColors(),
            elevation = ButtonDefaults.elevatedButtonElevation(4.dp),
            onClick = {
                changeXCoordSliderPosition(0f)
                changeYCoordSliderPosition(0.25f)
                changeXSizeSliderPosition(1f)
                changeYSizeSliderPosition(0.5f)
            },
            content = {
                Text(text = stringResource(R.string.draw_scope_draw_rect_restart_button_label))
            }
        )
    }
}


@Composable
private fun RectExample(
    xCoordSliderPosition: () -> Float,
    yCoordSliderPosition: () -> Float,
    xSizeSliderPosition: () -> Float,
    ySizeSliderPosition: () -> Float
) {

    // Animation values for positionX, positionY, sizeX and sizeY
    val rectangleStartXCoord by animateFloatAsState(
        targetValue = xCoordSliderPosition(),
        label = "RectangleStartXCoordinateAnimation"
    )
    val rectangleStartYCoord by animateFloatAsState(
        targetValue = yCoordSliderPosition(),
        label = "RectangleStartYCoordinateAnimation"
    )
    val rectangleSizeX by animateFloatAsState(
        targetValue = xSizeSliderPosition(),
        label = "RectangleSizeXAnimation"
    )
    val rectangleSizeY by animateFloatAsState(
        targetValue = ySizeSliderPosition(),
        label = "RectangleSizeYAnimation"
    )

    val rectPrimaryColor = MaterialTheme.colorScheme.primary

    Canvas(
        modifier = Modifier.size(150.dp),
        onDraw = {
            drawRect(
                // El color para rellenar el rectangulo
                color = rectPrimaryColor,
                // Opcionalmente se puede asiganr un Brush para el color
                //brush = Brush.linearGradient(listOf(primaryColor, secondaryColor)),
                // Coordenadas de el origen local de 0,0 relativo al contenedor actual
                topLeft = Offset(
                    x = size.width * rectangleStartXCoord,
                    y = size.height * rectangleStartYCoord
                ),
                // Dimensiones del rectangulo que se va a dibujar
                size = Size(
                    width = size.width * rectangleSizeX,
                    height = size.height * rectangleSizeY
                ),
                // Opacidad a ser aplicada al rectangulo de 0.0f a 1.0f representando de transparente a opaco respectivamente
                alpha = 1f,
                // Define si el rectangulo esta trazado o rellenado
                style = Fill,
                //style = Stroke(width = 25f, miter = 2f, cap = StrokeCap.Round, join = StrokeJoin.Round, pathEffect = PathEffect.cornerPathEffect(2f)),
                // ColorFilter a aplicar al color cuando se dibuja el rectangulo
                colorFilter = null,
                // Algoritmo de fusion que se aplicara cuando el rectangulo se dibuja
                blendMode = DrawScope.DefaultBlendMode
            )
        }
    )
}


@CompactSizeScreenThemePreview
@Composable
private fun DrawRectExampleContentPreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        DrawRectExampleContent(
            xCoordSliderPosition = { 0f },
            yCoordSliderPosition = { 0.25f },
            xSizeSliderPosition = { 1f },
            ySizeSliderPosition = { 0.5f },
            changeXCoordSliderPosition = {},
            changeYCoordSliderPosition = {},
            changeXSizeSliderPosition = {},
            changeYSizeSliderPosition = {}
        )
    }
}

