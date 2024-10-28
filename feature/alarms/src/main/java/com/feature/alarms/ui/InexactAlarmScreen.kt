package com.feature.alarms.ui

import androidx.compose.animation.AnimatedVisibility
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
import com.feature.alarms.InexactAlarmsInvokeType


@Composable
internal fun InexactAlarmScreen(
    alarmsViewModel: AlarmsViewModel
) {

    // Current alarm type state flow
    val currentAlarmType = alarmsViewModel.alarmType.collectAsState().value
    // Current alarm invoke type state flow
    val currentAlarmInvokeType = alarmsViewModel.inexactAlarmInvokeType.collectAsState().value
    // Current time in millis state flow
    val currentTimeInMillis = alarmsViewModel.alarmTargetTimeMilliseconds.collectAsState().value
    // Alarm running flag state flow
    val isAlarmRunning = alarmsViewModel.isAlarmRunning.collectAsState().value

    InexactAlarmScreenContent(
        currentAlarmType = { currentAlarmType },
        onAlarmTypeInvokeTypeChange = alarmsViewModel::changeAlarmTypeInvokeTimeType,
        onAlarmTypeWakeupTypeChange = alarmsViewModel::changeAlarmTypeDeviceAwake,
        currentAlarmInvokeType = { currentAlarmInvokeType },
        onChangeInvokeType = alarmsViewModel::changeInexactAlarmInvokeType,
        currentTimeInMillis = { currentTimeInMillis },
        onCurrentTimeInMillisChange = alarmsViewModel::updateAlarmTargetTime,
        isAlarmWindow = { currentAlarmInvokeType == InexactAlarmsInvokeType.WINDOW },
        onCurrentWindowLenghtInMillisChange = alarmsViewModel::updateAlarmTargetTimeWindow,
        isAlarmRunning = { isAlarmRunning },
        onCancelAlarmClick = alarmsViewModel::cancelAlarm,
        onStartAlarmClick = alarmsViewModel::startAlarm,
        modifier = Modifier.fillMaxSize()
    )
}


@Composable
private fun InexactAlarmScreenContent(
    currentAlarmType: () -> AlarmType,
    onAlarmTypeInvokeTypeChange: (Boolean) -> Unit,
    onAlarmTypeWakeupTypeChange: (Boolean) -> Unit,
    currentAlarmInvokeType: () -> AlarmsInvokeType,
    onChangeInvokeType: (AlarmsInvokeType) -> Unit,
    currentTimeInMillis: () -> Long,
    onCurrentTimeInMillisChange: (Long) -> Unit,
    isAlarmWindow: () -> Boolean,
    onCurrentWindowLenghtInMillisChange: (Long) -> Unit,
    isAlarmRunning: () -> Boolean,
    onCancelAlarmClick: () -> Unit,
    onStartAlarmClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Inexact alarm screen column content
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
            onAlarmTypeInvokeTimeTypeChange = onAlarmTypeInvokeTypeChange,
            onAlarmTypeWakeupTypeChange = onAlarmTypeWakeupTypeChange
        )

        // Alarm invoke type selectors
        AlarmInvokeTypeSelectors(
            isExactAlarm = { false },
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

        // If is alarm window, show the window lenght picker
        AnimatedVisibility(
            visible = isAlarmWindow(),
            label = "WindowLenghtPickerAnimation"
        ) {
            ElapsedTimePicker(
                onCurrentTimeInMillisChange = onCurrentWindowLenghtInMillisChange,
                isChooseWindowRangeVariant = true
            )
        }

        Spacer(modifier.weight(1f))

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
private fun InexactAlarmScreenContentPreview() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Light theme
        PreviewAppTheme(
            darkTheme = isSystemInDarkTheme()
        ) {
            var currentAlarmType = remember { mutableStateOf(AlarmType(true, false)) }
            var currentAlarmInvokeType = remember { mutableStateOf(InexactAlarmsInvokeType.REPEATING) }

            InexactAlarmScreenContent(
                currentAlarmType = { currentAlarmType.value },
                onAlarmTypeInvokeTypeChange = { currentAlarmType.value = currentAlarmType.value.copy(isElapsedTime = it) },
                onAlarmTypeWakeupTypeChange = { currentAlarmType.value = currentAlarmType.value.copy(isWakeup = it) },
                currentAlarmInvokeType = { currentAlarmInvokeType.value },
                onChangeInvokeType = { currentAlarmInvokeType.value = it as InexactAlarmsInvokeType },
                currentTimeInMillis = { 0L },
                onCurrentTimeInMillisChange = { },
                isAlarmWindow = { currentAlarmInvokeType.value == InexactAlarmsInvokeType.WINDOW },
                onCurrentWindowLenghtInMillisChange = { },
                isAlarmRunning = { false },
                onCancelAlarmClick = { },
                onStartAlarmClick = { },
                modifier = Modifier.size(500.dp, 1000.dp)
            )
        }
    }
}