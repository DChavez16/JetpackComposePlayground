package com.example.configuration

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ui.theme.AppTheme
import com.example.ui.ui.DefaultTopAppBar
import com.example.ui.ui.ThemePreview


@Composable
fun ConfigurationScreen(
    onBackButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    configurationViewModel: ConfigurationViewModel = hiltViewModel()
) {

    val isDynamicTheme by configurationViewModel.dynamicThemeFlow.collectAsState()

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = "Configuration",
                onMenuButtonClick = {},
                onBackButtonPressed = onBackButtonClick,
                isPrincipalScreen = false
            )
        }
    ) { innerPadding ->
        ConfigurationOptions(
            isDynamicTheme = isDynamicTheme,
            onDynamicThemeChange = configurationViewModel::updateDynamicTheme,
            modifier = modifier.padding(innerPadding)
        )
    }
}


@Composable
private fun ConfigurationOptions(
    isDynamicTheme: Boolean,
    onDynamicThemeChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Dynamic theme option row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onDynamicThemeChange(!isDynamicTheme) }
                .semantics {
                    testTag = "DynamicThemeOption"
                    contentDescription =
                        "The dynamic theme is ${if (isDynamicTheme) "enabled" else "disabled"}, click to toggle"
                }
        ) {
            // Switch label
            Text(
                text = "Use dynamic theme",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.weight(1f))

            // Switch
            Switch(
                checked = isDynamicTheme,
                onCheckedChange = { onDynamicThemeChange(!isDynamicTheme) },
                modifier = Modifier.semantics { testTag = "DynamicThemeSwitch" }
            )
        }
    }
}


@ThemePreview
@Composable
private fun ConfigurationScreenPreview() {
    AppTheme {
        ConfigurationScreen(
            onBackButtonClick = {}
        )
    }
}