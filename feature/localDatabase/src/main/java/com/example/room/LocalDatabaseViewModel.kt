package com.example.room

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.Product
import com.example.model.emptyProduct
import com.example.product.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject


// Interface that represents the state of the ui based on requests responses
internal sealed interface ProductsUiState {
    data class Success(val products: List<Product>) : ProductsUiState
    data class Error(val errorMessage: String) : ProductsUiState
    data object Loading : ProductsUiState
}


@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    // Backing property to avoid state updates from other classes
    private val _productsUiState = MutableStateFlow<ProductsUiState>(ProductsUiState.Loading)

    // The UI collects from this StateFlow to get its state updates
    internal val productsUiState: StateFlow<ProductsUiState> = _productsUiState

    // State that represents the current product focused in the app
    var currentProduct by mutableStateOf(emptyProduct)
        private set

    var currentProductName by mutableStateOf("")
        private set


    // Block of code executed at ViewModel creation
    init {
        // Observe a flow of products list obtained from the repository
        viewModelScope.launch {
            productRepository.getProductsFlow()
                .onStart {
                    _productsUiState.value = ProductsUiState.Loading
                }
                .catch { error ->
                    _productsUiState.value = ProductsUiState.Error(error.message.toString())
                }
                .collect { products ->
                    _productsUiState.value = ProductsUiState.Success(products)
                }
        }
    }


    // Method that gets the product with the given ID from the database
    fun changeCurrentProductByID(productId: Long) {
        viewModelScope.launch {
            // Obtains the product with the given ID from the databas
            val newProduct = productRepository.getProduct(productId)

            // Sets the currentProduct as the obtained product
            currentProduct = newProduct
            // Sets the currentProductName as the name of the obtained product
            currentProductName = newProduct.name
        }
    }

    // Method that fully clears the current product
    fun setNewProduct() {
        currentProduct = emptyProduct
    }

    // Method that clears the current product fields and leaves the ID intact
    fun clearProductFields() {
        currentProduct = currentProduct.copy(
            name = "",
            description = "",
            quantity = 0,
            isAvailable = false
        )
    }

    // Method that inserts a new product in the database
    fun insertProduct() {
        viewModelScope.launch {
            productRepository.insertProduct(currentProduct)
        }
    }

    // Method that updates a product in the database
    fun updateProduct() {
        viewModelScope.launch {
            productRepository.updateProduct(currentProduct)
        }
    }

    // Method that deletes a product in the database
    fun deleteProduct() {
        viewModelScope.launch {
            productRepository.deleteProduct(currentProduct)
        }
    }

    // Method that changes the name of the current product
    fun updateProductName(newName: String) {
        currentProduct = currentProduct.copy(name = newName)
    }

    // Method that changes the quantity of the current product
    fun updateProductQuantity(newQuantity: Int) {
        currentProduct = currentProduct.copy(quantity = newQuantity)
    }

    // Method that changes the description of the current product
    fun updateProductDescription(newDescription: String) {
        currentProduct = currentProduct.copy(description = newDescription)
    }

    // Method that changes the availability of the current product
    fun updateProductAvailability(newAvailability: Boolean) {
        currentProduct = currentProduct.copy(isAvailable = newAvailability)
    }
}