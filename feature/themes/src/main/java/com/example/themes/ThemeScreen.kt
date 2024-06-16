package com.example.themes

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
import com.example.themes.ui.CardExample
import com.example.themes.ui.FABExample
import com.example.themes.ui.SurfaceExample
import com.example.ui.theme.AppTheme
import com.example.ui.ui.DefaultTopAppBar
import com.example.ui.ui.ExampleComponent
import com.example.ui.ui.CompactSizeScreenThemePreview

/**
 * This example displays some components with a ColorScheme set, as the visualization of a DynamicTheme
 * It describes too the implementation of Typography styles to the text in the app.
 * Also shows how the components can be set different Shapes based on their function.
 */
@Composable
fun ThemeScreen(
    onMenuButtonClick: () -> Unit
) {
    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = "Themes",
                onMenuButtonClick = onMenuButtonClick,
                // Empty since no seconday screen is used
                onBackButtonPressed = {}
            )
        }
    ) { innerPadding ->
        ThemesList(
            modifier = Modifier.padding(innerPadding)
        )
    }
}


/**
 * List of Material components with themes applied
 */
@Composable
private fun ThemesList(
    modifier: Modifier = Modifier
) {
    // List of individual components with themes applied
    LazyColumn(
        // A ColorScheme is set to the LazyColumn's background
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Surface and layouts example
        item {
            ExampleComponent(
                title = "Surface",
                description = "A los Composable que representan layouts (Row, Column, LazyRow, LazyColumn, etc.) se les puede asignar un color como parte del parametro background del parametro Modifier del Layout.\nEn este ejemplo se muestra un Row de 5 numeros con un ColorScheme surfaceVariant aplicado como background",
                content = { SurfaceExample() }
            )
        }

        // FloatingActionButton example
        item {
            ExampleComponent(
                title = "FloatingActionButton",
                description = "A los FloatingActionButton se les asigna un esquema de color por medio de sus parametros containerColor (Para el elemento del boton que representa el contenedor) y contentColor(Para los elementos dentro del contenedor como iconos o texto). En cuanto a colores se aplica el colorScheme primary o primaryContainer a SmallFAB y ExtendedFAB, se aplica secondary o secondaryContainer a FAB y tertiary o tertiaryContainer a LargeFAB",
                content = { FABExample() }
            )
        }

        // Card example
        item {
            ExampleComponent(
                title = "Card",
                description = "A las Card se les asigna un esquema de color por medio de su parametro colors, el cual recibe informacion de tipo cardColors, en el que se puede definir el container Color y contentColor del componente.",
                content = { CardExample() }
            )
        }
    }
}



@CompactSizeScreenThemePreview
@Composable
private fun AppThemeExamplePreview() {
    AppTheme {
        ThemeScreen(
            onMenuButtonClick = {}
        )
    }
}