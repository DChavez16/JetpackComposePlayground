package com.example.animations

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseInBack
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandIn
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@HiltViewModel
internal class AnimationsViewModel @Inject constructor() : ViewModel() {

    // Backing property and StateFlow for image visibility (AnimatedVisibilityExample)
    private val _imageVisibility = MutableStateFlow(false)
    internal val imageVisibility: StateFlow<Boolean> = _imageVisibility

    // Backing property and StateFlow for current transition (AnimatedVisibilityExample)
    private val _currentTransition = MutableStateFlow(Transitions.None)
    internal val currentTransition: StateFlow<Transitions> = _currentTransition


    // Methods to change image visibility and current transition (AnimatedVisibilityExample)
    fun changeImageVisibility(newImageVisibility: Boolean) {
        _imageVisibility.value = newImageVisibility
    }

    fun changeCurrentTransition(newCurrentTransitionString: String) {
        _currentTransition.value = Transitions.entries.find {
            it.transitionName == newCurrentTransitionString
        } ?: Transitions.None
    }
}


// Utils for the examples
internal data class Transition(
    val enterTransition: EnterTransition = EnterTransition.None,
    val exitTransition: ExitTransition = ExitTransition.None
)

internal enum class Transitions(val transitionName: String, val transition: Transition) {
    None("Sin transiciones", Transition()),
    Fade("Fade", Transition(fadeIn(), fadeOut())),
    Slide(
        "Slide",
        Transition(
            enterTransition = slideIn(initialOffset = { fullSize ->
                IntOffset(
                    fullSize.width,
                    fullSize.height
                )
            }),
            exitTransition = slideOut(targetOffset = { fullSize ->
                IntOffset(
                    -fullSize.width,
                    -fullSize.height
                )
            })
        )
    ),
    SlideHorizontally(
        "SlideHorizontally",
        Transition(
            enterTransition = slideInHorizontally(
                animationSpec = tween(1000, easing = EaseOutBack),
                initialOffsetX = { fullSize -> fullSize }),
            exitTransition = slideOutHorizontally(
                animationSpec = tween(1000, easing = EaseInBack),
                targetOffsetX = { fullSize -> -fullSize })
        )
    ),
    SlideVertically(
        "SlideVertically",
        Transition(
            enterTransition = slideInVertically(initialOffsetY = { fullSize -> fullSize }),
            exitTransition = slideOutVertically(targetOffsetY = { fullSize -> -fullSize })
        )
    ),
    Scale("Scale", Transition(scaleIn(), scaleOut())),
    ExpandShrink("Expand/Shrink", Transition(expandIn(), shrinkOut())),
    ExpandShrinkHorizontally(
        "Expand/Shrink Horizontally",
        Transition(
            expandHorizontally(),
            shrinkHorizontally()
        )
    ),
    ExpandShrinkVertically(
        "Expand/Shrink Vertically",
        Transition(
            expandVertically(),
            shrinkVertically()
        )
    )
}