package com.example.dependencyinjection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.model.Car
import com.example.ui.theme.AppTheme
import com.example.ui.ui.DefaultTopAppBar

@Composable
fun DependencyInjectionScreen(
    onMenuButtonClick: () -> Unit,
    viewModel: DependencyInjectionViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = "Dependency Injection",
                onMenuButtonClick = onMenuButtonClick,
                // Empty since no seconday screen is used
                onBackButtonPressed = {}
            )
        }
    ) { innerPadding ->
        CarList(
            oilCar = viewModel.oilCar,
            dieselCar =viewModel.dieselCar,
            hybridCar = viewModel.hybridCar,
            electricCar = viewModel.electricCar,
            modifier = Modifier.padding(innerPadding)
        )
    }
}


@Composable
private fun CarList(
    oilCar: Car,
    dieselCar: Car,
    hybridCar: Car,
    electricCar: Car,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Oil car text
        Text(
            text = oilCar.getCarEngineType(),
            style = MaterialTheme.typography.displaySmall
        )

        // Diesel car text
        Text(
            text = dieselCar.getCarEngineType(),
            style = MaterialTheme.typography.displaySmall
        )

        // Hybrid car text
        Text(
            text = hybridCar.getCarEngineType(),
            style = MaterialTheme.typography.displaySmall
        )

        // Electric car text
        Text(
            text = electricCar.getCarEngineType(),
            style = MaterialTheme.typography.displaySmall
        )
    }
}


@Preview
@Composable
private fun DependencyInjectionScreenPreview() {
    AppTheme {
        DependencyInjectionScreen(
            onMenuButtonClick = {}
        )
    }
}