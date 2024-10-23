package com.feature.alarms.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
    // Alarm running flag state flow
    val isAlarmRunning = alarmsViewModel.isAlarmRunning.collectAsState().value

    ExactAlarmScreenContent(
        currentAlarmType = { currentAlarmType },
        onAlarmInvokeTypeChange = alarmsViewModel::changeAlarmTypeInvokeTimeType,
        onAlarmTypeWakeupTypeChange = alarmsViewModel::changeAlarmTypeDeviceAwake,
        currentAlarmInvokeType = { currentAlarmInvokeType },
        onChangeInvokeType = alarmsViewModel::changeExactAlarmInvokeType,
        currentTimeInMillis = { currentTimeInMillis },
        onCurrentTimeInMillisChange = alarmsViewModel::updateAlarmTargetTime,
        isAlarmRunning = { isAlarmRunning },
        onCancelAlarmClick = alarmsViewModel::cancelAlarm,
        onStartAlarmClick = alarmsViewModel::startAlarm,
        modifier = Modifier.fillMaxSize()
    )
}


@Composable
private fun ExactAlarmScreenContent(
    currentAlarmType: () -> AlarmType,
    onAlarmInvokeTypeChange: () -> Unit,
    onAlarmTypeWakeupTypeChange: () -> Unit,
    currentAlarmInvokeType: () -> AlarmsInvokeType,
    onChangeInvokeType: (AlarmsInvokeType) -> Unit,
    currentTimeInMillis: () -> Long,
    onCurrentTimeInMillisChange: (Long) -> Unit,
    isAlarmRunning: () -> Boolean,
    onCancelAlarmClick: () -> Unit,
    onStartAlarmClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Column content
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(8.dp)
    ) {
        // Alarm type selectors
        AlarmTypeSelectors(
            currentAlarmType = currentAlarmType,
            onAlarmTypeInvokeTimeTypeChange = onAlarmInvokeTypeChange,
            onAlarmTypeWakeupTypeChange = onAlarmTypeWakeupTypeChange
        )

        // Alarm invoke type selectors
        AlarmInvokeTypeSelectors(
            isExactAlarm = { true },
            currentAlarmInvokeType = currentAlarmInvokeType,
            onChangeInvokeType = onChangeInvokeType
        )

        // Alarm invoke time pickers
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

        Spacer(Modifier.weight(1f))

        // Alarm action buttons
        AlarmActionButtons(
            isAlarmRunning = isAlarmRunning,
            onCancelAlarmClick = onCancelAlarmClick,
            onStartAlarmClick = onStartAlarmClick
        )
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
            ExactAlarmScreenContent(
                currentAlarmType = { AlarmType() },
                onAlarmInvokeTypeChange = { },
                onAlarmTypeWakeupTypeChange = { },
                currentAlarmInvokeType = { ExactAlarmsInvokeType.REPEATING },
                onChangeInvokeType = { },
                currentTimeInMillis = { 0L },
                onCurrentTimeInMillisChange = { },
                isAlarmRunning = { false },
                onCancelAlarmClick = { },
                onStartAlarmClick = { },
                modifier = Modifier.size(500.dp, 1000.dp)
            )
        }
    }
}