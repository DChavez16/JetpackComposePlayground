package com.example.drawscope.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.drawscope.DrawScopeViewModel
import com.example.drawscope.R
import com.example.ui.theme.PreviewAppTheme
import com.example.ui.ui.CompactSizeScreenThemePreview
import com.example.ui.ui.CustomSlider


@Composable
internal fun DrawArcExample(
    drawScopeViewModel: DrawScopeViewModel = hiltViewModel()
) {

    val sliderPosition by drawScopeViewModel.drawArcSliderPosition.collectAsState()

    DrawArcExampleContent(
        sliderPosition = { sliderPosition },
        changeSliderPosition = drawScopeViewModel::changeDrawArcSliderPosition
    )
}


@Composable
private fun DrawArcExampleContent(
    sliderPosition: () -> Float,
    changeSliderPosition: (Float) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        // Slider para modificar el angulo final del arco
        CustomSlider(
            sliderTextLabel = stringResource(R.string.draw_scope_draw_arc_slider_label),
            sliderValue = sliderPosition,
            sliderValueRange = -1f..1f,
            onSliderValueChange = { newPosition -> changeSliderPosition(newPosition) },
            onSliderValueReset = { changeSliderPosition(0f) }
        )

        // Arco que se va a dibujar usando drawArc
        ArcExample(sliderPosition = sliderPosition)
    }
}


@Composable
private fun ArcExample(
    sliderPosition: () -> Float
) {

    val finalAngleValue by animateFloatAsState(
        targetValue = sliderPosition().times(360),
        label = "FinalAngleAnimation"
    )

    val arcColor = MaterialTheme.colorScheme.primary

    Canvas(
        modifier = Modifier.size(150.dp),
        onDraw = {
            drawArc(
                // El color para rellenar el arco
                //color = arcColor
                // Opcionalmente se puede asignar un Brush para el color
                brush = Brush.linearGradient(colors = listOf(arcColor, Color.Black)),
                // Angulo inicial en grados. 0 representa las 3 en punto
                startAngle = 0f,
                // Tamaño del arco en grados que es dibujado en el sentido de las agujas del reloj relativo al angulo inicial
                sweepAngle = finalAngleValue,
                // Bandera que incia si el arco debe cerrar el centro de los limites
                useCenter = false,
                // Coordenadas del origen local 0,0 relativo al canvas
//                topLeft = Offset(x = -size.width / 2f, y = size.height / 8f),  <- this was used as an example to show the use of Offset
                topLeft = Offset(x = 0f, y = 0f),
                // Dimensiones del arco a dibujar
//                size = Size(width = size.width / 1.25f, height = size.height * 0.75f),    <- this was used as an example to show the use of Size
                size = Size(width = size.width, height = size.height),
                // Opacidad a ser aplicada al arco de 0.0f a 1.0f representando de transparente a opaco respectivamente
                alpha = 0.9f,
                // Define si el arco esta trazado o rellenado
                //style = Fill
                style = Stroke(
                    width = 25f,
                    miter = 2f,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round,
                    pathEffect = PathEffect.cornerPathEffect(2f)
                ),
                // ColorFilter a aplicar al color cuando se dibuja el arco
                colorFilter = ColorFilter.colorMatrix(ColorMatrix()),
                // Algoritmo de fusion que se aplicara cuando el arco se dibuje
                blendMode = DrawScope.DefaultBlendMode
            )
        }
    )
}




@CompactSizeScreenThemePreview
@Composable
private fun DrawArcExamplePreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        DrawArcExampleContent(
            sliderPosition = { -0.25f },
            changeSliderPosition = {}
        )
    }
}