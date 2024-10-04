@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)

package com.feature.alarms

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.example.ui.theme.PreviewAppTheme
import com.feature.alarms.ui.ExactAlarmScreen
import com.feature.alarms.ui.InexactAlarmScreen
import kotlinx.coroutines.launch


@Composable
fun AlarmsScreen(
    onMenuButtonClick: () -> Unit
) {

    // Stores the current ViewModelStoreOwner
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current)
    // Creates a viewModel instance binded to viewModelStoreOwner
    val alarmsViewModel: AlarmsViewModel = hiltViewModel(viewModelStoreOwner)

    // State that indicates if the alarm to start will be exact or inexact (also, indicates which screen to show)
    val isAlarmExact by alarmsViewModel.isAlarmExact.collectAsState()

    // TODO Create custom time picker composable for ELAPSED_TIME AlarmType
    // TODO Use defalut time picker composable for RTC AlarmType

    AlarmsScreenContent(
        isAlarmExact = { isAlarmExact },
        changeAlarmAccuracy = alarmsViewModel::changeAlarmAccuracy,
        onMenuButtonClick = onMenuButtonClick
    )
}


@Composable
private fun AlarmsScreenContent(
    isAlarmExact: () -> Boolean,
    changeAlarmAccuracy: () -> Unit,
    onMenuButtonClick: () -> Unit
) {

    // Coroutine scope for pager animations
    val coroutineScope = rememberCoroutineScope()

    // State for the horizontal pager
    val pagerState = rememberPagerState(
        pageCount = { AlarmsTabs.entries.size }
    )

    // Launched effect that observes with a Snapshot changes in the pager state' current page
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect {
            // Update the isAlarmExact state based
            changeAlarmAccuracy()
        }
    }

    Scaffold(
        topBar = {
            // TODO Add DefaultTopAppBar
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            // Tab row to select between Exact and Inexact Alarm
            SecondaryTabRow(
                selectedTabIndex = if (isAlarmExact()) 0 else 1
            ) {
                AlarmsTabs.entries.forEachIndexed { index, currentTab ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        text = {
                            Text(
                                text = currentTab.text,
                                style = MaterialTheme.typography.titleLarge
                            )
                        },
                        onClick = {
                            // Update the pager state current page only if the page is not the current one
                            if (pagerState.currentPage != index) {
                                coroutineScope.launch {
                                    // If the page is currently the 'Exact Alarm' move to the 'Inexact Alarm' page. Otherwise the opposite
                                    pagerState.scrollToPage(if (isAlarmExact()) 1 else 0)
                                }
                            }
                        }
                    )
                }
            }

            // Pager containing the Exact and Inexact Alarm screens
            HorizontalPager(
                pagerState
            ) { page ->
                when (page) {
                    // 0 for Exact Alarm
                    0 -> ExactAlarmScreen()
                    // 1 for Inexact Alarm
                    1 -> InexactAlarmScreen()
                }
            }
        }

        // TODO Add Alert Dialog indicating the current properties of the alarm
    }
}


@Preview
@Composable
private fun TemporalPreview() {
    var temporatIsAlarmExact by remember { mutableStateOf(true) }

    PreviewAppTheme(
        darkTheme = false
    ) {
        AlarmsScreenContent(
            isAlarmExact = { temporatIsAlarmExact },
            changeAlarmAccuracy = { temporatIsAlarmExact = !temporatIsAlarmExact },
            onMenuButtonClick = {}
        )
    }
}


enum class AlarmsTabs(
    val text: String
) {
    Exact("Exact"),
    Inexact("Inexact")
}