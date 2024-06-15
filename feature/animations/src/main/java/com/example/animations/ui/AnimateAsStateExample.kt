package com.example.animations.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.animations.R
import com.example.ui.theme.AppTheme

@Composable
internal fun AnimateAsStateExample() {
    var sliderPosition by remember { mutableFloatStateOf(0f) }

    val imageTransparency by animateFloatAsState(
        targetValue = 1-sliderPosition,
        label = "Image Transparency Animation"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        ImageTransparencySlider(
            sliderValue = sliderPosition,
            onSliderValueChange = { newPosition -> sliderPosition = newPosition }
        )
        ImageExample(imageTransparency = imageTransparency)
    }
}

@Composable
private fun ImageTransparencySlider(
    sliderValue: Float,
    onSliderValueChange: (Float) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Transparency: ", style = MaterialTheme.typography.titleMedium)
        Slider(
            value = sliderValue,
            onValueChange = { onSliderValueChange(it) },
            valueRange = 0f..1f
        )
    }
}

@Composable
private fun ImageExample(imageTransparency: Float) {
    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(R.drawable.jetpack_compose_icon),
            contentDescription = null,
            alpha = imageTransparency,
            modifier = Modifier.size(200.dp)
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun AnimateAsStateExamplePreview() {
    AppTheme {
        AnimateAsStateExample()
    }
}