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
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ui.theme.AppTheme
import com.example.ui.ui.CustomSlider


@Composable
internal fun DrawCircleExample() {
    var radiusSliderPosition by remember { mutableFloatStateOf(0.25f) }
    var xCoordSliderPosition by remember { mutableFloatStateOf(0.5f) }
    var yCoordSliderPosition by remember { mutableFloatStateOf(0.5f) }

    val circleRadius by animateFloatAsState(
        targetValue = radiusSliderPosition,
        label = "Circle Radius Animation"
    )

    val circleXCoordinate by animateFloatAsState(
        targetValue = xCoordSliderPosition,
        label = "Circle X Coordinate Animation"
    )

    val circleYCoordinate by animateFloatAsState(
        targetValue = yCoordSliderPosition,
        label = "Circle Y Coordinate Animation"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        // Slider para modificar el radio del circulo
        CustomSlider(
            sliderTextLabel = "Circle Radius",
            sliderValue = { radiusSliderPosition },
            sliderValueRange = 0f..0.5f,
            onSliderValueChange = { newPosition -> radiusSliderPosition = newPosition },
            onSliderValueReset = { radiusSliderPosition = 0.25f }
        )

        // Slider para modificar la coordenada X del circulo
        CustomSlider(
            sliderTextLabel = "Circle X Coord",
            sliderValue = { xCoordSliderPosition },
            sliderValueRange = 0f..1f,
            onSliderValueChange = { newPosition -> xCoordSliderPosition = newPosition },
            onSliderValueReset = { xCoordSliderPosition = 0.5f }
        )

        // Slider para modificar la coordenada Y del circulo
        CustomSlider(
            sliderTextLabel = "Circle Y Coord",
            sliderValue = { yCoordSliderPosition },
            sliderValueRange = 0f..1f,
            onSliderValueChange = { newPosition -> yCoordSliderPosition = newPosition },
            onSliderValueReset = { yCoordSliderPosition = 0.5f }
        )

        // Circulo que se va a dibujar usando drawArc
        CircleExample(
            circleRadiusValue = circleRadius,
            xCoordinateValue = circleXCoordinate,
            yCoordinateValue = circleYCoordinate
        )

        // Boton para reiniciar los tres valores al mismo tiempo
        OutlinedButton(
            colors = ButtonDefaults.elevatedButtonColors(),
            elevation = ButtonDefaults.elevatedButtonElevation(4.dp),
            onClick = {
                radiusSliderPosition = 0.25f
                xCoordSliderPosition = 0.5f
                yCoordSliderPosition = 0.5f
            },
            content = {
                Text(text = "Reiniciar valores")
            }
        )
    }
}

@Composable
private fun CircleExample(
    circleRadiusValue: Float,
    xCoordinateValue: Float,
    yCoordinateValue: Float
) {
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
                radius = size.maxDimension * circleRadiusValue,
                // El centro de coordenadas dondel el circulo va a ser dibujado
                //center = center,
                center = Offset(
                    x = size.width * xCoordinateValue,
                    y = size.height * yCoordinateValue
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


@Preview(showBackground = true)
@Composable
private fun DrawCircleExamplePreview() {
    AppTheme {
        DrawCircleExample()
    }
}