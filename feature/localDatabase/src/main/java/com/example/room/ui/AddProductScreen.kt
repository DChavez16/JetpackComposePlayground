package com.example.room.ui

import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.model.Product
import com.example.model.isValidForSave
import com.example.room.R
import com.example.ui.theme.PreviewAppTheme
import com.example.ui.ui.CompactSizeScreenThemePreview
import com.example.ui.ui.DefaultTopAppBar


/**
 * Composable to add a new product to the database
 */
@Composable
internal fun AddProductScreen(
    currentProduct: () -> Product,
    onSaveProduct: () -> Unit,
    onProductNameChange: (String) -> Unit,
    onProductQuantityChange: (Int) -> Unit,
    onProductDescriptionChange: (String) -> Unit,
    onProductAvailabilityChange: (Boolean) -> Unit,
    clearProductFields: () -> Unit,
    isSaveButtonEnabled: () -> Boolean,
    topAppBarTitle: String,
    onBackButtonPressed: () -> Unit,
    showDeletionActionButton: Boolean = false,
    onDeleteProduct: () -> Unit = {}
) {

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = { topAppBarTitle },
                onBackButtonPressed = onBackButtonPressed,
                isPrincipalScreen = { false },
                actionButtonIcon = { if (showDeletionActionButton) Icons.Default.Delete else null },
                onActionButtonClick = onDeleteProduct
            )
        }
    ) { innerPadding ->
        AddProductScreenContent(
            onSaveProduct = onSaveProduct,
            currentProduct = currentProduct,
            onProductNameChange = onProductNameChange,
            onProductQuantityChange = onProductQuantityChange,
            onProductDescriptionChange = onProductDescriptionChange,
            onProductAvailabilityChange = onProductAvailabilityChange,
            clearProductFields = clearProductFields,
            isSaveButtonEnabled = isSaveButtonEnabled,
            modifier = Modifier.padding(innerPadding)
        )
    }
}


/**
 * Add Product screen content
 */
@Composable
private fun AddProductScreenContent(
    onSaveProduct: () -> Unit,
    currentProduct: () -> Product,
    onProductNameChange: (String) -> Unit,
    onProductQuantityChange: (Int) -> Unit,
    onProductDescriptionChange: (String) -> Unit,
    onProductAvailabilityChange: (Boolean) -> Unit,
    clearProductFields: () -> Unit,
    isSaveButtonEnabled: () -> Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp),
    ) {
        ProductInputFields(
            currentProduct = currentProduct,
            onProductNameChange = onProductNameChange,
            onProductQuantityChange = onProductQuantityChange,
            onProductDescriptionChange = onProductDescriptionChange,
            onProductAvailabilityChange = onProductAvailabilityChange
        )

        AddProductScreenButtons(
            onSaveProduct = onSaveProduct,
            onClearFields = clearProductFields,
            isSaveButtonEnabled = isSaveButtonEnabled
        )
    }
}


/**
 * Set of composables to introduce the data of the product
 */
@Composable
private fun ProductInputFields(
    currentProduct: () -> Product,
    onProductNameChange: (String) -> Unit,
    onProductQuantityChange: (Int) -> Unit,
    onProductDescriptionChange: (String) -> Unit,
    onProductAvailabilityChange: (Boolean) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ProductNameField(
            productName = { currentProduct().name },
            onProductNameChange = onProductNameChange
        )

        ProductQuantityField(
            productQuantity = { currentProduct().quantity },
            onProductQuantityChange = onProductQuantityChange
        )

        ProductDescriptionField(
            productDescription = { currentProduct().description },
            onProductDescriptionChange = onProductDescriptionChange
        )

        ProductAvailabilityField(
            productAvailability = { currentProduct().isAvailable },
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
    isSaveButtonEnabled: () -> Boolean
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Button to save the product
        Button(
            onClick = onSaveProduct,
            enabled = isSaveButtonEnabled(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                text = stringResource(R.string.local_database_add_edit_save_button_text),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        // Button to clear the fields
        OutlinedButton(
            onClick = onClearFields,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground
            )
        ) {
            Text(
                text = stringResource(R.string.local_database_add_edit_clear_button_text),
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
    productName: () -> String,
    onProductNameChange: (String) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.local_database_add_edit_product_name_label),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(5f)
        )

        Spacer(modifier = Modifier.weight(1f))

        OutlinedTextField(
            value = productName(),
            onValueChange = onProductNameChange,
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
    productQuantity: () -> Int,
    onProductQuantityChange: (Int) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.local_database_add_edit_quantity_label),
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
                onClick = { onProductQuantityChange(productQuantity() - 1) }
            ) {
                Icon(
                    painter = painterResource(R.drawable.remove_icon),
                    contentDescription = stringResource(R.string.local_database_add_edit_decrease_quantity_content_description)
                )
            }

            Text(
                text = productQuantity().toString(),
                textAlign = TextAlign.Center,
                modifier = Modifier.width(64.dp)
            )

            IconButton(
                onClick = { onProductQuantityChange(productQuantity() + 1) }
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = stringResource(R.string.local_database_add_edit_increase_quantity_content_description))
            }
        }
    }
}


/**
 * Composable where the description of the product is introduced
 */
@Composable
private fun ProductDescriptionField(
    productDescription: () -> String,
    onProductDescriptionChange: (String) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.local_database_add_edit_description_label),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(5f)
            )

            Spacer(modifier = Modifier.weight(9f))
        }

        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
            value = productDescription(),
            onValueChange = onProductDescriptionChange,
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
    productAvailability: () -> Boolean,
    onProductAvailabilityChange: (Boolean) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.local_database_add_edit_availability_label),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(5f)
        )

        Spacer(modifier = Modifier.weight(1f))

        Checkbox(
            checked = productAvailability(),
            onCheckedChange = onProductAvailabilityChange,
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary,
                uncheckedColor = MaterialTheme.colorScheme.primary,
                checkmarkColor = MaterialTheme.colorScheme.onPrimary,
            ),
            modifier = Modifier.weight(8f)
        )
    }
}




@CompactSizeScreenThemePreview
@Composable
private fun AddProductScreenContentPreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        val currentProduct = Product(
            name = "Product name",
            description = "Product description",
            quantity = 5,
            isAvailable = true
        )

        AddProductScreenContent(
            onSaveProduct = {},
            currentProduct = { currentProduct },
            onProductNameChange = {},
            onProductQuantityChange = {},
            onProductDescriptionChange = {},
            onProductAvailabilityChange = {},
            clearProductFields = {},
            isSaveButtonEnabled = { currentProduct.isValidForSave() }
        )
    }
}