@file:OptIn(ExperimentalFoundationApi::class)

package com.example.drawscope

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.example.drawscope.ui.ClipPathExample
import com.example.drawscope.ui.ClipRectExample
import com.example.drawscope.ui.DrawArcExample
import com.example.drawscope.ui.DrawCircleExample
import com.example.drawscope.ui.DrawImageExample
import com.example.drawscope.ui.DrawLineExample
import com.example.drawscope.ui.DrawOutlineExample
import com.example.drawscope.ui.DrawOvalExample
import com.example.drawscope.ui.DrawPathExample
import com.example.drawscope.ui.DrawPointsExample
import com.example.drawscope.ui.DrawRectExample
import com.example.drawscope.ui.DrawRoundRectExample
import com.example.drawscope.ui.DrawTextExample
import com.example.drawscope.ui.InsetExample
import com.example.drawscope.ui.MultipleTransformExample
import com.example.drawscope.ui.RotateExample
import com.example.drawscope.ui.ScaleExample
import com.example.drawscope.ui.TranslateExample
import com.example.ui.theme.AppTheme
import com.example.ui.ui.CompactSizeScreenThemePreview
import com.example.ui.ui.DefaultTopAppBar
import com.example.ui.ui.ExampleComponent
import com.example.ui.ui.HorizontalListBanner


/**
 * Example that shows the draw scopes that can be used with Compose
 */
@Composable
fun DrawScopeScreen(
    onMenuButtonClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = stringResource(R.string.draw_scope_title),
                onMenuButtonClick = onMenuButtonClick,
                // Empty since no seconday screen is used
                onBackButtonPressed = {}
            )
        }
    ) { innerPadding ->
        DrawScopesList(
            modifier = Modifier.padding(innerPadding)
        )
    }
}


/**
 * List of draw scopes that can be used with Compose
 */
@Composable
private fun DrawScopesList(
    modifier: Modifier = Modifier
) {
    // Stores the current ViewModelStoreOwner
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current)

    // List of graphics and transformations that can be drawn with Compose
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp),
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        // DrawScope graphics horizontal banner
        stickyHeader(key = 1) {
            HorizontalListBanner(title = stringResource(R.string.draw_scope_list_banner_1))
        }

        // drawArc example
        item {
            ExampleComponent(
                title = stringResource(R.string.draw_scope_draw_arc_title),
                description = stringResource(R.string.draw_scope_draw_arc_description),
                content = {
                    DrawArcExample(
                        drawScopeViewModel = hiltViewModel<DrawScopeViewModel>(viewModelStoreOwner)
                    )
                }
            )
        }

        // drawCircle example
        item {
            ExampleComponent(
                title = stringResource(R.string.draw_scope_draw_circle_title),
                description = stringResource(R.string.draw_scope_draw_circle_description),
                content = {
                    DrawCircleExample(
                        drawScopeViewModel = hiltViewModel<DrawScopeViewModel>(viewModelStoreOwner)
                    )
                }
            )
        }

        // drawLine example
        item {
            ExampleComponent(
                title = stringResource(R.string.draw_scope_draw_line_title),
                description = stringResource(R.string.draw_scope_draw_line_description),
                content = {
                    DrawLineExample(
                        drawScopeViewModel = hiltViewModel<DrawScopeViewModel>(viewModelStoreOwner)
                    )
                }
            )
        }

        // drawOval example
        item {
            ExampleComponent(
                title = stringResource(R.string.draw_scope_draw_oval_title),
                description = stringResource(R.string.draw_scope_draw_oval_description),
                content = {
                    DrawOvalExample(
                        drawScopeViewModel = hiltViewModel<DrawScopeViewModel>(viewModelStoreOwner)
                    )
                }
            )
        }

        // drawPath example
        item {
            ExampleComponent(
                title = stringResource(R.string.draw_scope_draw_path_title),
                description = stringResource(R.string.draw_scope_draw_path_description),
                content = {
                    DrawPathExample(
                        drawScopeViewModel = hiltViewModel<DrawScopeViewModel>(viewModelStoreOwner)
                    )
                }
            )
        }

        // drawPoints example
        item {
            ExampleComponent(
                title = stringResource(R.string.draw_scope_draw_points_title),
                description = stringResource(R.string.draw_scope_draw_points_description),
                content = {
                    DrawPointsExample(
                        drawScopeViewModel = hiltViewModel<DrawScopeViewModel>(viewModelStoreOwner)
                    )
                }
            )
        }

        // drawRect example
        item {
            ExampleComponent(
                title = stringResource(R.string.draw_scope_draw_rect_title),
                description = stringResource(R.string.draw_scope_draw_rect_description),
                content = {
                    DrawRectExample(
                        drawScopeViewModel = hiltViewModel<DrawScopeViewModel>(viewModelStoreOwner)
                    )
                }
            )
        }

        // drawRoundRect example
        item {
            ExampleComponent(
                title = stringResource(R.string.draw_scope_draw_round_rect_title),
                description = stringResource(R.string.draw_scope_draw_round_rect_description),
                content = {
                    DrawRoundRectExample(
                        drawScopeViewModel = hiltViewModel<DrawScopeViewModel>(viewModelStoreOwner)
                    )
                }
            )
        }

        // TODO Add DrawScopeViewModel
        // TODO Refactorize to follow a testable aproach
        // TODO Change plain text with string resources
        // TODO Improve performance (less recompositions)
        // drawOutline example
        item {
            ExampleComponent(
                title = "",
                description = "El Outline define una forma simple, usado para limitar regiones graficas, la forma de una sombra proyectada por el componente, o para recortar los contenidos.\nEn el ejemplo se dibujara un outline al que se podra cambiar el largo y ancho.",
                content = { DrawOutlineExample() }
            )
        }

        // TODO Add DrawScopeViewModel
        // TODO Refactorize to follow a testable aproach
        // TODO Change plain text with string resources
        // TODO Improve performance (less recompositions)
        // drawText example
        item {
            ExampleComponent(
                title = "drawText",
                description = "DrawText se usa cuando se busca dibujar un texto con personalizacion. Dibuja un layout de texto existente producido por TextMeasurer.\nEn el ejemplo se muestra un texto que contiene un numero aleatorio de palabras, el cual su tamaño dependera de la cantidad de texto que contenga con un maximo de ancho y alto personalizable.",
                content = { DrawTextExample() }
            )
        }

        // TODO Add DrawScopeViewModel
        // TODO Refactorize to follow a testable aproach
        // TODO Change plain text with string resources
        // TODO Improve performance (less recompositions)
        // drawImage example
        item {
            ExampleComponent(
                title = "drawImage",
                description = "Para dibujar un imageBitmap con DrawScope, se carga la imagen con imageResource y se llama drawImage.\n ",
                content = { DrawImageExample() }
            )
        }

        // TODO Change plain text with string resources
        // DrawTransform transformations horizontal banner
        stickyHeader(key = 2) {
            HorizontalListBanner(title = "Transformaciones con DrawTransform")
        }

        // TODO Add DrawScopeViewModel
        // TODO Refactorize to follow a testable aproach
        // TODO Change plain text with string resources
        // TODO Improve performance (less recompositions)
        // clipPath example
        item {
            ExampleComponent(
                title = "clipPath",
                description = "Reduce la region del recorte a la interseccion del recorte actual y el rectangulo redondeado dado.\nEn este ejemplo se aplica un clipPath a una imagen.",
                content = { ClipPathExample() }
            )
        }

        // TODO Add DrawScopeViewModel
        // TODO Refactorize to follow a testable aproach
        // TODO Change plain text with string resources
        // TODO Improve performance (less recompositions)
        // clipRect example
        item {
            ExampleComponent(
                title = "clipRect",
                description = "Reduce la region de corte a la interseccion del recorte actual y el rectangulo dado indicado por los limites izquierdo, superior, derecho e inferior.\nEn este ejemplo se aplica un clipRect a la imagen",
                content = { ClipRectExample() }
            )
        }

        // TODO Add DrawScopeViewModel
        // TODO Refactorize to follow a testable aproach
        // TODO Change plain text with string resources
        // TODO Improve performance (less recompositions)
        // inset example
        item {
            ExampleComponent(
                title = "inset",
                description = "Ajusta los parametros predeterminados del DrawScope actual cambia los limites de dibujoj y traslada los dibujos segun corresponda.\nEl siguiente ejemplo ajusta la escala vertical y horizontal de una imagen.",
                content = { InsetExample() }
            )
        }

        // TODO Add DrawScopeViewModel
        // TODO Refactorize to follow a testable aproach
        // TODO Change plain text with string resources
        // TODO Improve performance (less recompositions)
        // rotate example
        item {
            ExampleComponent(
                title = "rotate",
                description = "Agrega una rotacion (en grados con direccion a las agujas del reloj) a la transformacion actual en el punto de pivote dado.\nEn el ejemplo se va a rotar una imagen en el sentido de las agujas del reloj.",
                content = { RotateExample() }
            )
        }

        // TODO Add DrawScopeViewModel
        // TODO Refactorize to follow a testable aproach
        // TODO Change plain text with string resources
        // TODO Improve performance (less recompositions)
        // scale example
        item {
            ExampleComponent(
                title = "scale",
                description = "Aumenta el tamaño de las operaciones de dibujo por un factor.\nEn el ejemplo se va a escalar una imagen horizontal y verticalmente con respecto a la parte superior izquierda del dibujo.",
                content = { ScaleExample() }
            )
        }

        // TODO Add DrawScopeViewModel
        // TODO Refactorize to follow a testable aproach
        // TODO Change plain text with string resources
        // TODO Improve performance (less recompositions)
        // translate example
        item {
            ExampleComponent(
                title = "traslate",
                description = "Mueve las operaciones de dibujo hacia arriba, hacia abajo, hacia la izquierda o hacia la derecha.\nEl ejemplo consiste en mover una imagen horizontal y verticalmente.",
                content = { TranslateExample() }
            )
        }

        // TODO Add DrawScopeViewModel
        // TODO Refactorize to follow a testable aproach
        // TODO Change plain text with string resources
        // TODO Improve performance (less recompositions)
        // withTransform (multiple transformations) example
        item {
            ExampleComponent(
                title = "Multiples transformaciones",
                description = "Se usa la funcion withTransform() para aplicar varias transformaciones a un elemento al mismo tiempo.\nEn el ejemplo, se va a trasladar una imagen en el eje vertical, mientras que al mismo tiempo cambia su rotacion con respecto a su posicion.",
                content = { MultipleTransformExample() }
            )
        }
    }
}


@CompactSizeScreenThemePreview
@Composable
private fun DrawScopeExamplePreview() {
    AppTheme {
        DrawScopeScreen(
            onMenuButtonClick = {}
        )
    }
}