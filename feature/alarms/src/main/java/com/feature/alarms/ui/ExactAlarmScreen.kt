package com.feature.alarms.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ui.theme.PreviewAppTheme
import com.feature.alarms.AlarmType
import com.feature.alarms.AlarmsInvokeType
import com.feature.alarms.AlarmsViewModel
import com.feature.alarms.ExactAlarmsInvokeType


@Composable
internal fun ExactAlarmScreen(
    alarmsViewModel: AlarmsViewModel
) {

    // Current alarm type state flow
    val currentAlarmType = alarmsViewModel.alarmType.collectAsState().value
    // Current alarm invoke type state flow
    val currentAlarmInvokeType = alarmsViewModel.exactAlarmInvokeType.collectAsState().value
    // Current time in millis state flow
    val currentTimeInMillis = alarmsViewModel.alarmTargetTimeMilliseconds.collectAsState().value

    ExactAlarmScreenContent(
        currentAlarmType = { currentAlarmType },
        currentAlarmInvokeType = { currentAlarmInvokeType },
        onChangeInvokeType = alarmsViewModel::changeExactAlarmInvokeType,
        currentTimeInMillis = { currentTimeInMillis },
        onCurrentTimeInMillisChange = alarmsViewModel::updateAlarmTargetTime,
        modifier = Modifier.fillMaxSize()
    )
}


@Composable
private fun ExactAlarmScreenContent(
    currentAlarmType: () -> AlarmType,
    currentAlarmInvokeType: () -> AlarmsInvokeType,
    onChangeInvokeType: (AlarmsInvokeType) -> Unit,
    currentTimeInMillis: () -> Long,
    onCurrentTimeInMillisChange: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    // Exact alarm screen column content
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(8.dp)
    ) {
        // Alarm invoke type selectors
        item(key = 0) {
            AlarmInvokeTypeSelectors(
                isExactAlarm = { true },
                currentAlarmInvokeType = currentAlarmInvokeType,
                onChangeInvokeType = onChangeInvokeType
            )
        }

        // Alarm invoke time pickers
        item(key = 1) {
            // If the current alarm type is ELAPSED_TIME
            if (currentAlarmType().isElapsedTime) {
                ElapsedTimePicker(
                    onCurrentTimeInMillisChange = onCurrentTimeInMillisChange
                )
            }
            // Else (is RTC)
            else {
                RtcTimePicker(
                    currentTimeInMillis = currentTimeInMillis,
                    onCurrentTimeInMillisChange = onCurrentTimeInMillisChange
                )
            }
        }
    }
}


/*
Previews
 */
@Preview
@Composable
private fun ExactAlarmScreenContentPreview() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Light theme
        PreviewAppTheme(
            darkTheme = isSystemInDarkTheme()
        ) {

            var currentAlarmType = remember { mutableStateOf(AlarmType()) }
            var currentAlarmInvokeType = remember { mutableStateOf(ExactAlarmsInvokeType.NORMAL) }

            ExactAlarmScreenContent(
                currentAlarmType = { currentAlarmType.value },
                currentAlarmInvokeType = { currentAlarmInvokeType.value },
                onChangeInvokeType = { currentAlarmInvokeType.value = it as ExactAlarmsInvokeType },
                currentTimeInMillis = { 0L },
                onCurrentTimeInMillisChange = { },
                modifier = Modifier.size(500.dp, 1000.dp)
            )
        }
    }
}