package com.example.animations

import android.content.Context
import androidx.annotation.StringRes
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@HiltViewModel
internal class AnimationsViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    // Backing property and StateFlow for image visibility (AnimatedVisibilityExample)
    private val _imageVisibility = MutableStateFlow(false)
    val imageVisibility: StateFlow<Boolean> = _imageVisibility

    // Backing property and StateFlow for current transition (AnimatedVisibilityExample)
    private val _currentTransition = MutableStateFlow(Transitions.None)
    val currentTransition: StateFlow<Transitions> = _currentTransition

    internal val transitionsNames =
        Transitions.entries.map { context.getString(it.transitionName) }

    // Backing property and StateFlow for current crossfade item (CrossfadeExample)
    private val _crossfadeItem = MutableStateFlow(CrossfadeItem(number = 1))
    val crossfadeItem: StateFlow<CrossfadeItem> = _crossfadeItem

    // Backing property and StateFlow expanded state (AnimatedContent)
    private val _isAnimatedContentExpanded = MutableStateFlow(false)
    val isAnimatedContentExpanded: StateFlow<Boolean> = _isAnimatedContentExpanded

    // Backing property and StateFlow expanded state (AnimatedContent)
    private val _isAnimatedContentNumber = MutableStateFlow(1)
    val isAnimatedContentNumber: StateFlow<Int> = _isAnimatedContentNumber


    // Methods to change image visibility and current transition (AnimatedVisibilityExample)
    fun changeImageVisibility(newImageVisibility: Boolean) {
        _imageVisibility.value = newImageVisibility
    }

    fun changeCurrentTransition(newCurrentTransitionString: String) {
        _currentTransition.value = Transitions.entries.find {
            context.getString(it.transitionName) == newCurrentTransitionString
        } ?: Transitions.None
    }

    // Methods to change crossfade item color and number (CrossfadeExample)
    fun changeCrossfadeItemColor() {
        _crossfadeItem.value = _crossfadeItem.value.copy(backgroundColor = getRandomColor())
    }

    fun changeCrossfadeItemNumber(newNumber: Int) {
        _crossfadeItem.value = _crossfadeItem.value.copy(number = newNumber)
    }

    // Methods to change animated content expanded value and number (AnimatedContent)
    fun changeAnimatedContentExpanded() {
        _isAnimatedContentExpanded.value = !_isAnimatedContentExpanded.value
    }

    fun changeAnimatedContentNumber(newNumber: Int) {
        _isAnimatedContentNumber.value = newNumber
    }
}


// Utils for the examples
internal data class Transition(
    val enterTransition: EnterTransition = EnterTransition.None,
    val exitTransition: ExitTransition = ExitTransition.None
)

internal enum class Transitions(
    @StringRes val transitionName: Int,
    val transition: Transition
) {
    None(
        transitionName = R.string.animations_screen_animated_visibility_transition_none,
        transition = Transition()
    ),
    Fade(
        transitionName = R.string.animations_screen_animated_visibility_transition_fade,
        transition = Transition(
            enterTransition = fadeIn(),
            exitTransition = fadeOut()
        )
    ),
    Slide(
        transitionName = R.string.animations_screen_animated_visibility_transition_slide,
        transition = Transition(
            enterTransition = slideIn(
                initialOffset = { fullSize ->
                    IntOffset(
                        fullSize.width,
                        fullSize.height
                    )
                }),
            exitTransition = slideOut(
                targetOffset = { fullSize ->
                    IntOffset(
                        -fullSize.width,
                        -fullSize.height
                    )
                })
        )
    ),
    SlideHorizontally(
        transitionName = R.string.animations_screen_animated_visibility_transition_slide_horizontally,
        transition = Transition(
            enterTransition = slideInHorizontally(
                animationSpec = tween(1000, easing = EaseOutBack),
                initialOffsetX = { fullSize -> fullSize }),
            exitTransition = slideOutHorizontally(
                animationSpec = tween(1000, easing = EaseInBack),
                targetOffsetX = { fullSize -> -fullSize })
        )
    ),
    SlideVertically(
        transitionName = R.string.animations_screen_animated_visibility_transition_slide_vertically,
        transition = Transition(
            enterTransition = slideInVertically(initialOffsetY = { fullSize -> fullSize }),
            exitTransition = slideOutVertically(targetOffsetY = { fullSize -> -fullSize })
        )
    ),
    Scale(
        transitionName = R.string.animations_screen_animated_visibility_transition_scale,
        transition = Transition(
            enterTransition = scaleIn(),
            exitTransition = scaleOut()
        )
    ),
    ExpandShrink(
        transitionName = R.string.animations_screen_animated_visibility_transition_expand_shrink,
        transition = Transition(
            enterTransition = expandIn(),
            exitTransition = shrinkOut()
        )
    ),
    ExpandShrinkHorizontally(
        transitionName = R.string.animations_screen_animated_visibility_transition_expand_shrink_horizontally,
        transition = Transition(
            enterTransition = expandHorizontally(),
            exitTransition = shrinkHorizontally()
        )
    ),
    ExpandShrinkVertically(
        transitionName = R.string.animations_screen_animated_visibility_transition_expand_shrink_vertically,
        transition = Transition(
            enterTransition = expandVertically(),
            exitTransition = shrinkVertically()
        )
    )
}

internal data class CrossfadeItem(
    var number: Int,
    var backgroundColor: Color = Color.White
)

private fun getRandomColor() =
    Color(
        red = (0..255).random(),
        green = (0..255).random(),
        blue = (0..255).random()
    )