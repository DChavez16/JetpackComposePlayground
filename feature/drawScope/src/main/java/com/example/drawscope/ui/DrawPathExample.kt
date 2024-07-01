package com.example.drawscope.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.drawscope.DrawScopeViewModel
import com.example.drawscope.R
import com.example.ui.theme.PreviewAppTheme
import com.example.ui.ui.CompactSizeScreenThemePreview
import kotlin.math.ceil


@Composable
internal fun DrawPathExample(
    drawScopeViewModel: DrawScopeViewModel = hiltViewModel()
) {

    val valuesList by drawScopeViewModel.drawPathValuesList.collectAsState()

    DrawPathExampleContent(
        valuesList = { valuesList },
        updateValuesList = drawScopeViewModel::updateDrawPathValuesList
    )
}


@Composable
private fun DrawPathExampleContent(
    valuesList: () -> List<Int>,
    updateValuesList: () -> Unit
) {

    var isPathUpdating by remember { mutableStateOf(true) }

    val pathVerticalClip by animateFloatAsState(
        targetValue = if (isPathUpdating) 1f else 0f,
        animationSpec = tween(
            durationMillis = if (isPathUpdating) 1000 else 0
        ),
        label = "PathVerticalClipAnimation",
        finishedListener = { isPathUpdating = true }
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(
                R.string.draw_scope_draw_path_values_list,
                valuesList().toString()
            ),
            style = MaterialTheme.typography.titleSmall
        )

        // Grafico que se va a dibujar
        PathExample(
            valuesList = valuesList,
            isPathUpdating = { isPathUpdating },
            pathVerticalClip = { pathVerticalClip }
        )

        // Boton para generar los valores del grafico
        OutlinedButton(
            colors = ButtonDefaults.elevatedButtonColors(),
            elevation = ButtonDefaults.elevatedButtonElevation(4.dp),
            onClick = {
                isPathUpdating = false
                updateValuesList()
            },
            content = {
                Text(text = stringResource(R.string.draw_scope_draw_path_generate_values_button_label))
            },
            enabled = pathVerticalClip == 1f
        )
    }
}


@Composable
private fun PathExample(
    valuesList: () -> List<Int>,
    isPathUpdating: () -> Boolean,
    pathVerticalClip: () -> Float
) {

    val textMeasurer = rememberTextMeasurer()
    val maxValue = (valuesList().maxOrNull() ?: 0).roundUp()
    val graphStartingXCoord = 0.11f
    val pathPrimaryColor = MaterialTheme.colorScheme.primary
    val pathSecondaryColor = MaterialTheme.colorScheme.secondary
    val backgrounPrimaryColor = MaterialTheme.colorScheme.onBackground

    Canvas(
        modifier = Modifier
            .size(width = 300.dp, height = 150.dp)
    ) {
        // Graph Background
        for (i in 0..4) {
            if (i % 2 == 0) {
                val measuredText = textMeasurer.measure(
                    text = "${maxValue - (maxValue * (i / 4.0)).toInt()}"
                )

                drawText(
                    textLayoutResult = measuredText,
                    color = backgrounPrimaryColor,
                    topLeft = Offset(
                        x = (size.width * graphStartingXCoord / 2) - (measuredText.size.width / 2),
                        y = ((size.height / 4) * i) - (measuredText.size.height / 2)
                    )
                )
            }
            drawLine(
                color = backgrounPrimaryColor,
                start = Offset(size.width * graphStartingXCoord, (size.height / 4) * i),
                end = Offset(size.width, (size.height / 4) * i),
                strokeWidth = 1.dp.toPx(),
                alpha = if (i != 4) 0.25f else 1f
            )
        }

        // Path of the graph
        val path = Path()
        for (i in 0..6) {
            if (i == 0) {
                path.moveTo(
                    x = size.width * graphStartingXCoord,
                    y = size.height * (1 - (valuesList()[i] / maxValue.toFloat()))
                )
            } else {
                path.lineTo(
                    x = size.width * (graphStartingXCoord + (i * (1 - graphStartingXCoord) / 6)),
                    y = size.height * (1 - (valuesList()[i] / maxValue.toFloat()))
                )
            }
        }
        clipRect(
            left = size.width * graphStartingXCoord,
            right = if (!isPathUpdating()) 0f else size.width * pathVerticalClip(),
            clipOp = ClipOp.Intersect
        ) {
            drawPath(
                // Ruta a dibujar
                path = path,
                // El color para aplicar a la ruta
                //color = ovalPrimaryColor,
                // Opcionalmente se puede asiganr un Brush para el color
                brush = Brush.linearGradient(
                    colors = listOf(pathPrimaryColor, pathSecondaryColor),
                    start = Offset(0.5f, size.height * 1f),
                    end = Offset(0.5f, size.height * 0f)
                ),
                // Opacidad a ser aplicada a la ruta de 0.0f a 1.0f representando de transparente a opaco respectivamente
                alpha = 1f,
                // Define si la ruta esta trazada o rellenada
                style = Stroke(width = 5f, pathEffect = PathEffect.cornerPathEffect(3f)),
                //style = Fill,
                // ColorFilter a aplicar al color cuando se dibuja la ruta
                colorFilter = null,
                // Algoritmo de fusion que se aplicara cuando la ruta se dibuja
                blendMode = DrawScope.DefaultBlendMode
            )
        }
    }
}


@CompactSizeScreenThemePreview
@Composable
private fun DrawPathExamplePreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        DrawPathExampleContent(
            valuesList = { listOf(200, 100, 300, 150, 250, 400, 50) },
            updateValuesList = {}
        )
    }
}


private fun Int.roundUp(): Int {
    val decimalValue = (this / 100.0)

    return ceil(decimalValue).toInt() * 100
}