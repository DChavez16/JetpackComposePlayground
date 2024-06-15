package com.example.animations

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
import com.example.animations.ui.AnimateAsStateExample
import com.example.animations.ui.AnimateContentSizeExample
import com.example.animations.ui.AnimatedContentExample
import com.example.animations.ui.AnimatedVisibilityExample
import com.example.animations.ui.CrossfadeExample
import com.example.animations.ui.InfiniteTransitionExample
import com.example.animations.ui.UpdateTrasitionExample
import com.example.ui.theme.AppTheme
import com.example.ui.ui.DefaultTopAppBar
import com.example.ui.ui.ExampleComponent
import com.example.ui.ui.HorizontalListBanner
import com.example.ui.ui.ThemePreview


/**
 * Example that shows the various animations that can be used with Compose
 */
@Composable
fun AnimationScreen(
    onMenuButtonClick: () -> Unit
) {
    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = "Animations",
                onMenuButtonClick = onMenuButtonClick,
                // Empty since no seconday screen is used
                onBackButtonPressed = {}
            )
        }
    ) { innerPadding ->
        AnimationsList(
            modifier = Modifier.padding(innerPadding)
        )
    }
}


/**
 * List of animations that can be used with Compose
 */
@Composable
private fun AnimationsList(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Animation examples for content changes in the layout
        item(key = 1) { HorizontalListBanner(title = "Animaciones en cambios de contenido en el diseño") }

        // AnimatedVisibility example
        item {
            ExampleComponent(
                title = "AnimatedVisibility",
                description = "Se usa para ocultar o mostrar un elemento Composable\nSe puede personalizar la transicion al especificar EnterTransition y ExitTransition.\nEn este ejemplo se observa la animacion al aparecer y desaparecer un elemento usando una TweenSpec",
                content = { AnimatedVisibilityExample() }
            )
        }

        // Crossfade example
        item {
            ExampleComponent(
                title = "Crossfade",
                description = "Crossfade anima entre dos diseños con una animacion de encadenado. Si alternas el calor que se pasa al parametro current, el contenido se cambia con una animacion de encadenado.\nEn este ejemplo, se muestra la animacion al aumentar o disminuir el numero, asi como al cambiar el color de fondo",
                content = { CrossfadeExample() }
            )
        }

        // AnimatedContent example
        item {
            ExampleComponent(
                title = "AnimatedContent",
                description = "Este elemento que admite composicion anima su contenido a medida que cambia en funcion de un estado objetivo\nEste ejemplo se va a separar en dos partes, una para mostrar el uso de SizeTransform (Personalizando la forma en la que un componente cambia su tamaño al expandirse) y la otra para mostra el uso de ContentTransform (Usando Enter/ExitTransitions para animar la aparicion y desaparicion de elementos)",
                content = { AnimatedContentExample() }
            )
        }

        // animateContentSize example
        item {
            ExampleComponent(
                title = "animateContentSize",
                description = "El modificador animateContentSize anima un cambio de tamaño, se debe utilizar antes de otro modificador de tamaño para asegurar un correcto funcionamiento\nEl siguiente ejemplo consiste en expandir y contraer un contenedor para ver mas lineas de texto de una descripcion usando la animacion elegida",
                content = { AnimateContentSizeExample() }
            )
        }

        // Animations based on state examples
        item(key = 2) { HorizontalListBanner(title = "Animaciones basadas en el estado") }

        // rememberInfiniteTransition example
        item {
            ExampleComponent(
                title = "rememberInfiniteTransition",
                description = "Contiene una o mas animaciones secundarias, como Transition, pero las animaciones comienzan a ejecutarse apenas entran en la composicion y no se detienen, a menos que se las quite.\nEste ejemplo consiste simplemente en una imagen que gira indefinidamente.",
                content = { InfiniteTransitionExample() }
            )
        }

        // updateTransition example
        item {
            ExampleComponent(
                title = "updateTransition",
                description = "updateTransition crea y recuerda una instancia de Transition, y actualiza su estado.\nEn el siguiente ejemplo se va a expandir un cuadro de texto verticalmente, mientras que se le va a animar el color de fondo, elevacion, tipo de borde y rotacion de un boton de expandir.",
                content = { UpdateTrasitionExample() }
            )
        }

        // animate*AsState example
        item {
            ExampleComponent(
                title = "animate*AsState",
                description = "Son las APIs mas simples para crear un valor unico. So hay que proporcionar el valor final (o valor objetvio), y la API comenzara la animacion desde el valor actual hasta el especificada.\nEn el siguiente ejemplo se mostrara una imagen que cambiara su transparencia por medio de un Slider.",
                content = { AnimateAsStateExample() }
            )
        }
    }
}



@ThemePreview
@Composable
private fun AnimationsExamplePreview() {
    AppTheme {
        AnimationScreen(
            onMenuButtonClick = {}
        )
    }
}