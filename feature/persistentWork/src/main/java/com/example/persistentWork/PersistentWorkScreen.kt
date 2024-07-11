package com.example.persistentWork

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ui.theme.PreviewAppTheme
import com.example.ui.ui.CompactSizeScreenThemePreview
import com.example.ui.ui.DefaultTopAppBar


@Composable
fun PersistentWorkScreen(
    onMenuButtonClick: () -> Unit,
    persistentWorkViewModel: PersistentWorkViewModel = hiltViewModel()
) {

    val isWorkRunning by persistentWorkViewModel.isWorkRunning.collectAsState()

    PersistentWorkScreenContent(
        isWorkRunning = { isWorkRunning },
        onMenuButtonClick = onMenuButtonClick,
        showNotification = persistentWorkViewModel::showNotification,
        cancelNotification = persistentWorkViewModel::cancelNotification
    )
}


@Composable
private fun PersistentWorkScreenContent(
    isWorkRunning: () -> Boolean,
    onMenuButtonClick: () -> Unit,
    showNotification: () -> Unit,
    cancelNotification: () -> Unit
) {

    val topAppBarTitle = stringResource(R.string.persistent_work_title)

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = { topAppBarTitle },
                onMenuButtonClick = onMenuButtonClick,
                // Empty since no seconday screen is used
                onBackButtonPressed = {}
            )
        }
    ) { innerPadding ->
        NotificationActions(
            isWorkRunning = isWorkRunning,
            showNotification = showNotification,
            cancelNotification = cancelNotification,
            modifier = Modifier.padding(innerPadding)
        )
    }
}


@Composable
private fun NotificationActions(
    isWorkRunning: () -> Boolean,
    showNotification: () -> Unit,
    cancelNotification: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Start notification button
            Button(
                onClick = { showNotification() },
                enabled = !isWorkRunning(),
                modifier = Modifier.semantics {
                    testTag = "ShowNotificationButton"
                }
            ) {
                Text(text = stringResource(R.string.persistent_work_show_notification_button_text))
            }

            // Cancel notification button
            OutlinedButton(
                onClick = { cancelNotification() },
                enabled = isWorkRunning(),
                modifier = Modifier.semantics {
                    testTag = "CancelNotificationButton"
                }
            ) {
                Text(text = stringResource(R.string.persistent_work_cancel_notification_button_text))
            }
        }
    }
}




@CompactSizeScreenThemePreview
@Composable
private fun PersistentWorkScreenContentPreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        PersistentWorkScreenContent(
            isWorkRunning = { false },
            onMenuButtonClick = {},
            showNotification = {},
            cancelNotification = {}
        )
    }
}