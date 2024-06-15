package com.example.lazylayouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lazylayouts.ui.LazyColumnExample
import com.example.lazylayouts.ui.LazyHorizontalGridExample
import com.example.lazylayouts.ui.LazyHorizontalStaggeredGridExample
import com.example.lazylayouts.ui.LazyRowExample
import com.example.lazylayouts.ui.LazyVerticalGridExample
import com.example.lazylayouts.ui.LazyVerticalStaggeredGridExample
import com.example.ui.theme.AppTheme
import com.example.ui.ui.DefaultTopAppBar
import com.example.ui.ui.ExampleComponent
import com.example.ui.ui.HorizontalListBanner
import com.example.ui.ui.ThemePreview


/**
 * Example that shows the lazy layouts that can be used with Compose
 */
@Composable
fun LazyLayoutScreen(
    onMenuButtonClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = "Lazy Layouts",
                onMenuButtonClick = onMenuButtonClick,
                // Empty since no seconday screen is used
                onBackButtonPressed = {  }
            )
        }
    ) { innerPadding ->
        LazyLayoutsList(
            modifier = Modifier.padding(innerPadding)
        )
    }
}


/**
 * List of lazy layouts that can be used with Compose
 */
@Composable
private fun LazyLayoutsList(
    modifier: Modifier = Modifier
) {
    // Lazy layouts that can be used with Jetpack Compose
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Lazy lists horizontal banner
        item(key = 1) { HorizontalListBanner(title = "Listas verticales y horizontales") }

        // LazyRow example
        item {
            ExampleComponent(
                title = "LazyRow",
                description = "Un LazyRow es una lista desplazable horizontal que solo renderiza los elementos que se encuentran visibles.",
                content = { LazyRowExample() }
            )
        }

        // LazyColumn example
        item {
            ExampleComponent(
                title = "LazyColumn",
                description = "Un LazyColumn es una lista desplazable vertical que solo renderiza los elementos que se encuentran visibles.",
                content = { LazyColumnExample() }
            )
        }

        // Lazy grids horizontal banner
        item(key = 2) { HorizontalListBanner(title = "Parrillas horizontales y verticales") }

        // LazyHorizontalGrid example
        item {
            ExampleComponent(
                title = "LazyHorizontalGrid",
                description = "Para ordenar una lista de elementos en una malla de forma horizontal, este componente nos da la opción de definir la altura aproximado de los elementos dentro de él",
                content = { LazyHorizontalGridExample() }
            )
        }

        // LazyerticalGrid example
        item {
            ExampleComponent(
                title = "LazyVerticalGrid",
                description = "Para ordenar una lista de elementos en una malla de forma vertical, este componente nos da la opción de definir el ancho aproximado de los elementos dentro de él.",
                content = { LazyVerticalGridExample() }
            )
        }

        // Lazy staggered grids horizontal banner
        item(key = 3) { HorizontalListBanner(title = "Parrillas escalonadas horizontales y verticales") }

        // LazyHorizontalStaggeredGrid example
        item {
            ExampleComponent(
                title = "LazyHorizontalStaggeredGrid",
                description = "Esta API muestra los elementos de igual forma que una LazyHorizontalGrid, con la diferencia de que permite que cada elemento pueda tener un ancho independiente al de los demás elementos de la lista.",
                content = { LazyHorizontalStaggeredGridExample() }
            )
        }

        // LazyVerticalStaggeredGrid example
        item {
            ExampleComponent(
                title = "LazyVerticalStaggeredGrid",
                description = "Esta API muestra los elementos de igual forma que una LazyVerticalGrid, con la diferencia de que permite que cada elemento pueda tener una altura independiente a la de los demás elementos de la lista.",
                content = { LazyVerticalStaggeredGridExample() }
            )
        }
    }
}



@ThemePreview
@Composable
private fun LazyListScreenPreview() {
    AppTheme {
        LazyLayoutScreen(
            onMenuButtonClick = {}
        )
    }
}