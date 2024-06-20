package com.example.animations

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
        item(key = 1) { HorizontalListBanner(title = stringResource(R.string.animations_screen_list_banner_1)) }

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
        item(key = 2) { HorizontalListBanner(title = "Animaciones basadas en el estado") }

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

        // TODO Add the AnimationsViewModel to the example
        // TODO Implement string values
        // TODO Improve performance
        // TODO Improve accessibility
        // updateTransition example
        item {
            ExampleComponent(
                title = "updateTransition",
                description = "updateTransition crea y recuerda una instancia de Transition, y actualiza su estado.\nEn el siguiente ejemplo se va a expandir un cuadro de texto verticalmente, mientras que se le va a animar el color de fondo, elevacion, tipo de borde y rotacion de un boton de expandir.",
                content = { UpdateTrasitionExample() }
            )
        }

        // TODO Add the AnimationsViewModel to the example
        // TODO Implement string values
        // TODO Improve performance
        // TODO Improve accessibility
        // animate*AsState example
        item {
            ExampleComponent(
                title = "animate*AsState",
                description = "Son las APIs mas simples para crear un valor unico. So hay que proporcionar el valor final (o valor objetvio), y la API comenzara la animacion desde el valor actual hasta el especificada.\nEn el siguiente ejemplo se mostrara una imagen que cambiara su transparencia por medio de un Slider.",
                content = { AnimateAsStateExample() }
            )
        }
    }
}