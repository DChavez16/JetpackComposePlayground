package com.example.drawscope.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.drawscope.DrawScopeViewModel
import com.example.drawscope.PointModeOption
import com.example.drawscope.R
import com.example.ui.theme.PreviewAppTheme
import com.example.ui.ui.CompactSizeScreenThemePreview
import com.example.ui.ui.CustomDropdownMenu


@Composable
internal fun DrawPointsExample(
    drawScopeViewModel: DrawScopeViewModel = hiltViewModel()
) {

    val pointsList by drawScopeViewModel.drawPointsList.collectAsState()
    val pointModeOption by drawScopeViewModel.drawPointsPointMode.collectAsState()

    DrawPointsExampleContent(
        pointsList = { pointsList },
        pointModeOption = { pointModeOption },
        addPoint = drawScopeViewModel::addOffsetToPointsList,
        removeLastPoint = drawScopeViewModel::deleteLastOffsetFromPointsList,
        emptyPoints = drawScopeViewModel::cleanPointsList,
        changePointMode = drawScopeViewModel::changePointMode
    )
}


@Composable
private fun DrawPointsExampleContent(
    pointsList: () -> List<Offset>,
    pointModeOption: () -> PointModeOption,
    addPoint: (Offset) -> Unit,
    removeLastPoint: () -> Unit,
    emptyPoints: () -> Unit,
    changePointMode: (PointModeOption) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Dropdown menu para cambiar el modo de punto
        CustomDropdownMenu(
            dropdownMenuLabel = stringResource(R.string.draw_scope_draw_points_dropdown_menu_label),
            currentElementDisplay = { pointModeOption().pointModeName },
            optionsList = PointModeOption.entries.map { pointModeOption -> pointModeOption.pointModeName },
            onElementSelected = { elementSelected ->
                changePointMode(PointModeOption.entries.find { it.pointModeName == elementSelected }!!)
            }
        )

        // Espacio interactuable donde se van a dibujar los puntos
        PointsExample(
            pointsList = pointsList,
            currentPointMode = { pointModeOption().pointMode },
            onPointAdded = addPoint
        )

        // Botones para eliminar uno o todos los puntos
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(
                colors = ButtonDefaults.elevatedButtonColors(),
                elevation = ButtonDefaults.elevatedButtonElevation(4.dp),
                onClick = removeLastPoint,
                content = {
                    Text(text = stringResource(R.string.draw_scope_draw_points_delete_last_point_button_label))
                }
            )

            OutlinedButton(
                colors = ButtonDefaults.elevatedButtonColors(),
                elevation = ButtonDefaults.elevatedButtonElevation(4.dp),
                onClick = emptyPoints,
                content = {
                    Text(text = stringResource(R.string.draw_scope_draw_points_empty_points_button_label))
                }
            )
        }
    }
}


@Composable
private fun PointsExample(
    pointsList: () -> List<Offset>,
    currentPointMode: () -> PointMode,
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
            points = pointsList(),
            // PointMode usado para indicar como se van a dibujar los puntos: Lines, Points, Polygon
            pointMode = currentPointMode(),
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




@CompactSizeScreenThemePreview
@Composable
private fun DrawPointsExamplePreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        DrawPointsExampleContent(
            pointsList = { listOf(Offset(100f, 100f), Offset(200f, 200f), Offset(300f, 300f)) },
            pointModeOption = { PointModeOption.Points },
            addPoint = {},
            removeLastPoint = {},
            emptyPoints = {},
            changePointMode = {}
        )
    }
}