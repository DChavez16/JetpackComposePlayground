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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.drawscope.R
import com.example.ui.theme.AppTheme
import com.example.ui.ui.CustomSlider


@Composable
internal fun TranslateExample() {
    // SliderPosition para positionX y positionY
    var positionXSliderPosition by remember { mutableFloatStateOf(0f) }
    var positionYSliderPosition by remember { mutableFloatStateOf(0f) }

    // Valor animable para positionX y positionY
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
        // Slider para cambiar la posicion vertical de la imagen
        CustomSlider(
            sliderTextLabel = "Position X",
            sliderValue = { positionXSliderPosition },
            sliderValueRange = -10f..10f,
            onSliderValueChange = { newPosition -> positionXSliderPosition = newPosition },
            onSliderValueReset = { positionXSliderPosition = 0f }
        )

        // Slider para cambiar la posicion horizontal de la imagen
        CustomSlider(
            sliderTextLabel = "Position Y",
            sliderValue = { positionYSliderPosition },
            sliderValueRange = -10f..10f,
            onSliderValueChange = { newPosition -> positionYSliderPosition = newPosition },
            onSliderValueReset = { positionYSliderPosition = 0f }
        )

        // Imagen de ejemplo que se va a recortar usando clipPath
        ImageExample(
            positionX = imagePositionX,
            positionY = imagePositionY
        )

        // Boton para reiniciar los valores de los sliders
        OutlinedButton(
            colors = ButtonDefaults.elevatedButtonColors(),
            elevation = ButtonDefaults.elevatedButtonElevation(4.dp),
            onClick = {
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
    positionX: Float,
    positionY: Float,
) {
    val imageBitmap = ImageBitmap.imageResource(id = R.drawable.jetpack_compose_icon)

    Canvas(modifier = Modifier.size(150.dp)) {
        translate(
            // Pixeles a trasladar el espacio de coordenadas en el eje x
            left = size.width * positionX,
            // Pixeles a trasladar el espacio de coordenadas en el eje y
            top = size.height * positionY
        ) {
            drawImage(
                image = imageBitmap,
                dstSize = IntSize((size.width).toInt(), size.height.toInt())
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun TranslateExamplePreview() {
    AppTheme {
        TranslateExample()
    }
}