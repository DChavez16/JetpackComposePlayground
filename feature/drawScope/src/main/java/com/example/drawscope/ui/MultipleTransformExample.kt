package com.example.drawscope.ui

import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.withTransform
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
internal fun MultipleTransformExample(
    drawScopeViewModel: DrawScopeViewModel = hiltViewModel()
) {

    // SliderPosition for verticalValue
    val horizontalValueSliderPosition by drawScopeViewModel.horizontalValueSliderPosition.collectAsState()

    MultipleTransformExampleContent(
        horizontalValueSliderPosition = { horizontalValueSliderPosition },
        changeHorizontalValueSliderPosition = drawScopeViewModel::changeHorizontalValueSliderPosition
    )
}


@Composable
private fun MultipleTransformExampleContent(
    horizontalValueSliderPosition: () -> Float,
    changeHorizontalValueSliderPosition: (Float) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Slider for changing the horizontal position of the image
        CustomSlider(
            sliderTextLabel = stringResource(R.string.draw_scope_multiple_transformations_horizontal_value),
            sliderValue = horizontalValueSliderPosition,
            sliderValueRange = -1f..1f,
            onSliderValueChange = changeHorizontalValueSliderPosition,
            onSliderValueReset = { changeHorizontalValueSliderPosition(0f) }
        )

        // Example image to apply multiple transformations
        ImageExample(
            horizontalValueSliderPosition = horizontalValueSliderPosition
        )
    }
}


@Composable
private fun ImageExample(
    horizontalValueSliderPosition: () -> Float
) {

    // Animated values for verticalValue
    val imageHorizontalValue by animateFloatAsState(
        targetValue = horizontalValueSliderPosition(),
        animationSpec = tween(
            durationMillis = 500,
            easing = EaseOutBack
        ),
        label = "ImageHorizontalValueAnimation"
    )

    val imageBitmap = ImageBitmap.imageResource(id = R.drawable.jetpack_compose_icon)

    Canvas(modifier = Modifier.size(150.dp)) {
        withTransform(
            // Bloque que contiene las transformaciones que se van a aplicar al DrawScope
            transformBlock = {
                translate(left = size.width * imageHorizontalValue)
                rotate(degrees = 360 * imageHorizontalValue)
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




@CompactSizeScreenThemePreview
@Composable
private fun MultipleTransformExampleContentPreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        MultipleTransformExampleContent(
            horizontalValueSliderPosition = { 0f },
            changeHorizontalValueSliderPosition = {}
        )
    }
}