@file:OptIn(ExperimentalFoundationApi::class)

package com.example.animations

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.example.animations.ui.AnimateAsStateExample
import com.example.animations.ui.AnimateContentSizeExample
import com.example.animations.ui.AnimatedContentExample
import com.example.animations.ui.AnimatedVisibilityExample
import com.example.animations.ui.CrossfadeExample
import com.example.animations.ui.InfiniteTransitionExample
import com.example.animations.ui.UpdateTrasitionExample
import com.example.ui.ui.DefaultTopAppBar
import com.example.ui.ui.ExampleComponent
import com.example.ui.ui.HorizontalListBanner


/**
 * Example that shows the various animations that can be used with Compose
 */
@Composable
fun AnimationScreen(
    onMenuButtonClick: () -> Unit
) {
    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = stringResource(R.string.animations_screen_title),
                onMenuButtonClick = onMenuButtonClick,
                // Empty since no seconday screen is used
                onBackButtonPressed = {}
            )
        }
    ) { innerPadding ->
        AnimationsList(
            modifier = Modifier.padding(innerPadding)
        )
    }
}


/**
 * List of animations that can be used with Compose
 */
@Composable
private fun AnimationsList(
    modifier: Modifier = Modifier
) {
    // Stores the current ViewModelStoreOwner
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Animation examples for content changes in the layout
        stickyHeader(key = 1) {
            HorizontalListBanner(title = stringResource(R.string.animations_screen_list_banner_1))
        }

        // AnimatedVisibility example
        item {
            ExampleComponent(
                title = stringResource(R.string.animations_screen_animated_visibility_title),
                description = stringResource(R.string.animations_screen_animated_visibility_description),
                content = {
                    AnimatedVisibilityExample(
                        animationsViewModel = hiltViewModel<AnimationsViewModel>(viewModelStoreOwner)
                    )
                }
            )
        }

        // Crossfade example
        item {
            ExampleComponent(
                title = stringResource(R.string.animations_screen_crossfade_title),
                description = stringResource(R.string.animations_screen_crossfade_description),
                content = {
                    CrossfadeExample(
                        animationsViewModel = hiltViewModel<AnimationsViewModel>(viewModelStoreOwner)
                    )
                }
            )
        }

        // AnimatedContent example
        item {
            ExampleComponent(
                title = stringResource(R.string.animations_screen_animated_content_title),
                description = stringResource(R.string.animations_screen_animated_content_description),
                content = {
                    AnimatedContentExample(
                        animationsViewModel = hiltViewModel<AnimationsViewModel>(viewModelStoreOwner)
                    )
                }
            )
        }

        // animateContentSize example
        item {
            ExampleComponent(
                title = stringResource(R.string.animations_screen_animate_content_size_title),
                description = stringResource(R.string.animations_screen_animate_content_size_description),
                content = {
                    AnimateContentSizeExample(
                        animationViewModel = hiltViewModel<AnimationsViewModel>(viewModelStoreOwner)
                    )
                }
            )
        }

        // Animations based on state examples
        stickyHeader(key = 2) {
            HorizontalListBanner(title = stringResource(R.string.animations_screen_list_banner_2))
        }

        // rememberInfiniteTransition example
        item {
            ExampleComponent(
                title = stringResource(R.string.animations_screen_remember_infinite_transition_title),
                description = stringResource(R.string.animations_screen_remember_infinite_transition_description),
                content = {
                    InfiniteTransitionExample()
                }
            )
        }

        // updateTransition example
        item {
            ExampleComponent(
                title = stringResource(R.string.animations_screen_update_transition_title),
                description = stringResource(R.string.animations_screen_update_transition_description),
                content = {
                    UpdateTrasitionExample(
                        animationsViewModel = hiltViewModel<AnimationsViewModel>(viewModelStoreOwner)
                    )
                }
            )
        }

        // animate*AsState example
        item {
            ExampleComponent(
                title = stringResource(R.string.animations_screen_animate_as_state_title),
                description = stringResource(R.string.animations_screen_animate_as_state_description),
                content = {
                    AnimateAsStateExample(
                        animationsViewModel = hiltViewModel<AnimationsViewModel>(viewModelStoreOwner)
                    )
                }
            )
        }
    }
}