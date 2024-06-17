package com.example.navigationdrawer

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ui.theme.PreviewAppTheme
import com.example.ui.ui.MediumSizeScreenThemePreview
import com.example.util.RootNavigationDestination


@Composable
fun CustomNavigationRail(
    currentSelectedItem: RootNavigationDestination,
    onRailItemClick: (RootNavigationDestination) -> Unit,
    onConfigurationButtonClick: () -> Unit,
    navigationDrawerViewModel: NavigationUIViewModel = hiltViewModel()
) {
    // Value that indicates if the current theme is dark or not
    val isDarkTheme by navigationDrawerViewModel.darkThemeFlow.collectAsState()

    // Calls the navigation rail content
    NavigationRailContent(
        currentSelectedItem = currentSelectedItem,
        isDarkTheme = isDarkTheme,
        onRailItemClick = onRailItemClick,
        onConfigurationButtonClick = onConfigurationButtonClick,
        onThemeButtonClick = navigationDrawerViewModel::updateDarkTheme
    )
}

@Composable
private fun NavigationRailContent(
    currentSelectedItem: RootNavigationDestination,
    isDarkTheme: Boolean,
    onRailItemClick: (RootNavigationDestination) -> Unit,
    onConfigurationButtonClick: () -> Unit,
    onThemeButtonClick: (Boolean) -> Unit
) {
    NavigationRail(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        modifier = Modifier
            .fillMaxHeight()
            .width(64.dp)
    ) {
        // Navigation Rail Items
        NavigationRailContentItems(
            currentSelectedItem = currentSelectedItem,
            onRailItemClicked = onRailItemClick
        )

        Spacer(
            modifier = Modifier.weight(1f)
        )

        // Extra Rail Buttons
        NavigationRailContentExtraButtons(
            isDarkTheme = isDarkTheme,
            enableConfigurationButton = currentSelectedItem != RootNavigationDestination.Configuration,
            onConfigurationButtonClick = onConfigurationButtonClick,
            onThemeButtonClick = onThemeButtonClick
        )
    }
}


@Composable
private fun NavigationRailContentItems(
    currentSelectedItem: RootNavigationDestination,
    onRailItemClicked: (RootNavigationDestination) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .semantics { testTag = "drawerNavigationItems" }
    ) {
        for (drawerItem in RootNavigationDestination.entries.filter { it.itemTitle != null })
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .height(40.dp)
            ) {
                NavigationRailItem(
                    selected = drawerItem == currentSelectedItem,
                    onClick = { onRailItemClicked(drawerItem) },
                    icon = {
                        Icon(
                            imageVector = drawerItem.itemIcon ?: Icons.Default.Settings,
                            contentDescription = null,
                            modifier = Modifier.size(28.dp)
                        )
                    },
                    label = null,
                    colors = NavigationRailItemColors(
                        selectedIconColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedTextColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedIndicatorColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        unselectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        disabledIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        disabledTextColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
    }
}


@Composable
private fun NavigationRailContentExtraButtons(
    isDarkTheme: Boolean,
    enableConfigurationButton: Boolean,
    onConfigurationButtonClick: () -> Unit,
    onThemeButtonClick: (Boolean) -> Unit
) {
    val iconContentDescription = "Change to ${if (isDarkTheme) "light" else "dark"} theme"

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Theme Button
        IconButton(
            onClick = {
                onThemeButtonClick(!isDarkTheme)
            },
            modifier = Modifier
                .size(28.dp)
                .semantics(
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

        // Configuration Button
        IconButton(
            onClick = {
                onConfigurationButtonClick()
            },
            enabled = enableConfigurationButton,
            modifier = Modifier
                .size(28.dp)
                .semantics(
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
}


@MediumSizeScreenThemePreview
@Composable
private fun NavigationRailContentPreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        NavigationRailContent(
            currentSelectedItem = RootNavigationDestination.LazyLayouts,
            isDarkTheme = isSystemInDarkTheme(),
            onRailItemClick = {},
            onConfigurationButtonClick = {},
            onThemeButtonClick = {}
        )
    }
}