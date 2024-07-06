package com.example.room

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

    // Backing property for the current product to avoid state updates from other classes
    private val _currentProduct = MutableStateFlow(emptyProduct)
    // The UI collects from this StateFlow to get its state updates
    internal val currentProduct: StateFlow<Product> = _currentProduct

    // Backing property for the current product's name to avoid state updates from other classes
    private val _currentProductName = MutableStateFlow("")
    // The UI collects from this StateFlow to get its state updates
    internal val currentProductName: StateFlow<String> = _currentProductName


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
            _currentProduct.value = newProduct
            // Sets the currentProductName as the name of the obtained product
            _currentProductName.value = newProduct.name
        }
    }

    // Method that fully clears the current product
    fun setNewProduct() {
        _currentProduct.value = emptyProduct
    }

    // Method that clears the current product fields and leaves the ID intact
    fun clearProductFields() {
        _currentProduct.value = _currentProduct.value.copy(
            name = "",
            description = "",
            quantity = 0,
            isAvailable = false
        )
    }

    // Method that inserts a new product in the database
    fun insertProduct() {
        viewModelScope.launch {
            productRepository.insertProduct(currentProduct.value)
        }
    }

    // Method that updates a product in the database
    fun updateProduct() {
        viewModelScope.launch {
            productRepository.updateProduct(currentProduct.value)
        }
    }

    // Method that deletes a product in the database
    fun deleteProduct() {
        viewModelScope.launch {
            productRepository.deleteProduct(currentProduct.value)
        }
    }

    // Method that changes the name of the current product
    fun updateProductName(newName: String) {
        _currentProduct.value = currentProduct.value.copy(name = newName)
    }

    // Method that changes the quantity of the current product
    fun updateProductQuantity(newQuantity: Int) {
        _currentProduct.value = currentProduct.value.copy(quantity = newQuantity)
    }

    // Method that changes the description of the current product
    fun updateProductDescription(newDescription: String) {
        _currentProduct.value = currentProduct.value.copy(description = newDescription)
    }

    // Method that changes the availability of the current product
    fun updateProductAvailability(newAvailability: Boolean) {
        _currentProduct.value = currentProduct.value.copy(isAvailable = newAvailability)
    }
}