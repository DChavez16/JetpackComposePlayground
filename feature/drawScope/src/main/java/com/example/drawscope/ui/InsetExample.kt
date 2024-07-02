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
import androidx.compose.ui.graphics.drawscope.inset
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
internal fun InsetExample(
    drawScopeViewModel: DrawScopeViewModel = hiltViewModel()
) {

    // SliderPosition for verticalScale and horizontalScale
    val verticalScaleSliderPosition by drawScopeViewModel.insetVerticalScaleSliderPosition.collectAsState()
    val horizontalScaleSliderPosition by drawScopeViewModel.insetHorizontalScaleSliderPosition.collectAsState()


    InsetExampleContent(
        verticalScaleSliderPosition = { verticalScaleSliderPosition },
        horizontalScaleSliderPosition = { horizontalScaleSliderPosition },
        changeVerticalScaleSliderPosition = drawScopeViewModel::changeInsetVerticalScaleSliderPosition,
        changeHorizontalScaleSliderPosition = drawScopeViewModel::changeInsetHorizontalScaleSliderPosition
    )
}


@Composable
private fun InsetExampleContent(
    verticalScaleSliderPosition: () -> Float,
    horizontalScaleSliderPosition: () -> Float,
    changeVerticalScaleSliderPosition: (Float) -> Unit,
    changeHorizontalScaleSliderPosition: (Float) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Slider for changing the vertical position of the image
        CustomSlider(
            sliderTextLabel = stringResource(R.string.draw_scope_inset_vertical_scale),
            sliderValue = verticalScaleSliderPosition,
            sliderValueRange = -0.5f..0.5f,
            onSliderValueChange = changeVerticalScaleSliderPosition,
            onSliderValueReset = { changeVerticalScaleSliderPosition(0f) }
        )

        // Slider for changing the horizontal position of the image
        CustomSlider(
            sliderTextLabel = stringResource(R.string.draw_scope_inset_horizontal_scale),
            sliderValue = horizontalScaleSliderPosition,
            sliderValueRange = -0.5f..0.5f,
            onSliderValueChange = changeHorizontalScaleSliderPosition,
            onSliderValueReset = { changeHorizontalScaleSliderPosition(0f) }
        )

        // Example image that will be clipped using clipPath
        ImageExample(
            verticalScaleSliderPosition = verticalScaleSliderPosition,
            horizontalScaleSliderPosition = horizontalScaleSliderPosition
        )

        // Button for resetting the values of the sliders
        OutlinedButton(
            colors = ButtonDefaults.elevatedButtonColors(),
            elevation = ButtonDefaults.elevatedButtonElevation(4.dp),
            onClick = {
                changeVerticalScaleSliderPosition(0f)
                changeHorizontalScaleSliderPosition(0f)
            },
            content = {
                Text(text = stringResource(R.string.draw_scope_inset_restart_button_label))
            }
        )
    }
}


@Composable
private fun ImageExample(
    verticalScaleSliderPosition: () -> Float,
    horizontalScaleSliderPosition: () -> Float
) {

    // Animated values for verticalScale and horizontalScale
    val imageVerticalScale by animateFloatAsState(
        targetValue = verticalScaleSliderPosition(),
        label = "ImageVerticalScaleAnimation"
    )
    val imageHorizontalScale by animateFloatAsState(
        targetValue = horizontalScaleSliderPosition(),
        label = "ImageHorizontalScaleAnimation"
    )

    val imageBitmap = ImageBitmap.imageResource(id = R.drawable.jetpack_compose_icon)

    Canvas(modifier = Modifier.size(150.dp)) {
        inset(
            // Numero de pixeles para encuadrar el limite izquierdo del dibujo
            left = size.width * imageHorizontalScale,
            // Numero de pixeles para encuadrar el limite superior del dibujo
            top = size.height * imageVerticalScale,
            // Numero de pixeles para encuadrar el limite derecho del dibujo
            right = size.width * imageHorizontalScale,
            // Numero de pixeles para encuadrar el limite inferior del dibujo
            bottom = size.height * imageVerticalScale,
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
private fun InsetExampleContentPreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        InsetExampleContent(
            verticalScaleSliderPosition = { 0f },
            horizontalScaleSliderPosition = { 0f },
            changeVerticalScaleSliderPosition = {},
            changeHorizontalScaleSliderPosition = {}
        )
    }
}