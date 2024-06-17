package com.example.animations.ui

import androidx.compose.animation.AnimatedVisibility
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
    AnimatedVisibilityContent(
        imageVisibility = imageVisibility,
        currentTransition = currentTransition,
        onImageVisibilityChange = animationsViewModel::changeImageVisibility,
        onTransitionChange = animationsViewModel::changeCurrentTransition
    )
}


@Composable
private fun AnimatedVisibilityContent(
    imageVisibility: Boolean,
    currentTransition: Transitions,
    onImageVisibilityChange: (Boolean) -> Unit,
    onTransitionChange: (String) -> Unit
) {
    // TODO Apply accesbility best practices
    // TODO Apply performance best practices

    Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        // DropdownMenu that shows the enter and exit transitions of the animation
        CustomDropdownMenu(
            dropdownMenuLabel = "Transicion",
            currentElementDisplay = currentTransition.transitionName,
            optionsList = Transitions.entries.map { transition -> transition.transitionName },
            onElementSelected = onTransitionChange
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Box containing the image that will be animated in the example
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(3f)
                    .aspectRatio(1f)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.onSurface,
                        shape = RoundedCornerShape(4.dp)
                    )
            ) {
                this@Row.AnimatedVisibility(
                    visible = imageVisibility,
                    enter = currentTransition.transition.enterTransition,
                    exit = currentTransition.transition.exitTransition,
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

            Spacer(modifier = Modifier.weight(1f))

            // Button to trigger the animation (enable or disable the visibility of the image)
            Button(
                onClick = { onImageVisibilityChange(!imageVisibility) },
                modifier = Modifier.weight(3f)
            ) {
                Text(
                    text = if (imageVisibility) "Ocultar imagen" else "Mostrar Imagen",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}




@CompactSizeScreenThemePreview
@Composable
private fun AnimatedVisibilityExamplePreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        AnimatedVisibilityContent(
            imageVisibility = false,
            currentTransition = Transitions.None,
            onImageVisibilityChange = {},
            onTransitionChange = {}
        )
    }
}