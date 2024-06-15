package com.example.product

import com.example.database.daos.ProductDAO
import com.example.model.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


interface ProductRepository {
    fun getProductsFlow(): Flow<List<Product>>
    suspend fun getProduct(id: Long): Product
    suspend fun insertProduct(product: Product)
    suspend fun updateProduct(product: Product)
    suspend fun deleteProduct(product: Product)
}


class LocalProductRepository @Inject constructor(
    private val productDao: ProductDAO
): ProductRepository {
    /**
     * Obtains a [Flow] of a [List] of [Product] from the local database
     */
    override fun getProductsFlow(): Flow<List<Product>> = productDao.getProductsFlow()

    /**
     * Obtains a [Product] from the local database
     */
    override suspend fun getProduct(id: Long): Product = productDao.getProduct(id)

    /**
     * Inserts a [Product] into the local database
     */
    override suspend fun insertProduct(product: Product) = productDao.insertProduct(product)

    /**
     * Updates a [Product] in the local database
     */
    override suspend fun updateProduct(product: Product) = productDao.updateProduct(product)

    /**
     * Deletes a [Product] from the local database
     */
    override suspend fun deleteProduct(product: Product) = productDao.deleteProduct(product)
}