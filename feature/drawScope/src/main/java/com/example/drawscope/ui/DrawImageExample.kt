package com.example.drawscope.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.drawscope.R
import com.example.ui.theme.AppTheme
import com.example.ui.ui.CustomSlider
import kotlin.math.absoluteValue


@Composable
internal fun DrawImageExample() {
    // SliderPositions para width, height, positionX y positionY
    var widthSliderPosition by remember { mutableFloatStateOf(10f) }
    var heightSliderPosition by remember { mutableFloatStateOf(10f) }
    var positionXSliderPosition by remember { mutableFloatStateOf(0f) }
    var positionYSliderPosition by remember { mutableFloatStateOf(0f) }

    // Valores animables para width, height, positionX y positionY
    val imageWidth by animateFloatAsState(
        targetValue = widthSliderPosition / 10,
        label = "Image Width Animation"
    )
    val imageHeight by animateFloatAsState(
        targetValue = heightSliderPosition / 10,
        label = "Image Height Animation"
    )
    val imagePositionX by animateFloatAsState(
        targetValue = positionXSliderPosition / 10,
        label = "Image Position X Animation"
    )
    val imagePositionY by animateFloatAsState(
        targetValue = positionYSliderPosition / 10,
        label = "Image Position Y Animation"
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Slider para cambiar el ancho de la imagen
        CustomSlider(
            sliderTextLabel = "Width",
            sliderValue = { widthSliderPosition },
            sliderValueRange = 0f..10f,
            onSliderValueChange = { newPosition -> widthSliderPosition = newPosition },
            onSliderValueReset = { widthSliderPosition = 10f }
        )

        // Slider para cambiar el alto de la imagen
        CustomSlider(
            sliderTextLabel = "Height",
            sliderValue = { heightSliderPosition },
            sliderValueRange = 0f..10f,
            onSliderValueChange = { newPosition -> heightSliderPosition = newPosition },
            onSliderValueReset = { heightSliderPosition = 10f }
        )

        // Slider para cambiar la posicion X de la imagen
        CustomSlider(
            sliderTextLabel = "Position X",
            sliderValue = { positionXSliderPosition },
            sliderValueRange = -10f..10f,
            onSliderValueChange = { newPosition -> positionXSliderPosition = newPosition },
            onSliderValueReset = { positionXSliderPosition = 0f }
        )

        // Slider para cambiar la posicion Y de la imagen
        CustomSlider(
            sliderTextLabel = "Position Y",
            sliderValue = { positionYSliderPosition },
            sliderValueRange = -10f..10f,
            onSliderValueChange = { newPosition -> positionYSliderPosition = newPosition },
            onSliderValueReset = { positionYSliderPosition = 0f }
        )

        // Imagen a ser dibujada
        ImageExample(
            imageWidth = imageWidth,
            imageHeight = imageHeight,
            imagePositionX = imagePositionX,
            imagePositionY = imagePositionY
        )

        // Boton para reiniciar los valores de los sliders
        OutlinedButton(
            colors = ButtonDefaults.elevatedButtonColors(),
            elevation = ButtonDefaults.elevatedButtonElevation(4.dp),
            onClick = {
                widthSliderPosition = 10f
                heightSliderPosition = 10f
                positionXSliderPosition = 0f
                positionYSliderPosition = 0f
            },
            content = {
                Text(text = "Reiniciar valores")
            }
        )
    }
}

@Composable
private fun ImageExample(
    imageWidth: Float,
    imageHeight: Float,
    imagePositionX: Float,
    imagePositionY: Float
) {
    val imageBitmap = ImageBitmap.imageResource(id = R.drawable.jetpack_compose_icon)

    Canvas(modifier = Modifier.size(150.dp)) {
        drawImage(
            // La imagen fuente a ser dibujada
            image = imageBitmap,
            // Coordenadas opcionales representando las coordenadas superiores izquierdas de la imagen
            srcOffset = IntOffset.Zero,
            // Dimensiones opcionales de la imagen fuente a dibujar
            srcSize = IntSize(
                width = (imageBitmap.width / imageWidth).toInt(),
                height = (imageBitmap.height / imageHeight).toInt(),
            ),
            // Coordenadas opcionales representando las coordenadas de destino de la imagen que se va a dibujar
            dstOffset = IntOffset(
                x = (size.width * imagePositionX).toInt(),
                y = (size.height * imagePositionY).toInt()
            ),
            // Dimensmiones opcionales del destino que se va a dibujar
            dstSize = IntSize((size.width).toInt(), size.height.toInt()),
            // Opacidad a ser aplicada a la imagen de 0.0f a 1.0f representando de transparente a opaco respectivamente
            alpha = 1 - (imagePositionX.absoluteValue + imagePositionY.absoluteValue) / 2,
            // Define si la imagen esta trazado o rellenado
            style = Fill,
            //style = Stroke(width = 25f, miter = 2f, cap = StrokeCap.Round, join = StrokeJoin.Round, pathEffect = PathEffect.cornerPathEffect(2f)),
            // ColorFilter a aplicar al color cuando se dibuja la imagen
            colorFilter = null,
            // Algoritmo de fusion que se aplicara cuando la imagen se dibuja
            blendMode = DrawScope.DefaultBlendMode,
            // Filtro de calidad aplicado a la imagen cuando es escalada y dibujada en el destino: None, Low, Medium o High
            filterQuality = FilterQuality.Medium
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun DrawImageExamplePreview() {
    AppTheme {
        DrawImageExample()
    }
}