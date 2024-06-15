package com.example.room

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.product.ProductRepository
import com.example.product.di.ProductRepositoryModule
import com.example.testing.repositories.FakeLocalProductRepository
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Rule
import org.junit.Test


@UninstallModules(ProductRepositoryModule::class)
@HiltAndroidTest
class ProductsNavigationTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<LocalDatabaseTestActivity>()

    // Binds the ProductRepository implementation to FakeLocalProductRepository
    @BindValue
    @JvmField
    val productRepository: ProductRepository = FakeLocalProductRepository()


    @Test
    fun `add product fab leads to add product screen`() {
        // Starting in products list screen

        // Click on the add product fab
        composeTestRule.onNodeWithContentDescription("Add new product").performClick()

        // Verify that the screen top app bar title is Add product
        composeTestRule.onNodeWithTag("TopAppBarTitle").assertTextEquals("Add product")
    }


    @Test
    fun `go to edit screen at product click`() {
        // Starting in products list screen

        // Click on the first product in the list
        composeTestRule.onNodeWithTag("ProductsList").onChildren().onFirst().performClick()

        // Verify that the screen top app bar title is Product 1
        composeTestRule.onNodeWithTag("TopAppBarTitle").assertTextEquals("Product 1")
    }


    @Test
    fun `return to products list screen after adding product`() {
        // Starting in products list screen

        // Click on the add product fab
        composeTestRule.onNodeWithContentDescription("Add new product").performClick()

        // Add product name
        composeTestRule.onNodeWithTag("ProductNameTextField").performTextInput("Product 4")

        // Add product description
        composeTestRule.onNodeWithTag("ProductDescriptionTextField")
            .performTextInput("Product 4 description")

        // Click on the save button
        composeTestRule.onNodeWithText("Save").performClick()

        // Verify that the screen top app bar title is Products
        composeTestRule.onNodeWithTag("TopAppBarTitle").assertTextEquals("Products")
    }


    @Test
    fun `return to products list screen after editing product`() {
        // Starting in products list screen

        // Click on the first product in the list
        composeTestRule.onNodeWithTag("ProductsList").onChildren().onFirst().performClick()

        // Change the product's description
        composeTestRule.onNodeWithTag("ProductDescriptionTextField")
            .performTextInput("Product new description")

        // Click on the save button
        composeTestRule.onNodeWithText("Save").performClick()

        // Verify that the screen top app bar title is Products
        composeTestRule.onNodeWithTag("TopAppBarTitle").assertTextEquals("Products")
    }


    @Test
    fun `return to products list screen after deleting product`() {
        // Starting in products list screen

        // Click on the first product in the list
        composeTestRule.onNodeWithTag("ProductsList").onChildren().onFirst().performClick()

        // Click on the delete icon button
        composeTestRule.onNodeWithContentDescription("Delete the product", substring = true)
            .performClick()

        // Verify that the screen top app bar title is Products
        composeTestRule.onNodeWithTag("TopAppBarTitle").assertTextEquals("Products")
    }
}