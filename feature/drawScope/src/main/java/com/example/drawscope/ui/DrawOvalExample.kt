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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ui.theme.AppTheme
import com.example.ui.ui.CustomSlider


@Composable
internal fun DrawOvalExample() {
    // SliderPositions para positionX, positionY, sizeX y sizeY
    var xCoordSliderPosition by remember { mutableFloatStateOf(1f) }
    var yCoordSliderPosition by remember { mutableFloatStateOf(2.5f) }
    var xSizeSliderPosition by remember { mutableFloatStateOf(8f) }
    var ySizeSliderPosition by remember { mutableFloatStateOf(5f) }

    // Valores animables para positionX, positionY, sizeX y sizeY
    val ovalStartXCoord by animateFloatAsState(
        targetValue = xCoordSliderPosition / 10,
        label = "Oval Start X Coordinate Animation"
    )
    val ovalStartYCoord by animateFloatAsState(
        targetValue = yCoordSliderPosition / 10,
        label = "Oval Start Y Coordinate Animation"
    )
    val ovalSizeX by animateFloatAsState(
        targetValue = xSizeSliderPosition / 10,
        label = "Oval Size X Animation"
    )
    val ovalSizeY by animateFloatAsState(
        targetValue = ySizeSliderPosition / 10,
        label = "Oval Size Y Animation"
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Slider para cambiar la coordenada X de inicio del ovalo
        CustomSlider(
            sliderTextLabel = "Start X Position",
            sliderValue = { xCoordSliderPosition },
            sliderValueRange = -10f..10f,
            onSliderValueChange = { newPosition -> xCoordSliderPosition = newPosition },
            onSliderValueReset = { xCoordSliderPosition = 1f }
        )

        // Slider para cambiar la coordenada Y de inicio del ovalo
        CustomSlider(
            sliderTextLabel = "Start Y Position",
            sliderValue = { yCoordSliderPosition },
            sliderValueRange = 0f..10f,
            onSliderValueChange = { newPosition -> yCoordSliderPosition = newPosition },
            onSliderValueReset = { yCoordSliderPosition = 2.5f }
        )

        // Slider para cambiar el tamaño X del ovalo
        CustomSlider(
            sliderTextLabel = "Size X",
            sliderValue = { xSizeSliderPosition },
            sliderValueRange = 0f..10f,
            onSliderValueChange = { newPosition -> xSizeSliderPosition = newPosition },
            onSliderValueReset = { xSizeSliderPosition = 8f }
        )

        // Slider para cambiar el tamaño Y del ovalo
        CustomSlider(
            sliderTextLabel = "Size Y",
            sliderValue = { ySizeSliderPosition },
            sliderValueRange = 0f..10f,
            onSliderValueChange = { newPosition -> ySizeSliderPosition = newPosition },
            onSliderValueReset = { ySizeSliderPosition = 5f }
        )

        // Ovalo que va a ser dibujado
        OvalExample(
            ovalStartXCoord = ovalStartXCoord,
            ovalStartYCoord = ovalStartYCoord,
            ovalSizeX = ovalSizeX,
            ovalSizeY = ovalSizeY
        )

        // Boton para reiniciar los valores de los sliders
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
private fun OvalExample(
    ovalStartXCoord: Float,
    ovalStartYCoord: Float,
    ovalSizeX: Float,
    ovalSizeY: Float
) {
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


@Preview(showBackground = true)
@Composable
private fun DrawOvalExamplePreview() {
    AppTheme {
        DrawOvalExample()
    }
}