package com.example.drawscope.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.drawscope.R
import com.example.ui.theme.AppTheme
import com.example.ui.ui.CustomSlider


@Composable
internal fun RotateExample() {
    // SliderPosition para angleDegrees
    var angleDegreesSliderPosition by remember { mutableFloatStateOf(0f) }

    // Valor animable para verticalScale y horizontalScale
    val imageAngleDegrees by animateFloatAsState(
        targetValue = angleDegreesSliderPosition,
        label = "Image Angle Degrees Animation"
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Slider para cambiar la posicion vertical de la imagen
        CustomSlider(
            sliderTextLabel = "Angle Degrees",
            sliderValue = { angleDegreesSliderPosition },
            sliderValueRange = -360f..360f,
            onSliderValueChange = { newPosition -> angleDegreesSliderPosition = newPosition },
            onSliderValueReset = { angleDegreesSliderPosition = 0f }
        )

        // Imagen de ejemplo que se va a recortar usando clipPath
        ImageExample(
            angleDegrees = { imageAngleDegrees }
        )
    }
}

@Composable
private fun ImageExample(
    angleDegrees: () -> Float
) {
    val imageBitmap = ImageBitmap.imageResource(id = R.drawable.jetpack_compose_icon)

    Canvas(modifier = Modifier.size(150.dp)) {
        rotate(
            // Grados para rotar en el sentido del reloj
            degrees = angleDegrees(),
            // Las coordenadas del punto de pivote, por defecto se ubica en el centro del espacio de coordenadas
            pivot = center
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
private fun RotateExamplePreview() {
    AppTheme {
        RotateExample()
    }
}