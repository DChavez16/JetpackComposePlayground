package com.example.animations.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.animations.AnimationsViewModel
import com.example.animations.R
import com.example.ui.theme.PreviewAppTheme
import com.example.ui.ui.CompactSizeScreenThemePreview

@Composable
internal fun CrossfadeExample(
    animationsViewModel: AnimationsViewModel = hiltViewModel()
) {

    val crossfadeItem by animationsViewModel.crossfadeItem.collectAsState()

    // Display the Crossfade example content
    CrossfadeExampleContent(
        crossfadeItemNumber = { crossfadeItem.number },
        crossfadeItemColor = { crossfadeItem.backgroundColor },
        changeCrossfadeItemNumber = { animationsViewModel.changeCrossfadeItemNumber(it) },
        changeCrossfadeItemColor = { animationsViewModel.changeCrossfadeItemColor() }
    )
}


@Composable
private fun CrossfadeExampleContent(
    crossfadeItemNumber: () -> Int,
    crossfadeItemColor: () -> Color,
    changeCrossfadeItemNumber: (Int) -> Unit,
    changeCrossfadeItemColor: () -> Unit
) {
    // Row that shows the container and buttons
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Box that shows the container for the number
        CrossfadeExampleContentContainer(
            crossfadeItemNumber = { crossfadeItemNumber() },
            crossfadeItemColor = { crossfadeItemColor() },
            modifier = Modifier.weight(3f)
        )

        Spacer(modifier = Modifier.weight(1f))

        // Column that shows the buttons to modify the number and the button to change the background
        CrossfadeExampleContentInput(
            isDecreaseButtonEnabled = { crossfadeItemNumber() > 1 },
            isIncreaseButtonEnabled = { crossfadeItemNumber() < 10 },
            decreaseCrossfadeItemNumber = { changeCrossfadeItemNumber(crossfadeItemNumber().dec()) },
            increaseCrossfadeItemNumber = { changeCrossfadeItemNumber(crossfadeItemNumber().inc()) },
            changeCrossfadeItemColor = changeCrossfadeItemColor,
            modifier = Modifier.weight(3f)
        )
    }
}


@Composable
private fun CrossfadeExampleContentContainer(
    crossfadeItemNumber: () -> Int,
    crossfadeItemColor: () -> Color,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .aspectRatio(1f)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(4.dp)
            )
    ) {
        /* DEV NOTE
        * The following code have some lines commented out, this is because with the current
        * code, the number of recompositions will be lightly less, but the dev will be unable to
        * correctly make use of the IDE's animation preview.
        * To use de animation preview, comment the current lines of code and uncomment the other
        * lines, this will let you preview the crossfade animations.
        * */

        Crossfade(
//            targetState = crossfadeItemColor(),
            targetState = crossfadeItemColor,
            label = "CrossfadeColorAnimation"
        ) { crossfadeItemColor ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .drawBehind {
                        drawRect(
//                            color = crossfadeItemColor
                            color = crossfadeItemColor()
                        )
                    },
                content = { }
            )
        }
        Crossfade(
//            targetState = crossfadeItemNumber(),
            targetState = crossfadeItemNumber,
            label = "CrossfadeNumberAnimation"
        ) { crossfadeItemNumer ->
            Text(
//                text = "$crossfadeItemNumer",
                text = "${crossfadeItemNumer()}",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.displaySmall
            )
        }
    }
}


@Composable
private fun CrossfadeExampleContentInput(
    isDecreaseButtonEnabled: () -> Boolean,
    isIncreaseButtonEnabled: () -> Boolean,
    decreaseCrossfadeItemNumber: () -> Unit,
    increaseCrossfadeItemNumber: () -> Unit,
    changeCrossfadeItemColor: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Row that shows the buttons to reduce and increase the number
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Button that decreases the number, if the number is 1, it cannot be decreased
            IconButton(
                onClick = decreaseCrossfadeItemNumber,
                enabled = isDecreaseButtonEnabled(),
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Filled.Remove,
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = if (isDecreaseButtonEnabled()) stringResource(R.string.animations_screen_crossfade_decrease_number) else null,
                    modifier = Modifier.alpha(if (isDecreaseButtonEnabled()) 1f else 0.5f)
                )
            }

            Text(
                text = stringResource(R.string.animations_screen_crossfade_change_number),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.sizeIn(maxWidth = 100.dp)
            )

            // Button that increases the number, if the number is 10, it cannot be increased
            IconButton(
                onClick = increaseCrossfadeItemNumber,
                enabled = isIncreaseButtonEnabled(),
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = if (isIncreaseButtonEnabled()) stringResource(R.string.animations_screen_crossfade_increase_number) else null,
                    modifier = Modifier.alpha(if (isIncreaseButtonEnabled()) 1f else 0.5f)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Button that changes the background color
        Button(
            onClick = changeCrossfadeItemColor,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.animations_screen_crossfade_change_color),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}


@CompactSizeScreenThemePreview
@Composable
private fun CrossfadeExampleContentPreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        CrossfadeExampleContent(
            crossfadeItemNumber = { 1 },
            crossfadeItemColor = { Color.White },
            changeCrossfadeItemNumber = {},
            changeCrossfadeItemColor = {}
        )
    }
}