package com.example.mobile.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.example.mobile.util.RootNavHost
import com.example.navigationdrawer.CustomNavigationDrawer
import com.example.navigationdrawer.CustomNavigationRail
import com.example.util.RootNavigationDestination
import kotlinx.coroutines.launch


@Composable
fun ComposePlaygroundApp(
    rootNavController: NavHostController,
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
) {

    // TODO Add background to navigation drawer header so the elements can't be seen behind it
    // TODO Fix Navigation rail adding padding outside it bounds
    // TODO Fix Drawer being draggable in child routes

    // Defines a coroutine scope
    val scope = rememberCoroutineScope()

    // Defines the drawer initial state as closed
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    // Observes the rootNavController BackStackEntry as State
    val rootNavBackStackEntry by rootNavController.currentBackStackEntryAsState()
    // Current route based on the current back stack entry
    val currentRoute: String? = rootNavBackStackEntry?.destination?.let {
        it.parent?.route ?: it.route
    }


    /* Navigation drawer behavior:
        When windowWidthSizeClass == WindowWidthSizeClass.COMPACT hide the drawer and show it by pressing the menu button
        When windowWidthSizeClass == WindowWidthSizeClass.MEDIUM use a static navigation rail
        When windowWidthSizeClass == WindowWidthSizeClass.EXPANDED use a static navigation drawer
     */
    when (windowSizeClass.windowWidthSizeClass) {
        // Window Width Size Class is COMPACT use a Modal Navigation Drawer
        WindowWidthSizeClass.COMPACT -> {
            // Modal Navigation Drawer containing the Nav Host
            ModalNavigationDrawer(
                drawerContent = {
                    CustomNavigationDrawer(
                        // Returns a RootNavigationDestination that matches with the rootNavBackStackEntry's route
                        currentSelectedItem = { getNavigationDestinationFromRoute(currentRoute) },
                        onDrawerItemClick = { selectedDestination ->
                            // Changes the current destination when an item is clicked, navigates to that
                            // destination and closes the drawer

                            // Clears the back stack and navigates to the selected destination
                            rootNavController.navigate(
                                route = selectedDestination.itemRouteName,
                                navOptions = NavOptions
                                    .Builder()
                                    .setPopUpTo(0, true)
                                    .build()
                            )

                            scope.launch {
                                // Closes the Navigation Drawer
                                drawerState.close()
                            }
                        },
                        onConfigurationButtonClick = {
                            // Navigates to the configuration destination
                            rootNavController.navigate(
                                route = RootNavigationDestination.Configuration.itemRouteName
                            )

                            scope.launch {
                                // Closes the Navigation Drawer
                                drawerState.close()
                            }
                        }
                    )
                },
                drawerState = drawerState,
                gesturesEnabled = currentRoute != RootNavigationDestination.Configuration.itemRouteName
            ) {
                // Sets the root NavHost that handles the navigation to the features destinations
                RootNavHost(
                    navController = rootNavController,
                    onMenuButtonClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    },
                    onBackButtonClick = {
                        rootNavController.popBackStack()
                    }
                )
            }
        }

        // Window Width Size Class is MEDIUM use a Navigation Rail
        WindowWidthSizeClass.MEDIUM -> {
            Row {
                // Navigation Rail
                CustomNavigationRail(
                    currentSelectedItem = { getNavigationDestinationFromRoute(currentRoute) },
                    onRailItemClick = { selectedDestination ->
                        // Changes the current destination when an item is clicked, navigates to that
                        // destination and closes the drawer

                        // Clears the back stack and navigates to the selected destination
                        rootNavController.navigate(
                            route = selectedDestination.itemRouteName,
                            navOptions = NavOptions
                                .Builder()
                                .setPopUpTo(0, true)
                                .build()
                        )

                        scope.launch {
                            // Closes the Navigation Drawer
                            drawerState.close()
                        }
                    },
                    onConfigurationButtonClick = {
                        // Navigates to the configuration destination
                        rootNavController.navigate(
                            route = RootNavigationDestination.Configuration.itemRouteName
                        )

                        scope.launch {
                            // Closes the Navigation Drawer
                            drawerState.close()
                        }
                    }
                )

                // Nav Host
                RootNavHost(
                    navController = rootNavController,
                    onMenuButtonClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    },
                    onBackButtonClick = {
                        rootNavController.popBackStack()
                    }
                )
            }
        }

        // Window Width Size Class is EXPANDED use a Permanent Navigation Drawer
        WindowWidthSizeClass.EXPANDED -> {
            // Permanent Navigation Drawer containing the Nav Host
            PermanentNavigationDrawer(
                drawerContent = {
                    CustomNavigationDrawer(
                        // Returns a RootNavigationDestination that matches with the rootNavBackStackEntry's route
                        currentSelectedItem = { getNavigationDestinationFromRoute(currentRoute) },
                        onDrawerItemClick = { selectedDestination ->
                            // Changes the current destination when an item is clicked, navigates to that
                            // destination and closes the drawer

                            // Clears the back stack and navigates to the selected destination
                            rootNavController.navigate(
                                route = selectedDestination.itemRouteName,
                                navOptions = NavOptions
                                    .Builder()
                                    .setPopUpTo(0, true)
                                    .build()
                            )

                            scope.launch {
                                // Closes the Navigation Drawer
                                drawerState.close()
                            }
                        },
                        onConfigurationButtonClick = {
                            // Navigates to the configuration destination
                            rootNavController.navigate(
                                route = RootNavigationDestination.Configuration.itemRouteName
                            )

                            scope.launch {
                                // Closes the Navigation Drawer
                                drawerState.close()
                            }
                        }
                    )
                },
            ) {
                RootNavHost(
                    navController = rootNavController,
                    onMenuButtonClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    },
                    onBackButtonClick = {
                        rootNavController.popBackStack()
                    }
                )
            }
        }
    }
}


private fun getNavigationDestinationFromRoute(route: String?) =
    RootNavigationDestination.entries.find {
        it.itemRouteName == route
    } ?: RootNavigationDestination.LazyLayouts