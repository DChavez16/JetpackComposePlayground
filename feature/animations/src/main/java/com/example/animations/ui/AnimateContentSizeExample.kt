package com.example.animations.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseInOutBack
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ui.theme.AppTheme


@Composable
internal fun AnimateContentSizeExample() {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.background, shape = MaterialTheme.shapes.medium
            )
    ) {
        ExpandedTitleAndDescription(isExpanded)

        ExpandContractButton(isExpanded = isExpanded) {
            isExpanded = !isExpanded
        }
    }
}

@Composable
private fun ExpandContractButton(isExpanded: Boolean, onClick: () -> Unit) {
    val angle by animateFloatAsState(
        targetValue = if (isExpanded) -180f else 0f,
        animationSpec = tween(durationMillis = 1000, easing = EaseInOutBack),
        label = "Expand button angle rotation"
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
                modifier = Modifier.rotate(angle)
            )
        }
    }
}

@Composable
private fun ExpandedTitleAndDescription(isExpanded: Boolean) {
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
                    easing = FastOutSlowInEasing
                )
            )
    ) {
        Text(
            text = "Titulo",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .paddingFromBaseline(bottom = 16.dp)
        )
        Text(
            text = exampleString,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Justify,
            softWrap = true,
            maxLines = if (isExpanded) Int.MAX_VALUE else 3,
            modifier = Modifier
                .padding(bottom = if (isExpanded) 0.dp else 16.dp)
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun AnimateContentSizeExamplePreview() {
    AppTheme {
        AnimateContentSizeExample()
    }
}

private const val exampleString =
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec quis ipsum non sapien vulputate aliquet pharetra elementum tortor. Phasellus consectetur posuere erat eu sollicitudin. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam erat volutpat. In rutrum cursus tincidunt. Ut lectus erat, dignissim sed turpis quis, varius aliquet eros. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus."