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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.drawscope.ClipOperationClipPath
import com.example.drawscope.DrawScopeViewModel
import com.example.drawscope.R
import com.example.ui.theme.PreviewAppTheme
import com.example.ui.ui.CompactSizeScreenThemePreview
import com.example.ui.ui.CustomDropdownMenu
import com.example.ui.ui.CustomSlider


@Composable
internal fun ClipPathExample(
    drawScopeViewModel: DrawScopeViewModel = hiltViewModel()
) {

    // Slider position for clipPosition
    val clipPositionSliderPosition by drawScopeViewModel.clipPositionSliderPosition.collectAsState()
    // Selected clip operation
    val selectedClipOperation by drawScopeViewModel.selectedClipOperation.collectAsState()

    ClipPathExampleContent(
        clipPositionSliderPosition = { clipPositionSliderPosition },
        selectedClipOperation = { selectedClipOperation },
        changeClipPositionSliderPosition = drawScopeViewModel::changeClipPositionSliderPosition,
        changeSelectedClipOperation = drawScopeViewModel::changeSelectedClipOperation
    )
}


@Composable
private fun ClipPathExampleContent(
    clipPositionSliderPosition: () -> Float,
    selectedClipOperation: () -> ClipOperationClipPath,
    changeClipPositionSliderPosition: (Float) -> Unit,
    changeSelectedClipOperation: (ClipOperationClipPath) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Slider for changing the clip position of the image
        CustomSlider(
            sliderTextLabel = stringResource(R.string.draw_scope_clip_path_clip_position),
            sliderValue = clipPositionSliderPosition,
            sliderValueRange = 0f..1f,
            onSliderValueChange = changeClipPositionSliderPosition,
            onSliderValueReset = { changeClipPositionSliderPosition(0f) }
        )

        // Dropdown menu for changing the clip operation
        CustomDropdownMenu(
            dropdownMenuLabel = stringResource(R.string.draw_scope_clip_path_dropdown_menu_label),
            currentElementDisplay = { selectedClipOperation().clipOperationName },
            optionsList = ClipOperationClipPath.entries.map { clipOperation -> clipOperation.clipOperationName },
            onElementSelected = { elementSelected ->
                changeSelectedClipOperation(ClipOperationClipPath.entries.find { it.clipOperationName == elementSelected }!!)
            }
        )

        // Example image that will be clipped using clipPath
        ImageExample(
            clipPositionSliderPosition = clipPositionSliderPosition,
            selectedClipOperation = { selectedClipOperation().clipOperation }
        )

        // Button for resetting the values of the sliders
        OutlinedButton(
            colors = ButtonDefaults.elevatedButtonColors(),
            elevation = ButtonDefaults.elevatedButtonElevation(4.dp),
            onClick = {
                changeClipPositionSliderPosition(0f)
                changeSelectedClipOperation(ClipOperationClipPath.Intersect)
            },
            content = {
                Text(text = stringResource(R.string.draw_scope_clip_path_restart_button_label))
            }
        )
    }
}


@Composable
private fun ImageExample(
    clipPositionSliderPosition: () -> Float,
    selectedClipOperation: () -> ClipOp
) {

    // Valor animable para clipPosition
    val imageClipPosition by animateFloatAsState(
        targetValue = clipPositionSliderPosition(),
        label = "Image Clip Position Animation"
    )

    val imageBitmap = ImageBitmap.imageResource(id = R.drawable.jetpack_compose_icon)

    Canvas(modifier = Modifier.size(150.dp)) {
        clipPath(
            // Forma (definida por una ruta) a recortar el contenido de dibujo dentro
            path = Path().apply {
                moveTo(0f, 0f)
                lineTo(size.width, 0f)
                lineTo(size.width, size.height)
                lineTo(
                    x = size.width * (if (imageClipPosition > 0.5) 1f else imageClipPosition * 2),
                    y = size.height * (if (imageClipPosition < 0.5) 1f else 1f - ((imageClipPosition - 0.5f) * 2f))
                )
                close()
            },
            // Operacion de recorte a realizar en los limites dados: Intersect o Difference
            clipOp = selectedClipOperation()
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
private fun ClipPathExampleContentPreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        ClipPathExampleContent(
            clipPositionSliderPosition = { 0f },
            selectedClipOperation = { ClipOperationClipPath.Intersect },
            changeClipPositionSliderPosition = {},
            changeSelectedClipOperation = {}
        )
    }
}