package com.example.room

import com.example.model.emptyProduct
import com.example.model.fakeProductsList
import com.example.room.ui.ProductsUiState
import com.example.room.ui.ProductsViewModel
import com.example.testing.repositories.FakeLocalProductRepository
import com.example.testing.rules.MainCoroutineRule
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class ProductsViewModelUnitTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: ProductsViewModel

    @Before
    fun setup() = runTest {
        viewModel = ProductsViewModel(
            productRepository = FakeLocalProductRepository()
        )
        advanceUntilIdle()
    }

    @Test
    fun `change current product by ID`() = runTest {
        // When the productsUiState is Success
        // Trigger the viewModel's method with the ID from the second product in the list
        viewModel.changeCurrentProductByID(
            (viewModel.productsUiState.value as ProductsUiState.Success).products[1].id ?: 0
        )
        advanceUntilIdle()

        // Assert the currentProduct is the second product in the products list
        assertEquals(
            /* expected = */ fakeProductsList[1],
            /* actual = */ viewModel.currentProduct
        )
    }

    @Test
    fun `fully clear current product`() = runTest {
        // Set the current product as the second product in the list
        viewModel.changeCurrentProductByID(
            (viewModel.productsUiState.value as ProductsUiState.Success).products[1].id ?: 0
        )
        advanceUntilIdle()

        // Trigger the viewModel's method to set the current product to empty with an id of 4
        viewModel.setNewProduct()

        // Assert the currentProduct is empty
        assertEquals(
            /* expected = */ emptyProduct,
            /* actual = */ viewModel.currentProduct
        )
    }

    @Test
    fun `clear current product fields`() = runTest {
        // Set the current product as the second product in the list
        viewModel.changeCurrentProductByID(
            (viewModel.productsUiState.value as ProductsUiState.Success).products[1].id ?: 0
        )
        advanceUntilIdle()

        // Trigger the viewModel's method to set the current product to empty with an id of 4
        viewModel.clearProductFields()

        // Assert the currentProduct is empty besides de ID
        assertEquals(
            /* expected = */ emptyProduct,
            /* actual = */ viewModel.currentProduct.value.copy(id = null)
        )

        // Assert the currentProduct ID is the same as the second product in the list
        assertEquals(
            /* expected = */ viewModel.currentProduct.value.id,
            /* actual = */ (viewModel.productsUiState.value as ProductsUiState.Success).products[1].id
        )
    }

    @Test
    fun `update current product name`() {
        // Sets the current product's name as "Product 4"
        viewModel.updateProductName("Product 4")

        // Assert the current product's name is "Product 4"
        assertEquals(
            /* expected = */ "Product 4",
            /* actual = */ viewModel.currentProduct.value.name
        )
    }

    @Test
    fun `update current product quantity`() {
        // Sets the current product's quantity as 40
        viewModel.updateProductQuantity(40)

        // Assert the current product's quantity is 40
        assertEquals(
            /* expected = */ 40,
            /* actual = */ viewModel.currentProduct.value.quantity
        )
    }

    @Test
    fun `update current product description`() {
        // Sets the current product's description as "Product 4 description"
        viewModel.updateProductDescription("Product 4 description")

        // Assert the current product's description is "Product 4 description"
        assertEquals(
            /* expected = */ "Product 4 description",
            /* actual = */ viewModel.currentProduct.value.description
        )
    }

    @Test
    fun `update current product availability`() {
        // Sets the current product's availability as false
        viewModel.updateProductAvailability(false)

        // Assert the current product's availability is false
        assertEquals(
            /* expected = */ false,
            /* actual = */ viewModel.currentProduct.value.isAvailable
        )
    }

    @Test
    fun `insert current product`() = runTest {
        // Set the current product as the last product in the list
        viewModel.changeCurrentProductByID(
            (viewModel.productsUiState.value as ProductsUiState.Success).products.last().id ?: 0
        )
        advanceUntilIdle()

        // Change the product's name, quantity, description, and availability
        viewModel.updateProductName("Product 4")
        viewModel.updateProductQuantity(40)
        viewModel.updateProductDescription("Product 4 description")
        viewModel.updateProductAvailability(false)

        // Trigger the viewModel's method to insert the current product
        viewModel.insertProduct()
        advanceUntilIdle()

        // Assert the current product's name is the same as the last in the list
        assertEquals(
            /* expected = */ viewModel.currentProduct.value.copy(id = null),
            /* actual = */
            (viewModel.productsUiState.value as ProductsUiState.Success).products.last().copy(id = null)
        )
    }

    @Test
    fun `update current product`() = runTest {
        // Set the current product as the first product in the list
        viewModel.changeCurrentProductByID(
            (viewModel.productsUiState.value as ProductsUiState.Success).products.first().id ?: 0
        )
        advanceUntilIdle()

        // Change the currents produc't quantity to 9 and availability to false
        viewModel.updateProductQuantity(9)
        viewModel.updateProductAvailability(false)

        // Trigger the viewModel's method to update the current product
        viewModel.updateProduct()
        advanceUntilIdle()

        // Assert the first product in the list has a quantity of 9
        assertEquals(
            /* expected = */ 9,
            /* actual = */
            (viewModel.productsUiState.value as ProductsUiState.Success).products.first().quantity
        )

        // Assert the first product in the list has an availability of false
        assertEquals(
            /* expected = */ false,
            /* actual = */
            (viewModel.productsUiState.value as ProductsUiState.Success).products.first().isAvailable
        )
    }

    @Test
    fun `delete current product`() = runTest {
        // Set the current product as the last product in the list
        viewModel.changeCurrentProductByID(
            (viewModel.productsUiState.value as ProductsUiState.Success).products.last().id ?: 0
        )
        advanceUntilIdle()

        // Trigger the viewModel's method to delete the current product
        viewModel.deleteProduct()
        advanceUntilIdle()

        // Assert the current product is NOT the same as the last in the list
        assertNotEquals(
            /* unexpected = */ viewModel.currentProduct,
            /* actual = */ (viewModel.productsUiState.value as ProductsUiState.Success).products.last()
        )
    }
}