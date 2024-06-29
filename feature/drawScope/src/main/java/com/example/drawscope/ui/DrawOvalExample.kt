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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
internal fun DrawOvalExample(
    drawScopeViewModel: DrawScopeViewModel = hiltViewModel()
) {
    // SliderPositions for positionX, positionY, sizeX y sizeY
    val xCoordSliderPosition by drawScopeViewModel.drawOvalStartXCoordSliderPosition.collectAsState()
    val yCoordSliderPosition by drawScopeViewModel.drawOvalStartYCoordSliderPosition.collectAsState()
    val xSizeSliderPosition by drawScopeViewModel.drawOvalSizeXSliderPosition.collectAsState()
    val ySizeSliderPosition by drawScopeViewModel.drawOvalSizeYSliderPosition.collectAsState()

    DrawOvalExampleContent(
        xCoordSliderPosition = { xCoordSliderPosition },
        yCoordSliderPosition = { yCoordSliderPosition },
        xSizeSliderPosition = { xSizeSliderPosition },
        ySizeSliderPosition = { ySizeSliderPosition },
        changeXCoordSliderPosition = drawScopeViewModel::changeDrawOvalStartXCoordSliderPosition,
        changeYCoordSliderPosition = drawScopeViewModel::changeDrawOvalStartYCoordSliderPosition,
        changeXSizeSliderPosition = drawScopeViewModel::changeDrawOvalSizeXSliderPosition,
        changeYSizeSliderPosition = drawScopeViewModel::changeDrawOvalSizeYSliderPosition
    )
}


@Composable
private fun DrawOvalExampleContent(
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
        // Slider to change the start X coordinate of the oval
        CustomSlider(
            sliderTextLabel = stringResource(R.string.draw_scope_draw_oval_start_x_position),
            sliderValue = xCoordSliderPosition,
            sliderValueRange = -1f..1f,
            onSliderValueChange = changeXCoordSliderPosition,
            onSliderValueReset = { changeXCoordSliderPosition(0f) }
        )

        // Slider to change the start Y coordinate of the oval
        CustomSlider(
            sliderTextLabel = stringResource(R.string.draw_scope_draw_oval_start_y_position),
            sliderValue = yCoordSliderPosition,
            sliderValueRange = 0f..0.5f,
            onSliderValueChange = changeYCoordSliderPosition,
            onSliderValueReset = { changeYCoordSliderPosition(0.25f) }
        )

        // Slider to change the size X of the oval
        CustomSlider(
            sliderTextLabel = stringResource(R.string.draw_scope_draw_oval_size_x),
            sliderValue = xSizeSliderPosition,
            sliderValueRange = 0f..1f,
            onSliderValueChange = changeXSizeSliderPosition,
            onSliderValueReset = { changeXSizeSliderPosition(1f) }
        )

        // Slider to change the size Y of the oval
        CustomSlider(
            sliderTextLabel = stringResource(R.string.draw_scope_draw_oval_size_y),
            sliderValue = ySizeSliderPosition,
            sliderValueRange = 0f..1f,
            onSliderValueChange = changeYSizeSliderPosition,
            onSliderValueReset = { changeYSizeSliderPosition(0.5f) }
        )

        // Oval that will be drawn
        OvalExample(
            xCoordSliderPosition = xCoordSliderPosition,
            yCoordSliderPosition = yCoordSliderPosition,
            xSizeSliderPosition = xSizeSliderPosition,
            ySizeSliderPosition = ySizeSliderPosition
        )

        // Button to reset the values of the sliders
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
                Text(text = stringResource(R.string.draw_scope_draw_oval_restart_button_label))
            }
        )
    }
}


@Composable
private fun OvalExample(
    xCoordSliderPosition: () -> Float,
    yCoordSliderPosition: () -> Float,
    xSizeSliderPosition: () -> Float,
    ySizeSliderPosition: () -> Float
) {

    // Animated values for positionX, positionY, sizeX y sizeY
    val ovalStartXCoord by animateFloatAsState(
        targetValue = xCoordSliderPosition(),
        label = "OvalStartXCoordinateAnimation"
    )
    val ovalStartYCoord by animateFloatAsState(
        targetValue = yCoordSliderPosition(),
        label = "OvalStartYCoordinateAnimation"
    )
    val ovalSizeX by animateFloatAsState(
        targetValue = xSizeSliderPosition(),
        label = "OvalSizeXAnimation"
    )
    val ovalSizeY by animateFloatAsState(
        targetValue = ySizeSliderPosition(),
        label = "OvalSizeYAnimation"
    )

    val ovalPrimaryColor = MaterialTheme.colorScheme.primary
    val ovalSecondaryColor = Color.Black

    Canvas(modifier = Modifier.size(150.dp)) {
        drawOval(
            // El color para rellenar el ovalo
            //color = ovalPrimaryColor,
            // Opcionalmente se puede asiganr un Brush para el color
            brush = Brush.linearGradient(listOf(ovalPrimaryColor, ovalSecondaryColor)),
            // Coordenadas de el origen local de 0,0 relativo al contenedor actual
            topLeft = Offset(x = size.width * ovalStartXCoord, y = size.height * ovalStartYCoord),
            // Dimensiones del rectangulo sobre el que el ovalo se va a dibujar
            size = Size(width = size.width * ovalSizeX, height = size.height * ovalSizeY),
            // Opacidad a ser aplicada al ovalo de 0.0f a 1.0f representando de transparente a opaco respectivamente
            alpha = 1f,
            // Define si el ovalo esta trazado o rellenado
            style = Fill,
            //style = Stroke(width = 25f, miter = 2f, cap = StrokeCap.Round, join = StrokeJoin.Round, pathEffect = PathEffect.cornerPathEffect(2f)),
            // ColorFilter a aplicar al color cuando se dibuja el ovalo
            colorFilter = null,
            // Algoritmo de fusion que se aplicara cuando el ovalo se dibuja
            blendMode = DrawScope.DefaultBlendMode
        )
    }
}




@CompactSizeScreenThemePreview
@Composable
private fun DrawOvalExamplePreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        DrawOvalExampleContent(
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