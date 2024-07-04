package com.example.lazylayouts.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.lazyLayouts.R
import com.example.ui.theme.PreviewAppTheme
import com.example.ui.ui.CompactSizeScreenThemePreview
import kotlinx.coroutines.launch

/**
Funcion Composable que muestra como usar el componente LazyColumn y todas sus funciones
 **/
@Composable
internal fun LazyColumnExample() {

    // El estado de la lista nos permite acceder a sus funciones de scroll
    val lazyColumnState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        // El modifier para personalizar el LazyColumn
        modifier = Modifier.fillMaxWidth().height(200.dp),
        // Asigna el estado de la lista para interactuar con sus funciones
        state = lazyColumnState,
        // Para poder ajustar la separacion entre los elementos de la lista, acepta varios valores
        contentPadding = PaddingValues(all = 16.dp),
        // Para invertir el orden de los elementos de la lista
        reverseLayout = true,
        // Para alinear verticalmente los elementos de la lista
        verticalArrangement = Arrangement.spacedBy(8.dp),
        // Para alinear horizontalmente los elementos de la lista
        horizontalAlignment = Alignment.CenterHorizontally,
        // Para definir el comportamiento de la lista al hacer scroll
        flingBehavior = ScrollableDefaults.flingBehavior(),
        // Para habilitar o deshabilitar el scroll de la lista
        userScrollEnabled = true,
        // Contenido del LazyColumn
        content = {
            // Elemento individual de la lista
            item {
                /* Al presionar el elemento de la lista, este realizara un scroll SIN animacion
                hacia el final de la lista */
                LazyColumnItem(
                    texto = stringResource(R.string.lazy_layouts_lazy_column_first_item_label),
                    onClick = { coroutineScope.launch { lazyColumnState.scrollToItem(101) } })
            }

            // Conjunto de elementos que se mostraran en la lista
            items(
                items = (2..99).toList(),
                key = { numero -> numero }
            ) { numero ->
                LazyColumnItem(
                    texto = stringResource(R.string.lazy_layouts_lazy_column_intemediate_items_label, numero)
                )
            }

            // Elemento individual de la lista
            item {
                /* Al presionar el elemento de la lista, este realizara un scroll CON animacion
                hacia el inicio de la lista */
                LazyColumnItem(
                    texto = stringResource(R.string.lazy_layouts_lazy_column_last_item_label),
                    onClick = { coroutineScope.launch { lazyColumnState.animateScrollToItem(0) } })
            }
        }
    )
}


@Composable
private fun LazyColumnItem(texto: String, onClick: () -> Unit = {}) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(size = 4.dp),
        shadowElevation = 4.dp,
        modifier = Modifier.fillMaxWidth().clickable(enabled = onClick != {}) { onClick() })
    {
        Text(texto, Modifier.padding(8.dp))
    }
}



@CompactSizeScreenThemePreview
@Composable
private fun LazyColumnExamplePreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        LazyColumnExample()
    }
}