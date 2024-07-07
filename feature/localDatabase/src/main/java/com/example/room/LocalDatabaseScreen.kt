package com.example.room

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.room.util.LocalDatabaseDestinations
import com.example.room.util.LocalDatabaseNavHost
import com.example.ui.ui.DefaultTopAppBar


@Composable
fun LocalDatabaseScreen(
    onMenuButtonClick: () -> Unit
) {

    // Nav controller and current back stack entry to observe the current route
    val localDatabaseNavController = rememberNavController()
    // Observes the localDatabaseNavController BackStackEntry as State
    val currentBackStackEntry by localDatabaseNavController.currentBackStackEntryAsState()
    // Current route based on the current back stack entry
    val currentRoute: String? = currentBackStackEntry?.destination?.route

    // Stores the current ViewModelStoreOwner
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current)
    // Creates a viewModel instance binded to viewModelStoreOwner
    val productsViewModel: ProductsViewModel = hiltViewModel(viewModelStoreOwner)

    val currentProductName by productsViewModel.currentProductName.collectAsState()

    LocalDatabaseScreenContent(
        currentRoute = { currentRoute },
        currentProductName = { currentProductName },
        onMenuButtonClick = onMenuButtonClick,
        localDatabaseNavController = { localDatabaseNavController },
        viewModelStoreOwner = viewModelStoreOwner,
        deleteProduct = productsViewModel::deleteProduct,
        setNewProduct = productsViewModel::setNewProduct,
        changeCurrentProductByID = productsViewModel::changeCurrentProductByID,
        insertProduct = productsViewModel::insertProduct,
        updateProduct = productsViewModel::updateProduct
    )
}


@Composable
private fun LocalDatabaseScreenContent(
    currentRoute: () -> String?,
    currentProductName: () -> String,
    onMenuButtonClick: () -> Unit,
    localDatabaseNavController: () -> NavHostController,
    viewModelStoreOwner: ViewModelStoreOwner,
    deleteProduct: () -> Unit,
    setNewProduct: () -> Unit,
    changeCurrentProductByID: (Long) -> Unit,
    insertProduct: () -> Unit,
    updateProduct: () -> Unit
) {
    Scaffold(
        topBar = {
            DefaultTopAppBar(
                // If the current route is the EditProduct screen, display the name of the product,
                // otherwise display the title of the screen
                title = if (currentRoute() == LocalDatabaseDestinations.EditProduct.screenRouteName) currentProductName()
                else stringResource(id = getNavigationDestinarionFromRoute(currentRoute()).screenTitle),
                onMenuButtonClick = onMenuButtonClick,
                onBackButtonPressed = { localDatabaseNavController().popBackStack() },
                isPrincipalScreen = currentRoute() == LocalDatabaseDestinations.ProductsList.screenRouteName,
                // Show the delete action button icon only when the current route is the EditProduct screen
                actionButtonIcon = if (currentRoute() == LocalDatabaseDestinations.EditProduct.screenRouteName) Icons.Default.Delete else null,
                onActionButtonClick = {
                    // Deletes the current product
                    deleteProduct()
                    // Returns to the previous screen
                    localDatabaseNavController().popBackStack()
                },
                actionButtonContentDescription = "Delete the product ${currentProductName()}"
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = currentRoute() == "productsList",
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                FloatingActionButton(
                    onClick = {
                        // Fully clears the current product
                        setNewProduct()
                        localDatabaseNavController().navigate("addProduct")
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add new product"
                    )
                }
            }
        }
    ) { innerPadding ->
        // LocalDatabase screen NavHost
        LocalDatabaseNavHost(
            navController = { localDatabaseNavController() },
            viewModelStoreOwner = { viewModelStoreOwner },
            navigateToProduct = { selectedProductId ->
                // Changes the current product based in the ID
                changeCurrentProductByID(selectedProductId)
                // Navigates to the Add Product screen
                localDatabaseNavController().navigate(
                    route = LocalDatabaseDestinations.EditProduct.screenRouteName
                )
            },
            onAddProductClick = {
                // Insert a new product in the database
                insertProduct()
                // Returns to the previous screen
                localDatabaseNavController().popBackStack()
            },
            onUpdateProductClick = {
                // Update the current product in the database
                updateProduct()
                // Returns to the previous screen
                localDatabaseNavController().popBackStack()
            },
            modifier = { Modifier.padding(innerPadding) }
        )
    }
}




private fun getNavigationDestinarionFromRoute(route: String?) =
    LocalDatabaseDestinations.entries.find {
        it.screenRouteName == route
    } ?: LocalDatabaseDestinations.ProductsList