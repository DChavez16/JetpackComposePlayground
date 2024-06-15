package com.example.lazylayouts.ui

import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ui.theme.AppTheme


/**
 *  Funcion Composable que muestra como usar el componente LazyHorizontalGrid y todas sus funciones
 */
@Composable
internal fun LazyHorizontalGridExample() {
    // El estado de la lista nos permite acceder a sus funciones de scroll
    val lazyHorizontalGridState = rememberLazyGridState()

    LazyHorizontalGrid(
        // Define el numero de filas de la lista, en este caso se una cantidad fija de 4 filas
        // rows = GridCells.Fixed(count = 4),
        // Para este parametro se usa un tamaño fijo para las filas
        // rows = GridCells.FixedSize(60.dp),
        // Para este parametro se usa un tamaño minimo para las filas
        rows = GridCells.Adaptive(80.dp),
        // El modifier para personalizar el LazyHorizontalGrid
        modifier = Modifier.height(280.dp),
        // Asigna el estado a la lista para interactuar con sus funciones
        state = lazyHorizontalGridState,
        // Para poder ajustar la separacion entre los elementos de la lista, acepta varios valores
        contentPadding = PaddingValues(all = 8.dp),
        // Para invertir el orden de los elementos de la lista
        reverseLayout = false,
        // El orden vertical de los elementos de la lista
        verticalArrangement = Arrangement.spacedBy(8.dp),
        // El orden horizontal de los elementos de la lista
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        // Para definir el comportamiento de scroll de la lista
        flingBehavior = ScrollableDefaults.flingBehavior(),
        // Para habilitar o deshabilitar el scroll de la lista
        userScrollEnabled = true,
        // Contenido del LazyHorizontalGrid
        content = {
            for (i in 1..10) {
                // Agregar el parametro span a un elemento del grid nos permite definir el tamaño de
                // este elemento en especifico. Al usar maxLineSpan se asigna al tamaño del elemento
                // el maximo de lineas posibles
                item(span = { GridItemSpan(maxLineSpan) }) {
                    LazyHorizontalGridHeader(initialNumber = (i * 10) - 9, finalNumber = (i * 10))
                }
                items(((i * 10) - 9..(i * 10)).toList()) { numero ->
                    LazyHorizontalGridItem(numero = numero)
                }
            }
        }
    )
}

@Composable
private fun LazyHorizontalGridHeader(initialNumber: Int, finalNumber: Int) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(size = 4.dp),
        shadowElevation = 4.dp,
        modifier = Modifier
    ) {
        Column(verticalArrangement = Arrangement.Center) {
            Text("Numeros del $initialNumber al $finalNumber", Modifier.padding(8.dp))
        }
    }
}

@Composable
private fun LazyHorizontalGridItem(numero: Int) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(size = 4.dp),
        shadowElevation = 4.dp,
        modifier = Modifier
    ) {
        Column(verticalArrangement = Arrangement.Center) {
            Text("$numero", Modifier.padding(16.dp))
        }
    }
}


@Preview
@Composable
private fun LazyHorizontalGridExamplePreview() {
    AppTheme {
        LazyHorizontalGridExample()
    }
}