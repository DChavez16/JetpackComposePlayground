@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.feature.alarms.ui

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.BedtimeOff
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.example.ui.theme.AppTheme
import com.feature.alarms.AlarmType
import com.feature.alarms.AlarmsInvokeType
import com.feature.alarms.ExactAlarmsInvokeType
import com.feature.alarms.InexactAlarmsInvokeType
import com.feature.alarms.R
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale
import kotlin.math.absoluteValue


@Composable
internal fun AlarmTypeSelectors(
    currentAlarmType: () -> AlarmType,
    onAlarmTypeInvokeTimeTypeChange: (Boolean) -> Unit,
    onAlarmTypeWakeupTypeChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {

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
            onAlarmTypeInvokeTimeTypeChange(it == 0)
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

            var isInputChipSelected by remember { mutableStateOf(currentAlarmType().isWakeup) }
            InputChip(
                selected = isInputChipSelected,
                onClick = {
                    isInputChipSelected = !isInputChipSelected
                    onAlarmTypeWakeupTypeChange(isInputChipSelected)
                },
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
internal fun ElapsedTimePicker(
    onCurrentTimeInMillisChange: (Long) -> Unit,
    modifier: Modifier = Modifier,
    isChooseWindowRangeVariant: Boolean = false
) {

    // PagerState for hours, minutes, and, seconds
    val hoursPagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { if (isChooseWindowRangeVariant) 1 else 25 }
    )
    val minutesPagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { if (isChooseWindowRangeVariant) 59 else 60 }
    )
    val secondsPagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { 60 }
    )

    // State for showing or not the info Alert Dialog
    var showAlertDialog by remember { mutableStateOf(false) }

    // State that enables, disables the user scroll of the seconds pager
    var secondsUserScrollEnabled by remember { mutableStateOf(true) }

    // Launched effect that observes with a Snapshot changes in the pager state' current pages
    LaunchedEffect(hoursPagerState) {
        snapshotFlow { hoursPagerState.currentPage }.collect { newPage ->
            // Return the total of this pager current page's value as hours in millis, plus the value for the rest of the pagers
            onCurrentTimeInMillisChange(
                (newPage * 3600000L) + (minutesPagerState.currentPage * 60000L) + (secondsPagerState.currentPage * 1000L)
            )
        }
    }
    LaunchedEffect(minutesPagerState) {
        snapshotFlow { minutesPagerState.currentPage }.collect { newPage ->
            // If the time picker is a window range selection AND the new page is 58
            if (isChooseWindowRangeVariant && newPage == 58) {
                // Scroll the seconds pager to 0
                secondsPagerState.scrollToPage(0)
                // Disable the user scroll of the seconds pager
                secondsUserScrollEnabled = false
            }
            // Else, enable it
            else secondsUserScrollEnabled = true

            // Return the total of this pager current page's value as hours in millis, plus the value for the rest of the pagers
            onCurrentTimeInMillisChange(
                (hoursPagerState.currentPage * 3600000L) + (newPage.plus(if (isChooseWindowRangeVariant) 2 else 0) * 60000L) + (secondsPagerState.currentPage * 1000L)
            )
        }
    }
    LaunchedEffect(secondsPagerState) {
        snapshotFlow { secondsPagerState.currentPage }.collect { newPage ->
            // Return the total of this pager current page's value as hours in millis, plus the value for the rest of the pagers
            onCurrentTimeInMillisChange(
                (hoursPagerState.currentPage * 3600000L) + (minutesPagerState.currentPage.plus(if (isChooseWindowRangeVariant) 2 else 0) * 60000L) + (newPage * 1000L)
            )
        }
    }

    // Elapsed time picker content
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.widthIn(max = 500.dp)
    ) {
        // If the time picker is a window range selection
        if (isChooseWindowRangeVariant) {
            // Title and info
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.alarms_shared_alarm_window_header),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = stringResource(R.string.alarms_shared_alarm_window_info_accesibility),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.clickable {
                        showAlertDialog = true
                    }
                )
            }
        }

        // Elapsed time picker
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(168.dp)
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    shape = RoundedCornerShape(32.dp)
                )
        ) {

            // Shared modifier for each pager
            val pagerModifier = Modifier
                .width(96.dp)
                .padding(horizontal = 8.dp)

            // Shared modifier for each text in the pagers
            fun pagesTextModifier(pagerState: PagerState, page: Int) = Modifier.graphicsLayer {
                val pageOffset = (
                        (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                        ).absoluteValue

                alpha = lerp(
                    start = 0.25f,
                    stop = 1f,
                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                )

                scaleY = lerp(
                    start = 0.75f,
                    stop = 1f,
                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                )

                scaleX = lerp(
                    start = 0.75f,
                    stop = 1f,
                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                )
            }

            val pagerSeparatorText = @Composable { modifier: Modifier ->
                Text(
                    text = ":",
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = modifier
                )
            }

            // Hour vertical pager
            VerticalPager(
                state = hoursPagerState,
                contentPadding = PaddingValues(vertical = 52.dp),
                modifier = pagerModifier
            ) { page ->
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Text(
                        text = convertNumberToTwoDigitString(page),
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = pagesTextModifier(hoursPagerState, page).graphicsLayer {
                            if (isChooseWindowRangeVariant) {
                                alpha = 0.25f
                            }
                        }
                    )
                }
            }

            pagerSeparatorText(Modifier.graphicsLayer {
                if (isChooseWindowRangeVariant) {
                    alpha = 0.25f
                }
            })

            // Minute vertical pager
            VerticalPager(
                state = minutesPagerState,
                contentPadding = PaddingValues(vertical = 52.dp),
                modifier = pagerModifier
            ) { page ->
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Text(
                        text = convertNumberToTwoDigitString(
                            if (isChooseWindowRangeVariant) page.plus(2) else page
                        ),
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = pagesTextModifier(minutesPagerState, page)
                    )
                }
            }

            pagerSeparatorText(Modifier)

            // Seconds vertical pager
            VerticalPager(
                state = secondsPagerState,
                contentPadding = PaddingValues(vertical = 52.dp),
                userScrollEnabled = secondsUserScrollEnabled,
                modifier = pagerModifier
            ) { page ->
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Text(
                        text = convertNumberToTwoDigitString(page),
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = pagesTextModifier(secondsPagerState, page).graphicsLayer {
                            if (!secondsUserScrollEnabled) {
                                alpha = 0.25f
                            }
                        }
                    )
                }
            }
        }
    }

    // Alert dialog providing info about how the time window works
    if (showAlertDialog) {
        InfoAlertDialog(
            alertDialogTitle = R.string.alarms_shared_alarm_window_header,
            alertDialogContent = R.string.alarms_shared_alarm_window_info,
            onDismissRequest = { showAlertDialog = false }
        )
    }
}


@Composable
internal fun RtcTimePicker(
    currentTimeInMillis: () -> Long,
    onCurrentTimeInMillisChange: (Long) -> Unit,
    modifier: Modifier = Modifier,
    calendar: Calendar = Calendar.getInstance()
) {

    // Set the calendar current time to the given currentTimenInMillis argument
    calendar.timeInMillis = currentTimeInMillis()

    // State for showing or not the calendar date picker
    var showDatePicker by remember { mutableStateOf(false) }

    // State for showing or not the time input
    var showTimeInput by remember { mutableStateOf(false) }

    // RTC picker content
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = CenterHorizontally,
        modifier = modifier
            .widthIn(max = 500.dp)
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(32.dp)
            )
            .padding(16.dp)
    ) {
        // Date text (day, month and year)
        Text(
            text =
            "${calendar.getDisplayName(Calendar.MONTH, Calendar.LONG_STANDALONE, Locale.US)} " +
                    "${calendar.get(Calendar.DAY_OF_MONTH)}, " +
                    "${calendar.get(Calendar.YEAR)}",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable { showDatePicker = true }
        )

        // Time text (hours and minutes)
        Text(
            text = convertNumberToTwoDigitString(calendar.get(Calendar.HOUR_OF_DAY)) +
                    " : " +
                    convertNumberToTwoDigitString(calendar.get(Calendar.MINUTE)),
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable { showTimeInput = true }
        )
    }

    // Alert dialog containing a date picker
    if (showDatePicker) {

        // Date picker state
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = calendar.timeInMillis
        )

        // Content of the Alert Dialog (Date picker)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Get the current millis of the hour (and minutes) assigned to the calendar
                        val currentHourInMillis =
                            (calendar.get(Calendar.HOUR_OF_DAY) * 3600000L) + (calendar.get(Calendar.MINUTE) * 60000L)

                        // Change the hour and minutes of the calentar instance with the ones in the time input state. After that add the current hour in millis
                        calendar.timeInMillis =
                            datePickerState.selectedDateMillis?.plus(currentHourInMillis)
                                ?: calendar.timeInMillis

                        // Call the onCurrentTimeInMillisChange callback with the new calendar time in millis
                        onCurrentTimeInMillisChange(calendar.timeInMillis)

                        // Set the showTimeInput state to false
                        showDatePicker = false
                    },
                    colors = ButtonDefaults.textButtonColors().copy(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = stringResource(R.string.alarms_screen_alert_dialog_confirm_button_label)
                    )
                }
            },
            dismissButton = {
                // Dismiss Button
                TextButton(
                    onClick = { showDatePicker = false },
                    colors = ButtonDefaults.textButtonColors().copy(
                        contentColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text(
                        text = stringResource(R.string.alarms_screen_alert_dialog_dismiss_button_label)
                    )
                }
            },
        ) {
            DatePicker(
                state = datePickerState
            )
        }
    }

    // Alert dialog containing a time input
    if (showTimeInput) {
        BasicAlertDialog(
            onDismissRequest = { showTimeInput = false },
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp)
        ) {

            // Time input state
            val timeInputState = rememberTimePickerState(
                initialHour = calendar.get(Calendar.HOUR_OF_DAY),
                initialMinute = calendar.get(Calendar.MINUTE),
                is24Hour = true
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Title of the Alert Dialog
                Text(
                    text = stringResource(R.string.alarms_shared_alarm_rtc_alert_dialog_time_input_header),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .align(CenterHorizontally)
                )

                // Content of the Alert Dialog (Time input)
                TimeInput(
                    state = timeInputState,
                    modifier = Modifier.align(CenterHorizontally)
                )

                // Action buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.align(End)
                ) {
                    // Dismiss Button
                    TextButton(
                        onClick = { showTimeInput = false },
                        colors = ButtonDefaults.textButtonColors().copy(
                            contentColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.alarms_screen_alert_dialog_dismiss_button_label)
                        )
                    }

                    // Confirm button
                    TextButton(
                        onClick = {
                            // Change the hour and minutes of the calentar instance with the ones in the time input state
                            calendar.set(Calendar.HOUR_OF_DAY, timeInputState.hour)
                            calendar.set(Calendar.MINUTE, timeInputState.minute)

                            // Call the onCurrentTimeInMillisChange callback with the new calendar time in millis
                            onCurrentTimeInMillisChange(calendar.timeInMillis)

                            // Set the showTimeInput state to false
                            showTimeInput = false
                        },
                        colors = ButtonDefaults.textButtonColors().copy(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.alarms_screen_alert_dialog_confirm_button_label)
                        )
                    }
                }
            }
        }
    }
}


@Composable
internal fun AlarmActionButtons(
    isAlarmRunning: () -> Boolean,
    onCancelAlarmClick: () -> Unit,
    onStartAlarmClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .widthIn(max = 500.dp)
    ) {
        // Cancel button (disable if an alarm is running)
        OutlinedButton(
            onClick = onCancelAlarmClick,
            enabled = isAlarmRunning(),
            modifier = Modifier.width(100.dp)
        ) {
            Text(
                text = stringResource(R.string.alarms_shared_alarm_cancel_alarm_button_label)
            )
        }

        // Start button
        Button(
            onClick = onStartAlarmClick,
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 8.dp,
                pressedElevation = 4.dp,
                hoveredElevation = 12.dp,
                focusedElevation = 12.dp,
                disabledElevation = 0.dp
            ),
            modifier = Modifier.width(100.dp)
        ) {
            Text(
                text = stringResource(R.string.alarms_shared_alarm_start_button_label)
            )
        }
    }
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


private fun convertNumberToTwoDigitString(number: Int): String =
    if (number < 10) "0$number" else number.toString()


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
                    previewAlarmType = previewAlarmType.copy(isElapsedTime = it)
                },
                onAlarmTypeWakeupTypeChange = {
                    previewAlarmType = previewAlarmType.copy(isWakeup = it)
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
                    previewAlarmType = previewAlarmType.copy(isElapsedTime = it)
                },
                onAlarmTypeWakeupTypeChange = {
                    previewAlarmType = previewAlarmType.copy(isWakeup = it)
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
            val calendarOne = Calendar.getInstance()
            calendarOne.set(Calendar.HOUR, 0)

            ElapsedTimePicker(
                onCurrentTimeInMillisChange = {
                    calendarOne.timeInMillis = it

                    Log.d(
                        "ElapseTimePickerPreview",
                        "Current time: " +
                                "${calendarOne.get(Calendar.HOUR)} : " +
                                "${calendarOne.get(Calendar.MINUTE)} : " +
                                "${calendarOne.get(Calendar.SECOND)}"
                    )
                },
                modifier = Modifier.background(MaterialTheme.colorScheme.background)
            )
        }

        // Dark theme preview
        AppTheme(
            isDarkTheme = { true }
        ) {
            val calendarTwo = Calendar.getInstance()
            calendarTwo.set(Calendar.HOUR, 0)

            ElapsedTimePicker(
                onCurrentTimeInMillisChange = {
                    calendarTwo.timeInMillis = it

                    Log.d(
                        "ElapseTimePickerPreview",
                        "Current window time: " +
                                "${calendarTwo.get(Calendar.HOUR)} : " +
                                "${calendarTwo.get(Calendar.MINUTE)} : " +
                                "${calendarTwo.get(Calendar.SECOND)}"
                    )
                },
                modifier = Modifier.background(MaterialTheme.colorScheme.background),
                isChooseWindowRangeVariant = true
            )
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

        val calendar = Calendar.getInstance()
        var currentTimenInMillis by remember { mutableLongStateOf(calendar.timeInMillis) }

        // Light theme preview
        AppTheme(
            isDarkTheme = { false }
        ) {
            RtcTimePicker(
                currentTimeInMillis = { currentTimenInMillis },
                onCurrentTimeInMillisChange = { currentTimenInMillis = it },
                modifier = Modifier.background(MaterialTheme.colorScheme.background)
            )
        }

        // Dark theme preview
        AppTheme(
            isDarkTheme = { true }
        ) {
            RtcTimePicker(
                currentTimeInMillis = { currentTimenInMillis },
                onCurrentTimeInMillisChange = { currentTimenInMillis = it },
                modifier = Modifier.background(MaterialTheme.colorScheme.background)
            )
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
        var isAlarmRunning by remember { mutableStateOf(false) }

        // Light theme preview
        AppTheme(
            isDarkTheme = { false }
        ) {
            AlarmActionButtons(
                isAlarmRunning = { isAlarmRunning },
                onCancelAlarmClick = { isAlarmRunning = false },
                onStartAlarmClick = { isAlarmRunning = true },
                modifier = Modifier.background(MaterialTheme.colorScheme.background)
            )
        }

        // Dark theme preview
        AppTheme(
            isDarkTheme = { true }
        ) {
            AlarmActionButtons(
                isAlarmRunning = { isAlarmRunning },
                onCancelAlarmClick = { isAlarmRunning = false },
                onStartAlarmClick = { isAlarmRunning = true },
                modifier = Modifier.background(MaterialTheme.colorScheme.background)
            )
        }
    }
}