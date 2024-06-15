package com.example.ui.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import java.math.RoundingMode

@Composable
fun CustomSlider(
    sliderTextLabel: String,
    sliderValue: () -> Float,
    sliderValueRange: ClosedFloatingPointRange<Float>,
    onSliderValueChange: (Float) -> Unit,
    onSliderValueReset: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Slider label
        Text(
            text = sliderTextLabel,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(4f)
        )

        // Slider
        Slider(
            value = sliderValue(),
            onValueChange = {
                onSliderValueChange(it.toBigDecimal().setScale(1, RoundingMode.HALF_EVEN).toFloat())
            },
            valueRange = sliderValueRange,
            modifier = Modifier.weight(6f)
        )

        // Reset icon button
        IconButton(onClick = onSliderValueReset, modifier = Modifier.weight(1f)) {
            Icon(imageVector = Icons.Filled.Refresh, contentDescription = null)
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun CustomSliderComposablePreview() {
    com.example.ui.theme.AppTheme {
        CustomSlider(
            sliderTextLabel = "Slider label",
            sliderValue = { 0.5f },
            sliderValueRange = 0f..1f,
            onSliderValueChange = {},
            onSliderValueReset = {}
        )
    }
}