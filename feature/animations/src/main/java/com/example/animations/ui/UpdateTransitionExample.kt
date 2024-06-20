package com.example.animations.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.animations.AnimationsViewModel
import com.example.animations.BoxState
import com.example.animations.R
import com.example.ui.theme.PreviewAppTheme
import com.example.ui.ui.CompactSizeScreenThemePreview

@Composable
internal fun UpdateTrasitionExample(
    animationsViewModel: AnimationsViewModel = hiltViewModel()
) {

    val boxState by animationsViewModel.boxState.collectAsState()

    // Create and remember an instance of Transition
    val transition = updateTransition(boxState, label = "BoxStateUpdateTransition")

    // Initialize the animations that will be used
    // Elevation shadow
    val shadowElevationDp by transition.animateFloat(
        transitionSpec = {
            tween(durationMillis = 1000, easing = FastOutLinearInEasing)
        },
        label = "ShadowElevationTransition"
    ) {
        when (it) {
            BoxState.Collapsed -> 0f
            BoxState.Expanded -> 20f
        }
    }
    // Expand button rotation
    val iconAngle by transition.animateFloat(
        transitionSpec = {
            tween(durationMillis = 1000, easing = FastOutLinearInEasing)
        },
        label = "IconAngleTransition"
    ) {
        when (it) {
            BoxState.Collapsed -> 90f
            BoxState.Expanded -> 270f
        }
    }
    // Element border
    val elementBorder by transition.animateDp(
        transitionSpec = {
            tween(durationMillis = 1000, easing = FastOutLinearInEasing)
        },
        label = "ElementBorderTransition"
    ) {
        when (it) {
            BoxState.Collapsed -> 8.dp
            BoxState.Expanded -> 16.dp
        }
    }


    UpdateTransitionExampleContent(
        isExpanded = { boxState == BoxState.Expanded },
        shadowElevationDp = { shadowElevationDp },
        iconAngle = { iconAngle },
        elementBorder = { elementBorder },
        onExpandButtonClick = { animationsViewModel.changeBoxState() }
    )
}


@Composable
private fun UpdateTransitionExampleContent(
    isExpanded: () -> Boolean,
    shadowElevationDp: () -> Float,
    iconAngle: () -> Float,
    elementBorder: () -> Dp,
    onExpandButtonClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .graphicsLayer {
                shape = RoundedCornerShape(size = elementBorder())
                shadowElevation = shadowElevationDp()
                clip = true
            }
            .background(color = MaterialTheme.colorScheme.surfaceVariant)
            .height(IntrinsicSize.Min)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .animateContentSize(
                    tween(durationMillis = 1000, easing = FastOutLinearInEasing)
                )
                .weight(1f, fill = false)
        ) {
            DecorativeImage()

            if (isExpanded()) TitleDescriptionText()
        }


        ExpandButton(
            iconAngle = iconAngle,
            isExpanded = isExpanded,
            onExpandButtonClick = onExpandButtonClick,
            modifier = Modifier
                .width(width = 32.dp)
        )
    }
}


@Composable
private fun DecorativeImage() {
    Image(
        painter = painterResource(R.drawable.jetpack_compose_icon),
        contentDescription = null,
        modifier = Modifier.size(100.dp)
    )
}


@Composable
private fun TitleDescriptionText() {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Text(
            text = stringResource(R.string.animations_screen_example_title),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        Text(
            text = stringResource(R.string.animations_screen_example_text),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Justify,
            softWrap = true,
            maxLines = 3,
            modifier = Modifier
                .padding(bottom = 8.dp)
        )
    }
}


@Composable
private fun ExpandButton(
    iconAngle: () -> Float,
    isExpanded: () -> Boolean,
    onExpandButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxHeight()
            .clickable(
                onClick = onExpandButtonClick,
                onClickLabel = stringResource(
                    if (isExpanded()) R.string.animations_screen_update_transition_contract
                    else R.string.animations_screen_update_transition_expand
                )
            )
    ) {
        Icon(
            imageVector = Icons.Filled.KeyboardArrowUp,
            contentDescription = null,
            modifier = Modifier
                .graphicsLayer {
                    rotationZ = iconAngle()
                }
        )
    }
}


@CompactSizeScreenThemePreview
@Composable
private fun UpdateTransitionExamplePreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        UpdateTransitionExampleContent(
            isExpanded = { false },
            shadowElevationDp = { 0f },
            iconAngle = { 90f },
            elementBorder = { 4.dp },
            onExpandButtonClick = {}
        )
    }
}