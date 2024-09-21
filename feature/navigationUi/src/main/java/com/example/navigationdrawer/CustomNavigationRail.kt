package com.example.navigationdrawer

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.window.core.layout.WindowSizeClass
import com.example.navigationdrawer.util.NavigationUiExtraButtons
import com.example.ui.theme.PreviewAppTheme
import com.example.ui.ui.MediumSizeScreenThemePreview
import com.example.util.RootNavigationDestination


@Composable
fun CustomNavigationRail(
    currentSelectedItem: () -> RootNavigationDestination,
    onRailItemClick: (RootNavigationDestination) -> Unit,
    onConfigurationButtonClick: () -> Unit,
    navigationDrawerViewModel: NavigationUIViewModel = hiltViewModel()
) {

    // TODO Update to the new design guidelines

    // Value that indicates if the current theme is dark or not
    val isDarkTheme by navigationDrawerViewModel.darkThemeFlow.collectAsState()

    val navigationExtraButtonsWindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    // Calls the navigation rail content
    NavigationRailContent(
        currentSelectedItem = currentSelectedItem,
        isDarkTheme = { isDarkTheme },
        navigationExtraButtonsWindowSizeClass = { navigationExtraButtonsWindowSizeClass },
        onRailItemClick = onRailItemClick,
        onConfigurationButtonClick = onConfigurationButtonClick,
        onThemeButtonClick = navigationDrawerViewModel::updateDarkTheme
    )
}

@Composable
private fun NavigationRailContent(
    currentSelectedItem: () -> RootNavigationDestination,
    isDarkTheme: () -> Boolean,
    navigationExtraButtonsWindowSizeClass: () -> WindowSizeClass,
    onRailItemClick: (RootNavigationDestination) -> Unit,
    onConfigurationButtonClick: () -> Unit,
    onThemeButtonClick: (Boolean) -> Unit
) {
    NavigationRail(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        modifier = Modifier
            .fillMaxHeight()
            .padding(4.dp)
            .width(56.dp)
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
        NavigationUiExtraButtons(
            isDarkTheme = isDarkTheme,
            enableConfigurationButton = { currentSelectedItem() != RootNavigationDestination.Configuration },
            onConfigurationButtonClick = onConfigurationButtonClick,
            onThemeButtonClick = onThemeButtonClick,
            windowSizeClass = navigationExtraButtonsWindowSizeClass
        )
    }
}


@Composable
private fun NavigationRailContentItems(
    currentSelectedItem: () -> RootNavigationDestination,
    onRailItemClicked: (RootNavigationDestination) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .semantics { testTag = "drawerNavigationItems" }
    ) {
        items(
            items = RootNavigationDestination.entries.filter { it.itemTitle != null },
            key = { it.ordinal }
        ) { drawerItem ->
            // Navigation Rail Item icon button
            IconButton(
                onClick = { onRailItemClicked(drawerItem) },
                colors = IconButtonDefaults.iconButtonColors().copy(
                    containerColor = with(MaterialTheme.colorScheme) {
                        // If is selected, set color to primary, else, set Color.Transparent
                        if (drawerItem == currentSelectedItem()) primary else Color.Transparent
                    },
                    contentColor = with(MaterialTheme.colorScheme) {
                        // If is selected, set color to primaryContainer, else, set to onPrimaryContainer
                        if (drawerItem == currentSelectedItem()) primaryContainer else onPrimaryContainer
                    }
                ),
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = drawerItem.itemIcon() ?: Icons.Rounded.Settings,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}




@MediumSizeScreenThemePreview
@Composable
private fun NavigationRailContentPreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {

        val isDarkTheme = isSystemInDarkTheme()
        val navigationExtraButtonsWindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

        NavigationRailContent(
            currentSelectedItem = { RootNavigationDestination.LazyLayouts },
            isDarkTheme = { isDarkTheme },
            navigationExtraButtonsWindowSizeClass = { navigationExtraButtonsWindowSizeClass },
            onRailItemClick = {},
            onConfigurationButtonClick = {},
            onThemeButtonClick = {}
        )
    }
}