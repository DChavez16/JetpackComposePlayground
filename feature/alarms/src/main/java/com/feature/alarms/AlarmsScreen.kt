@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)

package com.feature.alarms

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.example.ui.theme.PreviewAppTheme
import com.example.ui.ui.DefaultTopAppBar
import com.feature.alarms.ui.AlarmActionButtons
import com.feature.alarms.ui.AlarmTypeSelectors
import com.feature.alarms.ui.ExactAlarmScreen
import com.feature.alarms.ui.InexactAlarmScreen
import kotlinx.coroutines.launch


@Composable
fun AlarmsScreen(
    onMenuButtonClick: () -> Unit
) {

    // TODO Add feedback when an alarm has started

    // Stores the current ViewModelStoreOwner
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current)
    // Creates a viewModel instance binded to viewModelStoreOwner
    val alarmsViewModel: AlarmsViewModel = hiltViewModel(viewModelStoreOwner)

    // State that indicates if the alarm to start will be exact or inexact (also, indicates which screen to show)
    val isAlarmExact by alarmsViewModel.isAlarmExact.collectAsState()
    // State that collects the current alarm type
    val currentAlarmType by alarmsViewModel.alarmType.collectAsState()
    // State that indicates if the alarm is running
    val isAlarmRunning by alarmsViewModel.isAlarmRunning.collectAsState()

    AlarmsScreenContent(
        isAlarmExact = { isAlarmExact },
        changeAlarmAccuracy = alarmsViewModel::changeAlarmAccuracy,
        currentAlarmType = { currentAlarmType },
        onAlarmTypeInvokeTypeChange = alarmsViewModel::changeAlarmTypeInvokeTimeType,
        onAlarmTypeWakeupTypeChange = alarmsViewModel::changeAlarmTypeDeviceAwake,
        getAlarmInvokeTypeTitle = alarmsViewModel::getAlarmInvokeTypeTitle,
        getAlarmInvokeTypeDescription = alarmsViewModel::getAlarmInvokeTypeDescription,
        getAlarmTypeTitle = alarmsViewModel::getAlarmTypeTitle,
        getAlarmTypeDescription = alarmsViewModel::getAlarmTypeDescription,
        isAlarmRunning = { isAlarmRunning },
        onCancelAlarmClick = alarmsViewModel::cancelAlarm,
        onStartAlarmClick = alarmsViewModel::startAlarm,
        onMenuButtonClick = onMenuButtonClick
    ) { page ->
        when (page) {
            // 0 for Exact Alarm
            0 -> ExactAlarmScreen(
                alarmsViewModel = hiltViewModel<AlarmsViewModel>(viewModelStoreOwner)
            )
            // 1 for Inexact Alarm
            1 -> InexactAlarmScreen(
                alarmsViewModel = hiltViewModel<AlarmsViewModel>(viewModelStoreOwner)
            )
        }
    }
}


@Composable
private fun AlarmsScreenContent(
    isAlarmExact: () -> Boolean,
    changeAlarmAccuracy: (Boolean) -> Unit,
    currentAlarmType: () -> AlarmType,
    onAlarmTypeInvokeTypeChange: (Boolean) -> Unit,
    onAlarmTypeWakeupTypeChange: (Boolean) -> Unit,
    isAlarmRunning: () -> Boolean,
    onCancelAlarmClick: () -> Unit,
    onStartAlarmClick: () -> Unit,
    getAlarmInvokeTypeTitle: () -> String,
    getAlarmInvokeTypeDescription: () -> String,
    getAlarmTypeTitle: () -> String,
    getAlarmTypeDescription: () -> String,
    onMenuButtonClick: () -> Unit,
    content: @Composable (Int) -> Unit
) {

    // Coroutine scope for pager animations
    val coroutineScope = rememberCoroutineScope()

    // State for the horizontal pager
    val pagerState = rememberPagerState(
        initialPage = if (isAlarmExact()) 0 else 1,
        pageCount = { AlarmsTabs.entries.size }
    )

    // Value that holds the pagers current page
    var savedPageIndex by rememberSaveable { mutableIntStateOf(if(isAlarmExact()) 0 else 1) }

    // Launched effect that observes with a Snapshot changes in the pager state' current page
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { newPage ->
            // If savedPageIndex is different than the newPage, update the savedPageIndex and the alarm accuracy
            if (savedPageIndex != newPage) {
                savedPageIndex = newPage
                // Update the isAlarmExact state based
                changeAlarmAccuracy(newPage == 0)
            }
        }
    }

    // State that indicates if the Alert Dialog is showing
    var isAlertDialogShowing by remember { mutableStateOf(false) }

    // Screen TopAppBar title
    val topAppBarTitle = stringResource(R.string.alarms_screen_title)

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = { topAppBarTitle },
                onMenuButtonClick = onMenuButtonClick,
                onBackButtonPressed = {},
                actionButtonIcon = { Icons.Rounded.Info },
                onActionButtonClick = { isAlertDialogShowing = true }
            )
        }
    ) { innerPadding ->
        Column(
            horizontalAlignment = CenterHorizontally,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Tab row to select between Exact and Inexact Alarm
            SecondaryTabRow(
                selectedTabIndex = savedPageIndex
            ) {
                AlarmsTabs.entries.forEachIndexed { index, currentTab ->
                    Tab(
                        selected = savedPageIndex == index,
                        enabled = savedPageIndex != index,
                        text = {
                            Text(
                                text = currentTab.text,
                                style = MaterialTheme.typography.titleLarge
                            )
                        },
                        onClick = {
                            coroutineScope.launch {
                                // If the page is currently the 'Exact Alarm' move to the 'Inexact Alarm' page. Otherwise the opposite
                                pagerState.animateScrollToPage(if (isAlarmExact()) 1 else 0)
                            }
                        }
                    )
                }
            }

            // Alarm type selectors
            AlarmTypeSelectors(
                currentAlarmType = currentAlarmType,
                onAlarmTypeInvokeTimeTypeChange = onAlarmTypeInvokeTypeChange,
                onAlarmTypeWakeupTypeChange = onAlarmTypeWakeupTypeChange,
                modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp)
            )

            // Pager containing the Exact and Inexact Alarm screens
            HorizontalPager(
                state = pagerState,
                pageContent = { page -> content(page) },
                modifier = Modifier.weight(1f)
            )

            // Alarm action buttons
            AlarmActionButtons(
                isAlarmRunning = isAlarmRunning,
                onCancelAlarmClick = onCancelAlarmClick,
                onStartAlarmClick = onStartAlarmClick,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        if (isAlertDialogShowing) {
            AlarmDetailsAlertDialog(
                onDismiss = { isAlertDialogShowing = false },
                isAlarmExact = isAlarmExact,
                alarmInvokeTypeTitle = getAlarmInvokeTypeTitle,
                alarmInvokeTypeDescription = getAlarmInvokeTypeDescription,
                alarmTypeTitle = getAlarmTypeTitle,
                alarmTypeDescription = getAlarmTypeDescription
            )
        }
    }
}


@Composable
private fun AlarmDetailsAlertDialog(
    onDismiss: () -> Unit,
    isAlarmExact: () -> Boolean,
    alarmInvokeTypeTitle: () -> String,
    alarmInvokeTypeDescription: () -> String,
    alarmTypeTitle: () -> String,
    alarmTypeDescription: () -> String
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .width(300.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Title of the Alert Dialog
            Text(
                text = stringResource(R.string.alarms_screen_alert_dialog_title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .align(CenterHorizontally)
            )

            // Content of the Alert Dialog
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .heightIn(max = 200.dp)
            ) {
                // Alarm accuracy details
                item(key = 0) {
                    // Alarm accuracy title
                    Text(
                        text = if (isAlarmExact()) stringResource(R.string.alarms_alarms_description_exact_alarm_title)
                        else stringResource(R.string.alarms_alarms_description_inexact_alarm_title),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    // Alarm accuracy description
                    Text(
                        text = if (isAlarmExact()) stringResource(R.string.alarms_alarms_description_exact_alarm_description)
                        else stringResource(R.string.alarms_alarms_description_inexact_alarm_description),
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Justify,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(Modifier.height(8.dp))
                }

                // Alarm invoke type details
                item(key = 1) {
                    // Alarm invoke type title
                    Text(
                        text = alarmInvokeTypeTitle(),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    // Alarm invoke type description
                    Text(
                        text = alarmInvokeTypeDescription(),
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Justify,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(Modifier.height(8.dp))
                }

                // Alarm invoke type details
                item(key = 2) {
                    // Alarm type title
                    Text(
                        text = alarmTypeTitle(),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    // Alarm type description
                    Text(
                        text = alarmTypeDescription(),
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Justify,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Dismiss Button
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors().copy(
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.align(End)
            ) {
                Text(
                    text = stringResource(R.string.alarms_screen_alert_dialog_dismiss_button_label)
                )
            }
        }
    }
}


/*
 * Previews
 */
@Preview
@Composable
private fun AlarmDetailsAlertDialogPreview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        // Alarm Detail Alert Dialog preview in light mode
        PreviewAppTheme(
            darkTheme = false
        ) {
            AlarmDetailsAlertDialog(
                onDismiss = {},
                isAlarmExact = { true },
                alarmInvokeTypeTitle = { "invokeTypeTitle()" },
                alarmInvokeTypeDescription = { "Invoke type description" },
                alarmTypeTitle = { "alarmTypeTitle()" },
                alarmTypeDescription = { "Alarm type description" }
            )
        }
    }
}


enum class AlarmsTabs(
    val text: String
) {
    Exact("Exact"),
    Inexact("Inexact")
}