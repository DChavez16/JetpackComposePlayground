package com.example.room

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
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
class ProductsScreenUserFlow {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<LocalDatabaseTestActivity>()

    // Binds the ProductRepository implementation to FakeLocalProductRepository
    @BindValue
    @JvmField
    val productRepository: ProductRepository = FakeLocalProductRepository()


    @Test
    fun `save product button is disabled when products name and description are empty`() {
        // Starting in products list screen

        // Clicks on the add product fab
        composeTestRule.onNodeWithContentDescription("Add new product").performClick()

        // Asserts the save product button is disabled
        composeTestRule.onNodeWithText("Save").assertIsNotEnabled()

        // Adds product's name and leaves the description empty
        composeTestRule.onNodeWithTag("ProductNameTextField").performTextInput("New Product Name")
        composeTestRule.onNodeWithTag("ProductDescriptionTextField").performTextClearance()

        // Asserts the save product button is disabled
        composeTestRule.onNodeWithText("Save").assertIsNotEnabled()

        // Adds product's description and leaves the name empty
        composeTestRule.onNodeWithTag("ProductNameTextField").performTextClearance()
        composeTestRule.onNodeWithTag("ProductDescriptionTextField")
            .performTextInput("New Product Description")

        // Asserts the save product button is disabled
        composeTestRule.onNodeWithText("Save").assertIsNotEnabled()

        // Adds both product's name and description
        composeTestRule.onNodeWithTag("ProductNameTextField").performTextInput("New Product Name")
        composeTestRule.onNodeWithTag("ProductDescriptionTextField")
            .performTextInput("New Product Description")

        // Asserts the save product button is enabled
        composeTestRule.onNodeWithText("Save").assertIsEnabled()
    }


    @Test
    fun `add new product into the list`() {
        // Starting in products list screen

        // Clicks on the add product fab
        composeTestRule.onNodeWithContentDescription("Add new product").performClick()

        // Adds product's fields
        composeTestRule.onNodeWithTag("ProductNameTextField").performTextInput("New Product Name")
        composeTestRule.onNodeWithTag("ProductDescriptionTextField")
            .performTextInput("New Product Description")
        // Increase quantity to 3
        composeTestRule.onNodeWithContentDescription("Increase quantity").performClick().performClick().performClick()

        // Click on the save button
        composeTestRule.onNodeWithText("Save").performClick()

        // Asserts a product with the introduced fields exists in the list
        composeTestRule.onNodeWithTag("ProductsList").onChildren().apply {
            filter(hasText("New Product Name")).onFirst().assertExists()
            filter(hasText("New Product Description")).onFirst().assertExists()
            filter(hasText("(3)")).onFirst().assertExists()
        }
    }


    @Test
    fun `edit from the list`() {
        // Starting in products list screen

        // Clicks on the product with name "Product 2" in the list
        composeTestRule.onNodeWithTag("ProductsList").onChildren()
            .filter(hasText("Product 2")).onFirst().performClick()

        // Changes the product name
        composeTestRule.onNodeWithTag("ProductNameTextField").performTextReplacement("New Product Name")

        // Click on the save button
        composeTestRule.onNodeWithText("Save").performClick()

        // Asserts a product with name "New Product Name" exist in the list
        composeTestRule.onNodeWithTag("ProductsList").onChildren()
            .filter(hasText("New Product Name")).onFirst().assertExists()
    }


    @Test
    fun `delete product from the list`() {
        // Starting in products list screen

        // Asserts a product with name "Product 3" exists in the list
        composeTestRule.onNodeWithTag("ProductsList").onChildren()
            .filter(hasText("Product 3")).onFirst().assertExists()

        // Clicks on the product with name "Product 2" in the list
        composeTestRule.onNodeWithTag("ProductsList").onChildren()
            .filter(hasText("Product 3")).onFirst().performClick()

        // Click on the delete icon button
        composeTestRule.onNodeWithContentDescription("Delete the product", substring = true)
            .performClick()

        // Asserts a product with name "Product 2" doesnt exist in the list
        composeTestRule.onNodeWithTag("ProductsList").onChildren()
            .filter(hasText("Product 3")).onFirst().assertDoesNotExist()
    }
}
