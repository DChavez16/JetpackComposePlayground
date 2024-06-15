package com.example.drawscope.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ui.theme.AppTheme
import com.example.ui.ui.CustomSlider


@Composable
internal fun DrawLineExample() {
    var xCoordSliderPosition by remember { mutableFloatStateOf(5f) }
    var yCoordSliderPosition by remember { mutableFloatStateOf(5f) }

    val endXCoordinate by animateFloatAsState(
        targetValue = xCoordSliderPosition/10,
        label = "X Coordinate Animation"
    )
    val endYCoordinate by animateFloatAsState(
        targetValue = yCoordSliderPosition/10,
        label = "Y Coordinate Animation"
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Slider para controlar la coordenada X del final de la linea
        CustomSlider(
            sliderTextLabel = "End X Coord",
            sliderValue = { xCoordSliderPosition },
            sliderValueRange = 0f..10f,
            onSliderValueChange = { newPosition -> xCoordSliderPosition = newPosition },
            onSliderValueReset = { xCoordSliderPosition = 5f }
        )

        // Slider para controlar la coordenada Y del final de la linea
        CustomSlider(
            sliderTextLabel = "End Y Coord",
            sliderValue = { yCoordSliderPosition },
            sliderValueRange = 0f..10f,
            onSliderValueChange = { newPosition -> yCoordSliderPosition = newPosition },
            onSliderValueReset = { yCoordSliderPosition = 5f }
        )

        // Linea que se va a dibujar usando drawLine
        LineExample(endXCoordinate = endXCoordinate, endYCoordinate = endYCoordinate)

        // Boton para reiniciar los dos valores
        OutlinedButton(
            colors = ButtonDefaults.elevatedButtonColors(),
            elevation = ButtonDefaults.elevatedButtonElevation(4.dp),
            onClick = {
                xCoordSliderPosition = 5f
                yCoordSliderPosition = 5f
            },
            content = {
                Text(text = "Reiniciar valores")
            }
        )
    }
}

@Composable
private fun LineExample(endXCoordinate: Float, endYCoordinate: Float) {
    val linePrimaryColor = MaterialTheme.colorScheme.primary

    Canvas(modifier = Modifier.size(150.dp)) {
        drawLine(
            // El color para rellenar la linea
            color = linePrimaryColor,
            // Opcionalmente se puede asiganr un Brush para el color
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
}


@Preview(showBackground = true)
@Composable
private fun DrawLineExamplePreview() {
    AppTheme {
        DrawLineExample()
    }
}
