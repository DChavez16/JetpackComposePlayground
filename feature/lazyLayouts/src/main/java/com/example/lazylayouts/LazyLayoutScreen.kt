@file:OptIn(ExperimentalFoundationApi::class)

package com.example.lazylayouts

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.lazyLayouts.R
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
import com.example.ui.ui.CompactSizeScreenThemePreview


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
        stickyHeader(key = 1) {
            HorizontalListBanner(title = stringResource(R.string.lazy_layouts_list_banner_1))
        }

        // LazyRow example
        item {
            ExampleComponent(
                title = stringResource(R.string.lazy_layouts_lazy_row_title),
                description = stringResource(R.string.lazy_layouts_lazy_row_description),
                content = { LazyRowExample() }
            )
        }

        // LazyColumn example
        item {
            ExampleComponent(
                title = stringResource(R.string.lazy_layouts_lazy_column_title),
                description = stringResource(R.string.lazy_layouts_lazy_column_description),
                content = { LazyColumnExample() }
            )
        }

        // Lazy grids horizontal banner
        stickyHeader(key = 2) {
            HorizontalListBanner(title = stringResource(R.string.lazy_layouts_list_banner_2))
        }

        // LazyHorizontalGrid example
        item {
            ExampleComponent(
                title = stringResource(R.string.lazy_layouts_lazy_horizontal_grid_title),
                description = stringResource(R.string.lazy_layouts_lazy_horizontal_grid_description),
                content = { LazyHorizontalGridExample() }
            )
        }

        // TODO Replace all plain text with string resources
        // TODO Refactorize the screen so it follows a testable approach
        // TODO Refactorize to be more performant (less recompositions)
        // LazyerticalGrid example
        item {
            ExampleComponent(
                title = "LazyVerticalGrid",
                description = "Para ordenar una lista de elementos en una malla de forma vertical, este componente nos da la opción de definir el ancho aproximado de los elementos dentro de él.",
                content = { LazyVerticalGridExample() }
            )
        }

        // TODO Replace all plain text with string resources
        // Lazy staggered grids horizontal banner
        stickyHeader(key = 3) {
            HorizontalListBanner(title = "Parrillas escalonadas horizontales y verticales")
        }

        // TODO Replace all plain text with string resources
        // TODO Refactorize the screen so it follows a testable approach
        // TODO Refactorize to be more performant (less recompositions)
        // LazyHorizontalStaggeredGrid example
        item {
            ExampleComponent(
                title = "LazyHorizontalStaggeredGrid",
                description = "Esta API muestra los elementos de igual forma que una LazyHorizontalGrid, con la diferencia de que permite que cada elemento pueda tener un ancho independiente al de los demás elementos de la lista.",
                content = { LazyHorizontalStaggeredGridExample() }
            )
        }

        // TODO Replace all plain text with string resources
        // TODO Refactorize the screen so it follows a testable approach
        // TODO Refactorize to be more performant (less recompositions)
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



@CompactSizeScreenThemePreview
@Composable
private fun LazyListScreenPreview() {
    AppTheme {
        LazyLayoutScreen(
            onMenuButtonClick = {}
        )
    }
}