package com.example.drawscope.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ui.theme.AppTheme
import com.example.ui.ui.CustomSlider

@Composable
internal fun DrawArcExample() {
    var sliderPosition by remember { mutableFloatStateOf(0f) }

    val finalAngleValue by animateFloatAsState(
        targetValue = sliderPosition,
        label = "Final Angle Animation"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        // Slider para modificar el angulo final del arco
        CustomSlider(
            sliderTextLabel = " Final Angle",
            sliderValue = { sliderPosition },
            sliderValueRange = -360f..360f,
            onSliderValueChange = { newPosition -> sliderPosition = newPosition },
            onSliderValueReset = { sliderPosition = 0f }
        )

        // Arco que se va a dibujar usando drawArc
        ArcExample(finalAngleValue = finalAngleValue)
    }
}

@Composable
private fun ArcExample(finalAngleValue: Float) {
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
                topLeft = Offset(x = -size.width / 2f, y = size.height / 8f),
                // Dimensiones del arco a dibujar
                size = Size(width = size.width / 1.25f, height = size.height * 0.75f),
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


@Preview(showBackground = true)
@Composable
private fun DrawArcExamplePreview() {
    AppTheme {
        DrawArcExample()
    }
}