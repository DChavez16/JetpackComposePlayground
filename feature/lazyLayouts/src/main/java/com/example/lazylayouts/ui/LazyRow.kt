package com.example.lazylayouts.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ui.theme.AppTheme
import kotlinx.coroutines.launch


/**
Funcion Composable que muestra como usar el componente LazyRow y todas sus funciones
 **/
@Composable
internal fun LazyRowExample() {
    // El estado de la lista nos permite acceder a sus funciones de scroll
    val lazyRowState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LazyRow(
        // El modifier para personalizar el LazyRow
        modifier = Modifier.fillMaxSize(),
        // Asigna el estado de la lista para interactuar con sus funciones
        state = lazyRowState,
        // Para poder ajustar la separacion entre los elementos de la lista, acepta varios valores
        contentPadding = PaddingValues(all = 16.dp),
        // Para invertir el orden de los elementos de la lista
        reverseLayout = true,
        // Para alinear horizontalmente los elementos de la lista
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        // Para alinear verticalmente los elementos de la lista
        verticalAlignment = Alignment.CenterVertically,
        // Para definir el comportamiento de la lista al hacer scroll
        flingBehavior = ScrollableDefaults.flingBehavior(),
        // Para habilitar o deshabilitar el scroll de la lista
        userScrollEnabled = true,
        // Contenido del LazyRow
        content = {
            // Elemento individual de la lista
            item {
                /* Al presionar el elemento de la lista, este realizara un scroll SIN animacion
                hacia el final de la lista */
                LazyRowItem(
                    numero = 1,
                    onClick = { coroutineScope.launch { lazyRowState.scrollToItem(101) } })
            }

            // Conjunto de elementos que se mostraran en la lista
            items(
                items = (2..99).toList(),
                key = { numero -> numero }
            ) { numero ->
                LazyRowItem(numero = numero)
            }

            // Elemento individual de la lista
            item {
                /* Al presionar el elemento de la lista, este realizara un scroll CON animacion
                hacia el inicio de la lista */
                LazyRowItem(
                    numero = 100,
                    onClick = { coroutineScope.launch { lazyRowState.animateScrollToItem(0) } })
            }
        }
    )
}

@Composable
private fun LazyRowItem(numero: Int, onClick: () -> Unit = {}) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(size = 4.dp),
        shadowElevation = 4.dp,
        modifier = Modifier.clickable(enabled = onClick != {}) { onClick() })
    {
        Text("$numero", Modifier.padding(8.dp))
    }
}

@Preview
@Composable
private fun LazyRowExamplePreview() {
    AppTheme {
        LazyRowExample()
    }
}