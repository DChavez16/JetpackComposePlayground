package com.example.mobile.util

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.example.animations.AnimationScreen
import com.example.configuration.ConfigurationScreen
import com.example.datapersistence.DataPersistenceScreen
import com.example.drawscope.DrawScopeScreen
import com.example.lazylayouts.LazyLayoutScreen
import com.example.persistentWork.PersistentWorkScreen
import com.example.remotedatabase.NotesScreen
import com.example.room.LocalDatabaseScreen
import com.example.themes.ThemeScreen
import com.example.util.RootNavigationDestination
import com.feature.alarms.AlarmsScreen

const val URI = "https://www.compose-playground.com"

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

        // Remote database destination
        composable(
            route = RootNavigationDestination.RemoteDatabase.itemRouteName,
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "$URI/notes/{notesPath}/{noteId}"
                }
            )
        ) { backStackEntry ->

            // Obtain the parameters from the backStackEntry
            val notesPath = backStackEntry.arguments?.getString("notesPath")
            val noteId = backStackEntry.arguments?.getLong("noteId")

            // Notes Screen nav controller
            val notesScreenNavController = rememberNavController()

            NotesScreen(
                notesNavController = notesScreenNavController,
                onMenuButtonClick = onMenuButtonClick
            )

            // If the deeplink paramethers are not null, navigate notesScreenNavController to the desired Uri
            if(notesPath != null) {
                Log.d("RootNavHost", "Path obtained from the backstack entry: $notesPath. with note id: ${noteId ?: "null"}")

                val deepLinkUri = when(notesPath) {
                    "notesList" -> { "$URI/notes/notesList/$noteId".toUri() }
                    "newNote" -> { "$URI/notes/newNote".toUri() }
                    else -> {
                        // Redirect to the notes list
                        "$URI/notes/notesList".toUri()
                    }
                }

                notesScreenNavController.navigate(deepLinkUri)
            }
        }

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