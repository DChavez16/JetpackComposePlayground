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
internal fun DrawCircleExample(
    drawScopeViewModel: DrawScopeViewModel = hiltViewModel()
) {

    val radiusSliderPosition by drawScopeViewModel.drawCircleRadiusSliderPosition.collectAsState()
    val xCoordSliderPosition by drawScopeViewModel.drawCircleXCoordSliderPosition.collectAsState()
    val yCoordSliderPosition by drawScopeViewModel.drawCircleYCoordSliderPosition.collectAsState()


    DrawCircleExampleContent(
        radiusSliderPosition = { radiusSliderPosition },
        xCoordSliderPosition = { xCoordSliderPosition },
        yCoordSliderPosition = { yCoordSliderPosition },
        changeRadiusSliderPosition = drawScopeViewModel::changeDrawCircleRadiusSliderPosition,
        changeXCoordSliderPosition = drawScopeViewModel::changeDrawCircleXCoordSliderPosition,
        changeYCoordSliderPosition = drawScopeViewModel::changeDrawCircleYCoordSliderPosition
    )
}


@Composable
private fun DrawCircleExampleContent(
    radiusSliderPosition: () -> Float,
    xCoordSliderPosition: () -> Float,
    yCoordSliderPosition: () -> Float,
    changeRadiusSliderPosition: (Float) -> Unit,
    changeXCoordSliderPosition: (Float) -> Unit,
    changeYCoordSliderPosition: (Float) -> Unit

) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        // Slider para modificar el radio del circulo
        CustomSlider(
            sliderTextLabel = stringResource(R.string.draw_scope_draw_circle_radius_slider_label),
            sliderValue = { radiusSliderPosition() },
            sliderValueRange = 0f..0.5f,
            onSliderValueChange = { newPosition -> changeRadiusSliderPosition(newPosition) },
            onSliderValueReset = { changeRadiusSliderPosition(0.25f) }
        )

        // Slider para modificar la coordenada X del circulo
        CustomSlider(
            sliderTextLabel = stringResource(R.string.draw_scope_draw_circle_x_coord_slider_label),
            sliderValue = { xCoordSliderPosition() },
            sliderValueRange = 0f..1f,
            onSliderValueChange = { newPosition -> changeXCoordSliderPosition(newPosition) },
            onSliderValueReset = { changeXCoordSliderPosition(0.5f) }
        )

        // Slider para modificar la coordenada Y del circulo
        CustomSlider(
            sliderTextLabel = stringResource(R.string.draw_scope_draw_circle_y_coord_slider_label),
            sliderValue = { yCoordSliderPosition() },
            sliderValueRange = 0f..1f,
            onSliderValueChange = { newPosition -> changeYCoordSliderPosition(newPosition) },
            onSliderValueReset = { changeYCoordSliderPosition(0.5f) }
        )

        // Circulo que se va a dibujar usando drawArc
        CircleExample(
            radiusSliderPosition = radiusSliderPosition,
            xCoordSliderPosition = xCoordSliderPosition,
            yCoordSliderPosition = yCoordSliderPosition
        )

        // Boton para reiniciar los tres valores al mismo tiempo
        OutlinedButton(
            colors = ButtonDefaults.elevatedButtonColors(),
            elevation = ButtonDefaults.elevatedButtonElevation(4.dp),
            onClick = {
                changeRadiusSliderPosition(0.25f)
                changeXCoordSliderPosition(0.5f)
                changeYCoordSliderPosition(0.5f)
            },
            content = {
                Text(text = stringResource(R.string.draw_scope_draw_circle_restart_button_label))
            }
        )
    }
}


@Composable
private fun CircleExample(
    radiusSliderPosition: () -> Float,
    xCoordSliderPosition: () -> Float,
    yCoordSliderPosition: () -> Float
) {

    val circleRadius by animateFloatAsState(
        targetValue = radiusSliderPosition(),
        label = "CircleRadiusAnimation"
    )

    val circleXCoordinate by animateFloatAsState(
        targetValue = xCoordSliderPosition(),
        label = "CircleXCoordinateAnimation"
    )

    val circleYCoordinate by animateFloatAsState(
        targetValue = yCoordSliderPosition(),
        label = "CircleYCoordinateAnimation"
    )

    val circleColor = MaterialTheme.colorScheme.primary

    Canvas(
        modifier = Modifier.size(150.dp),
        onDraw = {
            drawCircle(
                // El color para rellenar el circulo
                color = circleColor,
                // Opcionalmente se puede asiganr un Brush para el color
                //brush = Brush.linearGradient(listOf(circlePrimaryColor, circleSecondaryColor)),
                // El radio del circulo
                radius = size.maxDimension * circleRadius,
                // El centro de coordenadas dondel el circulo va a ser dibujado
                //center = center,
                center = Offset(
                    x = size.width * circleXCoordinate,
                    y = size.height * circleYCoordinate
                ),
                // Opacidad a ser aplicada al circulo de 0.0f a 1.0f representando de transparente a opaco respectivamente
                alpha = 1f,
                // Define si el circulo esta trazado o rellenado
                style = Fill,
                //style = Stroke(width = 25f, miter = 2f, cap = StrokeCap.Round, join = StrokeJoin.Round, pathEffect = PathEffect.cornerPathEffect(2f)),
                // ColorFilter a aplicar al color cuando se dibuja el circulo
                colorFilter = null,
                // Algoritmo de fusion que se aplicara cuando el circulo se dibuja
                blendMode = DrawScope.DefaultBlendMode
            )
        }
    )
}




@CompactSizeScreenThemePreview
@Composable
private fun DrawCircleExamplePreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        DrawCircleExampleContent(
            radiusSliderPosition = { 0.25f },
            xCoordSliderPosition = { 0.5f },
            yCoordSliderPosition = { 0.5f },
            changeRadiusSliderPosition = {},
            changeXCoordSliderPosition = {},
            changeYCoordSliderPosition = {}
        )
    }
}