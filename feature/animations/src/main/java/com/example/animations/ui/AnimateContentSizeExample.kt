package com.example.animations.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseInOutBack
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.animations.AnimationsViewModel
import com.example.animations.R
import com.example.ui.theme.PreviewAppTheme
import com.example.ui.ui.CompactSizeScreenThemePreview


@Composable
internal fun AnimateContentSizeExample(
    animationViewModel: AnimationsViewModel = hiltViewModel()
) {
    val isExpanded by animationViewModel.isAnimateContentSizeExpanded.collectAsState()

    AnimateContentSizeExampleContent(
        isExpanded = { isExpanded },
        changeExpandedValue = { animationViewModel.changeAnimateContentSizeExpanded() }
    )
}


@Composable
private fun AnimateContentSizeExampleContent(
    isExpanded: () -> Boolean,
    changeExpandedValue: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = MaterialTheme.shapes.medium
            )
    ) {
        ExpandedTitleAndDescription(
            isExpanded = isExpanded
        )

        ExpandContractButton(
            isExpanded = isExpanded,
            onClick = changeExpandedValue
        )
    }
}


@Composable
private fun ExpandedTitleAndDescription(
    isExpanded: () -> Boolean
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.medium
            )
            .padding(8.dp)
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 1000,
                    easing = EaseInOutBack
                )
            )
    ) {
        Text(
            text = stringResource(R.string.animations_screen_animate_content_size_example_title),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .paddingFromBaseline(bottom = 16.dp)
        )
        Text(
            text = stringResource(R.string.animations_screen_animate_content_size_example_text),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Justify,
            softWrap = true,
            maxLines = if (isExpanded()) Int.MAX_VALUE else 3,
            modifier = Modifier
                .padding(bottom = if (isExpanded()) 0.dp else 16.dp)
        )
    }
}


@Composable
private fun ExpandContractButton(
    isExpanded: () -> Boolean,
    onClick: () -> Unit
) {

    val expandButtonRotation by animateFloatAsState(
        targetValue = if (isExpanded()) -180f else 0f,
        animationSpec = tween(durationMillis = 1000, easing = EaseInOutBack),
        label = "ExpandButtonRotation"
    )

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        IconButton(onClick = { onClick() }) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .graphicsLayer {
                        rotationZ = expandButtonRotation
                    }
            )
        }
    }
}




@CompactSizeScreenThemePreview
@Composable
private fun AnimateContentSizeExamplePreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        AnimateContentSizeExampleContent(
            isExpanded = { false },
            changeExpandedValue = {}
        )
    }
}