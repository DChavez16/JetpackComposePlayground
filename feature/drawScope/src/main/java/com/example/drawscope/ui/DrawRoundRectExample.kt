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
import androidx.compose.ui.geometry.CornerRadius
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
internal fun DrawRoundRectExample(
    drawScopeViewModel: DrawScopeViewModel = hiltViewModel()
) {

    // SliderPositions for sizeX, sizeY, radiusX and radiusY
    val xSizeSliderPosition by drawScopeViewModel.drawRoundRectXSizeSliderPosition.collectAsState()
    val ySizeSliderPosition by drawScopeViewModel.drawRoundRectYSizeSliderPosition.collectAsState()
    val xRadiusSliderPosition by drawScopeViewModel.drawRoundRectXRadiusSliderPosition.collectAsState()
    val yRadiusSliderPosition by drawScopeViewModel.drawRoundRectYRadiusSliderPosition.collectAsState()

    DrawRoundRectExampleContent(
        xSizeSliderPosition = { xSizeSliderPosition },
        ySizeSliderPosition = { ySizeSliderPosition },
        xRadiusSliderPosition = { xRadiusSliderPosition },
        yRadiusSliderPosition = { yRadiusSliderPosition },
        changeXSizeSliderPosition = drawScopeViewModel::changeDrawRoundRectXSizeSliderPosition,
        changeYSizeSliderPosition = drawScopeViewModel::changeDrawRoundRectYSizeSliderPosition,
        changeXRadiusSliderPosition = drawScopeViewModel::changeDrawRoundRectXRadiusSliderPosition,
        changeYRadiusSliderPosition = drawScopeViewModel::changeDrawRoundRectYRadiusSliderPosition
    )
}


@Composable
private fun DrawRoundRectExampleContent(
    xSizeSliderPosition: () -> Float,
    ySizeSliderPosition: () -> Float,
    xRadiusSliderPosition: () -> Float,
    yRadiusSliderPosition: () -> Float,
    changeXSizeSliderPosition: (Float) -> Unit,
    changeYSizeSliderPosition: (Float) -> Unit,
    changeXRadiusSliderPosition: (Float) -> Unit,
    changeYRadiusSliderPosition: (Float) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Slider for changing the size X of the rectangle
        CustomSlider(
            sliderTextLabel = stringResource(R.string.draw_scope_draw_round_rect_size_x),
            sliderValue = xSizeSliderPosition,
            sliderValueRange = 0f..1f,
            onSliderValueChange = changeXSizeSliderPosition,
            onSliderValueReset = { changeXSizeSliderPosition(0.8f) }
        )

        // Slider for changing the size Y of the rectangle
        CustomSlider(
            sliderTextLabel = stringResource(R.string.draw_scope_draw_round_rect_size_y),
            sliderValue = ySizeSliderPosition,
            sliderValueRange = 0f..1f,
            onSliderValueChange = changeYSizeSliderPosition,
            onSliderValueReset = { changeYSizeSliderPosition(0.5f) }
        )

        // Slider for changing the corner radius X of the rectangle
        CustomSlider(
            sliderTextLabel = stringResource(R.string.draw_scope_draw_round_rect_corner_radius_x),
            sliderValue = xRadiusSliderPosition,
            sliderValueRange = 0f..0.5f,
            onSliderValueChange = changeXRadiusSliderPosition,
            onSliderValueReset = { changeXRadiusSliderPosition(0.2f) }
        )

        // Slider for changing the corner radius Y of the rectangle
        CustomSlider(
            sliderTextLabel = stringResource(R.string.draw_scope_draw_round_rect_corner_radius_y),
            sliderValue = yRadiusSliderPosition,
            sliderValueRange = 0f..0.5f,
            onSliderValueChange = changeYRadiusSliderPosition,
            onSliderValueReset = { changeYRadiusSliderPosition(0.2f) }
        )

        // Rectangle that will be drawn
        RoundRectExample(
            xSizeSliderPosition = xSizeSliderPosition,
            ySizeSliderPosition = ySizeSliderPosition,
            xRadiusSliderPosition = xRadiusSliderPosition,
            yRadiusSliderPosition = yRadiusSliderPosition
        )

        // Button to reset the four values
        OutlinedButton(
            colors = ButtonDefaults.elevatedButtonColors(),
            elevation = ButtonDefaults.elevatedButtonElevation(4.dp),
            onClick = {
                changeXSizeSliderPosition(0.8f)
                changeYSizeSliderPosition(0.5f)
                changeXRadiusSliderPosition(0.2f)
                changeYRadiusSliderPosition(0.2f)
            },
            content = {
                Text(text = stringResource(R.string.draw_scope_draw_round_rect_restart_button_label))
            }
        )
    }
}


@Composable
private fun RoundRectExample(
    xSizeSliderPosition: () -> Float,
    ySizeSliderPosition: () -> Float,
    xRadiusSliderPosition: () -> Float,
    yRadiusSliderPosition: () -> Float
) {

    // Animated values for sizeX, sizeY, radiusX and radiusY
    val roundRectangleSizeX by animateFloatAsState(
        targetValue = xSizeSliderPosition(),
        label = "RoundRectangleSizeXAnimation"
    )
    val roundRectangleSizeY by animateFloatAsState(
        targetValue = ySizeSliderPosition(),
        label = "RoundRectangleSizeYAnimation"
    )
    val roundRectangleCornerRadiusX by animateFloatAsState(
        targetValue = xRadiusSliderPosition(),
        label = "RoundRectangleStartXCornerRadiusAnimation"
    )
    val roundRectangleCornerRadiusY by animateFloatAsState(
        targetValue = yRadiusSliderPosition(),
        label = "RoundRectangleStartYCornerRadiusAnimation"
    )

    val rectPrimaryColor = MaterialTheme.colorScheme.primary

    Canvas(modifier = Modifier.size(150.dp)) {
        drawRoundRect(
            // El color para rellenar el rectangulo redondeado
            color = rectPrimaryColor,
            // Opcionalmente se puede asiganr un Brush para el color
            //brush = Brush.linearGradient(listOf(primaryColor, secondaryColor)),
            // Coordenadas de el origen local de 0,0 relativo al contenedor actual
            topLeft = Offset(
                x = size.width / 2 * (1 - roundRectangleSizeX),
                y = size.height / 2 * (1 - roundRectangleSizeY)
            ),
            // Dimensiones del rectangulo redondeado que se va a dibujar
            size = Size(width = size.width * roundRectangleSizeX, height = size.height * roundRectangleSizeY),
            // Radio de las esquinas del rectangulo redondeado, valores negativos son convertidos a 0
            cornerRadius = CornerRadius(
                x = size.width * (roundRectangleSizeX * roundRectangleCornerRadiusX),
                y = size.height * (roundRectangleSizeY * roundRectangleCornerRadiusY)
            ),
            // Opacidad a ser aplicada al rectangulo redondeado de 0.0f a 1.0f representando de transparente a opaco respectivamente
            alpha = 1f,
            // Define si el rectangulo redondeado esta trazado o rellenado
            style = Fill,
            //style = Stroke(width = 25f, miter = 2f, cap = StrokeCap.Round, join = StrokeJoin.Round, pathEffect = PathEffect.cornerPathEffect(2f)),
            // ColorFilter a aplicar al color cuando se dibuja el rectangulo redondeado
            colorFilter = null,
            // Algoritmo de fusion que se aplicara cuando el rectangulo redondeado se dibuja
            blendMode = DrawScope.DefaultBlendMode
        )
    }
}




@CompactSizeScreenThemePreview
@Composable
private fun DrawRoundRectExampleContentPreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        DrawRoundRectExampleContent(
            xSizeSliderPosition = { 0.8f },
            ySizeSliderPosition = { 0.5f },
            xRadiusSliderPosition = { 0.2f },
            yRadiusSliderPosition = { 0.2f },
            changeXSizeSliderPosition = {},
            changeYSizeSliderPosition = {},
            changeXRadiusSliderPosition = {},
            changeYRadiusSliderPosition = {}
        )
    }
}