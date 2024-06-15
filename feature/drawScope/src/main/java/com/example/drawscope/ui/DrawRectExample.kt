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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ui.theme.AppTheme
import com.example.ui.ui.CustomSlider


@Composable
internal fun DrawRectExample() {
    // SliderPositions para positionX, positionY, sizeX, sizeY
    var xCoordSliderPosition by remember { mutableFloatStateOf(1f) }
    var yCoordSliderPosition by remember { mutableFloatStateOf(2.5f) }
    var xSizeSliderPosition by remember { mutableFloatStateOf(8f) }
    var ySizeSliderPosition by remember { mutableFloatStateOf(5f) }

    // Valores animables para positionX, positionY, sizeX y sizeY
    val rectangleStartXCoord by animateFloatAsState(
        targetValue = xCoordSliderPosition / 10,
        label = "Rectangle Start X Coordinate Animation"
    )
    val rectangleStartYCoord by animateFloatAsState(
        targetValue = yCoordSliderPosition / 10,
        label = "Rectangle Start Y Coordinate Animation"
    )
    val rectangleSizeX by animateFloatAsState(
        targetValue = xSizeSliderPosition / 10,
        label = "Rectangle Size X Animation"
    )
    val rectangleSizeY by animateFloatAsState(
        targetValue = ySizeSliderPosition / 10,
        label = "Rectangle Size Y Animation"
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Slider para cambiar la coordenada X de inicio del rectangulo
        CustomSlider(
            sliderTextLabel = "Start X Position",
            sliderValue = { xCoordSliderPosition },
            sliderValueRange = -10f..10f,
            onSliderValueChange = { newPosition -> xCoordSliderPosition = newPosition },
            onSliderValueReset = { xCoordSliderPosition = 1f }
        )

        // Slider para cambiar la coordenada X de inicio del rectangulo
        CustomSlider(
            sliderTextLabel = "Start Y Position",
            sliderValue = { yCoordSliderPosition },
            sliderValueRange = 0f..10f,
            onSliderValueChange = { newPosition -> yCoordSliderPosition = newPosition },
            onSliderValueReset = { yCoordSliderPosition = 2.5f }
        )

        // Slider para cambiar el tamaño X del rectangulo
        CustomSlider(
            sliderTextLabel = "Size X",
            sliderValue = { xSizeSliderPosition },
            sliderValueRange = 0f..10f,
            onSliderValueChange = { newPosition -> xSizeSliderPosition = newPosition },
            onSliderValueReset = { xSizeSliderPosition = 8f }
        )

        // Slider para cambiar el tamaño Y del rectangulo
        CustomSlider(
            sliderTextLabel = "Size Y",
            sliderValue = { ySizeSliderPosition },
            sliderValueRange = 0f..10f,
            onSliderValueChange = { newPosition -> ySizeSliderPosition = newPosition },
            onSliderValueReset = { ySizeSliderPosition = 5f }
        )

        // Rectagnulo que se va a dibujar
        RectExample(
            rectStartXCoord = rectangleStartXCoord,
            rectStartYCoord = rectangleStartYCoord,
            rectSizeX = rectangleSizeX,
            rectSizeY = rectangleSizeY
        )

        // Boton para reiniciar los cuatro valores
        OutlinedButton(
            colors = ButtonDefaults.elevatedButtonColors(),
            elevation = ButtonDefaults.elevatedButtonElevation(4.dp),
            onClick = {
                xCoordSliderPosition = 1f
                yCoordSliderPosition = 2.5f
                xSizeSliderPosition = 8f
                ySizeSliderPosition = 5f
            },
            content = {
                Text(text = "Reiniciar valores")
            }
        )
    }
}
@Composable
private fun RectExample(
    rectStartXCoord: Float,
    rectStartYCoord: Float,
    rectSizeX: Float,
    rectSizeY: Float
) {
    val rectPrimaryColor = MaterialTheme.colorScheme.primary

    Canvas(modifier = Modifier.size(150.dp)) {
        drawRect(
            // El color para rellenar el rectangulo
            color = rectPrimaryColor,
            // Opcionalmente se puede asiganr un Brush para el color
            //brush = Brush.linearGradient(listOf(primaryColor, secondaryColor)),
            // Coordenadas de el origen local de 0,0 relativo al contenedor actual
            topLeft = Offset(x = size.width * rectStartXCoord, y = size.height * rectStartYCoord),
            // Dimensiones del rectangulo que se va a dibujar
            size = Size(width = size.width * rectSizeX, height = size.height * rectSizeY),
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
}


@Preview(showBackground = true)
@Composable
private fun DrawRectExamplePreview() {
    AppTheme {
        DrawRectExample()
    }
}

