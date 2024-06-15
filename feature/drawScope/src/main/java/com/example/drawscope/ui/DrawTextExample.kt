package com.example.drawscope.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.example.ui.theme.AppTheme
import com.example.ui.ui.CustomSlider
import kotlin.random.Random


@Composable
internal fun DrawTextExample() {
    // Slider para cambiar el maxWidth y maxHeight
    var maxWidthSliderPosition by remember { mutableFloatStateOf(7.5f) }
    var maxHeightSliderPosition by remember { mutableFloatStateOf(7.5f) }

    // Valor animable de maxWidth y maxHeight
    val textLayoutMaxWidth by animateFloatAsState(
        targetValue = maxWidthSliderPosition / 10,
        label = "Text Layout Max Width Animation"
    )
    val textLayoutMaxHeight by animateFloatAsState(
        targetValue = maxHeightSliderPosition / 10,
        label = "Text Layout Max Height Animation"
    )

    var textExample by remember { mutableStateOf(getExampleText()) }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        // Slider para cambiar el ancho maximo del textLayout
        CustomSlider(
            sliderTextLabel = "Max Width",
            sliderValue = { maxWidthSliderPosition },
            sliderValueRange = 2f..10f,
            onSliderValueChange = { newPosition -> maxWidthSliderPosition = newPosition },
            onSliderValueReset = { maxWidthSliderPosition = 7.5f }
        )

        // Slider para cambiar la altura maxima del textLayout
        CustomSlider(
            sliderTextLabel = "Max Height",
            sliderValue = { maxHeightSliderPosition },
            sliderValueRange = 2f..10f,
            onSliderValueChange = { newPosition -> maxHeightSliderPosition = newPosition },
            onSliderValueReset = { maxHeightSliderPosition = 7.5f }
        )

        // Texto de ejemplo
        TextExample(
            textExample = textExample,
            textLayoutMaxWidth = textLayoutMaxWidth,
            textLayoutMaxHeight = textLayoutMaxHeight
        )

        // Botones para cambiar la cantidad de caracteres en el texto de prueba y reinicar el tamaño maximo del textLayout
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(
                colors = ButtonDefaults.elevatedButtonColors(),
                elevation = ButtonDefaults.elevatedButtonElevation(4.dp),
                onClick = {
                    textExample = getExampleText()
                },
                content = {
                    Text(text = "Cambiar texto")
                }
            )

            OutlinedButton(
                colors = ButtonDefaults.elevatedButtonColors(),
                elevation = ButtonDefaults.elevatedButtonElevation(4.dp),
                onClick = {
                    maxWidthSliderPosition = 7.5f
                    maxHeightSliderPosition = 7.5f
                },
                content = {
                    Text(text = "Reiniciar valores")
                }
            )
        }
    }
}

@Composable
private fun TextExample(
    textExample: String,
    textLayoutMaxWidth: Float,
    textLayoutMaxHeight: Float
) {
    val textMeasurer = rememberTextMeasurer()

    val backgroundPrimaryColor = MaterialTheme.colorScheme.primary
    val textPrimaryColor = MaterialTheme.colorScheme.onPrimary
    val textExampleTypography = MaterialTheme.typography.bodyMedium

    Canvas(
        modifier = Modifier
            .height(150.dp)
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        val measuredText = textMeasurer.measure(
            // El texto a ser dibujad
            text = textExample,
            // El estilo a ser aplicado a el texto
            style = textExampleTypography,
            // Como el desborde visual debe ser tratado: Ellipsis, Clip o Visible
            overflow = TextOverflow.Ellipsis,
            // Indica si el texto debe ser envuelto
            softWrap = true,
            // Un numero opcional de lineas maximas para el texto
            //maxLines = 5,
            // Indica que tan ancho o largo puede o debe ser el texto
            constraints = Constraints(
                maxWidth = (size.width * textLayoutMaxWidth).toInt(),
                maxHeight = (size.height * textLayoutMaxHeight).toInt()
            ),
            // Direccion de la medicion del layout: Ltr o Rtl
            layoutDirection = LayoutDirection.Ltr,
            // Densidad del entorno de medicion
            //density = Density(0.15f, 20f),
            // Desabilita la optimizacion del cache si el valor indicado es true
            skipCache = false
        )

        drawRect(
            color = backgroundPrimaryColor,
            topLeft = Offset(
                x = size.width / 2 - (measuredText.size.width / 2),
                y = size.height / 2 - (measuredText.size.height / 2)
            ),
            size = measuredText.size.toSize()
        )

        drawText(
            // Layout del texto que se va a dibujar
            textLayoutResult = measuredText,
            // Color del texto a usar
            color = textPrimaryColor,
            // Coordenadas del texto desde el punto superior izquierdo del sistema de coordenadas actuales
            topLeft = Offset(
                x = size.width / 2 - (measuredText.size.width / 2),
                y = size.height / 2 - (measuredText.size.height / 2)
            ),
            // Opacidad a ser aplicada texto de 0.0f a 1.0f representando de transparente a opaco respectivamente
            alpha = 1f,
            // Efecto de sombra a ser aplicada al texto, las coordenadas proporcionadas tienen como punto de origen la esquina superior izquerda del texto
            shadow = Shadow(
                color = Color.Black,
                offset = Offset(
                    x = 2.5f,
                    y = 2.5f
                ),
                blurRadius = 10f
            ),
            // Decoraciones a pintar en el texto: None, Underline o LineThrough
            textDecoration = TextDecoration.None,
            // Define si el texto esta trazado o rellenado
            //drawStyle = Stroke(0.1f),
            // Algoritmo de fusion que se aplicara cuando el texto se dibuja
            blendMode = DrawScope.DefaultBlendMode
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun DrawTextExamplePreview() {
    AppTheme {
        DrawTextExample()
    }
}


private fun getExampleText(): String {
    val numDeCaracteres = Random.nextInt(0, exampleString.length)

    return exampleString.substring(0, numDeCaracteres)
}

private const val exampleString =
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec quis ipsum non sapien vulputate aliquet pharetra elementum tortor. Phasellus consectetur posuere erat eu sollicitudin. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam erat volutpat. In rutrum cursus tincidunt. Ut lectus erat, dignissim sed turpis quis, varius aliquet eros. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus."