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
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.drawscope.R
import com.example.ui.theme.AppTheme
import com.example.ui.ui.CustomSlider


@Composable
internal fun InsetExample() {
    // SliderPosition para verticalScale y horizontalScale
    var verticalScaleSliderPosition by remember { mutableFloatStateOf(0f) }
    var horizontalScaleSliderPosition by remember { mutableFloatStateOf(0f) }

    // Valor animable para verticalScale y horizontalScale
    val imageVerticalScale by animateFloatAsState(
        targetValue = verticalScaleSliderPosition / 10,
        label = "Image Vertical Scale Animation"
    )
    val imageHorizontalScale by animateFloatAsState(
        targetValue = horizontalScaleSliderPosition / 10,
        label = "Image Horizontal Scale Animation"
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Slider para cambiar la posicion vertical de la imagen
        CustomSlider(
            sliderTextLabel = "Vertical Scale",
            sliderValue = { verticalScaleSliderPosition },
            sliderValueRange = -5f..5f,
            onSliderValueChange = { newPosition -> verticalScaleSliderPosition = newPosition },
            onSliderValueReset = { verticalScaleSliderPosition = 0f }
        )

        // Slider para cambiar la posicion horizontal de la imagen
        CustomSlider(
            sliderTextLabel = "Horizontal Scale",
            sliderValue = { horizontalScaleSliderPosition },
            sliderValueRange = -5f..5f,
            onSliderValueChange = { newPosition -> horizontalScaleSliderPosition = newPosition },
            onSliderValueReset = { horizontalScaleSliderPosition = 0f }
        )

        // Imagen de ejemplo que se va a recortar usando clipPath
        ImageExample(
            verticalScale = imageVerticalScale,
            horizontalScale = imageHorizontalScale
        )

        // Boton para reiniciar los valores de los sliders
        OutlinedButton(
            colors = ButtonDefaults.elevatedButtonColors(),
            elevation = ButtonDefaults.elevatedButtonElevation(4.dp),
            onClick = {
                verticalScaleSliderPosition = 0f
                horizontalScaleSliderPosition = 0f
            },
            content = {
                Text(text = "Reiniciar valores")
            }
        )
    }
}

@Composable
private fun ImageExample(
    verticalScale: Float,
    horizontalScale: Float
) {
    val imageBitmap = ImageBitmap.imageResource(id = R.drawable.jetpack_compose_icon)

    Canvas(modifier = Modifier.size(150.dp)) {
        inset(
            // Numero de pixeles para encuadrar el limite izquierdo del dibujo
            left = size.width * horizontalScale,
            // Numero de pixeles para encuadrar el limite superior del dibujo
            top = size.height * verticalScale,
            // Numero de pixeles para encuadrar el limite derecho del dibujo
            right = size.width * horizontalScale,
            // Numero de pixeles para encuadrar el limite inferior del dibujo
            bottom = size.height * verticalScale,
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
private fun InsetExamplePreview() {
    AppTheme {
        InsetExample()
    }
}