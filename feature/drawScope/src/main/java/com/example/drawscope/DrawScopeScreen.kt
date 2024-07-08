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

    val topAppBarTitle = stringResource(R.string.draw_scope_title)

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = { topAppBarTitle },
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

        // drawOutline example
        item {
            ExampleComponent(
                title = stringResource(R.string.draw_scope_draw_outline_tilte),
                description = stringResource(R.string.draw_scope_draw_outline_description),
                content = {
                    DrawOutlineExample(
                        drawScopeViewModel = hiltViewModel<DrawScopeViewModel>(viewModelStoreOwner)
                    )
                }
            )
        }

        // drawText example
        item {
            ExampleComponent(
                title = stringResource(R.string.draw_scope_draw_text_title),
                description = stringResource(R.string.draw_scope_draw_text_description),
                content = {
                    DrawTextExample(
                        drawScopeViewModel = hiltViewModel<DrawScopeViewModel>(viewModelStoreOwner)
                    )
                }
            )
        }

        // drawImage example
        item {
            ExampleComponent(
                title = stringResource(R.string.draw_scope_draw_image_title),
                description = stringResource(R.string.draw_scope_draw_image_description),
                content = {
                    DrawImageExample(
                        drawScopeViewModel = hiltViewModel<DrawScopeViewModel>(viewModelStoreOwner)
                    )
                }
            )
        }

        // DrawTransform transformations horizontal banner
        stickyHeader(key = 2) {
            HorizontalListBanner(title = stringResource(R.string.draw_scope_list_banner_2))
        }

        // clipPath example
        item {
            ExampleComponent(
                title = stringResource(R.string.draw_scope_clip_path_title),
                description = stringResource(R.string.draw_scope_clip_path_description),
                content = {
                    ClipPathExample(
                        drawScopeViewModel = hiltViewModel<DrawScopeViewModel>(viewModelStoreOwner)
                    )
                }
            )
        }

        // clipRect example
        item {
            ExampleComponent(
                title = stringResource(R.string.draw_scope_clip_rect_title),
                description = stringResource(R.string.draw_scope_clip_rect_description),
                content = {
                    ClipRectExample(
                        drawScopeViewModel = hiltViewModel<DrawScopeViewModel>(viewModelStoreOwner)
                    )
                }
            )
        }

        // inset example
        item {
            ExampleComponent(
                title = stringResource(R.string.draw_scope_inset_title),
                description = stringResource(R.string.draw_scope_inset_description),
                content = {
                    InsetExample(
                        drawScopeViewModel = hiltViewModel<DrawScopeViewModel>(viewModelStoreOwner)
                    )
                }
            )
        }

        // rotate example
        item {
            ExampleComponent(
                title = stringResource(R.string.draw_scope_rotate_title),
                description = stringResource(R.string.draw_scope_rotate_description),
                content = {
                    RotateExample(
                        drawScopeViewModel = hiltViewModel<DrawScopeViewModel>(viewModelStoreOwner)
                    )
                }
            )
        }

        // scale example
        item {
            ExampleComponent(
                title = stringResource(R.string.draw_scope_scale_title),
                description = stringResource(R.string.draw_scope_scale_description),
                content = {
                    ScaleExample(
                        drawScopeViewModel = hiltViewModel<DrawScopeViewModel>(viewModelStoreOwner)
                    )
                }
            )
        }

        // translate example
        item {
            ExampleComponent(
                title = stringResource(R.string.draw_scope_translate_title),
                description = stringResource(R.string.draw_scope_translate_description),
                content = {
                    TranslateExample(
                        drawScopeViewModel = hiltViewModel<DrawScopeViewModel>(viewModelStoreOwner)
                    )
                }
            )
        }

        // TODO Improve performance (less recompositions)
        // withTransform (multiple transformations) example
        item {
            ExampleComponent(
                title = stringResource(R.string.draw_scope_multiple_transformations_title),
                description = stringResource(R.string.draw_scope_multiple_transformations_description),
                content = {
                    MultipleTransformExample(
                        drawScopeViewModel = hiltViewModel<DrawScopeViewModel>(viewModelStoreOwner)
                    )
                }
            )
        }
    }
}