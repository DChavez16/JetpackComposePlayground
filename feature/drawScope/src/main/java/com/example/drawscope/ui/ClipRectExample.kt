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
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.drawscope.ClipOperation
import com.example.drawscope.DrawScopeViewModel
import com.example.drawscope.R
import com.example.ui.theme.PreviewAppTheme
import com.example.ui.ui.CompactSizeScreenThemePreview
import com.example.ui.ui.CustomDropdownMenu
import com.example.ui.ui.CustomSlider


@Composable
internal fun ClipRectExample(
    drawScopeViewModel: DrawScopeViewModel = hiltViewModel()
) {

    // SilderPosition for clipScale
    val clipScaleSliderPosition by drawScopeViewModel.clipScaleSliderPosition.collectAsState()
    // Selected clip operation
    val selectedClipOperation by drawScopeViewModel.selectedClipRectOperation.collectAsState()

    ClipRectExampleContent(
        clipScaleSliderPosition = { clipScaleSliderPosition },
        selectedClipOperation = { selectedClipOperation },
        changeClipScaleSliderPosition = drawScopeViewModel::changeClipScaleSliderPosition,
        changeSelectedClipOperation = drawScopeViewModel::changeSelectedClipRectOperation
    )
}


@Composable
private fun ClipRectExampleContent(
    clipScaleSliderPosition: () -> Float,
    selectedClipOperation: () -> ClipOperation,
    changeClipScaleSliderPosition: (Float) -> Unit,
    changeSelectedClipOperation: (ClipOperation) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Slider for changing the clip position of the image
        CustomSlider(
            sliderTextLabel = stringResource(R.string.draw_scope_clip_rect_clip_scale),
            sliderValue = clipScaleSliderPosition,
            sliderValueRange = 0.5f..1f,
            onSliderValueChange = changeClipScaleSliderPosition,
            onSliderValueReset = { changeClipScaleSliderPosition(1f) }
        )

        // Dropdown menu for changing the clip operation
        CustomDropdownMenu(
            dropdownMenuLabel = stringResource(R.string.draw_scope_clip_rect_dropdown_menu_label),
            currentElementDisplay = { selectedClipOperation().clipOperationName },
            optionsList = ClipOperation.entries.map { clipOperation -> clipOperation.clipOperationName },
            onElementSelected = { elementSelected ->
                changeSelectedClipOperation(ClipOperation.entries.find { it.clipOperationName == elementSelected }!!)
            }
        )

        // Example image that will be clipped using clipRect
        ImageExample(
            clipScaleSliderPosition = clipScaleSliderPosition,
            selectedClipOperation = { selectedClipOperation().clipOperation }
        )

        // Button for resetting the values of the sliders
        OutlinedButton(
            colors = ButtonDefaults.elevatedButtonColors(),
            elevation = ButtonDefaults.elevatedButtonElevation(4.dp),
            onClick = {
                changeClipScaleSliderPosition(1f)
                changeSelectedClipOperation(ClipOperation.Intersect)
            },
            content = {
                Text(text = stringResource(R.string.draw_scope_clip_rect_restart_button_label))
            }
        )
    }
}


@Composable
private fun ImageExample(
    clipScaleSliderPosition: () -> Float,
    selectedClipOperation: () -> ClipOp
) {

    // Animated value for clipPosition
    val imageClipScale by animateFloatAsState(
        targetValue = clipScaleSliderPosition(),
        label = "ImageClipScaleAnimation"
    )

    val imageBitmap = ImageBitmap.imageResource(id = R.drawable.jetpack_compose_icon)

    Canvas(
        modifier = Modifier.size(150.dp),
        onDraw = {
            clipRect(
                // Limite izquierdo del rectangulo para recortar
                left = size.width * (1 - imageClipScale),
                // Limite superior del rectangulo para recortar
                top = size.height * (1 - imageClipScale),
                // Limite derecho del rectangulo para recortar
                right = size.width * imageClipScale,
                // Limite inferior del rectangulo para recortar
                bottom = size.height * imageClipScale,
                // Operacion de recorte a realizar en los limites dados: Intersect o Difference
                clipOp = selectedClipOperation()
            ) {
                drawImage(
                    image = imageBitmap,
                    dstSize = IntSize((size.width).toInt(), size.height.toInt())
                )
            }
        }
    )
}




@CompactSizeScreenThemePreview
@Composable
private fun ClipRectExampleContentPreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        ClipRectExampleContent(
            clipScaleSliderPosition = { 1f },
            selectedClipOperation = { ClipOperation.Intersect },
            changeClipScaleSliderPosition = {},
            changeSelectedClipOperation = {}
        )
    }
}