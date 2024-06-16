package com.example.navigationdrawer

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ui.theme.PreviewAppTheme
import com.example.ui.ui.CompactSizeScreenThemePreview
import com.example.ui.ui.ExpandedSizeScreenThemePreview
import com.example.util.RootNavigationDestination


@Composable
fun CustomNavigationDrawer(
    currentSelectedItem: RootNavigationDestination,
    isWidthScreenExpanded: Boolean = false,
    onDrawerItemClick: (RootNavigationDestination) -> Unit,
    onConfigurationButtonClick: () -> Unit,
    navigationDrawerViewModel: NavigationUIViewModel = hiltViewModel()
) {
    // Value that indicates if the current theme is dark or not
    val isDarkTheme by navigationDrawerViewModel.darkThemeFlow.collectAsState()

    // Calls the navigation drawer content
    NavigationDrawerContent(
        currentSelectedItem = currentSelectedItem,
        isDarkTheme = isDarkTheme,
        isWidthScreenExpanded = isWidthScreenExpanded,
        onDrawerItemClick = onDrawerItemClick,
        onConfigurationButtonClick = onConfigurationButtonClick,
        onThemeButtonClick = navigationDrawerViewModel::updateDarkTheme
    )
}

@Composable
private fun NavigationDrawerContent(
    currentSelectedItem: RootNavigationDestination,
    isDarkTheme: Boolean,
    isWidthScreenExpanded: Boolean,
    onDrawerItemClick: (RootNavigationDestination) -> Unit,
    onConfigurationButtonClick: () -> Unit,
    onThemeButtonClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    /*  Main column of the navigation drawer content. It holds the following:
        1 - The name of the app at the top
        2 - An horizontal separator line
        3 - The navigation items just under the separator line
        4 - A row of configuration and theme buttons in the bottom
     */
    Column(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth(
                if(isWidthScreenExpanded) 0.225f else 0.75f
            )
            .background(color = MaterialTheme.colorScheme.primaryContainer)
    ) {
        // App's Name
        DrawerContentAppName()

        // Horizontal separator line
        HorizontalDivider(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        // Navigation items
        DrawerContentNavigationItems(
            currentSelectedItem = currentSelectedItem,
            onDrawerItemClicked = onDrawerItemClick
        )

        Spacer(
            modifier = Modifier.weight(1f)
        )

        // Configuration and theme buttons
        DrawerContentBottomButtons(
            isDarkTheme = isDarkTheme,
            onConfigurationButtonClick = onConfigurationButtonClick,
            onThemeButtonClick = onThemeButtonClick
        )
    }
}


@Composable
private fun DrawerContentAppName() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
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
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}


@Composable
private fun DrawerContentNavigationItems(
    currentSelectedItem: RootNavigationDestination,
    onDrawerItemClicked: (RootNavigationDestination) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .semantics { testTag = "drawerNavigationItems" }
    ) {
        for (drawerItem in RootNavigationDestination.entries.filter { it.itemTitle != null }) {
            // Get navigation item title as String
            val navigationItemTitle = stringResource(drawerItem.itemTitle ?: 0)

            // Individual element in the drawer
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = if (currentSelectedItem == drawerItem) MaterialTheme.colorScheme.primary
                        else Color.Transparent
                    )
                    .clickable(
                        onClick = { onDrawerItemClicked(drawerItem) },
                        onClickLabel = navigationItemTitle
                    )
                    .padding(
                        vertical = 6.dp,
                        horizontal = 12.dp
                    )
                    .semantics(mergeDescendants = true) { contentDescription = navigationItemTitle }
            ) {
                // Drawer element image
                Image(
                    painter = painterResource(drawerItem.itemIcon ?: 0),
                    contentDescription = null,
                    modifier = Modifier
                        .size(32.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Drawer element title
                Text(
                    text = navigationItemTitle,
                    style = MaterialTheme.typography.bodyLarge,
                    color = with(MaterialTheme.colorScheme) {
                        if (currentSelectedItem == drawerItem) onPrimary else onPrimaryContainer
                    }
                )
            }
        }
    }
}


@Composable
private fun DrawerContentBottomButtons(
    isDarkTheme: Boolean,
    onConfigurationButtonClick: () -> Unit,
    onThemeButtonClick: (Boolean) -> Unit
) {
    val iconContentDescription = "Change to ${if (isDarkTheme) "light" else "dark"} theme"

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Configuration button
        IconButton(
            onClick = {
                onConfigurationButtonClick()
            },
            modifier = Modifier.semantics(
                mergeDescendants = true
            ) {
                contentDescription = "Configuration"
            }
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

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
}


@CompactSizeScreenThemePreview
@Composable
private fun NavigationDrawerCompactSizePreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Transparent)
        ) {
            NavigationDrawerContent(
                currentSelectedItem = RootNavigationDestination.LazyLayouts,
                isDarkTheme = isSystemInDarkTheme(),
                isWidthScreenExpanded = false,
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Transparent)
        ) {
            NavigationDrawerContent(
                currentSelectedItem = RootNavigationDestination.LazyLayouts,
                isDarkTheme = isSystemInDarkTheme(),
                isWidthScreenExpanded = true,
                onDrawerItemClick = {},
                onConfigurationButtonClick = {},
                onThemeButtonClick = {}
            )
        }
    }
}