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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.drawscope.R
import com.example.ui.theme.AppTheme
import com.example.ui.ui.CustomDropdownMenu
import com.example.ui.ui.CustomSlider


@Composable
internal fun ClipRectExample() {
    // SliderPosition para clipScale
    var clipScaleSliderPosition by remember { mutableFloatStateOf(10f) }

    // Valor animable para clipScale
    val imageClipScale by animateFloatAsState(
        targetValue = clipScaleSliderPosition / 10,
        label = "Image Clip Scale Animation"
    )

    var selectedClipOperation by remember { mutableStateOf(ClipOperationClipRect.Intersect) }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Slider para cambiar la posicion de recorte de la imagen
        CustomSlider(
            sliderTextLabel = "Clip Scale",
            sliderValue = { clipScaleSliderPosition },
            sliderValueRange = 5f..10f,
            onSliderValueChange = { newPosition -> clipScaleSliderPosition = newPosition },
            onSliderValueReset = { clipScaleSliderPosition = 10f }
        )

        // Dropdown menu para cambiar la operacion de recorte
        CustomDropdownMenu(
            dropdownMenuLabel = "Clip Operation",
            currentElementDisplay = selectedClipOperation.clipOperationName,
            optionsList = ClipOperationClipRect.entries.map { clipOperation -> clipOperation.clipOperationName },
            onElementSelected = { elementSelected ->
                selectedClipOperation =
                    ClipOperationClipRect.entries.find { it.clipOperationName == elementSelected }!!
            }
        )

        // Imagen de ejemplo que se va a recortar usando clipPath
        ImageExample(
            clipScale = imageClipScale,
            selectedClipOperation = selectedClipOperation.clipOperation
        )

        // Boton para reiniciar los valores de los sliders
        OutlinedButton(
            colors = ButtonDefaults.elevatedButtonColors(),
            elevation = ButtonDefaults.elevatedButtonElevation(4.dp),
            onClick = {
                clipScaleSliderPosition = 10f
                selectedClipOperation = ClipOperationClipRect.Intersect
            },
            content = {
                Text(text = "Reiniciar valores")
            }
        )
    }
}

@Composable
private fun ImageExample(
    clipScale: Float,
    selectedClipOperation: ClipOp
) {
    val imageBitmap = ImageBitmap.imageResource(id = R.drawable.jetpack_compose_icon)

    Canvas(modifier = Modifier.size(150.dp)) {
        clipRect(
            // Limite izquierdo del rectangulo para recortar
            left = size.width * (1 - clipScale),
            // Limite superior del rectangulo para recortar
            top = size.height * (1 - clipScale),
            // Limite derecho del rectangulo para recortar
            right = size.width * clipScale,
            // Limite inferior del rectangulo para recortar
            bottom = size.height * clipScale,
            // Operacion de recorte a realizar en los limites dados: Intersect o Difference
            clipOp = selectedClipOperation
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
private fun ClipRectExamplePreview() {
    AppTheme {
        ClipRectExample()
    }
}


private enum class ClipOperationClipRect(val clipOperationName: String, val clipOperation: ClipOp) {
    Intersect("Intersect", ClipOp.Intersect),
    Difference("Difference", ClipOp.Difference);
}