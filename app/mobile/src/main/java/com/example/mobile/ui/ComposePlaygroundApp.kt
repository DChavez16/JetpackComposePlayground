package com.example.mobile.ui

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavOptions
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mobile.util.RootNavHost
import com.example.navigationdrawer.NavigationDrawerContent
import com.example.ui.theme.AppTheme
import com.example.ui.ui.ThemePreview
import com.example.util.RootNavigationDestination
import kotlinx.coroutines.launch


@Composable
fun ComposePlaygroundApp() {

    // Defines a coroutine scope
    val scope = rememberCoroutineScope()

    // Defines the drawer initial state as closed
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    // Root NavHostController
    val rootNavController = rememberNavController()
    // Observes the rootNavController BackStackEntry as State
    val rootNavBackStackEntry by rootNavController.currentBackStackEntryAsState()
    // Current route based on the current back stack entry
    val currentRoute: String? = rootNavBackStackEntry?.destination?.route

    ModalNavigationDrawer(
        drawerContent = {
            NavigationDrawerContent(
                // Returns a RootNavigationDestination that matches with the rootNavBackStackEntry's route
                currentSelectedItem = getNavigationDestinationFromRoute(currentRoute),
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



private fun getNavigationDestinationFromRoute(route: String?) =
    RootNavigationDestination.entries.find {
        it.itemRouteName == route
    } ?: RootNavigationDestination.LazyLayouts




@ThemePreview
@Composable
private fun ComposePlaygroundAppPreview() {
    AppTheme {
        ComposePlaygroundApp()
    }
}