@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.feature.alarms.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.BedtimeOff
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ui.theme.AppTheme
import com.feature.alarms.AlarmType
import com.feature.alarms.AlarmsInvokeType
import com.feature.alarms.ExactAlarmsInvokeType
import com.feature.alarms.InexactAlarmsInvokeType
import com.feature.alarms.R
import kotlinx.coroutines.launch


@Composable
internal fun AlarmTypeSelectors(
    currentAlarmType: () -> AlarmType,
    onAlarmTypeInvokeTimeTypeChange: () -> Unit,
    onAlarmTypeWakeupTypeChange: () -> Unit,
    modifier: Modifier = Modifier
) {

    // TODO Add logs at user interactions
    // TODO Reduce recompositions

    // Coroutine scope for pager animations
    val coroutineScope = rememberCoroutineScope()

    // State for showing or not the info Alert Dialog
    var showAlertDialog by remember { mutableStateOf(false) }

    // State for the horizontal pager
    val pagerState = rememberPagerState(
        initialPage = if (currentAlarmType().isElapsedTime) 0 else 1,
        pageCount = { 2 }
    )

    // Launched effect that observes with a Snapshot changes in the pager state' current page
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect {
            // Change the alarm type invoke time type
            onAlarmTypeInvokeTimeTypeChange()
        }
    }

    // AlarmType selectors content
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.widthIn(max = 500.dp)
    ) {
        // Title and info
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.alarms_shared_alarm_type_header),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )

            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = stringResource(R.string.alarms_shared_alarm_type_info_accesibility),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.clickable {
                    showAlertDialog = true
                }
            )
        }

        // Selectors
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {
            // Elapsed type selector
            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 14.dp),
                modifier = Modifier
                    .weight(3f)
                    .clickable {
                        coroutineScope.launch {
                            // If the page is currently the 'ELAPSED_TIME' move to the 'RTC' page. Otherwise the opposite
                            pagerState.scrollToPage(if (pagerState.currentPage == 0) 1 else 0)

                            // Update the alarm invoke type
                            onAlarmTypeInvokeTimeTypeChange()
                        }
                    }
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainer,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(4.dp)
            ) { page ->
                when (page) {
                    // ELAPSED_TIME selected
                    0 -> {
                        Text(
                            text = stringResource(R.string.alarms_shared_alarm_type_elapsed_time),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = if (pagerState.currentPage == 0) TextAlign.Center else TextAlign.End,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    // RTC selected
                    1 -> {
                        Text(
                            text = stringResource(R.string.alarms_shared_alarm_type_rtc),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = if (pagerState.currentPage == 1) TextAlign.Center else TextAlign.Unspecified,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Wakeup type selector
            FilterChip(
                selected = currentAlarmType().isWakeup,
                onClick = onAlarmTypeWakeupTypeChange,
                label = {
                    Text(
                        text = stringResource(R.string.alarms_shared_alarm_type_wakeup),
                        style = MaterialTheme.typography.labelLarge,
                        textAlign = TextAlign.Center
                    )
                },
                colors = FilterChipDefaults.filterChipColors().copy(
                    labelColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.20f),
                    selectedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }

    // Alert dialog providing info about Alarm Types
    if (showAlertDialog) {
        InfoAlertDialog(
            alertDialogTitle = R.string.alarms_shared_alarm_type_header,
            alertDialogContent = R.string.alarms_shared_alarm_type_info,
            onDismissRequest = { showAlertDialog = false }
        )
    }
}


@Composable
internal fun AlarmInvokeTypeSelectors(
    isExactAlarm: () -> Boolean,
    currentAlarmInvokeType: () -> AlarmsInvokeType,
    onChangeInvokeType: (AlarmsInvokeType) -> Unit,
    modifier: Modifier = Modifier
) {

    // TODO Add logs at user interactions
    // TODO Reduce recompositions

    // State for showing or not the info Alert Dialog
    var showAlertDialog by remember { mutableStateOf(false) }

    // Alarm InvokeType selectos content
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.widthIn(max = 500.dp)
    ) {
        // Title and info
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.alarms_shared_alarm_invoke_type_header),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )

            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = stringResource(R.string.alarms_shared_alarm_invoke_type_info_accesibility),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.clickable {
                    showAlertDialog = true
                }
            )
        }

        // Selectors
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {
            // setWindow() or setAlarmClock() selector
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Selector label
                Text(
                    text = stringResource(
                        // If the alarm is exact use the setAlarmClock() selector
                        if (isExactAlarm()) {
                            R.string.alarms_shared_alarm_invoke_type_exact_precise_type
                        }
                        // Else use the setWindow() selector
                        else {
                            R.string.alarms_shared_alarm_invoke_type_inexact_window
                        }
                    ),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )

                // Selector switch
                Switch(
                    checked = isInvokeTypeSelectorSwitchActivated(
                        isExactAlarm = isExactAlarm,
                        currentAlarmInvokeType = currentAlarmInvokeType
                    ),
                    onCheckedChange = { isSwitchNowChecked ->
                        // If the alarm is exact
                        if (isExactAlarm()) {
                            onChangeInvokeType(
                                // If the switch is checked, return setAlarmClock()
                                if (isSwitchNowChecked) ExactAlarmsInvokeType.ALARM_CLOCK
                                // Else, return setExact()
                                else ExactAlarmsInvokeType.NORMAL
                            )
                        }
                        // Else
                        else {
                            onChangeInvokeType(
                                // If the switch is checked, return setWindow()
                                if (isSwitchNowChecked) InexactAlarmsInvokeType.WINDOW
                                // Else, return set()
                                else InexactAlarmsInvokeType.NORMAL
                            )
                        }
                    }
                )
            }

            // setRepeating() and setAndAllowWhileIdle() selectors
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                val selectedIconModifier = Modifier
                    .shadow(4.dp, shape = RoundedCornerShape(6.dp))
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainer,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .clickable {
                        onChangeInvokeType(if (isExactAlarm()) ExactAlarmsInvokeType.NORMAL else InexactAlarmsInvokeType.NORMAL)
                    }
                    .padding(8.dp)

                // Repeating selector button
                Icon(
                    imageVector = Icons.Rounded.Repeat,
                    contentDescription = stringResource(
                        if (isRepeatingAlarmSelected(currentAlarmInvokeType)) {
                            R.string.alarms_shared_alarm_invoke_type_repeating_accesibility_disable
                        } else R.string.alarms_shared_alarm_invoke_type_repeating_accesibility_enable
                    ),
                    tint = if (isRepeatingAlarmSelected(currentAlarmInvokeType)) {
                        MaterialTheme.colorScheme.primary
                    } else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.25f),
                    modifier = if (isRepeatingAlarmSelected(currentAlarmInvokeType)) selectedIconModifier else Modifier
                        .clickable {
                            onChangeInvokeType(if (isExactAlarm()) ExactAlarmsInvokeType.REPEATING else InexactAlarmsInvokeType.REPEATING)
                        }
                        .padding(8.dp)
                )

                Spacer(Modifier.width(20.dp))

                // Allow while idle selector button
                Icon(
                    imageVector = Icons.Rounded.BedtimeOff,
                    contentDescription = stringResource(
                        if (isAllowWhileIdleAlarmSelected(currentAlarmInvokeType)) {
                            R.string.alarms_shared_alarm_invoke_type_doze_accesibility_disable
                        } else R.string.alarms_shared_alarm_invoke_type_doze_accesibility_enable
                    ),
                    tint = if (isAllowWhileIdleAlarmSelected(currentAlarmInvokeType)) {
                        MaterialTheme.colorScheme.primary
                    } else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.25f),
                    modifier = if (isAllowWhileIdleAlarmSelected(currentAlarmInvokeType)) selectedIconModifier else Modifier
                        .clickable {
                            onChangeInvokeType(if (isExactAlarm()) ExactAlarmsInvokeType.ALLOW_WHILE_IDLE else InexactAlarmsInvokeType.ALLOW_WHILE_IDLE)
                        }
                        .padding(8.dp)
                )
            }
        }
    }

    // Alert dialog providing info about Alarm Invoke Types
    if (showAlertDialog) {
        InfoAlertDialog(
            alertDialogTitle = R.string.alarms_shared_alarm_invoke_type_header,
            alertDialogContent = R.string.alarms_shared_alarm_invoke_type_info,
            onDismissRequest = { showAlertDialog = false }
        )
    }
}


@Composable
internal fun ElapsedTimePicker() {
    // TODO Create custom time picker composable for ELAPSED_TIME AlarmType
}


@Composable
internal fun RtcTimePicker() {
    // TODO Use defalut time picker composable for RTC AlarmType
}


@Composable
internal fun AlarmActionButtons() {

}


@Composable
private fun InfoAlertDialog(
    alertDialogTitle: Int,
    alertDialogContent: Int,
    onDismissRequest: () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
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
                text = stringResource(alertDialogTitle),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .align(CenterHorizontally)
            )

            // Content of the Alert Dialog
            Text(
                text = stringResource(alertDialogContent),
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Justify,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Dismiss Button
            TextButton(
                onClick = onDismissRequest,
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
Helper methods
 */
private fun isInvokeTypeSelectorSwitchActivated(
    isExactAlarm: () -> Boolean,
    currentAlarmInvokeType: () -> AlarmsInvokeType
): Boolean {
    return if (isExactAlarm()) {
        currentAlarmInvokeType() == ExactAlarmsInvokeType.ALARM_CLOCK
    } else {
        currentAlarmInvokeType() == InexactAlarmsInvokeType.WINDOW
    }
}

private fun isRepeatingAlarmSelected(
    currentAlarmInvokeType: () -> AlarmsInvokeType
): Boolean =
    currentAlarmInvokeType() == ExactAlarmsInvokeType.REPEATING || currentAlarmInvokeType() == InexactAlarmsInvokeType.REPEATING


private fun isAllowWhileIdleAlarmSelected(
    currentAlarmInvokeType: () -> AlarmsInvokeType
): Boolean =
    currentAlarmInvokeType() == ExactAlarmsInvokeType.ALLOW_WHILE_IDLE || currentAlarmInvokeType() == InexactAlarmsInvokeType.ALLOW_WHILE_IDLE


/*
Previews
 */
@Composable
@Preview
private fun AlarmTypeSelectorsPreview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = Modifier
            .widthIn(max = 500.dp)
    ) {
        // Light theme preview
        AppTheme(
            isDarkTheme = { false }
        ) {
            var previewAlarmType by remember { mutableStateOf(AlarmType()) }

            AlarmTypeSelectors(
                currentAlarmType = { previewAlarmType },
                onAlarmTypeInvokeTimeTypeChange = {
                    previewAlarmType = previewAlarmType.changeInvokeTimeType()
                },
                onAlarmTypeWakeupTypeChange = {
                    previewAlarmType = previewAlarmType.changeWakeupType()
                },
                modifier = Modifier.background(MaterialTheme.colorScheme.background)
            )
        }

        // Dark theme preview
        AppTheme(
            isDarkTheme = { true }
        ) {
            var previewAlarmType by remember { mutableStateOf(AlarmType(false, true)) }

            AlarmTypeSelectors(
                currentAlarmType = { previewAlarmType },
                onAlarmTypeInvokeTimeTypeChange = {
                    previewAlarmType = previewAlarmType.changeInvokeTimeType()
                },
                onAlarmTypeWakeupTypeChange = {
                    previewAlarmType = previewAlarmType.changeWakeupType()
                },
                modifier = Modifier.background(MaterialTheme.colorScheme.background)
            )
        }
    }
}

@Composable
@Preview
private fun AlarmInvokeTypeSelectorsPreview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = Modifier
            .widthIn(max = 500.dp)
    ) {
        // Light theme preview
        AppTheme(
            isDarkTheme = { false }
        ) {
            var previewAlarmInvokeType by remember { mutableStateOf(ExactAlarmsInvokeType.NORMAL) }

            AlarmInvokeTypeSelectors(
                isExactAlarm = { true },
                currentAlarmInvokeType = { previewAlarmInvokeType },
                onChangeInvokeType = { previewAlarmInvokeType = it as ExactAlarmsInvokeType },
                modifier = Modifier.background(MaterialTheme.colorScheme.background)
            )
        }

        // Dark theme preview
        AppTheme(
            isDarkTheme = { true }
        ) {
            var previewAlarmInvokeType by remember { mutableStateOf(InexactAlarmsInvokeType.NORMAL) }

            AlarmInvokeTypeSelectors(
                isExactAlarm = { false },
                currentAlarmInvokeType = { previewAlarmInvokeType },
                onChangeInvokeType = { previewAlarmInvokeType = it as InexactAlarmsInvokeType },
                modifier = Modifier.background(MaterialTheme.colorScheme.background)
            )
        }
    }
}

@Composable
@Preview
private fun ElapseTimePickerPreview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = Modifier
            .widthIn(max = 350.dp)
    ) {
        // Light theme preview
        AppTheme(
            isDarkTheme = { false }
        ) {

        }

        // Dark theme preview
        AppTheme(
            isDarkTheme = { true }
        ) {

        }
    }
}

@Composable
@Preview
private fun RtcTimePickerPreview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = Modifier
            .widthIn(max = 350.dp)
    ) {
        // Light theme preview
        AppTheme(
            isDarkTheme = { false }
        ) {

        }

        // Dark theme preview
        AppTheme(
            isDarkTheme = { true }
        ) {

        }
    }
}

@Composable
@Preview
private fun AlarmActionButtonsPreview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = Modifier
            .widthIn(max = 350.dp)
    ) {
        // Light theme preview
        AppTheme(
            isDarkTheme = { false }
        ) {

        }

        // Dark theme preview
        AppTheme(
            isDarkTheme = { true }
        ) {

        }
    }
}