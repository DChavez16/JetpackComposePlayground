package com.example.drawscope.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ui.theme.AppTheme
import com.example.ui.ui.CustomDropdownMenu


@Composable
internal fun DrawPointsExample() {
    var pointsList: List<Offset> by remember { mutableStateOf(listOf()) }
    var pointModeOption by remember { mutableStateOf(PointModeOption.Points) }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Dropdown menu para cambiar el modo de punto
        CustomDropdownMenu(
            dropdownMenuLabel = "Point Mode",
            currentElementDisplay = { pointModeOption.pointModeName },
            optionsList = PointModeOption.entries.map { pointModeOption -> pointModeOption.pointModeName },
            onElementSelected = { elementSelected ->
                pointModeOption =
                    PointModeOption.entries.find { it.pointModeName == elementSelected }!!
            }
        )

        // Espacio interactuable donde se van a dibujar los puntos
        PointsExample(
            pointsList = pointsList,
            currentPointMode = pointModeOption.pointMode,
            onPointAdded = { newOffset -> pointsList = pointsList.addOffset(newOffset) }
        )

        // Botones para eliminar uno o todos los puntos
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(
                colors = ButtonDefaults.elevatedButtonColors(),
                elevation = ButtonDefaults.elevatedButtonElevation(4.dp),
                onClick = { pointsList = pointsList.deleteLastOffset() },
                content = {
                    Text(text = "Borrar ultimo punto")
                }
            )

            OutlinedButton(
                colors = ButtonDefaults.elevatedButtonColors(),
                elevation = ButtonDefaults.elevatedButtonElevation(4.dp),
                onClick = { pointsList = emptyList() },
                content = {
                    Text(text = "Borrar todos los puntos")
                }
            )
        }
    }
}

@Composable
fun PointsExample(
    pointsList: List<Offset>,
    currentPointMode: PointMode,
    onPointAdded: (Offset) -> Unit
) {
    val pointColor = MaterialTheme.colorScheme.primary
    val outlineColor = MaterialTheme.colorScheme.secondary

    Canvas(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(150.dp)
            .pointerInput(Unit) {
                detectTapGestures { offset -> onPointAdded(offset) }
            }
    ) {
        drawOutline(
            outline = Outline.Rectangle(
                rect = Rect(
                    topLeft = Offset(x = 0f, y = 0f),
                    bottomRight = Offset(x = size.width, y = size.height)
                )
            ),
            color = outlineColor,
            style = Stroke(size.minDimension * 0.005f)
        )
        drawPoints(
            // Lista de puntos a dibujar con su pointMode especificado
            points = pointsList,
            // PointMode usado para indicar como se van a dibujar los puntos: Lines, Points, Polygon
            pointMode = currentPointMode,
            // El color para rellenar la linea
            color = pointColor,
            // Opcionalmente se puede asiganr un Brush para el color
            //brush = Brush.linearGradient(listOf(circlePrimaryColor, circleSecondaryColor)),
            // El ancho del trazo
            strokeWidth = 10f,
            // Tratamiento aplicado al final de la linea: Round, Square, Butt
            cap = StrokeCap.Round,
            // Efecto o patron opcional a aplicar a los puntos
            pathEffect = null,
            // Opacidad a ser aplicada a los puntos de 0.0f a 1.0f representando de transparente a opaco respectivamente
            alpha = 1f,
            // ColorFilter a aplicar al color cuando se dibuja los puntos
            colorFilter = null,
            // Algoritmo de fusion que se aplicara cuando los puntos se dibujan
            blendMode = DrawScope.DefaultBlendMode
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun DrawPointsExamplePreview() {
    AppTheme {
        DrawPointsExample()
    }
}


private fun List<Offset>.addOffset(newOffset: Offset): List<Offset> {
    return this + newOffset
}

private fun List<Offset>.deleteLastOffset(): List<Offset> {
    return dropLast(1)
}

private enum class PointModeOption(val pointModeName: String, val pointMode: PointMode) {
    Points("Points", PointMode.Points),
    Lines("Lines", PointMode.Lines),
    Polygon("Polygon", PointMode.Polygon)
}