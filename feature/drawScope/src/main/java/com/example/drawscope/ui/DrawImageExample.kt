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
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.drawscope.DrawScopeViewModel
import com.example.drawscope.R
import com.example.ui.theme.PreviewAppTheme
import com.example.ui.ui.CompactSizeScreenThemePreview
import com.example.ui.ui.CustomSlider
import kotlin.math.absoluteValue


@Composable
internal fun DrawImageExample(
    drawScopeViewModel: DrawScopeViewModel = hiltViewModel()
) {

    // SliderPositions for width, height, positionX and positionY
    val widthSliderPosition by drawScopeViewModel.drawImageWidthSliderPosition.collectAsState()
    val heightSliderPosition by drawScopeViewModel.drawImageHeightSliderPosition.collectAsState()
    val positionXSliderPosition by drawScopeViewModel.drawImagePositionXSliderPosition.collectAsState()
    val positionYSliderPosition by drawScopeViewModel.drawImagePositionYSliderPosition.collectAsState()

    DrawImageExampleContent(
        widthSliderPosition = { widthSliderPosition },
        heightSliderPosition = { heightSliderPosition },
        positionXSliderPosition = { positionXSliderPosition },
        positionYSliderPosition = { positionYSliderPosition },
        changeWidthSliderPosition = drawScopeViewModel::changeDrawImageWidthSliderPosition,
        changeHeightSliderPosition = drawScopeViewModel::changeDrawImageHeightSliderPosition,
        changePositionXSliderPosition = drawScopeViewModel::changeDrawImagePositionXSliderPosition,
        changePositionYSliderPosition = drawScopeViewModel::changeDrawImagePositionYSliderPosition
    )
}


@Composable
private fun DrawImageExampleContent(
    widthSliderPosition: () -> Float,
    heightSliderPosition: () -> Float,
    positionXSliderPosition: () -> Float,
    positionYSliderPosition: () -> Float,
    changeWidthSliderPosition: (Float) -> Unit,
    changeHeightSliderPosition: (Float) -> Unit,
    changePositionXSliderPosition: (Float) -> Unit,
    changePositionYSliderPosition: (Float) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Slider for image width
        CustomSlider(
            sliderTextLabel = stringResource(R.string.draw_scope_draw_image_width),
            sliderValue = widthSliderPosition,
            sliderValueRange = 0f..1f,
            onSliderValueChange = changeWidthSliderPosition,
            onSliderValueReset = { changeWidthSliderPosition(1f) }
        )

        // Slider for image height
        CustomSlider(
            sliderTextLabel = stringResource(R.string.draw_scope_draw_image_height),
            sliderValue = heightSliderPosition,
            sliderValueRange = 0f..1f,
            onSliderValueChange = changeHeightSliderPosition,
            onSliderValueReset = { changeHeightSliderPosition(1f) }
        )

        // Slider for image position X
        CustomSlider(
            sliderTextLabel = stringResource(R.string.draw_scope_draw_image_position_x),
            sliderValue = positionXSliderPosition,
            sliderValueRange = -1f..1f,
            onSliderValueChange = changePositionXSliderPosition,
            onSliderValueReset = { changePositionXSliderPosition(0f) }
        )

        // Slider for image position Y
        CustomSlider(
            sliderTextLabel = stringResource(R.string.draw_scope_draw_image_position_y),
            sliderValue = positionYSliderPosition,
            sliderValueRange = -1f..1f,
            onSliderValueChange = changePositionYSliderPosition,
            onSliderValueReset = { changePositionYSliderPosition(0f) }
        )

        // Image to be drawn
        ImageExample(
            widthSliderPosition = widthSliderPosition,
            heightSliderPosition = heightSliderPosition,
            positionXSliderPosition = positionXSliderPosition,
            positionYSliderPosition = positionYSliderPosition
        )

        // Button for resetting the values of the sliders
        OutlinedButton(
            colors = ButtonDefaults.elevatedButtonColors(),
            elevation = ButtonDefaults.elevatedButtonElevation(4.dp),
            onClick = {
                changeWidthSliderPosition(1f)
                changeHeightSliderPosition(1f)
                changePositionXSliderPosition(0f)
                changePositionYSliderPosition(0f)
            },
            content = {
                Text(text = stringResource(R.string.draw_scope_draw_image_restart_button_label))
            }
        )
    }
}


@Composable
private fun ImageExample(
    widthSliderPosition: () -> Float,
    heightSliderPosition: () -> Float,
    positionXSliderPosition: () -> Float,
    positionYSliderPosition: () -> Float
) {

    // Animated values for width, height, positionX and positionY
    val imageWidth by animateFloatAsState(
        targetValue = widthSliderPosition(),
        label = "ImageWidthAnimation"
    )
    val imageHeight by animateFloatAsState(
        targetValue = heightSliderPosition(),
        label = "ImageHeightAnimation"
    )
    val imagePositionX by animateFloatAsState(
        targetValue = positionXSliderPosition(),
        label = "ImagePositionXAnimation"
    )
    val imagePositionY by animateFloatAsState(
        targetValue = positionYSliderPosition(),
        label = "ImagePositionYAnimation"
    )

    val imageBitmap = ImageBitmap.imageResource(id = R.drawable.jetpack_compose_icon)

    Canvas(modifier = Modifier.size(150.dp)) {
        drawImage(
            // La imagen fuente a ser dibujada
            image = imageBitmap,
            // Coordenadas opcionales representando las coordenadas superiores izquierdas de la imagen
            srcOffset = IntOffset.Zero,
            // Dimensiones opcionales de la imagen fuente a dibujar
            srcSize = IntSize(
                width = (imageBitmap.width / imageWidth).toInt(),
                height = (imageBitmap.height / imageHeight).toInt(),
            ),
            // Coordenadas opcionales representando las coordenadas de destino de la imagen que se va a dibujar
            dstOffset = IntOffset(
                x = (size.width * imagePositionX).toInt(),
                y = (size.height * imagePositionY).toInt()
            ),
            // Dimensmiones opcionales del destino que se va a dibujar
            dstSize = IntSize((size.width).toInt(), size.height.toInt()),
            // Opacidad a ser aplicada a la imagen de 0.0f a 1.0f representando de transparente a opaco respectivamente
            alpha = 1 - (imagePositionX.absoluteValue + imagePositionY.absoluteValue) / 2,
            // Define si la imagen esta trazado o rellenado
            style = Fill,
            //style = Stroke(width = 25f, miter = 2f, cap = StrokeCap.Round, join = StrokeJoin.Round, pathEffect = PathEffect.cornerPathEffect(2f)),
            // ColorFilter a aplicar al color cuando se dibuja la imagen
            colorFilter = null,
            // Algoritmo de fusion que se aplicara cuando la imagen se dibuja
            blendMode = DrawScope.DefaultBlendMode,
            // Filtro de calidad aplicado a la imagen cuando es escalada y dibujada en el destino: None, Low, Medium o High
            filterQuality = FilterQuality.Medium
        )
    }
}




@CompactSizeScreenThemePreview
@Composable
private fun DrawImageExamplePreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        DrawImageExampleContent(
            widthSliderPosition = { 1f },
            heightSliderPosition = { 1f },
            positionXSliderPosition = { 0f },
            positionYSliderPosition = { 0f },
            changeWidthSliderPosition = {},
            changeHeightSliderPosition = {},
            changePositionXSliderPosition = {},
            changePositionYSliderPosition = {}
        )
    }
}