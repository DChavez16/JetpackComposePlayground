@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)

package com.example.ui.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.example.ui.theme.PreviewAppTheme


/**
 * Default TopAppBar to be used across the app, it provides a menu or back button slot based on the screen hierarchy, also makes room for an optional action button; Handles visibility animations for the icon buttons and content animation for title changes.
 * @param title The title of the screen centered in the TopAppBar, displays an animation when changed.
 * @param onMenuButtonClick Callback activaded when the menu button in the principal screen's TopAppBar is pressed.
 * @param onBackButtonPressed Callback activaded when the back button in the secondary screen's TopAppBar is pressed.
 * @param isPrincipalScreen Optional value that indicates if the TopAppBar content will be for a principal or secondary screen, the default value is true.
 * @param actionButtonIcon Optional icon to be displayed as the action button in the TopAppBar, show only if the value is not null, the default value is null.
 * @param onActionButtonClick Optional action button callback, default value is empty.
 * @param actionButtonContentDescription Optional content description for the action button, default value is null.
 * @param windowSizeClass Observes the current Window Size Class, used to determine the visibility of the menu button
 */
@Composable
fun DefaultTopAppBar(
    title: () -> String,
    onMenuButtonClick: () -> Unit,
    onBackButtonPressed: () -> Unit,
    isPrincipalScreen: () -> Boolean = { true },
    actionButtonIcon: () -> ImageVector? = { null },
    onActionButtonClick: () -> Unit = {},
    actionButtonContentDescription: () -> String? = { null },
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
) {
    CenterAlignedTopAppBar(
        title = {
            // Handles TopAppBar's title content animation
            TopAppBarTitle(
                title = title
            )
        },
        navigationIcon = {
            /*
             Since both icons show at different [isPrincipalScreen] values, they will never overlap
             */

            // Shows the menu button icon if the principal screen is focused, and handles its visibility animation
            // Only show the menu button whe WindowWidthClass is Compact
            if(windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT) {
                MenuIconButton(
                    isPrincipalScreen = isPrincipalScreen,
                    onMenuButtonClick = onMenuButtonClick
                )
            }

            // Shows the back button icon if a secondary screen is focused, and handles its visibility animation
            BackIconButton(
                isPrincipalScreen = isPrincipalScreen,
                onBackButtonClick = onBackButtonPressed
            )
        },
        actions = {
            // Handles the optional action button and its visibility animation, it can safely been set as null
            ActionIconButton(
                icon = actionButtonIcon,
                onClick = onActionButtonClick,
                contentDescription = actionButtonContentDescription()
            )
        },
        colors = TopAppBarColors(
            /*
             Top app bar colors based on the app's theme branding
             */
            containerColor = MaterialTheme.colorScheme.primary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
            scrolledContainerColor = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun TopAppBarTitle(
    title: () -> String
) {
    // Changes the content based on the provided title, with an transition animation with each change
    AnimatedContent(
        targetState = title,
        /*
        The old title will fade out and slide out to the top
        The new title will fade in and slide in from the top to the bottom
         */
        transitionSpec = {
            // New title transition
            (slideInVertically(
                animationSpec = tween(
                    durationMillis = 500
                ),
                initialOffsetY = { -it }
            ) + fadeIn()).togetherWith(
                // Old title transition
                slideOutVertically(
                    animationSpec = tween(
                        durationMillis = 500
                    ),
                    targetOffsetY = { -it }
                ) + fadeOut()
            ).using(
                // Avoid wider titles from cliping when sliding in or out
                SizeTransform(clip = false)
            )
        },
        contentAlignment = Alignment.Center,
        label = "TopAppBar Title Animated Content"
    ) { topAppBarTitle ->
        Text(
            text = topAppBarTitle(),
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            modifier = Modifier.semantics { testTag = "TopAppBarTitle" }
        )
    }
}

@Composable
private fun MenuIconButton(
    isPrincipalScreen: () -> Boolean,
    onMenuButtonClick: () -> Unit
) {
    /*
     Makes the menu button visible if the [isPrincipalScreen] value is true
     The icon slides to the left and fade out when hiding
     and slides from the left to the right while fading in when is being showed
     */
    AnimatedVisibility(
        visible = isPrincipalScreen(),
        enter = slideInHorizontally(
            animationSpec = tween(
                durationMillis = 500
            ),
            initialOffsetX = { -it }
        ) + fadeIn(),
        exit = slideOutHorizontally(
            animationSpec = tween(
                durationMillis = 500
            ),
            targetOffsetX = { -it }
        ) + fadeOut(),
        label = "Menu Button Animated Visibility"
    ) {
        IconButton(
            onClick = { onMenuButtonClick() },
            modifier = Modifier.semantics { contentDescription = "Open Menu" }
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun BackIconButton(
    isPrincipalScreen: () -> Boolean,
    onBackButtonClick: () -> Unit
) {
    /*
     Makes the back button visible if the [isPrincipalScreen] value is false
     The icon slides to the left and fade out when hiding
     and slides from the left to the right while fading in when is being showed
     */
    AnimatedVisibility(
        visible = !isPrincipalScreen(),
        enter = slideInHorizontally(
            animationSpec = tween(
                durationMillis = 500
            ),
            initialOffsetX = { -it }
        ) + fadeIn(),
        exit = slideOutHorizontally(
            animationSpec = tween(
                durationMillis = 500
            ),
            targetOffsetX = { -it }
        ) + fadeOut(),
        label = "Back Button Animated Visibility"
    ) {
        IconButton(
            onClick = { onBackButtonClick() },
            modifier = Modifier.semantics { contentDescription = "Return to the previous screen" }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun ActionIconButton(
    icon: () -> ImageVector? = { null },
    onClick: () -> Unit = {},
    contentDescription: String? = null
) {
    /*
     Makes the action button visible if one was provided
     The icon slides to the right and fade out when hiding
     and slides from the right to the left while fading in when is being showed
     */
    AnimatedVisibility(
        visible = icon() != null,
        enter = slideInHorizontally(
            animationSpec = tween(
                durationMillis = 500
            ),
            initialOffsetX = { it }
        ) + fadeIn(),
        exit = slideOutHorizontally(
            animationSpec = tween(
                durationMillis = 500
            ),
            targetOffsetX = { it }
        ) + fadeOut(),
        label = "Action Button Animated Visibility"
    ) {
        if (icon() != null) {
            IconButton(onClick = { onClick() }) {
                Icon(
                    imageVector = icon()!!,
                    contentDescription = contentDescription
                )
            }
        }
    }
}




@CompactSizeScreenThemePreview
@Composable
private fun DefaultTopAppBarOnPrincipalScreenPreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        DefaultTopAppBar(
            title = { "Principal Screen Title" },
            onMenuButtonClick = {},
            onBackButtonPressed = {},
            isPrincipalScreen = { true },
            actionButtonIcon = { null },
            onActionButtonClick = {}
        )
    }
}

@CompactSizeScreenThemePreview
@Composable
private fun DefaultTopAppBarOnSecondaryScreenPreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        DefaultTopAppBar(
            title = { "Secondary Screen Title" },
            onMenuButtonClick = {},
            onBackButtonPressed = {},
            isPrincipalScreen = { false },
            actionButtonIcon = { Icons.Default.Delete },
            onActionButtonClick = {}
        )
    }
}