package com.example.animations.ui

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.animations.R
import com.example.ui.theme.AppTheme

@Composable
internal fun UpdateTrasitionExample() {

    // Crea y recuerda una instancia de Transition
    var currentState by remember { mutableStateOf(BoxState.Expanded) }
    val transition = updateTransition(currentState, label = "Box State uppdate transicion")

    // Inicializa las animaciones que se van a utilizar
    // Color de fondo
    val backgroundColor by transition.animateColor(
        transitionSpec = {
            when {
                BoxState.Collapsed isTransitioningTo BoxState.Expanded ->
                    tween(durationMillis = 1000, easing = FastOutLinearInEasing)

                else ->
                    tween(durationMillis = 1000, easing = FastOutLinearInEasing)
            }
        },
        label = "backGroundColor update transition"
    ) {
        when (it) {
            BoxState.Collapsed -> Color.Transparent
            BoxState.Expanded -> MaterialTheme.colorScheme.surfaceVariant
        }
    }
    // Elevacion
    val elevationDp by transition.animateDp(
        transitionSpec = {
            when {
                BoxState.Collapsed isTransitioningTo BoxState.Expanded ->
                    tween(durationMillis = 1000, easing = FastOutLinearInEasing)

                else ->
                    tween(durationMillis = 1000, easing = FastOutLinearInEasing)
            }
        },
        label = "elevationDp update transition"
    ) {
        when (it) {
            BoxState.Collapsed -> 0.dp
            BoxState.Expanded -> 16.dp
        }
    }
    // Sombra de elevacion
    val shadowElevationDp by transition.animateDp(
        transitionSpec = {
            when {
                BoxState.Collapsed isTransitioningTo BoxState.Expanded ->
                    tween(durationMillis = 1000, easing = FastOutLinearInEasing)

                else ->
                    tween(durationMillis = 1000, easing = FastOutLinearInEasing)
            }
        },
        label = "shadowElevationDp update Transition"
    ) {
        when (it) {
            BoxState.Collapsed -> 0.dp
            BoxState.Expanded -> 16.dp
        }
    }
    // Rotacion de un boton de expandir.
    val iconAngle by transition.animateFloat(
        transitionSpec = {
            when {
                BoxState.Collapsed isTransitioningTo BoxState.Expanded ->
                    tween(durationMillis = 1000, easing = FastOutLinearInEasing)

                else ->
                    tween(durationMillis = 1000, easing = FastOutLinearInEasing)
            }
        },
        label = "iconAngle update Transition"
    ) {
        when (it) {
            BoxState.Collapsed -> 0f
            BoxState.Expanded -> -180f
        }
    }

    Surface(
        color = backgroundColor,
        tonalElevation = elevationDp,
        shadowElevation = shadowElevationDp
    ) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Max)
                .width(IntrinsicSize.Max)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                DecorativeImage()
                if (currentState == BoxState.Expanded) TitleDescriptionText()
            }

            ExpandButton(
                iconAngle = iconAngle,
                modifier = Modifier.width(32.dp),
                onClick = {
                    currentState =
                        if (currentState == BoxState.Collapsed) BoxState.Expanded
                        else BoxState.Collapsed
                }
            )
        }
    }
}

// Imagen 
@Composable
fun DecorativeImage() {
    Image(
        painter = painterResource(R.drawable.jetpack_compose_icon),
        contentDescription = null,
        modifier = Modifier.size(100.dp)
    )
}

// Texto con titulo y descripcion
@Composable
private fun TitleDescriptionText() {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxHeight()
    ) {
        Text(
            text = "Titulo",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        Text(
            text = exampleString,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Justify,
            softWrap = true,
            maxLines = if (isExpanded) Int.MAX_VALUE else 3,
            modifier = Modifier
                .padding(bottom = 8.dp)
        )
        Text(
            text = if (isExpanded) "Ver menos" else "Ver mas",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .clickable { isExpanded = !isExpanded }
        )
    }
}

// Boton para contraer y expandir
@Composable
private fun ExpandButton(iconAngle: Float, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxHeight()
            .clickable { onClick() }
    ) {
        Icon(
            imageVector = Icons.Filled.KeyboardArrowRight,
            contentDescription = null,
            modifier = Modifier
                .scale(scaleX = 1f, scaleY = 1.5f)
                .rotate(iconAngle)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UpdateTransitionExamplePreview() {
    AppTheme {
        UpdateTrasitionExample()
    }
}


// Enum class para identificar si el recuadro esta contraido o expandido.
enum class BoxState {
    Collapsed,
    Expanded
}

private const val exampleString =
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec quis ipsum non sapien vulputate aliquet pharetra elementum tortor. Phasellus consectetur posuere erat eu sollicitudin. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam erat volutpat. In rutrum cursus tincidunt. Ut lectus erat, dignissim sed turpis quis, varius aliquet eros. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus."