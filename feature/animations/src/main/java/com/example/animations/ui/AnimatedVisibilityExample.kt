package com.example.animations.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseInBack
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandIn
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.animations.R
import com.example.ui.theme.AppTheme
import com.example.ui.ui.CustomDropdownMenu

// Ejemplo de AnimatedVisibility
@Composable
internal fun AnimatedVisibilityExample() {
    var imageVisibility by remember { mutableStateOf(false) }
    var currentTransition by remember { mutableStateOf(Transitions.None) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        // DropdownMenu que indica las transiciones de entrada y de salida de la animacion
        CustomDropdownMenu(
            dropdownMenuLabel = "Transicion",
            currentElementDisplay = currentTransition.transitionName,
            optionsList = Transitions.entries.map { transition -> transition.transitionName },
            onElementSelected = { elementSelected ->
                currentTransition =
                    Transitions.entries.find { it.transitionName == elementSelected }!!
            }
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Box que va a ser de contenedor para la imagen que se va a animar en el ejemplo
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(3f)
                    .aspectRatio(1f)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.onSurface,
                        shape = RoundedCornerShape(4.dp)
                    )
            ) {
                this@Row.AnimatedVisibility(
                    visible = imageVisibility,
                    enter = currentTransition.transition.enterTransition,
                    exit = currentTransition.transition.exitTransition
                ) {
                    Image(
                        painter = painterResource(R.drawable.jetpack_compose_icon),
                        contentDescription = null
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Boton para accionar la animacion (habilitar o deshabilitar la visiblidad de la imagen)
            Button(
                onClick = { imageVisibility = !imageVisibility },
                modifier = Modifier.weight(3f)
            ) {
                Text(
                    text = if (imageVisibility) "Ocultar imagen" else "Mostrar Imagen",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun AnimatedVisibilityExamplePreview() {
    AppTheme {
        AnimatedVisibilityExample()
    }
}


// Clases utiles para el ejemplo
private data class Transition(
    val enterTransition: EnterTransition = EnterTransition.None,
    val exitTransition: ExitTransition = ExitTransition.None
)

private enum class Transitions(val transitionName: String, val transition: Transition) {
    None("Sin transiciones", Transition()),
    Fade("Fade", Transition(fadeIn(), fadeOut())),
    Slide(
        "Slide",
        Transition(
            enterTransition = slideIn(initialOffset = { fullSize ->
                IntOffset(
                    fullSize.width,
                    fullSize.height
                )
            }),
            exitTransition = slideOut(targetOffset = { fullSize ->
                IntOffset(
                    -fullSize.width,
                    -fullSize.height
                )
            })
        )
    ),
    SlideHorizontally(
        "SlideHorizontally",
        Transition(
            enterTransition = slideInHorizontally(
                animationSpec = tween(1000, easing = EaseOutBack),
                initialOffsetX = { fullSize -> fullSize }),
            exitTransition = slideOutHorizontally(
                animationSpec = tween(1000, easing = EaseInBack),
                targetOffsetX = { fullSize -> -fullSize })
        )
    ),
    SlideVertically(
        "SlideVertically",
        Transition(
            enterTransition = slideInVertically(initialOffsetY = { fullSize -> fullSize }),
            exitTransition = slideOutVertically(targetOffsetY = { fullSize -> -fullSize })
        )
    ),
    Scale("Scale", Transition(scaleIn(), scaleOut())),
    ExpandShrink("Expand/Shrink", Transition(expandIn(), shrinkOut())),
    ExpandShrinkHorizontally(
        "Expand/Shrink Horizontally",
        Transition(
            expandHorizontally(),
            shrinkHorizontally()
        )
    ),
    ExpandShrinkVertically(
        "Expand/Shrink Vertically",
        Transition(
            expandVertically(),
            shrinkVertically()
        )
    )
}