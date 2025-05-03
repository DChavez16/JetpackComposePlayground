package com.example.room.util

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.model.isValidForSave
import com.example.room.R
import com.example.room.ui.AddProductScreen
import com.example.room.ui.ProductListScreen
import com.example.room.ui.ProductsViewModel


private const val LOG_TAG = "LocalDatabaseNavHost"

// Local database destinations enum class
internal enum class LocalDatabaseDestinations(
    @StringRes val screenTitle: Int,
    val screenRouteName: String
) {
    ProductsList(
        screenTitle = R.string.local_database_products_list_screen_title,
        screenRouteName = "productsList"
    ),
    AddProduct(
        screenTitle = R.string.local_database_add_product_screen_title,
        screenRouteName = "addProduct"
    ),
    EditProduct(
        screenTitle = R.string.local_database_edit_product_screen_title,
        screenRouteName = "editProduct"
    )
}

// Local database navigation graph
fun NavGraphBuilder.localDatabaseGraph(
    navController: NavHostController,
    graphRoute: String,
    onMenuButtonClick: () -> Unit
) {

    navigation(
        startDestination = LocalDatabaseDestinations.ProductsList.screenRouteName,
        route = graphRoute
    ) {
        // Products List destination
        composable(
            route = LocalDatabaseDestinations.ProductsList.screenRouteName
        ) {
            Log.i(LOG_TAG, "ProductListScreen created")

            // Creates viewModel using the parent entry
            val parentEntry = remember { navController.getBackStackEntry(graphRoute) }
            val productsViewModel = hiltViewModel<ProductsViewModel>(parentEntry)

            // Products list uiState
            val productsUiState by productsViewModel.productsUiState.collectAsState()

            ProductListScreen(
                productsUiState = { productsUiState },
                onAddProductClick = {
                    // Clears the current product
                    productsViewModel.setNewProduct()

                    // Navigates to the "Add product" screen
                    navController.navigate(LocalDatabaseDestinations.AddProduct.screenRouteName)
                },
                navigateToProduct = { selectedProductId ->
                    // Changes the current product based in the ID
                    productsViewModel.changeCurrentProductByID(selectedProductId)

                    // Navigates to the "Add Product" screen in edit mode
                    navController.navigate(LocalDatabaseDestinations.EditProduct.screenRouteName)
                },
                onMenuButtonClick = onMenuButtonClick
            )
        }

        // Add Product destination
        composable(
            route = LocalDatabaseDestinations.AddProduct.screenRouteName
        ) {
            Log.i(LOG_TAG, "AddProductScreen created in add mode")

            // Creates viewModel using the parent entry
            val parentEntry = remember { navController.getBackStackEntry(graphRoute) }
            val productsViewModel = hiltViewModel<ProductsViewModel>(parentEntry)

            // Current product state
            val currentProduct by productsViewModel.currentProduct.collectAsState()

            // Top App Bar title
            val topAppBarTitle = stringResource(LocalDatabaseDestinations.AddProduct.screenTitle)

            AddProductScreen(
                currentProduct = { currentProduct },
                onSaveProduct = {
                    // Insert a new product in the database
                    productsViewModel.insertProduct()

                    // Returns to the previous screen
                    navController.popBackStack()
                },
                onProductNameChange = productsViewModel::updateProductName,
                onProductQuantityChange = productsViewModel::updateProductQuantity,
                onProductDescriptionChange = productsViewModel::updateProductDescription,
                onProductAvailabilityChange = productsViewModel::updateProductAvailability,
                clearProductFields = productsViewModel::clearProductFields,
                isSaveButtonEnabled = { currentProduct.isValidForSave() },
                topAppBarTitle = topAppBarTitle,
                onBackButtonPressed = navController::popBackStack
            )
        }

        // Edit Product destination
        composable(
            route = LocalDatabaseDestinations.EditProduct.screenRouteName
        ) {
            Log.i(LOG_TAG, "AddProductScreen created in edit mode")

            // Creates viewModel using the parent entry
            val parentEntry = remember { navController.getBackStackEntry(graphRoute) }
            val productsViewModel = hiltViewModel<ProductsViewModel>(parentEntry)

            // Current product state
            val currentProduct by productsViewModel.currentProduct.collectAsState()

            // Top App Bar title
            val topAppBarTitle by productsViewModel.currentProductName.collectAsState()

            AddProductScreen(
                currentProduct = { currentProduct },
                onSaveProduct = {
                    // Update the current product in the database
                    productsViewModel.updateProduct()

                    // Returns to the previous screen
                    navController.popBackStack()
                },
                onProductNameChange = productsViewModel::updateProductName,
                onProductQuantityChange = productsViewModel::updateProductQuantity,
                onProductDescriptionChange = productsViewModel::updateProductDescription,
                onProductAvailabilityChange = productsViewModel::updateProductAvailability,
                clearProductFields = productsViewModel::clearProductFields,
                isSaveButtonEnabled = { currentProduct.isValidForSave() },
                topAppBarTitle = topAppBarTitle,
                onBackButtonPressed = navController::popBackStack,
                showDeletionActionButton = true,
                onDeleteProduct = productsViewModel::deleteProduct
            )
        }
    }
}