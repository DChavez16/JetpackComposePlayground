package com.example.room.util

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.room.ui.ProductsViewModel
import com.example.room.R
import com.example.room.ui.AddProductScreen
import com.example.room.ui.ProductListScreen

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


// Local Database NavHost Composable function
@Composable
internal fun LocalDatabaseNavHost(
    navController: NavHostController,
    viewModelStoreOwner: () -> ViewModelStoreOwner,
    navigateToProduct: (Long) -> Unit,
    onAddProductClick: () -> Unit,
    onUpdateProductClick: () -> Unit,
    innerPadding: () -> PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = LocalDatabaseDestinations.ProductsList.screenRouteName,
        modifier = Modifier.padding(innerPadding())
    ) {
        // Products List destination
        composable(
            route = LocalDatabaseDestinations.ProductsList.screenRouteName
        ) {

            Log.i("ProductListScreen", "ProductListScreen created")

            ProductListScreen(
                navigateToProduct = navigateToProduct,
                // Sets as a parameter the viewModel instance binded to viewModelStoreOwner
                productsViewModel = hiltViewModel<ProductsViewModel>(viewModelStoreOwner())
            )
        }

        // Edit Product destination
        composable(
            route = LocalDatabaseDestinations.EditProduct.screenRouteName
        ) {

            Log.i("AddProductScreen", "Screen created in edit mode")

            AddProductScreen(
                onSaveProduct = onUpdateProductClick,
                // Sets as a parameter the viewModel instance binded to viewModelStoreOwner
                productsViewModel = hiltViewModel<ProductsViewModel>(viewModelStoreOwner())
            )
        }

        // Add Product destination
        composable(
            route = LocalDatabaseDestinations.AddProduct.screenRouteName
        ) {

            Log.i("AddProductScreen", "Screen created in add mode")

            AddProductScreen(
                onSaveProduct = onAddProductClick,
                // Sets as a parameter the viewModel instance binded to viewModelStoreOwner
                productsViewModel = hiltViewModel<ProductsViewModel>(viewModelStoreOwner())
            )
        }
    }
}