package com.example.drawscope.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ui.theme.AppTheme
import com.example.ui.ui.CustomSlider


@Composable
internal fun DrawOutlineExample() {
    // Slider para width y height
    var widthSliderPosition by remember { mutableFloatStateOf(10f) }
    var heightSliderPosition by remember { mutableFloatStateOf(10f) }

    // Valores animables para width y height
    val outlineWidth by animateFloatAsState(
        targetValue = widthSliderPosition / 10,
        label = "Outline Width Animation"
    )
    val outlineHeight by animateFloatAsState(
        targetValue = heightSliderPosition / 10,
        label = "Outline Height Animation"
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Slider para cambiar el ancho del Outline
        CustomSlider(
            sliderTextLabel = "Outline Width",
            sliderValue = { widthSliderPosition },
            sliderValueRange = 0f..10f,
            onSliderValueChange = { newPosition -> widthSliderPosition = newPosition },
            onSliderValueReset = { widthSliderPosition = 10f }
        )

        // Slider para cambiar la altura del Outline
        CustomSlider(
            sliderTextLabel = "Outline Height",
            sliderValue = { heightSliderPosition },
            sliderValueRange = 0f..10f,
            onSliderValueChange = { newPosition -> heightSliderPosition = newPosition },
            onSliderValueReset = { heightSliderPosition = 10f }
        )

        // Outline que se va a dibujar
        OutlineExample(outlineWidth = outlineWidth, outlineHeight = outlineHeight)

        // Boton para reiniciar los dos valores
        OutlinedButton(
            colors = ButtonDefaults.elevatedButtonColors(),
            elevation = ButtonDefaults.elevatedButtonElevation(4.dp),
            onClick = {
                widthSliderPosition = 10f
                heightSliderPosition = 10f
            },
            content = {
                Text(text = "Reiniciar valores")
            }
        )
    }
}

@Composable
private fun OutlineExample(
    outlineWidth: Float,
    outlineHeight: Float
) {
    val outlineColor = MaterialTheme.colorScheme.secondary

    Canvas(
        modifier = Modifier
            .height(150.dp)
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        drawOutline(
            // El borde a dibujar
            outline = Outline.Rounded(
                RoundRect(
                    left = 0f,
                    top = 0f,
                    right = size.width * outlineWidth,
                    bottom = size.height * outlineHeight,
                    cornerRadius = CornerRadius(
                        x = size.width * (outlineWidth * 0.025f),
                        y = size.height * (outlineHeight * 0.025f)
                    )
                )
            ),
            // El color para rellenar el borde
            color = outlineColor,
            // Opcionalmente se puede asiganr un Brush para el color
            //brush = Brush.linearGradient(listOf(primaryColor, secondaryColor)),
            // Opacidad a ser aplicada al borde de 0.0f a 1.0f representando de transparente a opaco respectivamente
            alpha = 1f,
            // Define si el borde esta trazado o rellenado
            style = Stroke(width = size.minDimension * 0.01f),
            //style = Stroke(width = 25f, miter = 2f, cap = StrokeCap.Round, join = StrokeJoin.Round, pathEffect = PathEffect.cornerPathEffect(2f)),
            // ColorFilter a aplicar al color cuando se dibuja el borde
            colorFilter = null,
            // Algoritmo de fusion que se aplicara cuando el ovalo se borde
            blendMode = DrawScope.DefaultBlendMode
        )

        drawCircle(
            color = outlineColor,
            radius = size.minDimension * 0.25f,
            center = Offset(
                x = size.width * (outlineWidth / 2),
                y = size.height * (outlineHeight / 2)
            ),
            alpha = (outlineHeight * outlineWidth)
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun DrawOutlineExamplePreview() {
    AppTheme {
        DrawOutlineExample()
    }
}