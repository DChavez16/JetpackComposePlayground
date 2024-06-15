package com.example.lazylayouts.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.AppTheme
import kotlinx.coroutines.launch
import kotlin.random.Random


/**
 * Funcion Composable que muestra como usar el componente LazyVerticalStaggeredGrid y todas sus funciones
 */
@Composable
internal fun LazyVerticalStaggeredGridExample() {
    val lazyVerticalStaggeredGridState = rememberLazyStaggeredGridState()
    val coroutineScope = rememberCoroutineScope()

    LazyVerticalStaggeredGrid(
        // Establece que solo haya 2 columnas en el Grid
//        columns = StaggeredGridCells.Fixed(2),
        // Establece que las columnas tengan un ancho fijo de 50.dp
//        columns = StaggeredGridCells.FixedSize(100.dp),
        // Establece que las columnas requieran un ancho minimo de 50.dp para unirse a una fila
        columns = StaggeredGridCells.Adaptive(100.dp),
        // El modifier para personalizar el LazyVerticalStaggeredGrid
        modifier = Modifier.fillMaxWidth().height(200.dp),
        // Asigna el estado de la lista para interactuar con las funciones
        state = lazyVerticalStaggeredGridState,
        // Establece el padding del grid y evita el cliping de los elementos en la pantalla al desplazarse vertical
        contentPadding = PaddingValues(12.dp),
        // Define si los elementos del Grid se van a mostrar en orden invertido
        reverseLayout = false,
        // Asigna una separacion horizontal entre los elementos de 8.dp
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        // Asigna una separacion vertical entre los elementos de 8.dp
        verticalItemSpacing = 8.dp,
        // Define el comportamiento de la lista al hacer scroll
        flingBehavior = ScrollableDefaults.flingBehavior(),
        // Habilita o desabilita el scroll a traves de la lista
        userScrollEnabled = true,
        // Contenido del LazyVerticalStaggeredGrid
        content = {
            // Elemento individual de la lista
            item(key = 0, span = StaggeredGridItemSpan.FullLine) {
                LazyVerticalStaggeredGridBanner(
                    texto = "Desplazate al ultimo elemento del grid CON una animacion",
                    onClick = {
                        coroutineScope.launch {
                            lazyVerticalStaggeredGridState.animateScrollToItem(101)
                        }
                    },
                    isToTheEnd = true
                )
            }

            // Conjunto de elementos que se mostraran en la lista
            items(items = items, key = { item -> item.number }) { listItem ->
                LazyVerticalStaggeredGridItem(listItem = listItem)
            }

            // Elemento individual de la lista
            item(key = 101, span = StaggeredGridItemSpan.FullLine) {
                LazyVerticalStaggeredGridBanner(
                    texto = "Desplazate al primer elemento del grid SIN animacion",
                    onClick = {
                        coroutineScope.launch {
                            lazyVerticalStaggeredGridState.scrollToItem(0)
                        }
                    },
                    isToTheEnd = false
                )
            }
        }
    )
}

@Composable
private fun LazyVerticalStaggeredGridItem(listItem: VerticalListItem) {
    Surface(
        color = listItem.color,
        shape = RoundedCornerShape(4.dp),
        shadowElevation = 4.dp,
        modifier = Modifier.height(listItem.height)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = "${listItem.number}")
        }
    }
}

@Composable
private fun LazyVerticalStaggeredGridBanner(texto: String, onClick: () -> Unit, isToTheEnd: Boolean) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(4.dp),
        shadowElevation = 4.dp,
        modifier = Modifier
            .clickable { onClick() }
            .height(100.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(all = 12.dp)
        ) {
            Text(
                text = texto,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Icon(
                imageVector = with(Icons.Filled) {
                    if (isToTheEnd) KeyboardArrowDown else KeyboardArrowUp
                },
                contentDescription = null
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LazyVerticalStaggeredGridItemPreview() {
    AppTheme {
        LazyVerticalStaggeredGridExample()
    }
}

private data class VerticalListItem(
    val number: Int,
    val height: Dp,
    val color: Color
)

private val items = (1..100).map {
    VerticalListItem(
        number = it,
        height = Random.nextInt(100, 400).dp,
        color = Color(
            red = Random.nextInt(255),
            green = Random.nextInt(255),
            blue = Random.nextInt(255),
            alpha = 255
        )
    )
}