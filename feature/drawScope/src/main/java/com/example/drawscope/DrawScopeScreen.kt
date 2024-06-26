package com.example.drawscope

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
import androidx.compose.ui.unit.dp
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
import com.example.ui.ui.DefaultTopAppBar
import com.example.ui.ui.ExampleComponent
import com.example.ui.ui.CompactSizeScreenThemePreview


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
                title = "Draw Scopes",
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
    // List of graphics and transformations that can be drawn with Compose
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp),
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        // DrawScope graphics horizontal banner
        item(key = 1) { com.example.ui.ui.HorizontalListBanner(title = "Graficos con DrawScope") }

        // drawArc example
        item {
            ExampleComponent(
                title = "drawArc",
                description = "Dibuja un arco escalado para encajar dentro de un rectangulo dado.\nEn este ejemplo se dibujara un arco usando drawArc al que se le podra cambiar el angulo final.",
                content = { DrawArcExample() })
        }

        // drawCircle example
        item {
            ExampleComponent(
                title = "drawCircle",
                description = "Dibuja un circulo con el centro de coordenada y radio proporcionados.\nEn este ejemplo se dibujara un circulo, al cual se le prodra ajustar el tamaño y posicion.",
                content = { DrawCircleExample() }
            )
        }

        // drawLine example
        item {
            ExampleComponent(
                title = "drawLine",
                description = "Dibuja una linea entre los puntos establecidos.\nEn el siguiente ejemplo se dibujara una linea con la posicion final del trazo personalizable.",
                content = { DrawLineExample() }
            )
        }

        // drawOval example
        item {
            ExampleComponent(
                title = "drawOval",
                description = "Dibuja un ovalo de acuerdo con el tamaño y posicion determinados.\nEn el siguiente ejemplo se dibujara un ovalo con un tamaño y posicion personalizados.",
                content = { DrawOvalExample() }
            )
        }

        // drawPath example
        item {
            ExampleComponent(
                title = "drawPath",
                description = "Serie de instrucciones matematicas que dan como resultado un dibujo una vez ejecutado.\n En este ejemplo se trazara una grafica generada aleatoriamente usando un drawPath.",
                content = { DrawPathExample() }
            )
        }

        // drawPoints example
        item {
            ExampleComponent(
                title = "drawPoints",
                description = "Dibuja una secuencia de puntos de acuerdo con el PointMode proporcionado.\nEn este ejemplo se van a dibujar puntos en la pantalla en cada lugar que se presione, tambien se podra eliminar el ultimo punto agregado y borrarlos todos, asi como seleccionar el tipo de punto.",
                content = { DrawPointsExample() }
            )
        }

        // drawRect example
        item {
            ExampleComponent(
                title = "drawRect",
                description = "Dibuja un rectangulo con el tamaño y coordenadas proporcionadas.\nEn este ejemplo se dibujara un rectangulo con posicion y tamaño personalizables.",
                content = { DrawRectExample() }
            )
        }

        // drawRoundRect example
        item {
            ExampleComponent(
                title = "drawRoundRect",
                description = "Dibuja un rectangulo redondeado.\nEn el ejemplo se dibujara un rectangulo redondeado con posicion fija, pero con tamaño y radio de esquinas personalizables.",
                content = { DrawRoundRectExample() }
            )
        }

        // drawOutline example
        item {
            ExampleComponent(
                title = "",
                description = "El Outline define una forma simple, usado para limitar regiones graficas, la forma de una sombra proyectada por el componente, o para recortar los contenidos.\nEn el ejemplo se dibujara un outline al que se podra cambiar el largo y ancho.",
                content = { DrawOutlineExample() }
            )
        }

        // drawText example
        item {
            ExampleComponent(
                title = "drawText",
                description = "DrawText se usa cuando se busca dibujar un texto con personalizacion. Dibuja un layout de texto existente producido por TextMeasurer.\nEn el ejemplo se muestra un texto que contiene un numero aleatorio de palabras, el cual su tamaño dependera de la cantidad de texto que contenga con un maximo de ancho y alto personalizable.",
                content = { DrawTextExample() }
            )
        }

        // drawImage example
        item {
            ExampleComponent(
                title = "drawImage",
                description = "Para dibujar un imageBitmap con DrawScope, se carga la imagen con imageResource y se llama drawImage.\n ",
                content = { DrawImageExample() }
            )
        }

        // DrawTransform transformations horizontal banner
        item(key = 2) { com.example.ui.ui.HorizontalListBanner(title = "Transformaciones con DrawTransform") }

        // clipPath example
        item {
            ExampleComponent(
                title = "clipPath",
                description = "Reduce la region del recorte a la interseccion del recorte actual y el rectangulo redondeado dado.\nEn este ejemplo se aplica un clipPath a una imagen.",
                content = { ClipPathExample() }
            )
        }

        // clipRect example
        item {
            ExampleComponent(
                title = "clipRect",
                description = "Reduce la region de corte a la interseccion del recorte actual y el rectangulo dado indicado por los limites izquierdo, superior, derecho e inferior.\nEn este ejemplo se aplica un clipRect a la imagen",
                content = { ClipRectExample() }
            )
        }

        // inset example
        item {
            ExampleComponent(
                title = "inset",
                description = "Ajusta los parametros predeterminados del DrawScope actual cambia los limites de dibujoj y traslada los dibujos segun corresponda.\nEl siguiente ejemplo ajusta la escala vertical y horizontal de una imagen.",
                content = { InsetExample() }
            )
        }

        // rotate example
        item {
            ExampleComponent(
                title = "rotate",
                description = "Agrega una rotacion (en grados con direccion a las agujas del reloj) a la transformacion actual en el punto de pivote dado.\nEn el ejemplo se va a rotar una imagen en el sentido de las agujas del reloj.",
                content = { RotateExample() }
            )
        }

        // scale example
        item {
            ExampleComponent(
                title = "scale",
                description = "Aumenta el tamaño de las operaciones de dibujo por un factor.\nEn el ejemplo se va a escalar una imagen horizontal y verticalmente con respecto a la parte superior izquierda del dibujo.",
                content = { ScaleExample() }
            )
        }

        // translate example
        item {
            ExampleComponent(
                title = "traslate",
                description = "Mueve las operaciones de dibujo hacia arriba, hacia abajo, hacia la izquierda o hacia la derecha.\nEl ejemplo consiste en mover una imagen horizontal y verticalmente.",
                content = { TranslateExample() }
            )
        }

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