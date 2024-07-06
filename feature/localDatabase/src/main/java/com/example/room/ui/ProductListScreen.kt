package com.example.room.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.model.Product
import com.example.model.fakeProductsList
import com.example.room.ProductsUiState
import com.example.room.ProductsViewModel
import com.example.room.R
import com.example.ui.theme.PreviewAppTheme
import com.example.ui.ui.CompactSizeScreenThemePreview


/**
 * Composable that display a list of products
 */
@Composable
internal fun ProductListScreen(
    navigateToProduct: (Long) -> Unit,
    modifier: Modifier = Modifier,
    productsViewModel: ProductsViewModel = hiltViewModel()
) {

    // Products UI state obtained from the ViewModel
    val productsUiState by productsViewModel.productsUiState.collectAsState()

    ProductListScreenContent(
        productsUiState = { productsUiState },
        navigateToProduct = navigateToProduct,
        modifier = modifier
    )
}


/**
 * Products list screen content
 */
@Composable
private fun ProductListScreenContent(
    productsUiState: () -> ProductsUiState,
    navigateToProduct: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    when (productsUiState()) {
        is ProductsUiState.Loading -> {
            ProductsListLoadingScreen(
                modifier = modifier
            )
        }

        is ProductsUiState.Success -> {
            ProductsListSuccessScreen(
                productsList = { (productsUiState() as ProductsUiState.Success).products },
                onProductClick = navigateToProduct,
                modifier = modifier
            )
        }

        is ProductsUiState.Error -> {
            ProductsListErrorScreen(
                errorMessage = { (productsUiState() as ProductsUiState.Error).errorMessage },
                modifier = modifier
            )
        }
    }
}


/**
 * Products list loading screen
 */
@Composable
private fun ProductsListLoadingScreen(
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}


/**
 * Products list error screen
 */
@Composable
private fun ProductsListErrorScreen(
    errorMessage: () -> String,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = stringResource(R.string.local_database_products_list_error_message),
                style = MaterialTheme.typography.titleLarge,
                modifier = modifier
            )

            Text(
                text = errorMessage(),
                style = MaterialTheme.typography.titleMedium,
                modifier = modifier
            )
        }
    }
}


/**
 * Products list success screen
 */
@Composable
private fun ProductsListSuccessScreen(
    productsList: () -> List<Product>,
    onProductClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    if (productsList().isEmpty()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.fillMaxSize()
        ) {
            Text(
                text = stringResource(R.string.local_database_products_list_empty_list_message),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = modifier.widthIn(max = 240.dp)
            )
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(12.dp),
            modifier = modifier
                .fillMaxSize()
                .semantics { testTag = "ProductsList" }
        ) {
            items(
                items = productsList(),
                key = { product: Product -> product.id ?: 0 }
            ) { product ->
                ProductItem(
                    product = { product },
                    onProductClick = onProductClick
                )
            }
        }
    }
}


/**
 * Individual product item
 */
@Composable
private fun ProductItem(
    product: () -> Product,
    onProductClick: (Long) -> Unit,
) {

    // Card color
    val cardColor = MaterialTheme.colorScheme.primaryContainer

    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onProductClick(product().id ?: 0) }
    ) {
        Row(
            modifier = Modifier
                .drawBehind {
                    drawRect(
                        cardAvailabilityGradient(
                            cardColor = cardColor,
                            isAvailable = product().isAvailable
                        )
                    )
                }
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(8f)
            ) {
                Row {
                    Text(
                        text = product().name,
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.widthIn(max = 220.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "(${product().quantity})",
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        modifier = Modifier.width(60.dp)
                    )
                }

                Text(
                    text = product().description,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.weight(2f))
        }
    }
}



// Products list loading screen preview
@CompactSizeScreenThemePreview
@Composable
private fun ProductListScreenLoadingContentPreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        ProductListScreenContent(
            productsUiState = { ProductsUiState.Loading },
            navigateToProduct = {}
        )
    }
}


// Products list error screen preview
@CompactSizeScreenThemePreview
@Composable
private fun ProductListScreenErrorContentPreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        ProductListScreenContent(
            productsUiState = { ProductsUiState.Error("Preview error message") },
            navigateToProduct = {}
        )
    }
}


// Products list success screen preview
@CompactSizeScreenThemePreview
@Composable
private fun ProductListScreenSuccessContentPreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        ProductListScreenContent(
            productsUiState = {
                ProductsUiState.Success(
                    products = fakeProductsList
                )
            },
            navigateToProduct = {}
        )
    }
}


// Products list success screen empty list preview
@CompactSizeScreenThemePreview
@Composable
private fun ProductListScreenSuccessContentEmptyListPreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        ProductListScreenContent(
            productsUiState = {
                ProductsUiState.Success(
                    products = emptyList()
                )
            },
            navigateToProduct = {}
        )
    }
}





private fun cardAvailabilityGradient(cardColor: Color, isAvailable: Boolean) =
    Brush.horizontalGradient(
        0.9f to cardColor,
        1.0f to if (isAvailable) Color.Green else Color.Red
    )