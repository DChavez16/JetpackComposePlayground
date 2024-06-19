package com.example.animations.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.animations.AnimationsViewModel
import com.example.animations.R
import com.example.ui.theme.PreviewAppTheme
import com.example.ui.ui.CompactSizeScreenThemePreview


@Composable
internal fun AnimatedContentExample(
    animationsViewModel: AnimationsViewModel = hiltViewModel()
) {

    val isExpanded by animationsViewModel.isAnimatedContentExpanded.collectAsState()
    val currentNumber by animationsViewModel.isAnimatedContentNumber.collectAsState()

    AnimatedContentExampleContent(
        isExpanded = { isExpanded },
        currentNumber = { currentNumber },
        changeExpandedValue = { animationsViewModel.changeAnimatedContentExpanded() },
        changeNumber = { animationsViewModel.changeAnimatedContentNumber(it) }
    )
}


@Composable
private fun AnimatedContentExampleContent(
    isExpanded: () -> Boolean,
    currentNumber: () -> Int,
    changeExpandedValue: () -> Unit,
    changeNumber: (Int) -> Unit
) {
    val expandButtonRotation by animateFloatAsState(
        targetValue = if (isExpanded()) 180f else 0f,
        animationSpec = tween(300),
        label = "ExpandButtonRotation"
    )

    Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
        AnimatedContent(
            // State that defines if the content is expanded to run the animation
            targetState = isExpanded(),
            // TransitionSpec from the animation that contains a ContentTransform and a SizeTransform
            transitionSpec = {
                fadeIn(tween(100)) togetherWith // Content's enter animation
                        fadeOut(tween(100, 200)) using // Content's exit animation
                        SizeTransform { initialSize, targetSize ->
                            if (targetState) {
                                keyframes {
                                    // First it expands horizontally
                                    IntSize(targetSize.width, initialSize.height) at 100
                                    durationMillis = 300
                                }
                            } else {
                                keyframes {
                                    // First it contracts vertically
                                    IntSize(initialSize.width, targetSize.height) at 200
                                    durationMillis = 300
                                }
                            }
                        }
            },
            // AnimatedContent content alignment
            contentAlignment = Alignment.TopEnd,
            // Preview animation label
            label = "ContentExpand"
        ) { targetExpanded ->
            Column(
                horizontalAlignment = Alignment.End, modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.surfaceVariant)
                    .then(
                        if (isExpanded()) Modifier.fillMaxWidth() else Modifier
                    )
            ) {
                // Button to expand and contract the example
                IconButton(
                    onClick = changeExpandedValue
                ) {
                    Icon(
                        imageVector = Icons.Filled.ExpandMore,
                        contentDescription = stringResource(
                            if (targetExpanded) R.string.animations_screen_animated_content_expand
                            else R.string.animations_screen_animated_content_contract
                        ),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .graphicsLayer {
                                rotationZ = expandButtonRotation
                            }
                    )
                }

                if (targetExpanded) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Example container
                        ExampleContainer(
                            currentNumber = { currentNumber() },
                            modifier = Modifier.weight(3f)
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        // Example input buttons
                        ExampleInputButtons(
                            isIncreaseButtonEnabled = { currentNumber() < 10 },
                            isDecreaseButtonEnabled = { currentNumber() > 1 },
                            increaseNumber = { changeNumber(currentNumber().inc()) },
                            decreaseNumber = { changeNumber(currentNumber().dec()) },
                            modifier = Modifier.weight(3f)
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun ExampleContainer(
    currentNumber: () -> Int,
    modifier: Modifier = Modifier
) {
    // Box that will be a container for the number
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .aspectRatio(1f)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                shape = RoundedCornerShape(4.dp)
            )
    ) {
        AnimatedContent(
            // State that defines if the number changed to run the animation
            targetState = currentNumber(),
            // TransitionSpec from the animation that contains a ContentTransform and a SizeTransform
            transitionSpec = {
                // When the new number is greater than the current number
                if (targetState > initialState) {
                    // Slide the content up
                    slideInVertically { height -> height } + fadeIn() togetherWith
                            slideOutVertically { height -> -height } + fadeOut()
                } else {
                    // Slide the content down
                    slideInVertically { height -> -height } + fadeIn() togetherWith
                            slideOutVertically { height -> height } + fadeOut()
                }.using(
                    SizeTransform(clip = false)
                )
            },
            // Preview animation label
            label = "NumberChangeAnimation"
        ) { targetNumber ->
            Text(
                text = "$targetNumber",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


@Composable
private fun ExampleInputButtons(
    isIncreaseButtonEnabled: () -> Boolean,
    isDecreaseButtonEnabled: () -> Boolean,
    increaseNumber: () -> Unit,
    decreaseNumber: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        // Button to increase the number, enabled if the number is less than 10
        IconButton(
            onClick = increaseNumber,
            enabled = isIncreaseButtonEnabled()
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                contentDescription = stringResource(R.string.animations_screen_animated_content_increase_number),
                modifier = Modifier.alpha(if (isIncreaseButtonEnabled()) 1f else 0.5f)
            )
        }

        Text(
            text = stringResource(R.string.animations_screen_animated_content_change_number),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Button to decrease the number, enabled if the number is greater than 1
        IconButton(
            onClick = decreaseNumber,
            enabled = isDecreaseButtonEnabled()
        ) {
            Icon(
                imageVector = Icons.Filled.Remove,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                contentDescription = stringResource(R.string.animations_screen_animated_content_decrease_number),
                modifier = Modifier.alpha(if (isDecreaseButtonEnabled()) 1f else 0.5f)
            )
        }
    }
}


@CompactSizeScreenThemePreview
@Composable
private fun AnimatedContentExamplePreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        AnimatedContentExampleContent(
            isExpanded = { true },
            currentNumber = { 1 },
            changeExpandedValue = {},
            changeNumber = {}
        )
    }
}