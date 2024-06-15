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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.drawscope.R
import com.example.ui.theme.AppTheme
import com.example.ui.ui.CustomDropdownMenu
import com.example.ui.ui.CustomSlider


@Composable
internal fun ClipPathExample() {
    // SliderPosition para clipPosition
    var clipPositionSliderPosition by remember { mutableFloatStateOf(0f) }

    // Valor animable para clipPosition
    val imageClipPosition by animateFloatAsState(
        targetValue = clipPositionSliderPosition / 10,
        label = "Image Clip Position Animation"
    )

    var selectedClipOperation by remember { mutableStateOf(ClipOperationClipPath.Intersect) }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Slider para cambiar la posicion de recorte de la imagen
        CustomSlider(
            sliderTextLabel = "Clip Position",
            sliderValue = { clipPositionSliderPosition },
            sliderValueRange = 0f..10f,
            onSliderValueChange = { newPosition -> clipPositionSliderPosition = newPosition },
            onSliderValueReset = { clipPositionSliderPosition = 0f }
        )

        // Dropdown menu para cambiar la operacion de recorte
        CustomDropdownMenu(
            dropdownMenuLabel = "Clip Operation",
            currentElementDisplay = selectedClipOperation.clipOperationName,
            optionsList = ClipOperationClipPath.entries.map { clipOperation -> clipOperation.clipOperationName },
            onElementSelected = { elementSelected ->
                selectedClipOperation =
                    ClipOperationClipPath.entries.find { it.clipOperationName == elementSelected }!!
            }
        )

        // Imagen de ejemplo que se va a recortar usando clipPath
        ImageExample(
            clipPosition = imageClipPosition,
            selectedClipOperation = selectedClipOperation.clipOperation
        )

        // Boton para reiniciar los valores de los sliders
        OutlinedButton(
            colors = ButtonDefaults.elevatedButtonColors(),
            elevation = ButtonDefaults.elevatedButtonElevation(4.dp),
            onClick = {
                clipPositionSliderPosition = 0f
                selectedClipOperation = ClipOperationClipPath.Intersect
            },
            content = {
                Text(text = "Reiniciar valores")
            }
        )
    }
}

@Composable
private fun ImageExample(
    clipPosition: Float,
    selectedClipOperation: ClipOp
) {
    val imageBitmap = ImageBitmap.imageResource(id = R.drawable.jetpack_compose_icon)

    Canvas(modifier = Modifier.size(150.dp)) {
        clipPath(
            // Forma (definida por una ruta) a recortar el contenido de dibujo dentro
            path = Path().apply {
                moveTo(0f, 0f)
                lineTo(size.width, 0f)
                lineTo(size.width, size.height)
                lineTo(
                    x = size.width * (if(clipPosition > 0.5) 1f else clipPosition * 2),
                    y = size.height * (if(clipPosition < 0.5) 1f else 1f-((clipPosition-0.5f)*2f))
                )
                close()
            },
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
private fun ClipPathExamplePreview() {
    AppTheme {
        ClipPathExample()
    }
}


private enum class ClipOperationClipPath(val clipOperationName: String, val clipOperation: ClipOp) {
    Intersect("Intersect", ClipOp.Intersect),
    Difference("Difference", ClipOp.Difference);
}