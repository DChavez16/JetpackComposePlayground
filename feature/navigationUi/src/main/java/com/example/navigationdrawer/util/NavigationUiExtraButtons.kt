package com.example.navigationdrawer.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.example.ui.theme.PreviewAppTheme


@Composable
internal fun NavigationUiExtraButtons(
    isDarkTheme: () -> Boolean,
    enableConfigurationButton: () -> Boolean,
    onConfigurationButtonClick: () -> Unit,
    onThemeButtonClick: (Boolean) -> Unit,
    windowSizeClass: () -> WindowSizeClass
) {

    // If the Window Width Class is Medium, arrange the buttons vertically
    // Else, arrange the buttons horizontally
    if (windowSizeClass().windowWidthSizeClass == WindowWidthSizeClass.MEDIUM) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
        ) {
            // Show the configuration button
            ConfigurationExtraButton(
                enableConfigurationButton = enableConfigurationButton,
                onConfigurationButtonClick = onConfigurationButtonClick
            )

            // Show the theme button
            ThemeExtraButton(
                isDarkTheme = isDarkTheme,
                onThemeButtonClick = onThemeButtonClick
            )
        }
    } else {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
        ) {
            // Show the configuration button
            ConfigurationExtraButton(
                enableConfigurationButton = enableConfigurationButton,
                onConfigurationButtonClick = onConfigurationButtonClick
            )

            // Show the theme button
            ThemeExtraButton(
                isDarkTheme = isDarkTheme,
                onThemeButtonClick = onThemeButtonClick
            )
        }
    }
}


@Composable
private fun ConfigurationExtraButton(
    enableConfigurationButton: () -> Boolean,
    onConfigurationButtonClick: () -> Unit
) {
    // Configuration button
    IconButton(
        onClick = {
            onConfigurationButtonClick()
        },
        // Disable when the current destination is the configuration screen
        enabled = enableConfigurationButton(),
        modifier = Modifier
            .size(48.dp)
            .semantics(
                mergeDescendants = true
            ) {
                contentDescription = "Configuration"
            }
    ) {
        Icon(
            imageVector = Icons.Rounded.Settings,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier
                .alpha(
                    if (enableConfigurationButton()) 1f else 0.5f
                )
        )
    }
}


@Composable
private fun ThemeExtraButton(
    isDarkTheme: () -> Boolean,
    onThemeButtonClick: (Boolean) -> Unit
) {
    val iconContentDescription = "Change to ${if (isDarkTheme()) "light" else "dark"} theme"

    // Theme button
    IconButton(
        onClick = {
            onThemeButtonClick(!isDarkTheme())
        },
        modifier = Modifier
            .size(48.dp)
            .semantics(
                mergeDescendants = true
            ) {
                testTag = "ChangeThemeIconButton"
                contentDescription = iconContentDescription
            }
    ) {
        Icon(
            imageVector = with(Icons.Rounded) {
                if (isDarkTheme()) DarkMode else LightMode
            },
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}


/*
Previews
 */
@Preview
@Composable
private fun NavigateUiExtraButtonsPreview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        // Light Theme
        PreviewAppTheme(
            darkTheme = false
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(4.dp)
            ) {
                // Configuration button
                ConfigurationExtraButton(
                    enableConfigurationButton = { true },
                    onConfigurationButtonClick = {}
                )

                // Theme button
                ThemeExtraButton(
                    isDarkTheme = { false },
                    onThemeButtonClick = {}
                )
            }
        }

        // Dark Theme
        PreviewAppTheme(
            darkTheme = true
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(4.dp)
            ) {
                // Configuration button
                ConfigurationExtraButton(
                    enableConfigurationButton = { true },
                    onConfigurationButtonClick = {}
                )

                // Theme button
                ThemeExtraButton(
                    isDarkTheme = { true },
                    onThemeButtonClick = {}
                )
            }
        }
    }
}