package com.example.animations.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.animations.AnimationsViewModel
import com.example.animations.R
import com.example.ui.theme.PreviewAppTheme
import com.example.ui.ui.CompactSizeScreenThemePreview

@Composable
internal fun AnimateAsStateExample(
    animationsViewModel: AnimationsViewModel = hiltViewModel()
) {
    val sliderPosition by animationsViewModel.transparencySliderValue.collectAsState()

    AnimateAsStateExampleContent(
        sliderPosition = { sliderPosition },
        onSliderValueChange = { animationsViewModel.changeSliderValue(it) }
    )
}


@Composable
private fun AnimateAsStateExampleContent(
    sliderPosition: () -> Float,
    onSliderValueChange: (Float) -> Unit
) {
    val imageTransparency by animateFloatAsState(
        targetValue = sliderPosition(),
        label = "Image Transparency Animation"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        ImageTransparencySlider(
            sliderValue = sliderPosition,
            onSliderValueChange = onSliderValueChange
        )

        ImageExample(
            imageTransparency = { imageTransparency }
        )
    }
}


@Composable
private fun ImageTransparencySlider(
    sliderValue: () -> Float,
    onSliderValueChange: (Float) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.animations_screen_animate_as_state_slider_label),
            style = MaterialTheme.typography.titleMedium
        )

        Slider(
            value = sliderValue(),
            onValueChange = { onSliderValueChange(it) },
            valueRange = 0f..1f
        )
    }
}


@Composable
private fun ImageExample(
    imageTransparency: () -> Float
) {
    Image(
        painter = painterResource(R.drawable.jetpack_compose_icon),
        contentDescription = null,
        modifier = Modifier
            .size(200.dp)
            .graphicsLayer {
                alpha = 1 - imageTransparency()
            }
    )
}




@CompactSizeScreenThemePreview
@Composable
private fun AnimateAsStateExamplePreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        AnimateAsStateExampleContent(
            sliderPosition = { 0f },
            onSliderValueChange = {}
        )
    }
}