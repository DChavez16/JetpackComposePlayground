package com.example.drawscope.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.drawscope.DrawScopeViewModel
import com.example.drawscope.R
import com.example.ui.theme.PreviewAppTheme
import com.example.ui.ui.CompactSizeScreenThemePreview
import com.example.ui.ui.CustomSlider


@Composable
internal fun DrawOutlineExample(
    drawScopeViewModel: DrawScopeViewModel = hiltViewModel()
) {

    // Slider for width and height
    val heightSliderPosition by drawScopeViewModel.drawOutlineHeightSliderPosition.collectAsState()
    val widthSliderPosition by drawScopeViewModel.drawOutlineWidthSliderPosition.collectAsState()

    DrawOutlineExampleContent(
        heightSliderPosition = { heightSliderPosition },
        widthSliderPosition = { widthSliderPosition },
        changeHeightSliderPosition = drawScopeViewModel::changeOutlineHeightSliderPosition,
        changeWidthSliderPosition = drawScopeViewModel::changeOutlineWidthSliderPosition
    )
}


@Composable
private fun DrawOutlineExampleContent(
    heightSliderPosition: () -> Float,
    widthSliderPosition: () -> Float,
    changeHeightSliderPosition: (Float) -> Unit,
    changeWidthSliderPosition: (Float) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Slider to change the width of the Outline
        CustomSlider(
            sliderTextLabel = stringResource(R.string.draw_scope_draw_outline_width),
            sliderValue = widthSliderPosition,
            sliderValueRange = 0f..1f,
            onSliderValueChange = changeWidthSliderPosition,
            onSliderValueReset = { changeWidthSliderPosition(1f) }
        )

        // Slider to change the height of the Outline
        CustomSlider(
            sliderTextLabel = stringResource(R.string.draw_scope_draw_outline_height),
            sliderValue = heightSliderPosition,
            sliderValueRange = 0f..1f,
            onSliderValueChange = changeHeightSliderPosition,
            onSliderValueReset = { changeHeightSliderPosition(1f) }
        )

        // Outline to draw
        OutlineExample(
            heightSliderPosition = heightSliderPosition,
            widthSliderPosition = widthSliderPosition
        )

        // Button to reset the two values
        OutlinedButton(
            colors = ButtonDefaults.elevatedButtonColors(),
            elevation = ButtonDefaults.elevatedButtonElevation(4.dp),
            onClick = {
                changeHeightSliderPosition(1f)
                changeWidthSliderPosition(1f)
            },
            content = {
                Text(text = stringResource(R.string.draw_scope_draw_outline_restart_button_label))
            }
        )
    }
}


@Composable
private fun OutlineExample(
    heightSliderPosition: () -> Float,
    widthSliderPosition: () -> Float
) {

    // Animated values for width and height
    val outlineHeight by animateFloatAsState(
        targetValue = heightSliderPosition(),
        label = "Outline Height Animation"
    )
    val outlineWidth by animateFloatAsState(
        targetValue = widthSliderPosition(),
        label = "Outline Width Animation"
    )

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




@CompactSizeScreenThemePreview
@Composable
private fun DrawOutlineExamplePreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        DrawOutlineExampleContent(
            heightSliderPosition = { 1f },
            widthSliderPosition = { 1f },
            changeHeightSliderPosition = {},
            changeWidthSliderPosition = {}
        )
    }
}