package com.example.room.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.model.Product
import com.example.model.isValidForSave
import com.example.room.ProductsViewModel
import com.example.room.R
import com.example.ui.theme.AppTheme
import com.example.ui.ui.ThemePreview

/**
 * Composable to add a new product to the database
 */
@Composable
internal fun AddProductScreen(
    onSaveProduct: () -> Unit,
    modifier: Modifier = Modifier,
    productsViewModel: ProductsViewModel = hiltViewModel()
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp),
    ) {
        ProductInputFields(
            currentProduct = productsViewModel.currentProduct,
            onProductNameChange = productsViewModel::updateProductName,
            onProductQuantityChange = productsViewModel::updateProductQuantity,
            onProductDescriptionChange = productsViewModel::updateProductDescription,
            onProductAvailabilityChange = productsViewModel::updateProductAvailability
        )

        AddProductScreenButtons(
            onSaveProduct = onSaveProduct,
            onClearFields = productsViewModel::clearProductFields,
            isSaveButtonEnabled = productsViewModel.currentProduct.isValidForSave()
        )
    }
}


/**
 * Set of composables to introduce the data of the product
 */
@Composable
private fun ProductInputFields(
    currentProduct: Product,
    onProductNameChange: (String) -> Unit,
    onProductQuantityChange: (Int) -> Unit,
    onProductDescriptionChange: (String) -> Unit,
    onProductAvailabilityChange: (Boolean) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ProductNameField(
            productName = currentProduct.name,
            onProductNameChange = onProductNameChange
        )

        ProductQuantityField(
            productQuantity = currentProduct.quantity,
            onProductQuantityChange = onProductQuantityChange
        )

        ProductDescriptionField(
            productDescription = currentProduct.description,
            onProductDescriptionChange = onProductDescriptionChange
        )

        ProductAvailabilityField(
            productAvailability = currentProduct.isAvailable,
            onProductAvailabilityChange = onProductAvailabilityChange
        )
    }
}


/**
 * Set of button for saving the product or clearing the fields
 */
@Composable
private fun AddProductScreenButtons(
    onSaveProduct: () -> Unit,
    onClearFields: () -> Unit,
    isSaveButtonEnabled: Boolean
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Button to save the product
        Button(
            onClick = {
                onSaveProduct()
            },
            enabled = isSaveButtonEnabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                text = "Save",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        // Button to clear the fields
        OutlinedButton(
            onClick = { onClearFields() },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground
            )
        ) {
            Text(
                text = "Clear",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}


/**
 * Composable where the name of the product is introduced
 */
@Composable
private fun ProductNameField(
    productName: String,
    onProductNameChange: (String) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Product Name:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(5f)
        )

        Spacer(modifier = Modifier.weight(1f))

        OutlinedTextField(
            value = productName,
            onValueChange = { onProductNameChange(it) },
            modifier = Modifier
                .weight(8f)
                .semantics { testTag = "ProductNameTextField" }
        )
    }
}


/**
 * Composable where the quantity of the product is introduced
 */
@Composable
private fun ProductQuantityField(
    productQuantity: Int,
    onProductQuantityChange: (Int) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Quantity:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(5f)
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(8f)
        ) {
            IconButton(
                onClick = { onProductQuantityChange(productQuantity - 1) }
            ) {
                Icon(
                    painter = painterResource(R.drawable.remove_icon),
                    contentDescription = "Decrease quantity"
                )
            }

            Text(
                text = "$productQuantity",
                textAlign = TextAlign.Center,
                modifier = Modifier.width(64.dp)
            )

            IconButton(
                onClick = { onProductQuantityChange(productQuantity + 1) }
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Increase quantity")
            }
        }
    }
}


/**
 * Composable where the description of the product is introduced
 */
@Composable
private fun ProductDescriptionField(
    productDescription: String,
    onProductDescriptionChange: (String) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(Modifier.fillMaxWidth()) {
            Text(
                text = "Description:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(5f)
            )

            Spacer(modifier = Modifier.weight(9f))
        }

        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
            value = productDescription,
            onValueChange = { onProductDescriptionChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(124.dp)
                .padding(horizontal = 8.dp)
                .semantics { testTag = "ProductDescriptionTextField" }
        )
    }
}


/**
 * Composable where the availability of the product is introduced
 */
@Composable
private fun ProductAvailabilityField(
    productAvailability: Boolean,
    onProductAvailabilityChange: (Boolean) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Availability:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(5f)
        )

        Spacer(modifier = Modifier.weight(1f))

        Checkbox(
            checked = productAvailability,
            onCheckedChange = { onProductAvailabilityChange(it) },
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary,
                uncheckedColor = MaterialTheme.colorScheme.primary,
                checkmarkColor = MaterialTheme.colorScheme.onPrimary,
            ),
            modifier = Modifier.weight(8f)
        )
    }
}


@ThemePreview
@Composable
private fun AddProductScreenPreview() {
    AppTheme {
        AddProductScreen(
            onSaveProduct = {}
        )
    }
}