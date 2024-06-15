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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.drawscope.R
import com.example.ui.theme.AppTheme
import com.example.ui.ui.CustomSlider


@Composable
internal fun ScaleExample() {
    // SliderPosition para scaleX y scaleY
    var scaleXSliderPosition by remember { mutableFloatStateOf(10f) }
    var scaleYSliderPosition by remember { mutableFloatStateOf(10f) }

    // Valor animable para scaleX y scaleY
    val imageScaleX by animateFloatAsState(
        targetValue = scaleXSliderPosition / 10,
        label = "Image Scale X Animation"
    )
    val imageScaleY by animateFloatAsState(
        targetValue = scaleYSliderPosition / 10,
        label = "Image Scale Y Animation"
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Slider para cambiar la posicion vertical de la imagen
        CustomSlider(
            sliderTextLabel = "Scale X",
            sliderValue = { scaleXSliderPosition },
            sliderValueRange = 0f..10f,
            onSliderValueChange = { newPosition -> scaleXSliderPosition = newPosition },
            onSliderValueReset = { scaleXSliderPosition = 10f }
        )

        // Slider para cambiar la posicion horizontal de la imagen
        CustomSlider(
            sliderTextLabel = "Scale Y",
            sliderValue = { scaleYSliderPosition },
            sliderValueRange = 0f..10f,
            onSliderValueChange = { newPosition -> scaleYSliderPosition = newPosition },
            onSliderValueReset = { scaleYSliderPosition = 10f }
        )

        // Imagen de ejemplo que se va a recortar usando clipPath
        ImageExample(
            scaleX = imageScaleX,
            scaleY = imageScaleY
        )

        // Boton para reiniciar los valores de los sliders
        OutlinedButton(
            colors = ButtonDefaults.elevatedButtonColors(),
            elevation = ButtonDefaults.elevatedButtonElevation(4.dp),
            onClick = {
                scaleXSliderPosition = 10f
                scaleYSliderPosition = 10f
            },
            content = {
                Text(text = "Reiniciar valores")
            }
        )
    }
}

@Composable
private fun ImageExample(
    scaleX: Float,
    scaleY: Float,
) {
    val imageBitmap = ImageBitmap.imageResource(id = R.drawable.jetpack_compose_icon)

    Canvas(modifier = Modifier.size(150.dp)) {
        scale(
            // La cantidad de escala en X
            scaleX = scaleX,
            // La cantidad de escala en Y
            scaleY = scaleY,
            // Las coordenadas del punto de pivote, por defecto se ubica en el centro del espacio de coordenadas
            pivot = Offset.Zero
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
private fun ScaleExamplePreview() {
    AppTheme {
        ScaleExample()
    }
}