package com.example.drawscope.ui

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.ui.graphics.drawscope.rotate
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
internal fun RotateExample(
    drawScopeViewModel: DrawScopeViewModel = hiltViewModel()
) {

    // SliderPosition for angleDegrees
    val angleDegreesSliderPosition by drawScopeViewModel.rotateAngleDegreesSliderPosition.collectAsState()

    RotateExampleContent(
        angleDegreesSliderPosition = { angleDegreesSliderPosition },
        changeAngleDegreesSliderPosition = drawScopeViewModel::changeRotateAngleDegreesSliderPosition
    )
}


@Composable
private fun RotateExampleContent(
    angleDegreesSliderPosition: () -> Float,
    changeAngleDegreesSliderPosition: (Float) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Slider to change the vertical position of the image
        CustomSlider(
            sliderTextLabel = stringResource(R.string.draw_scope_rotate_angle_degrees),
            sliderValue = angleDegreesSliderPosition,
            sliderValueRange = -36f..36f,
            onSliderValueChange = changeAngleDegreesSliderPosition,
            onSliderValueReset = { changeAngleDegreesSliderPosition(0f) }
        )

        // Example image that is rotated
        ImageExample(
            angleDegreesSliderPosition = angleDegreesSliderPosition
        )
    }
}


@Composable
private fun ImageExample(
    angleDegreesSliderPosition : () -> Float
) {

    // Animated values for verticalScale and horizontalScale
    val imageAngleDegrees by animateFloatAsState(
        targetValue = angleDegreesSliderPosition(),
        label = "Image Angle Degrees Animation"
    )

    val imageBitmap = ImageBitmap.imageResource(id = R.drawable.jetpack_compose_icon)

    Canvas(modifier = Modifier.size(150.dp)) {
        rotate(
            // Grades to rotate clockwise
            degrees = imageAngleDegrees*10,
            // Coordinate of the pivot point, by default it is located in the center of the coordinate space
            pivot = center
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
private fun RotateExampleContentPreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        RotateExampleContent(
            angleDegreesSliderPosition = { 0f },
            changeAngleDegreesSliderPosition = {}
        )
    }
}