package com.example.drawscope.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.drawscope.DrawScopeViewModel
import com.example.drawscope.R
import com.example.ui.theme.PreviewAppTheme
import com.example.ui.ui.CompactSizeScreenThemePreview
import com.example.ui.ui.CustomSlider


@Composable
internal fun TranslateExample(
    drawScopeViewModel: DrawScopeViewModel = hiltViewModel()
) {

    // SliderPosition for positionX and positionY
    val positionXSliderPosition by drawScopeViewModel.translatePositionXSliderPosition.collectAsState()
    val positionYSliderPosition by drawScopeViewModel.translatePositionYSliderPosition.collectAsState()

    TranslateExampleContent(
        positionXSliderPosition = { positionXSliderPosition },
        positionYSliderPosition = { positionYSliderPosition },
        changePositionXSliderPosition = drawScopeViewModel::changePositionXSliderPosition,
        changePositionYSliderPosition = drawScopeViewModel::changePositionYSliderPosition
    )
}


@Composable
private fun TranslateExampleContent(
    positionXSliderPosition: () -> Float,
    positionYSliderPosition: () -> Float,
    changePositionXSliderPosition: (Float) -> Unit,
    changePositionYSliderPosition: (Float) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Slider for changing the vertical position of the image
        CustomSlider(
            sliderTextLabel = stringResource(R.string.draw_scope_translate_position_x),
            sliderValue = positionXSliderPosition,
            sliderValueRange = -1f..1f,
            onSliderValueChange = changePositionXSliderPosition,
            onSliderValueReset = { changePositionXSliderPosition(0f) }
        )

        // Slider for changing the horizontal position of the image
        CustomSlider(
            sliderTextLabel = stringResource(R.string.draw_scope_translate_position_y),
            sliderValue = positionYSliderPosition,
            sliderValueRange = -1f..1f,
            onSliderValueChange = changePositionYSliderPosition,
            onSliderValueReset = { changePositionYSliderPosition(0f) }
        )

        // Example image to be translated
        ImageExample(
            positionXSliderPosition = positionXSliderPosition,
            positionYSliderPosition = positionYSliderPosition
        )

        // Button for resetting the slider values
        OutlinedButton(
            colors = ButtonDefaults.elevatedButtonColors(),
            elevation = ButtonDefaults.elevatedButtonElevation(4.dp),
            onClick = {
                changePositionXSliderPosition(0f)
                changePositionYSliderPosition(0f)
            },
            content = {
                Text(text = stringResource(R.string.draw_scope_translate_restart_button_label))
            }
        )
    }
}


@Composable
private fun ImageExample(
    positionXSliderPosition : () -> Float,
    positionYSliderPosition : () -> Float
) {

    // Animated values for positionX and positionY
    val imagePositionX by animateFloatAsState(
        targetValue = positionXSliderPosition(),
        label = "Image Position X Animation"
    )
    val imagePositionY by animateFloatAsState(
        targetValue = positionYSliderPosition(),
        label = "Image Position Y Animation"
    )

    val imageBitmap = ImageBitmap.imageResource(id = R.drawable.jetpack_compose_icon)

    Canvas(modifier = Modifier.size(150.dp)) {
        translate(
            // Pixeles a trasladar el espacio de coordenadas en el eje x
            left = size.width * imagePositionX,
            // Pixeles a trasladar el espacio de coordenadas en el eje y
            top = size.height * imagePositionY
        ) {
            drawImage(
                image = imageBitmap,
                dstSize = IntSize((size.width).toInt(), size.height.toInt())
            )
        }
    }
}




@CompactSizeScreenThemePreview
@Composable
private fun TranslateExamplePreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        TranslateExampleContent(
            positionXSliderPosition = { 0f },
            positionYSliderPosition = { 0f },
            changePositionXSliderPosition = {},
            changePositionYSliderPosition = {}
        )
    }
}