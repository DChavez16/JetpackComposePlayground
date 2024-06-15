package com.example.ui.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ExampleComponent(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        // Forma del componente obtenida desde el tema de aplicación, al ser un Surface el
        //  Shape de este componente de acuerdo a los lineamientos de Material es Medium
        shape = MaterialTheme.shapes.medium,
        // Color del contenedor
        color = MaterialTheme.colorScheme.surface,
        // Color del contenido del contenedor, no es necesario definirlo puesto que MaterialTheme lo
        //  define automatica por medio del color del contenedor
        contentColor = MaterialTheme.colorScheme.onSurface,
        // Elevacion del tono del componente, el color del contenedor variara dependiendo de su elevacion
        tonalElevation = 2.dp,
        // Elevacion de la sombra del componente, la cantidad de sombra producida aumentara dependiendo de su elevacion
        shadowElevation = 2.dp,
        // Borde del componente, nulo en este caso, pues este estara definido pur su Shape
        border = null,
        // Contenido del Surface
        content = {
            Column(modifier = Modifier.padding(16.dp)) {
                // Texto que representa el titulo del componente individual
                Text(
                    text = title,
                    modifier = Modifier.paddingFromBaseline(bottom = 12.dp),
                    textAlign = TextAlign.Left,
                    // El estilo de la tipografia de este texto se obtiene del tema de la
                    //  aplicacion, en este caso un titleMedium
                    style = MaterialTheme.typography.titleMedium
                )

                // Texto que representa la descripcion del componente individual
                Text(
                    text = description,
                    modifier = Modifier.paddingFromBaseline(bottom = 8.dp),
                    textAlign = TextAlign.Justify,
                    // El estilo de la tipografia de este texto se obtiene del tema de la
                    //  aplicacion, en este caso un bodyMedium
                    style = MaterialTheme.typography.bodyMedium
                )

                // Componente el cual se describe en este IndividualComponent
                content()
            }
        }
    )
}