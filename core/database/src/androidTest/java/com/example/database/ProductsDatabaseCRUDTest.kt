package com.example.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.database.daos.ProductDAO
import com.example.model.fakeProductsList
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ProductsDatabaseCRUDTest {

    private lateinit var productDao: ProductDAO
    private lateinit var productsDatabase: ProductsDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        productsDatabase = Room.inMemoryDatabaseBuilder(
            context = context,
            klass = ProductsDatabase::class.java
        ).build()
        productDao = productsDatabase.productDao()
    }

    @After
    fun closeDb() {
        productsDatabase.close()
    }



    // Inserts three products into the database and reads one back
    @Test
    fun `insert and read product`() = runTest {
        // Imports a fake list of three products into the database
        insertProducts()

        // Obtains the product in the database with id 2L
        val insertedProduct = productDao.getProduct(2L)
        advanceUntilIdle()

        // Asserts the obtained product from the database is the same as the second product in the list
        assertEquals(
            /* expected = */ fakeProductsList[1],
            /* actual = */ insertedProduct
        )
    }


    // Updates a product from the database and confirms that the changes were made in the database
    @Test
    fun `update product`() = runTest {
        // Imports a fake list of three products into the database
        insertProducts()

        // Obtains the product in the database with id 1L
        val startingProduct = productDao.getProduct(1L)
        advanceUntilIdle()

        // Changes the product's availability value to false (The starting value is true)
        startingProduct.isAvailable = false

        // Updates the current product in the database
        productDao.updateProduct(startingProduct)

        // Gets the product with id 1L from the database
        val updatedProduct = productDao.getProduct(1L)
        advanceUntilIdle()

        // Asserts the availavility of the product with id 1L obtained from the database is false
        assertEquals(
            /* expected = */ false,
            /* actual = */ updatedProduct.isAvailable
        )
    }


    // Deletes a product from the database and confirms is no longer in the database
    @Test
    fun `delete product and flow functionality`() = runTest {
        // Gets a flow with the list of products from the database
        val productsFlow = productDao.getProductsFlow()

        // Imports a fake list of three products into the database
        insertProducts()

        // Obtains the product with id 3L from the database
        val productToDelete = productDao.getProduct(3L)
        advanceUntilIdle()

        // Verifies the product is in the list from the obtained flow
        assertEquals(
            /* expected = */ true,
            /* actual = */ productsFlow.first().contains(productToDelete)
        )

        // Deletes the product from the database
        productDao.deleteProduct(productToDelete)
        advanceUntilIdle()

        // Verifies the product is not in the list from the obtained flow
        assertEquals(
            /* expected = */ false,
            /* actual = */ productsFlow.first().contains(productToDelete)
        )
    }

    private fun insertProducts() = runTest {
        fakeProductsList.forEach { product ->
            productDao.insertProduct(product)
            advanceUntilIdle()
        }
    }
}