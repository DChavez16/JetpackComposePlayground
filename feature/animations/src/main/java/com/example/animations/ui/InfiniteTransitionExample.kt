package com.example.animations.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.animations.R
import com.example.ui.theme.AppTheme

@Composable
internal fun InfiniteTransitionExample() {
    // Crear una instancia de InfiniteTransition
    val infiniteTransition = rememberInfiniteTransition(label = "AngleInfiniteTRansition")

    // Crea el valor que va a cambiar constantemente
    val angleInfiniteTransition by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 5000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "AngleInfiniteTransition"
    )

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.jetpack_compose_icon),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .rotate(angleInfiniteTransition)
        )
    }
}


@Preview
@Composable
private fun InfiniteTransitionExamplePreview() {
    AppTheme {
        InfiniteTransitionExample()
    }
}