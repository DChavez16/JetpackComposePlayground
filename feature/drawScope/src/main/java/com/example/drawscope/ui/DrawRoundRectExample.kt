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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ui.theme.AppTheme
import com.example.ui.ui.CustomSlider

@Composable
internal fun DrawRoundRectExample() {
    // SliderPositions para sizeX, sizeY, radiusX y radiusY
    var xSizeSliderPosition by remember { mutableFloatStateOf(8f) }
    var ySizeSliderPosition by remember { mutableFloatStateOf(5f) }
    var xRadiusSliderPosition by remember { mutableFloatStateOf(2f) }
    var yRadiusSliderPosition by remember { mutableFloatStateOf(2f) }

    // Valores animables para sizeX, sizeY, radiusX y radiusY
    val roundRectangleSizeX by animateFloatAsState(
        targetValue = xSizeSliderPosition / 10,
        label = "Round Rectangle Size X Animation"
    )
    val roundRectangleSizeY by animateFloatAsState(
        targetValue = ySizeSliderPosition / 10,
        label = "Round Rectangle Size Y Animation"
    )
    val roundRectangleCornerRadiusX by animateFloatAsState(
        targetValue = xRadiusSliderPosition / 10,
        label = "Round Rectangle Start X Corner Radius Animation"
    )
    val roundRectangleCornerRadiusY by animateFloatAsState(
        targetValue = yRadiusSliderPosition / 10,
        label = "Round Rectangle Start Y CornerRadius Animation"
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
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

        // Slider para cambiar el tamaño X del radio de las esquinas del rectangulo
        CustomSlider(
            sliderTextLabel = "Corner Radius X",
            sliderValue = { xRadiusSliderPosition },
            sliderValueRange = 0f..10f,
            onSliderValueChange = { newPosition -> xRadiusSliderPosition = newPosition },
            onSliderValueReset = { xRadiusSliderPosition = 2f }
        )

        // Slider para cambiar el tamaño Y del radio de las esquinas del rectangulo<
        CustomSlider(
            sliderTextLabel = "Corner Radius Y",
            sliderValue = { yRadiusSliderPosition },
            sliderValueRange = 0f..10f,
            onSliderValueChange = { newPosition -> yRadiusSliderPosition = newPosition },
            onSliderValueReset = { yRadiusSliderPosition = 2f }
        )

        // Rectagnulo que se va a dibujar
        RoundRectExample(
            roundRectSizeX = roundRectangleSizeX,
            roundRectSizeY = roundRectangleSizeY,
            roundRectCornerRadiusX = roundRectangleCornerRadiusX,
            roundRectCornerRadiusY = roundRectangleCornerRadiusY
        )

        // Boton para reiniciar los cuatro valores
        OutlinedButton(
            colors = ButtonDefaults.elevatedButtonColors(),
            elevation = ButtonDefaults.elevatedButtonElevation(4.dp),
            onClick = {
                xSizeSliderPosition = 8f
                ySizeSliderPosition = 5f
                xRadiusSliderPosition = 2f
                yRadiusSliderPosition = 2f
            },
            content = {
                Text(text = "Reiniciar valores")
            }
        )
    }
}

@Composable
private fun RoundRectExample(
    roundRectSizeX: Float,
    roundRectSizeY: Float,
    roundRectCornerRadiusX: Float,
    roundRectCornerRadiusY: Float
) {
    val rectPrimaryColor = MaterialTheme.colorScheme.primary

    Canvas(modifier = Modifier.size(150.dp)) {
        drawRoundRect(
            // El color para rellenar el rectangulo redondeado
            color = rectPrimaryColor,
            // Opcionalmente se puede asiganr un Brush para el color
            //brush = Brush.linearGradient(listOf(primaryColor, secondaryColor)),
            // Coordenadas de el origen local de 0,0 relativo al contenedor actual
            topLeft = Offset(
                x = size.width / 2 * (1 - roundRectSizeX),
                y = size.height / 2 * (1 - roundRectSizeY)
            ),
            // Dimensiones del rectangulo redondeado que se va a dibujar
            size = Size(width = size.width * roundRectSizeX, height = size.height * roundRectSizeY),
            // Radio de las esquinas del rectangulo redondeado, valores negativos son convertidos a 0
            cornerRadius = CornerRadius(
                x = size.width * (roundRectSizeX * roundRectCornerRadiusX),
                y = size.height * (roundRectSizeY * roundRectCornerRadiusY)
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


@Preview(showBackground = true)
@Composable
private fun DrawRoundRectExamplePreview() {
    AppTheme {
        DrawRoundRectExample()
    }
}