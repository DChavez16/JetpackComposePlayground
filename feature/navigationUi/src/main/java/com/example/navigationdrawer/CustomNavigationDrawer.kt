@file:OptIn(ExperimentalFoundationApi::class)

package com.example.navigationdrawer

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.window.core.layout.WindowSizeClass
import com.example.navigationdrawer.util.NavigationUiExtraButtons
import com.example.ui.theme.PreviewAppTheme
import com.example.ui.ui.CompactSizeScreenThemePreview
import com.example.ui.ui.ExpandedSizeScreenThemePreview
import com.example.util.RootNavigationDestination


@Composable
fun CustomNavigationDrawer(
    currentSelectedItem: () -> RootNavigationDestination,
    onDrawerItemClick: (RootNavigationDestination) -> Unit,
    onConfigurationButtonClick: () -> Unit,
    navigationDrawerViewModel: NavigationUIViewModel = hiltViewModel()
) {
    // Value that indicates if the current theme is dark or not
    val isDarkTheme by navigationDrawerViewModel.darkThemeFlow.collectAsState()

    val navigationExtraButtonsWindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    // Calls the navigation drawer content
    NavigationDrawerContent(
        currentSelectedItem = currentSelectedItem,
        isDarkTheme = { isDarkTheme },
        navigationExtraButtonsWindowSizeClass = { navigationExtraButtonsWindowSizeClass },
        onDrawerItemClick = onDrawerItemClick,
        onConfigurationButtonClick = onConfigurationButtonClick,
        onThemeButtonClick = navigationDrawerViewModel::updateDarkTheme
    )
}

@Composable
private fun NavigationDrawerContent(
    currentSelectedItem: () -> RootNavigationDestination,
    isDarkTheme: () -> Boolean,
    navigationExtraButtonsWindowSizeClass: () -> WindowSizeClass,
    onDrawerItemClick: (RootNavigationDestination) -> Unit,
    onConfigurationButtonClick: () -> Unit,
    onThemeButtonClick: (Boolean) -> Unit
) {

    /*  Main column of the navigation drawer content. It holds the following:
        1 - The name of the app at the top
        2 - An horizontal separator line
        3 - The navigation items just under the separator line
        4 - A row of configuration and theme buttons in the bottom
     */
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxHeight()
            .background(
                color = MaterialTheme.colorScheme.primaryContainer
            )
            .width(250.dp)
    ) {
        // Drawer header and navigation items
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            stickyHeader(key = 0) {
                Column {
                    // App's Name
                    DrawerContentAppName()

                    // Horizontal separator line
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            }

            item(key = 1) {
                // Navigation items
                DrawerContentNavigationItems(
                    currentSelectedItem = currentSelectedItem,
                    onDrawerItemClicked = onDrawerItemClick
                )
            }
        }

        // Configuration and theme buttons
        NavigationUiExtraButtons(
            isDarkTheme = { isDarkTheme() },
            enableConfigurationButton = { currentSelectedItem() != RootNavigationDestination.Configuration },
            onConfigurationButtonClick = onConfigurationButtonClick,
            onThemeButtonClick = onThemeButtonClick,
            windowSizeClass = navigationExtraButtonsWindowSizeClass
        )
    }
}


@Composable
private fun DrawerContentAppName() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 8.dp)
    ) {
        // App Image
        Image(
            painter = painterResource(id = R.drawable.jetpack_compose_icon),
            contentDescription = null,
            modifier = Modifier.size(64.dp)
        )

        // App Name
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}


@Composable
private fun DrawerContentNavigationItems(
    currentSelectedItem: () -> RootNavigationDestination,
    onDrawerItemClicked: (RootNavigationDestination) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .semantics { testTag = "drawerNavigationItems" }
    ) {
        for (drawerItem in RootNavigationDestination.entries.filter { it.itemTitle != null }) {
            // Get navigation item title as String
            val navigationItemTitle = stringResource(drawerItem.itemTitle ?: 0)

            val elementBackgroundColor = MaterialTheme.colorScheme.primary

            // Individual element in the drawer
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        drawRoundRect(
                            color = if (currentSelectedItem() == drawerItem) elementBackgroundColor
                            else Color.Transparent,
                            cornerRadius = CornerRadius(
                                x = 20.dp.toPx(),
                                y = 20.dp.toPx()
                            )
                        )
                    }
                    .clickable(
                        onClick = { onDrawerItemClicked(drawerItem) },
                        onClickLabel = navigationItemTitle
                    )
                    .padding(
                        vertical = 4.dp,
                        horizontal = 4.dp
                    )
                    .semantics(mergeDescendants = true) { contentDescription = navigationItemTitle }
            ) {
                // Drawer element icon
                Icon(
                    imageVector = drawerItem.itemIcon() ?: Icons.Default.Settings,
                    contentDescription = null,
                    tint = with(MaterialTheme.colorScheme) {
                        if (currentSelectedItem() == drawerItem) primaryContainer else onPrimaryContainer
                    },
                    modifier = Modifier.size(32.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Drawer element title
                Text(
                    text = navigationItemTitle,
                    style = MaterialTheme.typography.titleSmall,
                    color = with(MaterialTheme.colorScheme) {
                        if (currentSelectedItem() == drawerItem) primaryContainer else onPrimaryContainer
                    }
                )
            }
        }
    }
}


/*
Previews
 */
@CompactSizeScreenThemePreview
@Composable
private fun NavigationDrawerCompactSizePreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {

        val isDarkTheme = isSystemInDarkTheme()
        val navigationExtraButtonsWindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Transparent)
        ) {
            NavigationDrawerContent(
                currentSelectedItem = { RootNavigationDestination.LazyLayouts },
                isDarkTheme = { isDarkTheme },
                navigationExtraButtonsWindowSizeClass = { navigationExtraButtonsWindowSizeClass },
                onDrawerItemClick = {},
                onConfigurationButtonClick = {},
                onThemeButtonClick = {}
            )
        }
    }
}


@ExpandedSizeScreenThemePreview
@Composable
private fun NavigationDrawerExpandedSizePreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {

        val isDarkTheme = isSystemInDarkTheme()
        val navigationExtraButtonsWindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Transparent)
        ) {
            NavigationDrawerContent(
                currentSelectedItem = { RootNavigationDestination.LazyLayouts },
                isDarkTheme = { isDarkTheme },
                navigationExtraButtonsWindowSizeClass = { navigationExtraButtonsWindowSizeClass },
                onDrawerItemClick = {},
                onConfigurationButtonClick = {},
                onThemeButtonClick = {}
            )
        }
    }
}