package com.example.themes.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ui.theme.AppTheme


@Composable
internal fun SurfaceExample() {

    Surface(
        shadowElevation = 2.dp,
        // Aplicando un Shape a un layout
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    // Aplicando el ColorScheme a un Layout
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
        ) {
            for (i in 1..5) {
                Text(
                    text = i.toString(),
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}


@Preview
@Composable
private fun SurfaceExamplePreview() {
    AppTheme {
        SurfaceExample()
    }
}