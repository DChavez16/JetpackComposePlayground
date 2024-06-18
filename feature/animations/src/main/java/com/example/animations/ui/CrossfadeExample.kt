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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
        increaseCrossfadeItemNumber = { animationsViewModel.increaseCrossfadeItemNumber() },
        decreaseCrossfadeItemNumber = { animationsViewModel.decreaseCrossfadeItemNumber() },
        changeCrossfadeItemColor = { animationsViewModel.changeCrossfadeItemColor() }
    )
}


@Composable
private fun CrossfadeExampleContent(
    crossfadeItemNumber: () -> Int,
    crossfadeItemColor: () -> Color,
    increaseCrossfadeItemNumber: () -> Unit,
    decreaseCrossfadeItemNumber: () -> Unit,
    changeCrossfadeItemColor: () -> Unit
) {
    // Row que muestra el contenedor y los botones
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Box que va a ser de contenedor para el numero
        CrossfadeExampleContentContainer(
            crossfadeItemNumber = { crossfadeItemNumber() },
            crossfadeItemColor = { crossfadeItemColor() },
            modifier = Modifier.weight(3f)
        )

        Spacer(modifier = Modifier.weight(1f))

        // Column que muestra los botones para modificar el numero y el boton para cambiar el fondo
        CrossfadeExampleContentInput(
            decreaseButtonEnabled = { crossfadeItemNumber() > 1 },
            increaseButtonEnabled = { crossfadeItemNumber() < 10 },
            decreaseCrossfadeItemNumber = decreaseCrossfadeItemNumber,
            increaseCrossfadeItemNumber = increaseCrossfadeItemNumber,
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
    decreaseButtonEnabled: () -> Boolean,
    increaseButtonEnabled: () -> Boolean,
    decreaseCrossfadeItemNumber: () -> Unit,
    increaseCrossfadeItemNumber: () -> Unit,
    changeCrossfadeItemColor: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Row que muestra los botones para reducir y aumentar el valor del numero
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Button para disminuir el valor del numero, si el numero es 1 no se puede disminuir
            IconButton(
                onClick = decreaseCrossfadeItemNumber,
                enabled = decreaseButtonEnabled(),
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    painter = painterResource(R.drawable.remove_icon),
                    contentDescription = if (decreaseButtonEnabled()) stringResource(R.string.animations_screen_crossfade_decrease_number) else null
                )
            }

            Text(
                text = stringResource(R.string.animations_screen_crossfade_change_number),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.sizeIn(maxWidth = 100.dp)
            )

            // Button para aumentar el valor del numero, si el numero es 10 no se puede aumentar
            IconButton(
                onClick = increaseCrossfadeItemNumber,
                enabled = increaseButtonEnabled(),
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = if (increaseButtonEnabled()) stringResource(R.string.animations_screen_crossfade_increase_number) else null
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Button para cambiar el color de fondo
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
            increaseCrossfadeItemNumber = {},
            decreaseCrossfadeItemNumber = {},
            changeCrossfadeItemColor = {}
        )
    }
}