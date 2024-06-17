package com.example.navigationdrawer.util

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.example.navigationdrawer.R


@Composable
internal fun NavigationUiExtraButtons(
    isDarkTheme: Boolean,
    enableConfigurationButton: Boolean,
    onConfigurationButtonClick: () -> Unit,
    onThemeButtonClick: (Boolean) -> Unit,
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
) {

    // If the Window Width Class is Medium, arrange the buttons vertically
    // Else, arrange the buttons horizontally
    if(windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.MEDIUM) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            // Show the theme button
            ThemeExtraButton(
                isDarkTheme = isDarkTheme,
                onThemeButtonClick = onThemeButtonClick
            )

            // Show the configuration button
            ConfigurationExtraButton(
                enableConfigurationButton = enableConfigurationButton,
                onConfigurationButtonClick = onConfigurationButtonClick
            )
        }
    } else {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
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
    enableConfigurationButton: Boolean,
    onConfigurationButtonClick: () -> Unit
) {
    // Configuration button
    IconButton(
        onClick = {
            onConfigurationButtonClick()
        },
        // Disable when the current destination is the configuration screen
        enabled = enableConfigurationButton,
        modifier = Modifier.semantics(
            mergeDescendants = true
        ) {
            contentDescription = "Configuration"
        }
    ) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.alpha(
                if (enableConfigurationButton) 1f else 0.5f
            )
        )
    }
}


@Composable
private fun ThemeExtraButton(
    isDarkTheme: Boolean,
    onThemeButtonClick: (Boolean) -> Unit
) {
    val iconContentDescription = "Change to ${if (isDarkTheme) "light" else "dark"} theme"

    // Theme button
    IconButton(
        onClick = {
            onThemeButtonClick(!isDarkTheme)
        },
        modifier = Modifier.semantics(
            mergeDescendants = true
        ) {
            testTag = "ChangeThemeIconButton"
            contentDescription = iconContentDescription
        }
    ) {
        Icon(
            painter = painterResource(
                id = if (isDarkTheme) R.drawable.night_mode
                else R.drawable.light_mode
            ),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}