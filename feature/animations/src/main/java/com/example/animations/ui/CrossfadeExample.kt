package com.example.animations.ui

import androidx.compose.animation.Crossfade
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.animations.R
import com.example.ui.theme.AppTheme

@Composable
internal fun CrossfadeExample() {
    // Crear un contenedor que tenga un numero y un backgroundColor definidos por CrossfadeItem
    // Agregar tres botones: Uno para disminuir el numero, otro para aumentar el numero y otro para cambiar el color de fondo
    // Usar crossfade para la transicion de numero y color de fondo
    var crossfadeItem by remember { mutableStateOf(CrossfadeItem(number = 1)) }

    // Row que muestra el contenedor y los botones
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Box que va a ser de contenedor para el numero
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
            Crossfade(targetState = crossfadeItem, label = "Example container") { crossfadeItem ->
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = crossfadeItem.backgroundColor)
                ) {
                    Text(
                        text = "${crossfadeItem.number}",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.displaySmall
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Column que muestra los botones para modificar el numero y el boton para cambiar el fondo
        Column(modifier = Modifier.weight(3f)) {
            // Row que muestra los botones para reducir y aumentar el valor del numero
            Row {
                // Button para disminuir el valor del numero, si el numero es 1 no se puede disminuir
                IconButton(
                    onClick = {
                        crossfadeItem = crossfadeItem.copy(number = crossfadeItem.number - 1)
                    },
                    enabled = crossfadeItem.number > 1,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.remove_icon),
                        contentDescription = null
                    )
                }

                Text(
                    text = "Cambiar numero",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.sizeIn(maxWidth = 100.dp)
                )

                // Button para aumentar el valor del numero, si el numero es 10 no se puede aumentar
                IconButton(
                    onClick = {
                        crossfadeItem = crossfadeItem.copy(number = crossfadeItem.number + 1)
                    },
                    enabled = crossfadeItem.number < 10,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Button para cambiar el color de fondo
            Button(
                onClick = { crossfadeItem = crossfadeItem.copy(backgroundColor = getRandomColor()) }
            ) {
                Text(
                    text = "Cambiar color de fondo",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun CrossfadeExamplePreview() {
    AppTheme {
        CrossfadeExample()
    }
}


private data class CrossfadeItem(
    var number: Int,
    var backgroundColor: Color = Color.White
)

// Obtener un color aleatorio
private fun getRandomColor() =
    Color(
        red = (0..255).random(),
        green = (0..255).random(),
        blue = (0..255).random()
    )