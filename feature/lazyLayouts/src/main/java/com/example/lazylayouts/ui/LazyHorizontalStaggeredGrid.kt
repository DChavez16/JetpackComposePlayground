package com.example.lazylayouts.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.lazyLayouts.R
import com.example.ui.theme.PreviewAppTheme
import com.example.ui.ui.CompactSizeScreenThemePreview
import kotlinx.coroutines.launch
import kotlin.random.Random


/**
 * Funcion Composable que muestra como usar el componente LazyHorizontalStaggeredGrid y todas sus funciones
 */
@Composable
internal fun LazyHorizontalStaggeredGridExample() {

    val lazyHorizontalStaggeredGridState = rememberLazyStaggeredGridState()
    val coroutineScope = rememberCoroutineScope()

    LazyHorizontalStaggeredGrid(
        // Establece que solo haya 2 filas en el Grid
        rows = StaggeredGridCells.Fixed(4),
        // Establece que las filas tengan una altura fija de 50.dp
//        rows = StaggeredGridCells.FixedSize(50.dp),
        // Establece que las filas requieran una altura minima de 50.dp para unirse a una columna
//        rows = StaggeredGridCells.Adaptive(70.dp),
        // El modifier para personalizar el LazyHorizontalStaggeredGrid
        modifier = Modifier.fillMaxWidth().height(280.dp),
        // Asigna el estado de la lista para interactuar con las funciones
        state = lazyHorizontalStaggeredGridState,
        // Establece el padding del grid y evita el cliping de los elementos en la pantalla al desplazarse horizontalmente
        contentPadding = PaddingValues(12.dp),
        // Define si los elementos del Grid se van a mostrar en orden invertido
        reverseLayout = false,
        // Asigna una separacion vertical entre los elementos de 8.dp
        verticalArrangement = Arrangement.spacedBy(8.dp),
        // Asigna una separacion horizontal entre los elementos de 8.dp
        horizontalItemSpacing = 8.dp,
        // Define el comportamiento de la lista al hacer scroll
        flingBehavior = ScrollableDefaults.flingBehavior(),
        // Habilita o desabilita el scroll a traves de la lista
        userScrollEnabled = true,
        // Contenido del LazyHorizontalStaggeredGrid
        content = {
            // Elemento individual de la lista
            item(key = 0, span = StaggeredGridItemSpan.FullLine) {
                LazyHorizontalStaggeredGridBanner(
                    texto = stringResource(R.string.lazy_layouts_lazy_horizontal_staggered_grid_first_header_label),
                    onClick = {
                        coroutineScope.launch {
                            lazyHorizontalStaggeredGridState.animateScrollToItem(101)
                        }
                    },
                    isToTheEnd = true
                )
            }

            // Conjunto de elementos que se mostraran en la lista
            items(items = items, key = { item -> item.number }) { listItem ->
                LazyHorizontalStaggeredGridItem(listItem = listItem)
            }

            // Elemento individual de la lista
            item(key = 101, span = StaggeredGridItemSpan.FullLine) {
                LazyHorizontalStaggeredGridBanner(
                    texto = stringResource(R.string.lazy_layouts_lazy_horizontal_staggered_grid_last_header_label),
                    onClick = {
                        coroutineScope.launch {
                            lazyHorizontalStaggeredGridState.scrollToItem(0)
                        }
                    },
                    isToTheEnd = false
                )
            }
        }
    )
}

@Composable
private fun LazyHorizontalStaggeredGridItem(listItem: HorizontalListItem) {
    Surface(
        color = listItem.color,
        shape = RoundedCornerShape(4.dp),
        shadowElevation = 4.dp,
        modifier = Modifier.width(listItem.width)
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
private fun LazyHorizontalStaggeredGridBanner(
    texto: String,
    onClick: () -> Unit,
    isToTheEnd: Boolean
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(4.dp),
        shadowElevation = 4.dp,
        modifier = Modifier
            .clickable { onClick() }
            .width(200.dp)
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
            Spacer(modifier = Modifier.height(16.dp))
            Icon(
                imageVector = with(Icons.AutoMirrored.Filled) {
                    if (isToTheEnd) ArrowForward else ArrowBack
                },
                contentDescription = null
            )
        }
    }
}




@CompactSizeScreenThemePreview
@Composable
private fun LazyHorizontalStaggeredGridItemPreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        LazyHorizontalStaggeredGridExample()
    }
}




private data class HorizontalListItem(
    val number: Int,
    val width: Dp,
    val color: Color
)

private val items = (1..100).map {
    HorizontalListItem(
        number = it,
        width = Random.nextInt(50, 200).dp,
        color = Color(
            red = Random.nextInt(255),
            green = Random.nextInt(255),
            blue = Random.nextInt(255),
            alpha = 255
        )
    )
}