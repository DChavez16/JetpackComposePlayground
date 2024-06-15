package com.example.animations.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.animations.R
import com.example.ui.theme.AppTheme


@Composable
internal fun AnimatedContentExample() {
    // Estado que indica si el ejemplo esta expandido
    var isExpanded by remember { mutableStateOf(false) }

    Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
        AnimatedContent(
            // Estado que define si el contenido esta expandido para ejecutar la animacion
            targetState = isExpanded,
            // TransitionSpec de la animacion que contiene un ContentTransform y un SizeTRansform
            transitionSpec = {
                fadeIn(tween(100)) togetherWith // Animacion de entrada del contenido
                        fadeOut(tween(100, 200)) using // Animacion de salida del contenido
                        SizeTransform { initialSize, targetSize ->
                            if (targetState) {
                                keyframes {
                                    // Primero se expande horizontalmente
                                    IntSize(targetSize.width, initialSize.height) at 100
                                    durationMillis = 300
                                }
                            } else {
                                keyframes {
                                    // Primero se contrae verticalmente
                                    IntSize(initialSize.width, targetSize.height) at 200
                                    durationMillis = 300
                                }
                            }
                        }
            },
            // Alineacion del contenido del AnimatedContent
            contentAlignment = Alignment.TopEnd,
            // Etiqueta para su identificacion en los Preview
            label = "SizeTransform Animation"
        ) { targetExpanded ->
            if (targetExpanded) {
                ExpandedExample { isExpanded = false }
            } else {
                IconButton(onClick = { isExpanded = true }, enabled = true) {
                    Icon(
                        painter = painterResource(R.drawable.expand_more_icon),
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
private fun ExpandedExample(onClick: () -> Unit) {
    var currentNumber by remember { mutableIntStateOf(1) }

    Column(
        horizontalAlignment = Alignment.End, modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        // Boton para ocntraer el ejemplo
        IconButton(onClick = onClick) {
            Icon(painter = painterResource(R.drawable.expand_less_icon), contentDescription = null)
        }

        // Row que muestra el contenedor y los botones
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            ExpandedExampleContainer(
                currentNumber = currentNumber,
                modifier = Modifier
                    .weight(3f)
                    .aspectRatio(1f)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.onSurface,
                        shape = RoundedCornerShape(4.dp)
                    )
            )

            Spacer(modifier = Modifier.weight(1f))

            // Column que muestra los botones para modificar el numero y el boton para cambiar el fondo
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(3f)
            ) {
                // Boton para incrementar el valor, habilitado si el numero es menor que 10
                IconButton(onClick = { currentNumber++ }, enabled = currentNumber < 10) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                }

                // Texto indicativo
                Text(
                    text = "Cambiar número",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )

                // Boton para disminuir el valor, habilirado si el numero es mayor que 1
                IconButton(onClick = { currentNumber-- }, enabled = currentNumber > 1) {
                    Icon(
                        painter = painterResource(R.drawable.remove_icon),
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
private fun ExpandedExampleContainer(currentNumber: Int, modifier: Modifier = Modifier) {
    // Box que va a ser de contenedor para el numero
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            AnimatedContent(
                // Estado que define si el contenido esta expandido para ejecutar la animacion
                targetState = currentNumber,
                // TransitionSpec de la animacion que contiene un ContentTransform y un SizeTRansform
                transitionSpec = {
                    // Cuando el nuevo numero sea mayor al actual
                    if (targetState > initialState) {
                        // Deslizar el contenido hacia arriba
                        slideInVertically { height -> height } + fadeIn() togetherWith
                                slideOutVertically { height -> -height } + fadeOut()
                    } else {
                        // Deslizar el contenido hacia abajo
                        slideInVertically { height -> -height } + fadeIn() togetherWith
                                slideOutVertically { height -> height } + fadeOut()
                    }.using(
                        SizeTransform(clip = false)
                    )
                },
                // Etiqueta para su identificacion en los Preview
                label = "ContentTransform Animation"
            ) { targetNumber ->
                Text(
                    text = "$targetNumber",
                    style = MaterialTheme.typography.displaySmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = false)
@Composable
private fun AnimatedContentExamplePreview() {
    AppTheme {
        AnimatedContentExample()
    }
}