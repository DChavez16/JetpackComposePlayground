package com.example.mobile.util

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.animations.AnimationScreen
import com.example.configuration.ConfigurationScreen
import com.example.datapersistence.DataPersistenceScreen
import com.example.drawscope.DrawScopeScreen
import com.example.lazylayouts.LazyLayoutScreen
import com.example.persistentWork.PersistentWorkScreen
import com.example.remotedatabase.util.remoteDatabaseGraph
import com.example.room.LocalDatabaseScreen
import com.example.themes.ThemeScreen
import com.example.util.RootNavigationDestination
import com.feature.alarms.AlarmsScreen

// Root Navigation Composable function
@Composable
internal fun RootNavHost(
    navController: NavHostController,
    onMenuButtonClick: () -> Unit,
    onBackButtonClick: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = RootNavigationDestination.LazyLayouts.itemRouteName
    ) {
        // Lazy Layout destination
        composable(
            route = RootNavigationDestination.LazyLayouts.itemRouteName
        ) {
            LazyLayoutScreen(
                onMenuButtonClick = onMenuButtonClick
            )
        }

        // Animation destination
        composable(
            route = RootNavigationDestination.Animations.itemRouteName
        ) {
            AnimationScreen(
                onMenuButtonClick = onMenuButtonClick
            )
        }

        // Draw Scope destination
        composable(
            route = RootNavigationDestination.DrawScope.itemRouteName
        ) {
            DrawScopeScreen(
                onMenuButtonClick = onMenuButtonClick
            )
        }

        // Theme destination
        composable(
            route = RootNavigationDestination.Themes.itemRouteName
        ) {
            ThemeScreen(
                onMenuButtonClick = onMenuButtonClick
            )
        }

        // Local Database destination
        composable(
            route = RootNavigationDestination.LocalDatabase.itemRouteName
        ) {
            LocalDatabaseScreen(
                onMenuButtonClick = onMenuButtonClick
            )
        }

        // Remote database graph
        remoteDatabaseGraph(
            navController = navController,
            graphRoute = RootNavigationDestination.RemoteDatabase.itemRouteName,
            onMenuButtonClick = onMenuButtonClick
        )

        // Data Persistence destination
        composable(
            route = RootNavigationDestination.DataPersistence.itemRouteName
        ) {
            DataPersistenceScreen(
                onMenuButtonClick = onMenuButtonClick
            )
        }

        // Persistent Work destination
        composable(
            route = RootNavigationDestination.PersistentWork.itemRouteName
        ) {
            PersistentWorkScreen(
                onMenuButtonClick = onMenuButtonClick
            )
        }

        // Alarms destination
        composable(
            route = RootNavigationDestination.Alarms.itemRouteName
        ) {
            AlarmsScreen(
                onMenuButtonClick = onMenuButtonClick
            )
        }

        // Configuration destination
        composable(
            route = RootNavigationDestination.Configuration.itemRouteName
        ) {
            ConfigurationScreen(
                onBackButtonClick = onBackButtonClick
            )
        }
    }
}