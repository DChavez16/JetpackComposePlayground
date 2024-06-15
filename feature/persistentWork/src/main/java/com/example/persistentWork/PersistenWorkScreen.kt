package com.example.persistentWork

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
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ui.theme.AppTheme
import com.example.ui.ui.DefaultTopAppBar
import com.example.ui.ui.ThemePreview


@Composable
fun PersistentWorkScreen(
    onMenuButtonClick: () -> Unit,
    persistentWorkViewModel: PersistentWorkViewModel = hiltViewModel()
) {
    val isWorkRunning by persistentWorkViewModel.isWorkRunning.collectAsState()

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = "Persistent Work",
                onMenuButtonClick = onMenuButtonClick,
                // Empty since no seconday screen is used
                onBackButtonPressed = {})
        }
    ) { innerPadding ->
        NotificationActions(
            showNotification = persistentWorkViewModel::showNotification,
            cancelNotification = persistentWorkViewModel::cancelNotification,
            isWorkRunning = isWorkRunning,
            modifier = Modifier.padding(innerPadding)
        )
    }
}


@Composable
private fun NotificationActions(
    showNotification: () -> Unit,
    cancelNotification: () -> Unit,
    isWorkRunning: Boolean,
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
                enabled = !isWorkRunning,
                modifier = Modifier.semantics {
                    testTag = "ShowNotificationButton"
                }
            ) {
                Text(text = "Show Notification")
            }

            // Cancel notification button
            OutlinedButton(
                onClick = { cancelNotification() },
                enabled = isWorkRunning,
                modifier = Modifier.semantics {
                    testTag = "CancelNotificationButton"
                }
            ) {
                Text(text = "Cancel Notification")
            }
        }
    }
}


@ThemePreview
@Composable
private fun WorkManagerExamplePreview() {
    AppTheme {
        PersistentWorkScreen(
            onMenuButtonClick = {}
        )
    }
}