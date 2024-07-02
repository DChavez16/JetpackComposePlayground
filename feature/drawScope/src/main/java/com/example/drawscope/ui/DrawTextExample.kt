package com.example.drawscope.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.drawscope.DrawScopeViewModel
import com.example.drawscope.R
import com.example.drawscope.getExampleText
import com.example.ui.theme.PreviewAppTheme
import com.example.ui.ui.CompactSizeScreenThemePreview
import com.example.ui.ui.CustomSlider


@Composable
internal fun DrawTextExample(
    drawScopeViewModel: DrawScopeViewModel = hiltViewModel()
) {

    // Slider to change maxWidth and maxHeight
    val maxWidthSliderPosition by drawScopeViewModel.drawTextMaxWidthSliderPosition.collectAsState()
    val maxHeightSliderPosition by drawScopeViewModel.drawTextMaxHeightSliderPosition.collectAsState()

    // Text example
    val textExample by drawScopeViewModel.drawTextExampleText.collectAsState()

    DrawTextExampleContent(
        maxWidthSliderPosition = { maxWidthSliderPosition },
        maxHeightSliderPosition = { maxHeightSliderPosition },
        textExample = { textExample },
        changeMaxWidthSliderPosition = drawScopeViewModel::changeTextMaxWidthSliderPosition,
        changeMaxHeightSliderPosition = drawScopeViewModel::changeTextMaxHeightSliderPosition,
        regenerateTextExample = drawScopeViewModel::regenerateTextExample
    )
}


@Composable
private fun DrawTextExampleContent(
    maxWidthSliderPosition: () -> Float,
    maxHeightSliderPosition: () -> Float,
    textExample: () -> String,
    changeMaxWidthSliderPosition: (Float) -> Unit,
    changeMaxHeightSliderPosition: (Float) -> Unit,
    regenerateTextExample: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        // Slider to change maxWidth
        CustomSlider(
            sliderTextLabel = stringResource(R.string.draw_scope_draw_text_max_width),
            sliderValue = maxWidthSliderPosition,
            sliderValueRange = 0.2f..1f,
            onSliderValueChange = changeMaxWidthSliderPosition,
            onSliderValueReset = { changeMaxWidthSliderPosition(0.75f) }
        )

        // Slider to change maxHeight
        CustomSlider(
            sliderTextLabel = stringResource(R.string.draw_scope_draw_text_max_height),
            sliderValue = maxHeightSliderPosition,
            sliderValueRange = 0.2f..1f,
            onSliderValueChange = changeMaxHeightSliderPosition,
            onSliderValueReset = { changeMaxHeightSliderPosition(0.75f) }
        )

        // Example text
        TextExample(
            maxWidthSliderPosition = maxWidthSliderPosition,
            maxHeightSliderPosition = maxHeightSliderPosition,
            textExample = textExample
        )

        // Buttons to change the number of characters in the text example and reset the maximum text layout size
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(
                colors = ButtonDefaults.elevatedButtonColors(),
                elevation = ButtonDefaults.elevatedButtonElevation(4.dp),
                onClick = regenerateTextExample,
                content = {
                    Text(text = stringResource(R.string.draw_scope_draw_text_change_text_button_label))
                }
            )

            OutlinedButton(
                colors = ButtonDefaults.elevatedButtonColors(),
                elevation = ButtonDefaults.elevatedButtonElevation(4.dp),
                onClick = {
                    changeMaxWidthSliderPosition(0.75f)
                    changeMaxHeightSliderPosition(0.75f)
                },
                content = {
                    Text(text = stringResource(R.string.draw_scope_draw_text_restart_button_label))
                }
            )
        }
    }
}


@Composable
private fun TextExample(
    maxWidthSliderPosition: () -> Float,
    maxHeightSliderPosition: () -> Float,
    textExample: () -> String
) {

    // Animated values for maxWidth and maxHeight
    val textLayoutMaxWidth by animateFloatAsState(
        targetValue = maxWidthSliderPosition(),
        label = "Text Layout Max Width Animation"
    )
    val textLayoutMaxHeight by animateFloatAsState(
        targetValue = maxHeightSliderPosition(),
        label = "Text Layout Max Height Animation"
    )

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
            text = textExample(),
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




@CompactSizeScreenThemePreview
@Composable
private fun DrawTextExamplePreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        DrawTextExampleContent(
            maxWidthSliderPosition = { 0.75f },
            maxHeightSliderPosition = { 0.75f },
            textExample = { getExampleText() },
            changeMaxWidthSliderPosition = {},
            changeMaxHeightSliderPosition = {},
            regenerateTextExample = {}
        )
    }
}