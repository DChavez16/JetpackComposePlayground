package com.example.util

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Animation
import androidx.compose.material.icons.rounded.Cloud
import androidx.compose.material.icons.rounded.Storefront
import androidx.compose.material.icons.rounded.Work
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource


// Root destinations enum class
enum class RootNavigationDestination(
    @StringRes val itemTitle: Int?,
    val itemRouteName: String,
    val itemIcon: @Composable () -> ImageVector?
) {

    // TODO Change the destination item icons to the declared in the Figma UI Kit

    LazyLayouts(
        itemTitle = R.string.lazy_layouts_screen,
        itemRouteName = "lazyLayouts",
        itemIcon = {
            ImageVector.vectorResource(R.drawable.responsive_layout)
        }
    ),
    Animations(
        itemTitle = R.string.animations_screen,
        itemRouteName = "animations",
        itemIcon = { Icons.Rounded.Animation }
    ),
    DrawScope(
        itemTitle = R.string.draw_scope_screen,
        itemRouteName = "drawScope",
        itemIcon = {
            ImageVector.vectorResource(R.drawable.draw_abstract)
        }
    ),
    Themes(
        itemTitle = R.string.themes_screen,
        itemRouteName = "themes",
        itemIcon = {
            ImageVector.vectorResource(R.drawable.colors)
        }
    ),
    LocalDatabase(
        itemTitle = R.string.local_database_screen,
        itemRouteName = "localDatabase",
        itemIcon = {
            ImageVector.vectorResource(R.drawable.database)
        }
    ),
    RemoteDatabase(
        itemTitle = R.string.remote_database_screen,
        itemRouteName = "remoteDatabase",
        itemIcon = { Icons.Rounded.Cloud }
    ),
    DataPersistence(
        itemTitle = R.string.data_persistence_screen,
        itemRouteName = "dataPersistence",
        itemIcon = { Icons.Rounded.Storefront }
    ),
    PersistentWork(
        itemTitle = R.string.persistent_work_screen,
        itemRouteName = "persistentWork",
        itemIcon = { Icons.Rounded.Work }
    ),
    Configuration(
        itemTitle = null,
        itemRouteName = "configuration",
        itemIcon = { null }
    )
}