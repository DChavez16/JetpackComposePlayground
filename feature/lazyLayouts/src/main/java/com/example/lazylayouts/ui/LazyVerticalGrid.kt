package com.example.lazylayouts.ui

import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.lazyLayouts.R
import com.example.ui.theme.PreviewAppTheme
import com.example.ui.ui.CompactSizeScreenThemePreview


/**
 *  Funcion Composable que muestra como usar el componente LazyVerticalGrid y todas sus funciones
 */
@Composable
internal fun LazyVerticalGridExample() {
    
    // El estado de la lista nos permite acceder a sus funciones de scroll
    val lazyVerticalGridState = rememberLazyGridState()

    LazyVerticalGrid(
        // Define el numero de columnas de la lista, en este caso se una cantidad fija de 5 columnas
        // columns = GridCells.Fixed(count = 5),
        // Para este parametro se usa un tamaño fijo para las columnas
        // columns = GridCells.FixedSize(60.dp),
        // Para este parametro se usa un tamaño minimo para las columnas
        columns = GridCells.Adaptive(80.dp),
        // El modifier para personalizar el LazyVerticalGrid
        modifier = Modifier.fillMaxWidth().height(200.dp),
        // Asigna el estado a la lista para interactuar con sus funciones
        state = lazyVerticalGridState,
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
        // Contenido del LazyVerticalGrid
        content = {
            for (i in 1..10) {
                // Agregar el parametro span a un elemento del grid nos permite definir el tamaño de
                // este elemento en especifico. Al usar maxLineSpan se asigna al tamaño del elemento
                // el maximo de lineas posibles
                item(span = { GridItemSpan(maxLineSpan) }) {
                    LazyVerticalGridHeader(
                        initialNumber = (i * 10) - 9,
                        finalNumber = (i * 10)
                    )
                }
                items(((i * 10) - 9..(i * 10)).toList()) { numero ->
                    LazyVerticalGridItem(numero = numero)
                }
            }
        }
    )
}

@Composable
private fun LazyVerticalGridHeader(
    initialNumber: Int,
    finalNumber: Int
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(size = 4.dp),
        shadowElevation = 4.dp,
        modifier = Modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.lazy_layouts_lazy_vertical_grid_header, initialNumber, finalNumber),
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
private fun LazyVerticalGridItem(numero: Int) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(size = 4.dp),
        shadowElevation = 4.dp,
        modifier = Modifier
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("$numero", Modifier.padding(16.dp))
        }
    }
}




@CompactSizeScreenThemePreview
@Composable
private fun LazyVerticalGridExamplePreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        LazyVerticalGridExample()
    }
}