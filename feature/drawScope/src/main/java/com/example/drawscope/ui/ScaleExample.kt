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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.scale
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
internal fun ScaleExample(
    drawScopeViewModel: DrawScopeViewModel = hiltViewModel()
) {

    // SliderPosition for scaleX and scaleY
    val scaleXSliderPosition by drawScopeViewModel.scaleXSliderPosition.collectAsState()
    val scaleYSliderPosition by drawScopeViewModel.scaleYSliderPosition.collectAsState()

    ScaleExampleContent(
        scaleXSliderPosition = { scaleXSliderPosition },
        scaleYSliderPosition = { scaleYSliderPosition },
        changeScaleXSliderPosition = drawScopeViewModel::changeScaleXSliderPosition,
        changeScaleYSliderPosition = drawScopeViewModel::changeScaleYSliderPosition
    )
}


@Composable
private fun ScaleExampleContent(
    scaleXSliderPosition: () -> Float,
    scaleYSliderPosition: () -> Float,
    changeScaleXSliderPosition: (Float) -> Unit,
    changeScaleYSliderPosition: (Float) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Slider for changing the vertical position of the image
        CustomSlider(
            sliderTextLabel = stringResource(R.string.draw_scope_scale_scale_x),
            sliderValue = scaleXSliderPosition,
            sliderValueRange = 0f..1f,
            onSliderValueChange = changeScaleXSliderPosition,
            onSliderValueReset = { changeScaleXSliderPosition(1f) }
        )

        // Slider for changing the horizontal position of the image
        CustomSlider(
            sliderTextLabel = stringResource(R.string.draw_scope_scale_scale_y),
            sliderValue = scaleYSliderPosition,
            sliderValueRange = 0f..1f,
            onSliderValueChange = changeScaleYSliderPosition,
            onSliderValueReset = { changeScaleYSliderPosition(1f) }
        )

        // Example image to be scaled
        ImageExample(
            scaleXSliderPosition = scaleXSliderPosition,
            scaleYSliderPosition = scaleYSliderPosition
        )

        // Button for resetting the slider values
        OutlinedButton(
            colors = ButtonDefaults.elevatedButtonColors(),
            elevation = ButtonDefaults.elevatedButtonElevation(4.dp),
            onClick = {
                changeScaleXSliderPosition(1f)
                changeScaleYSliderPosition(1f)
            },
            content = {
                Text(text = stringResource(R.string.draw_scope_scale_restart_button_label))
            }
        )
    }
}


@Composable
private fun ImageExample(
    scaleXSliderPosition : () -> Float,
    scaleYSliderPosition : () -> Float
) {

    // Animated values for scaleX and scaleY
    val imageScaleX by animateFloatAsState(
        targetValue = scaleXSliderPosition(),
        label = "ImageScaleXAnimation"
    )
    val imageScaleY by animateFloatAsState(
        targetValue = scaleYSliderPosition(),
        label = "ImageScaleYAnimation"
    )

    val imageBitmap = ImageBitmap.imageResource(id = R.drawable.jetpack_compose_icon)

    Canvas(modifier = Modifier.size(150.dp)) {
        scale(
            // La cantidad de escala en X
            scaleX = imageScaleX,
            // La cantidad de escala en Y
            scaleY = imageScaleY,
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




@CompactSizeScreenThemePreview
@Composable
private fun ScaleExampleContentPreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        ScaleExampleContent(
            scaleXSliderPosition = { 1f },
            scaleYSliderPosition = { 1f },
            changeScaleXSliderPosition = {},
            changeScaleYSliderPosition = {}
        )
    }
}