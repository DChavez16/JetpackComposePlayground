package com.example.util

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes


// Root destinations enum class
enum class RootNavigationDestination(
    @StringRes val itemTitle: Int?,
    val itemRouteName: String,
    @DrawableRes val itemIcon: Int?
) {
    LazyLayouts(
        itemTitle = R.string.lazy_layouts_screen,
        itemRouteName = "lazyLayouts",
        itemIcon = R.drawable.jetpack_compose_icon
    ),
    Animations(
        itemTitle = R.string.animations_screen,
        itemRouteName = "animations",
        itemIcon = R.drawable.jetpack_compose_icon
    ),
    DrawScope(
        itemTitle = R.string.draw_scope_screen,
        itemRouteName = "drawScope",
        itemIcon = R.drawable.jetpack_compose_icon
    ),
    Themes(
        itemTitle = R.string.themes_screen,
        itemRouteName = "themes",
        itemIcon = R.drawable.jetpack_compose_icon
    ),
    DependencyInjection(
        itemTitle = R.string.dependency_injection_screen,
        itemRouteName = "dependencyInjection",
        itemIcon = R.drawable.jetpack_compose_icon
    ),
    LocalDatabase(
        itemTitle = R.string.local_database_screen,
        itemRouteName = "localDatabase",
        itemIcon = R.drawable.jetpack_compose_icon
    ),
    DataPersistence(
        itemTitle = R.string.data_persistence_screen,
        itemRouteName = "dataPersistence",
        itemIcon = R.drawable.jetpack_compose_icon
    ),
    PersistentWork(
        itemTitle = R.string.persistent_work_screen,
        itemRouteName = "persistentWork",
        itemIcon = R.drawable.jetpack_compose_icon
    ),
    Configuration(
        itemTitle = null,
        itemRouteName = "configuration",
        itemIcon = null
    )
}