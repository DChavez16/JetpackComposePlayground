package com.example.testing.repositories

import com.example.model.Product
import com.example.model.fakeProductsList
import com.example.product.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeLocalProductRepository : ProductRepository {
    // Fake flows
    private val productsFlow = MutableStateFlow(fakeProductsList)

    override fun getProductsFlow(): Flow<List<Product>> =
        productsFlow

    override suspend fun getProduct(id: Long): Product =
        productsFlow.value.first { currentProduct: Product ->
            currentProduct.id == id
        }

    override suspend fun insertProduct(product: Product) {
        // Create a list of products and add the new product to it with a new ID
        val products = productsFlow.value.toMutableList()
        products.add(
            product.copy(id = generateProductID(products))
        )

        // Emit the new list into the flow
        productsFlow.emit(products)
    }

    override suspend fun updateProduct(product: Product) {
        // Create a list of products and replace the old product with the new one
        val products = productsFlow.value.toMutableList()
        products.replaceAll { currentProduct: Product ->
            if (currentProduct.id == product.id) product else currentProduct
        }

        // Emit the new list into the flow
        productsFlow.emit(products)
    }

    override suspend fun deleteProduct(product: Product) {
        // Create a list of products and remove the product from it
        val products = productsFlow.value.toMutableList()
        products.remove(product)

        // Emit the new list into the flow
        productsFlow.emit(products)
    }
}


private fun generateProductID(productList: List<Product>): Long {
    // Gets the max id from the list
    val maxId = productList.maxOf { it.id ?: 0 }

    return maxId + 1L
}