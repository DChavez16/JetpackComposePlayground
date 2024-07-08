package com.example.configuration

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ui.theme.PreviewAppTheme
import com.example.ui.ui.CompactSizeScreenThemePreview
import com.example.ui.ui.DefaultTopAppBar


@Composable
fun ConfigurationScreen(
    onBackButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    configurationViewModel: ConfigurationViewModel = hiltViewModel()
) {

    val isDynamicTheme by configurationViewModel.dynamicThemeFlow.collectAsState()

    val topAppBarTitle = stringResource(R.string.configuration_screen_title)

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = { topAppBarTitle },
                onMenuButtonClick = {},
                onBackButtonPressed = onBackButtonClick,
                isPrincipalScreen = { false }
            )
        }
    ) { innerPadding ->
        ConfigurationScreenContent(
            isDynamicTheme = { isDynamicTheme },
            updateDynamicTheme = configurationViewModel::updateDynamicTheme,
            modifier = modifier.padding(innerPadding)
        )
    }
}


@Composable
private fun ConfigurationScreenContent(
    isDynamicTheme: () -> Boolean,
    updateDynamicTheme: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ConfigurationOptions(
        isDynamicTheme = isDynamicTheme,
        onDynamicThemeChange = updateDynamicTheme,
        modifier = modifier
    )
}


@Composable
private fun ConfigurationOptions(
    isDynamicTheme: () -> Boolean,
    onDynamicThemeChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    val enabledSwitchContentDescription =
        stringResource(R.string.configuration_screen_dynamic_theme_enabled)
    val disabledSwitchContentDescription =
        stringResource(R.string.configuration_screen_dynamic_theme_disabled)

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
                .clickable(onClick = onDynamicThemeChange)
                .semantics {
                    testTag = "DynamicThemeOption"
                    contentDescription =
                        if (isDynamicTheme()) enabledSwitchContentDescription else disabledSwitchContentDescription
                }
        ) {
            // Switch label
            Text(
                text = stringResource(R.string.configuration_screen_dynamic_theme_label),
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.weight(1f))

            // Switch
            Switch(
                checked = isDynamicTheme(),
                onCheckedChange = { onDynamicThemeChange() },
                modifier = Modifier.semantics { testTag = "DynamicThemeSwitch" }
            )
        }
    }
}


@CompactSizeScreenThemePreview
@Composable
private fun ConfigurationScreenPreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        ConfigurationScreenContent(
            isDynamicTheme = { false },
            updateDynamicTheme = {}
        )
    }
}