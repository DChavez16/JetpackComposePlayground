package com.example.drawscope.ui

import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.drawscope.R
import com.example.ui.theme.AppTheme
import com.example.ui.ui.CustomSlider


@Composable
internal fun MultipleTransformExample() {
    // SliderPosition para verticalValue
    var verticalValueSliderPosition by remember { mutableFloatStateOf(0f) }

    // Valor animable para verticalScale y horizontalScale
    val imageVerticalValue by animateFloatAsState(
        targetValue = verticalValueSliderPosition / 10,
        animationSpec = tween(
            durationMillis = 500,
            easing = EaseOutBack
        ),
        label = "Image Vertical Value Animation"
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Slider para cambiar la posicion vertical de la imagen
        CustomSlider(
            sliderTextLabel = "Vertical Value",
            sliderValue = { verticalValueSliderPosition },
            sliderValueRange = -10f..10f,
            onSliderValueChange = { newPosition -> verticalValueSliderPosition = newPosition },
            onSliderValueReset = { verticalValueSliderPosition = 0f }
        )

        // Imagen de ejemplo que se va a recortar usando clipPath
        ImageExample(
            verticalValue = imageVerticalValue
        )
    }
}

@Composable
private fun ImageExample(
    verticalValue: Float
) {
    val imageBitmap = ImageBitmap.imageResource(id = R.drawable.jetpack_compose_icon)

    Canvas(modifier = Modifier.size(150.dp)) {
        withTransform(
            // Bloque que contiene las transformaciones que se van a aplicar al DrawScope
            transformBlock = {
                translate(left = size.width * verticalValue)
                rotate(degrees = 360 * verticalValue)
            },
            // Bloque que contiene los elementos que van a formar parte del DrawScope, y que van a ser afectados por las transformaciones definidas
            drawBlock = {
                drawImage(
                    image = imageBitmap,
                    dstSize = IntSize((size.width).toInt(), size.height.toInt())
                )
            }
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun MultipleTransformExamplePreview() {
    AppTheme {
        MultipleTransformExample()
    }
}