package com.example.animations.ui

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.animations.AnimationsViewModel
import com.example.animations.R
import com.example.animations.Transitions
import com.example.ui.theme.PreviewAppTheme
import com.example.ui.ui.CompactSizeScreenThemePreview
import com.example.ui.ui.CustomDropdownMenu

// AnimatedVisibility example
@Composable
internal fun AnimatedVisibilityExample(
    animationsViewModel: AnimationsViewModel = hiltViewModel()
) {

    val imageVisibility by animationsViewModel.imageVisibility.collectAsState()
    val currentTransition by animationsViewModel.currentTransition.collectAsState()

    // Display the AnimatedVisibility example content
    AnimatedVisibilityExampleContent(
        imageVisibility = { imageVisibility },
        currentTransition = { currentTransition },
        optionsList = animationsViewModel.transitionsNames,
        onImageVisibilityChange = { animationsViewModel.changeImageVisibility(!imageVisibility) },
        onTransitionChange = { animationsViewModel.changeCurrentTransition(it) }
    )
}


@Composable
private fun AnimatedVisibilityExampleContent(
    imageVisibility: () -> Boolean,
    currentTransition: () -> Transitions,
    optionsList: List<String>,
    onImageVisibilityChange: () -> Unit,
    onTransitionChange: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {

        AnimatedVisibilityExampleContentDropdownMenu(
            currentElementDisplay = { currentTransition().transitionName },
            optionsList = optionsList,
            onElementSelected = onTransitionChange
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            AnimatedVisibilityExampleContentContainer(
                imageVisibility = { imageVisibility() },
                enterTransition = { currentTransition().transition.enterTransition },
                exitTransition = { currentTransition().transition.exitTransition },
                modifier = Modifier.weight(3f)
            )

            Spacer(modifier = Modifier.weight(1f))

            AnimatedVisibilityExampleContentInputButton(
                onImageVisibilityChange = onImageVisibilityChange,
                buttonStringResource = {
                    if (imageVisibility()) R.string.animations_screen_animated_visibility_hide_image
                    else R.string.animations_screen_animated_visibility_show_image
                },
                modifier = Modifier.weight(3f)
            )
        }
    }
}


@Composable
private fun AnimatedVisibilityExampleContentDropdownMenu(
    @StringRes currentElementDisplay: () -> Int,
    optionsList: List<String>,
    onElementSelected: (String) -> Unit
) {
    val currentElementDisplayText = stringResource(currentElementDisplay())

    // DropdownMenu that shows the enter and exit transitions of the animation
    CustomDropdownMenu(
        dropdownMenuLabel = stringResource(R.string.animations_screen_animated_visibility_dropdown_menu_label),
        currentElementDisplay = { currentElementDisplayText },
        optionsList = optionsList,
        onElementSelected = onElementSelected
    )
}


@Composable
private fun AnimatedVisibilityExampleContentInputButton(
    onImageVisibilityChange: () -> Unit,
    buttonStringResource: () -> Int,
    modifier: Modifier = Modifier
) {
    // Button to trigger the animation (enable or disable the visibility of the image)
    Button(
        onClick = { onImageVisibilityChange() },
        modifier = modifier
    ) {
        Text(
            text = stringResource(buttonStringResource()),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}


@Composable
private fun AnimatedVisibilityExampleContentContainer(
    imageVisibility: () -> Boolean,
    enterTransition: () -> EnterTransition,
    exitTransition: () -> ExitTransition,
    modifier: Modifier = Modifier
) {
    // Row containing the image that will be animated in the example
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .aspectRatio(1f)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(4.dp)
            ),
        content = {
            AnimatedVisibility(
                visible = imageVisibility(),
                enter = enterTransition(),
                exit = exitTransition(),
                label = "ImageAnimatedVisiblity",
                // Added a clip to avoid the image to move outside the box limits during the animation
                modifier = Modifier.clipToBounds()
            ) {
                Image(
                    painter = painterResource(R.drawable.jetpack_compose_icon),
                    contentDescription = null
                )
            }
        }
    )
}


@CompactSizeScreenThemePreview
@Composable
private fun AnimatedVisibilityExamplePreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        AnimatedVisibilityExampleContent(
            imageVisibility = { false },
            currentTransition = { Transitions.None },
            optionsList = Transitions.entries.map { stringResource(it.transitionName) },
            onImageVisibilityChange = {},
            onTransitionChange = {}
        )
    }
}