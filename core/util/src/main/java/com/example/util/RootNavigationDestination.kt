package com.example.util

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.ui.graphics.vector.ImageVector


// Root destinations enum class
enum class RootNavigationDestination(
    @StringRes val itemTitle: Int?,
    val itemRouteName: String,
    val itemIcon: ImageVector?
) {
    LazyLayouts(
        itemTitle = R.string.lazy_layouts_screen,
        itemRouteName = "lazyLayouts",
//        itemIcon = R.drawable.jetpack_compose_icon
        itemIcon = Icons.Default.Build
    ),
    Animations(
        itemTitle = R.string.animations_screen,
        itemRouteName = "animations",
//        itemIcon = R.drawable.jetpack_compose_icon
        itemIcon = Icons.Default.Build
    ),
    DrawScope(
        itemTitle = R.string.draw_scope_screen,
        itemRouteName = "drawScope",
//        itemIcon = R.drawable.jetpack_compose_icon
        itemIcon = Icons.Default.Build
    ),
    Themes(
        itemTitle = R.string.themes_screen,
        itemRouteName = "themes",
//        itemIcon = R.drawable.jetpack_compose_icon
        itemIcon = Icons.Default.Build
    ),
    LocalDatabase(
        itemTitle = R.string.local_database_screen,
        itemRouteName = "localDatabase",
//        itemIcon = R.drawable.jetpack_compose_icon
        itemIcon = Icons.Default.Build
    ),
    DataPersistence(
        itemTitle = R.string.data_persistence_screen,
        itemRouteName = "dataPersistence",
//        itemIcon = R.drawable.jetpack_compose_icon
        itemIcon = Icons.Default.Build
    ),
    PersistentWork(
        itemTitle = R.string.persistent_work_screen,
        itemRouteName = "persistentWork",
//        itemIcon = R.drawable.jetpack_compose_icon
        itemIcon = Icons.Default.Build
    ),
    Configuration(
        itemTitle = null,
        itemRouteName = "configuration",
        itemIcon = null
    )
}