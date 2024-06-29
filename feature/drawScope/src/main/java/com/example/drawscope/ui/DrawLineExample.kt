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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.drawscope.DrawScopeViewModel
import com.example.drawscope.R
import com.example.ui.theme.PreviewAppTheme
import com.example.ui.ui.CompactSizeScreenThemePreview
import com.example.ui.ui.CustomSlider


@Composable
internal fun DrawLineExample(
    drawScopeViewModel: DrawScopeViewModel = hiltViewModel()
) {

    val xCoordSliderPosition by drawScopeViewModel.drawLineEndXCoordSliderPosition.collectAsState()
    val yCoordSliderPosition by drawScopeViewModel.drawLineEndYCoordSliderPosition.collectAsState()

    DrawLineExampleContent(
        xCoordSliderPosition = { xCoordSliderPosition },
        yCoordSliderPosition = { yCoordSliderPosition },
        changeXCoordSliderPosition = drawScopeViewModel::changeDrawLineEndXCoordSliderPosition,
        changeYCoordSliderPosition = drawScopeViewModel::changeDrawLineEndYCoordSliderPosition
    )
}


@Composable
private fun DrawLineExampleContent(
    xCoordSliderPosition: () -> Float,
    yCoordSliderPosition: () -> Float,
    changeXCoordSliderPosition: (Float) -> Unit,
    changeYCoordSliderPosition: (Float) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Slider para controlar la coordenada X del final de la linea
        CustomSlider(
            sliderTextLabel = stringResource(R.string.draw_scope_draw_line_end_x_coord_slider_label),
            sliderValue = xCoordSliderPosition,
            sliderValueRange = 0f..1f,
            onSliderValueChange = changeXCoordSliderPosition,
            onSliderValueReset = { changeXCoordSliderPosition(0.5f) }
        )

        // Slider para controlar la coordenada Y del final de la linea
        CustomSlider(
            sliderTextLabel = stringResource(R.string.draw_scope_draw_line_end_y_coord_slider_label),
            sliderValue = yCoordSliderPosition,
            sliderValueRange = 0f..1f,
            onSliderValueChange = changeYCoordSliderPosition,
            onSliderValueReset = { changeYCoordSliderPosition(0.5f) }
        )

        // Linea que se va a dibujar usando drawLine
        LineExample(
            xCoordSliderPosition = xCoordSliderPosition,
            yCoordSliderPosition = yCoordSliderPosition
        )

        // Boton para reiniciar los dos valores
        OutlinedButton(
            colors = ButtonDefaults.elevatedButtonColors(),
            elevation = ButtonDefaults.elevatedButtonElevation(4.dp),
            onClick = {
                changeXCoordSliderPosition(0.5f)
                changeYCoordSliderPosition(0.5f)
            },
            content = {
                Text(text = stringResource(R.string.draw_scope_draw_line_restart_button_label))
            }
        )
    }
}


@Composable
private fun LineExample(
    xCoordSliderPosition: () -> Float,
    yCoordSliderPosition: () -> Float
) {

    val endXCoordinate by animateFloatAsState(
        targetValue = xCoordSliderPosition(),
        label = "XCoordinateAnimation"
    )
    val endYCoordinate by animateFloatAsState(
        targetValue = yCoordSliderPosition(),
        label = "YCoordinateAnimation"
    )

    val linePrimaryColor = MaterialTheme.colorScheme.primary

    Canvas(
        modifier = Modifier.size(150.dp),
        onDraw = {
            drawLine(
                // El color para rellenar la linea
                color = linePrimaryColor,
                // Opcionalmente se puede asignar un Brush para el color
                //brush = Brush.linearGradient(listOf(circlePrimaryColor, circleSecondaryColor)),
                // Primer punto de la linea a ser dibujado
                start = center,
                // Segundo punto de la linea a ser dibujado
                end = Offset(
                    x = size.width * endXCoordinate,
                    y = size.height * endYCoordinate,
                ),
                // El ancho del trazo a aplicarse a la linea
                strokeWidth = 5f, // strokeWidth = Stroke.HairlineWidth,
                // Trato aplicado al final del segmento de la linea
                cap = StrokeCap.Round,
                // Efecto o patron opcional a aplicar a la linea
                pathEffect = null,
                // Opacidad a ser aplicada a la linea de 0.0f a 1.0f representando de transparente a opaco respectivamente
                alpha = 1f,
                // ColorFilter a aplicar al color cuando se dibuja la linea
                colorFilter = null,
                // Algoritmo de fusion que se aplicara cuando la linea se dibuja
                blendMode = DrawScope.DefaultBlendMode
            )
        }
    )
}




@CompactSizeScreenThemePreview
@Composable
private fun DrawLineExamplePreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        DrawLineExampleContent(
            xCoordSliderPosition = { 0.5f },
            yCoordSliderPosition = { 0.5f },
            changeXCoordSliderPosition = {},
            changeYCoordSliderPosition = {}
        )
    }
}
