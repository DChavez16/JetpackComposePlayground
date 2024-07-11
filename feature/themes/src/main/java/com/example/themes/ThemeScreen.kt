package com.example.themes

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import com.example.themes.ui.CardExample
import com.example.themes.ui.FABExample
import com.example.themes.ui.SurfaceExample
import com.example.ui.theme.PreviewAppTheme
import com.example.ui.ui.CompactSizeScreenThemePreview
import com.example.ui.ui.DefaultTopAppBar
import com.example.ui.ui.ExampleComponent

/**
 * This example displays some components with a ColorScheme set, as the visualization of a DynamicTheme
 * It describes too the implementation of Typography styles to the text in the app.
 * Also shows how the components can be set different Shapes based on their function.
 */
@Composable
fun ThemeScreen(
    onMenuButtonClick: () -> Unit
) {

    val topAppBarTitle = stringResource(R.string.theme_title)

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = { topAppBarTitle },
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
                title = stringResource(R.string.theme_surface_title),
                description = stringResource(R.string.theme_surface_description),
                content = { SurfaceExample() }
            )
        }

        // FloatingActionButton example
        item {
            ExampleComponent(
                title = stringResource(R.string.theme_fab_title),
                description = stringResource(R.string.theme_fab_description),
                content = { FABExample() }
            )
        }

        // Card example
        item {
            ExampleComponent(
                title = stringResource(R.string.theme_card_title),
                description = stringResource(R.string.theme_card_description),
                content = { CardExample() }
            )
        }
    }
}




@CompactSizeScreenThemePreview
@Composable
private fun AppThemeExamplePreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        ThemeScreen(
            onMenuButtonClick = {}
        )
    }
}