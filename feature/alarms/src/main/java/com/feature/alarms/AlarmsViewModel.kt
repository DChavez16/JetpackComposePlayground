package com.feature.alarms

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.example.alarmmanager.receiver.AlarmReceiver
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

const val TAG = "AlarmsViewModel"


@HiltViewModel
internal class AlarmsViewModel @Inject constructor(
    private val alarmManager: AlarmManager,
    @ApplicationContext private val context: Context
) : ViewModel() {

    // Define a Flow for isAlarmExact and its backing property
    private val _isAlarmExact = MutableStateFlow(true)
    val isAlarmExact: StateFlow<Boolean> = _isAlarmExact

    // Define a Flow for exactAlarmInvokeType and its backing property
    private val _exactAlarmInvokeType = MutableStateFlow(ExactAlarmsInvokeType.NORMAL)
    val exactAlarmInvokeType: StateFlow<ExactAlarmsInvokeType> = _exactAlarmInvokeType

    // Define a Flow for inexactAlarmInvokeType and its backing property
    private val _inexactAlarmInvokeType = MutableStateFlow(InexactAlarmsInvokeType.NORMAL)
    val inexactAlarmInvokeType: StateFlow<InexactAlarmsInvokeType> = _inexactAlarmInvokeType

    // Define a Flow for alarmType and its backing property
    private val _alarmType = MutableStateFlow(AlarmType())
    val alarmType: StateFlow<AlarmType> = _alarmType

    // Define a Flow for isAlarmRunning and its backing property
    private val _isAlarmRunning = MutableStateFlow(false)
    val isAlarmRunning: StateFlow<Boolean> = _isAlarmRunning

    // Define a Flow for alarmTargetTimeMiliseconds and its backing property
    private val _alarmTargetTimeMilliseconds = MutableStateFlow(0L)
    val alarmTargetTimeMilliseconds: StateFlow<Long> = _alarmTargetTimeMilliseconds

    // Define a Flow for alarmtTargetTimeWindowMilliseconds and its backing property
    private val _alarmTargetTimeWindowLenghtMilliseconds = MutableStateFlow(0L)
    val alarmTargetTimeWindowLenghtMilliseconds: StateFlow<Long> =
        _alarmTargetTimeWindowLenghtMilliseconds

    // Creting calendar variable
    var calendar: Calendar


    init {
        Log.d(TAG, "AlarmsViewModel Started")

        // Initialize calendar variable with a Calendar instance
        calendar = Calendar.getInstance()

        // Initialize the alarm target time
        initializeAlarmTargetTime()

        // Verify if there is an active alarm
        verifyAlarmsNotActive()
    }

    /**
     * Set alarm target time in base of the current alart type value
     */
    fun initializeAlarmTargetTime() {
        // If the alarm type is ELAPSED_TIME, set alarm target time to 0 millis, else set it to the calendar current time in millis
        updateAlarmTargetTime(if (_alarmType.value.isElapsedTime) 0L else calendar.timeInMillis)

        // Set alarm target time window to 2 minutes in millis
        updateAlarmTargetTimeWindow(2 * 60 * 1000)

        Log.d(TAG, "Restarted alarm target time to default")
    }

    /**
     * Initialize the properties of the Alarm to the default ones
     */
    private fun initializeAlarmProperties() {
        // If the alarm is exact
        if (_isAlarmExact.value) changeExactAlarmInvokeType(ExactAlarmsInvokeType.NORMAL)
        // else, if its inexact
        else changeInexactAlarmInvokeType(InexactAlarmsInvokeType.NORMAL)

        Log.d(TAG, "Restarted alarm properties to default")
    }

    /**
     * Verify if an alarm with the [AlarmReceiver] is currently active and returns a nullable [PendingIntent]
     *
     * @return A nullable [PendingIntent] if an alarm is active, null otherwise
     */
    private fun getActiveAlarmPendingIntent(): PendingIntent? {
        // Creates an Intent with the AlarmReceiver
        val alarmIntent = Intent(context, AlarmReceiver::class.java)
        Log.i(TAG, "Obtaining an active alarm pending intent")
        // Obtains a PendingIntent from a broadcast
        val pendingIntent = PendingIntent.getBroadcast(
            /* context = */ context,
            /* requestCode = */ 0,
            /* intent = */ alarmIntent,
            /* flags = */ PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        Log.i(TAG, if (pendingIntent != null) "Active alarm found" else "No active alarm found")

        return pendingIntent
    }

    /**
     * Verify if there is there is currently an alarm active, if not, _isAlarmRunning is set to false, true otherwise
     */
    private fun verifyAlarmsNotActive() {
        Log.d(TAG, "Verifying if there is an active alarm")
        // Attemps to call a pending intent to verify if an alarm is active, if is not null, _isAlarmRunning is set to true, false otherwise
        _isAlarmRunning.value = getActiveAlarmPendingIntent() != null
        Log.d(TAG, "There is currently ${if (_isAlarmRunning.value) "an" else "no"} active alarm")
    }

    /**
     * Update the current [_alarmTargetTimeMilliseconds] with the provided [Long] parameter
     *
     * @param newTimeInMillis The new value for [_alarmTargetTimeMilliseconds]
     */
    fun updateAlarmTargetTime(newTimeInMillis: Long) {
        _alarmTargetTimeMilliseconds.value = newTimeInMillis
    }

    /**
     * Update the current [_alarmTargetTimeWindowLenghtMilliseconds] with the provided [Long] parameter
     *
     * @param newTimeInMillis The new value for [_alarmTargetTimeWindowLenghtMilliseconds]
     */
    fun updateAlarmTargetTimeWindow(newTimeInMillis: Long) {
        _alarmTargetTimeWindowLenghtMilliseconds.value = newTimeInMillis
    }

    /**
     * Change the current [_isAlarmExact] to the opposite value
     */
    fun changeAlarmAccuracy(newIsAlarmExact: Boolean) {
        _isAlarmExact.value = newIsAlarmExact
        Log.d(TAG, "Alarm accuracy changed to ${if (_isAlarmExact.value) "Exact" else "Inexact"}")

        // Initialize alarm target time
        initializeAlarmTargetTime()
        // Initialize alarm properties
        initializeAlarmProperties()
    }

    /**
     * Change the current [_exactAlarmInvokeType] to the new [ExactAlarmsInvokeType] given
     *
     * @param newExactAlarmInvokeType The new [ExactAlarmsInvokeType] to be set
     */
    fun changeExactAlarmInvokeType(newExactAlarmInvokeType: AlarmsInvokeType) {
        _exactAlarmInvokeType.value = newExactAlarmInvokeType as ExactAlarmsInvokeType
        Log.d(
            TAG,
            "Exact alarm invoke time type changed to ${_exactAlarmInvokeType.value.name}"
        )
    }

    /**
     * Change the current [_inexactAlarmInvokeType] to the new [InexactAlarmsInvokeType] given
     *
     * @param newInexactAlarmInvokeType The new [InexactAlarmsInvokeType] to be set
     */
    fun changeInexactAlarmInvokeType(newInexactAlarmInvokeType: AlarmsInvokeType) {
        _inexactAlarmInvokeType.value = newInexactAlarmInvokeType as InexactAlarmsInvokeType
        Log.d(
            TAG,
            "Inexact alarm invoke time type changed to ${_inexactAlarmInvokeType.value.name}"
        )
    }

    /**
     * Change the [_alarmType]'s invoke time type value to the opposite one
     *
     * @param newIsElapsedTimeValue The new value for [_alarmType]'s invoke time type
     */
    fun changeAlarmTypeInvokeTimeType(newIsElapsedTimeValue: Boolean) {
        _alarmType.value = _alarmType.value.copy(isElapsedTime = newIsElapsedTimeValue)
        Log.d(
            TAG,
            "Alarm type changed to ${if (_alarmType.value.isElapsedTime) "ELAPSED_TIME" else "RTC"}"
        )

        // Initialize alarm target time
        initializeAlarmTargetTime()
    }

    /**
     * Change the [_alarmType]'s device awake value to the opposite one
     *
     * @param newIsWakeupValue The new value for [_alarmType]'s device awake
     */
    fun changeAlarmTypeDeviceAwake(newIsWakeupValue: Boolean) {
        _alarmType.value = _alarmType.value.copy(isWakeup = newIsWakeupValue)
        Log.d(TAG, "Alarm type changed to ${if (_alarmType.value.isWakeup) "WAKEUP" else "NORMAL"}")
    }

    /**
     * Manually cancels the active alarm if there is any
     */
    fun cancelAlarm() {
        // Get active alarm pending intent
        val activeAlarmPendingIntent = getActiveAlarmPendingIntent() ?: return
        Log.i(TAG, "Active alarm found, cancelling active alarm")

        alarmManager.cancel(activeAlarmPendingIntent)
        Log.i(TAG, "Active alarm cancelled")
    }

    /**
     * Starts a new inexact alarm with the current values. If one is already running, it will be canceled
     */
    fun startAlarm() {
        // Cancel active alarm if there is any
        cancelAlarm()

        // Create a PendingIntent for the alarm
        val alarmIntent: PendingIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        }

        // Gets the current Alarm Type as an Int to be asigned to the alarms
        val selectedAlarmType = _alarmType.value.getAlarmType()

        Log.d(
            TAG,
            "Starting ${if (_isAlarmExact.value) "exact" else "inexact"} alarm with ${
                if (_isAlarmExact.value) context.getText(
                    _exactAlarmInvokeType.value.functionName
                ) else context.getText(_inexactAlarmInvokeType.value.functionName)
            } as invoke type and ${getAlarmInvokeTypeTitle()} as alarm type"
        )

        // If the Alarm is exact
        if (_isAlarmExact.value) {
            // Start the correct alarm based in the exact alarm invoke type
            when (_exactAlarmInvokeType.value) {
                ExactAlarmsInvokeType.NORMAL -> {
                    alarmManager.setExact(
                        selectedAlarmType,
                        alarmTargetTimeMilliseconds.value,
                        alarmIntent
                    )
                }

                ExactAlarmsInvokeType.REPEATING -> {
                    alarmManager.setRepeating(
                        /* type = */ selectedAlarmType,
                        /* triggerAtMillis = */ alarmTargetTimeMilliseconds.value,
                        /* intervalMillis = */ AlarmManager.INTERVAL_HALF_HOUR,
                        /* operation = */ alarmIntent
                    )
                }

                ExactAlarmsInvokeType.ALLOW_WHILE_IDLE -> {
                    alarmManager.setExactAndAllowWhileIdle(
                        /* type = */ selectedAlarmType,
                        /* triggerAtMillis = */ alarmTargetTimeMilliseconds.value,
                        /* operation = */ alarmIntent
                    )
                }

                ExactAlarmsInvokeType.ALARM_CLOCK -> {
                    alarmManager.setAlarmClock(
                        /* info = */ AlarmManager.AlarmClockInfo(
                            /* triggerTime = */ alarmTargetTimeMilliseconds.value,
                            /* showIntent = */ alarmIntent
                        ),
                        /* operation = */ alarmIntent
                    )
                }
            }

            Log.d(TAG, "Exact alarm started")
        }
        // Else (is inexact)
        else {
            // Start the correct alarm based in the inexact alarm invoke type
            when (_inexactAlarmInvokeType.value) {
                InexactAlarmsInvokeType.NORMAL -> {
                    alarmManager.set(
                        /* type = */ selectedAlarmType,
                        /* triggerAtMillis = */ alarmTargetTimeMilliseconds.value,
                        /* operation = */ alarmIntent
                    )
                }

                InexactAlarmsInvokeType.REPEATING -> {
                    alarmManager.setInexactRepeating(
                        /* type = */ selectedAlarmType,
                        /* triggerAtMillis = */ alarmTargetTimeMilliseconds.value,
                        /* intervalMillis = */ AlarmManager.INTERVAL_HALF_HOUR,
                        /* operation = */ alarmIntent
                    )
                }

                InexactAlarmsInvokeType.ALLOW_WHILE_IDLE -> {
                    alarmManager.setAndAllowWhileIdle(
                        /* type = */ selectedAlarmType,
                        /* triggerAtMillis = */ alarmTargetTimeMilliseconds.value,
                        /* operation = */ alarmIntent
                    )
                }

                InexactAlarmsInvokeType.WINDOW -> {
                    alarmManager.setWindow(
                        /* type = */ selectedAlarmType,
                        /* windowStartMillis = */
                        alarmTargetTimeMilliseconds.value - alarmTargetTimeWindowLenghtMilliseconds.value,
                        /* windowLengthMillis = */
                        alarmTargetTimeMilliseconds.value + alarmTargetTimeWindowLenghtMilliseconds.value,
                        /* operation = */
                        alarmIntent
                    )
                }
            }

            Log.d(TAG, "Inexact alarm started")
        }

        // Verfy if there is an alarm running
        verifyAlarmsNotActive()
    }

    /**
     * Get alarm invoke type title as string
     *
     * @return The alarm invoke type title as string
     */
    fun getAlarmInvokeTypeTitle(): String = context.getString(
        if (_isAlarmExact.value) _exactAlarmInvokeType.value.functionName
        else _inexactAlarmInvokeType.value.functionName
    )

    /**
     * Get alarm invoke type description as string
     *
     * @return The alarm invoke type description as string
     */
    fun getAlarmInvokeTypeDescription(): String = context.getString(
        if (_isAlarmExact.value) _exactAlarmInvokeType.value.functionDescription
        else _inexactAlarmInvokeType.value.functionDescription
    )

    /**
     * Get alarm type title as string
     *
     * @return The alarm type title as string
     */
    fun getAlarmTypeTitle(): String = context.getString(
        if (_alarmType.value.isElapsedTime) {
            if (_alarmType.value.isWakeup) {
                R.string.alarms_alarms_description_elapsed_realtime_wakeup_title
            } else {
                R.string.alarms_alarms_description_elapsed_realtime_title
            }
        } else {
            if (_alarmType.value.isWakeup) {
                R.string.alarms_alarms_description_rtc_wakeup_title
            } else {
                R.string.alarms_alarms_description_rtc_title
            }
        }
    )

    /**
     * Get alarm type description as string
     *
     * @return The alarm type description as string
     */
    fun getAlarmTypeDescription(): String = context.getString(
        if (_alarmType.value.isElapsedTime) {
            if (_alarmType.value.isWakeup) {
                R.string.alarms_alarms_description_elapsed_realtime_wakeup_description
            } else {
                R.string.alarms_alarms_description_elapsed_realtime_description
            }
        } else {
            if (_alarmType.value.isWakeup) {
                R.string.alarms_alarms_description_rtc_wakeup_description
            } else {
                R.string.alarms_alarms_description_rtc_description
            }
        }
    )
}


/*
    Helper enum classes that represent the invoke alarm types
 */
// Invoke types interface
internal interface AlarmsInvokeType {
    val functionName: Int
    val functionDescription: Int
}

// Invoke types for exact alarms
internal enum class ExactAlarmsInvokeType(
    @StringRes override val functionName: Int,
    @StringRes override val functionDescription: Int
) : AlarmsInvokeType {
    NORMAL(
        functionName = R.string.alarms_alarms_description_set_exact_title,
        functionDescription = R.string.alarms_alarms_description_set_exact_description
    ),
    REPEATING(
        functionName = R.string.alarms_alarms_description_set_repeating_title,
        functionDescription = R.string.alarms_alarms_description_set_repeating_description
    ),
    ALLOW_WHILE_IDLE(
        functionName = R.string.alarms_alarms_description_set_exact_and_allow_while_idle_title,
        functionDescription = R.string.alarms_alarms_description_set_exact_and_allow_while_idle_description
    ),
    ALARM_CLOCK(
        functionName = R.string.alarms_alarms_description_set_alarm_clock_title,
        functionDescription = R.string.alarms_alarms_description_set_alarm_clock_description
    )
}

// Invoke types for inexact alarms
internal enum class InexactAlarmsInvokeType(
    @StringRes override val functionName: Int,
    @StringRes override val functionDescription: Int
) : AlarmsInvokeType {
    NORMAL(
        functionName = R.string.alarms_alarms_description_set_title,
        functionDescription = R.string.alarms_alarms_description_set_description
    ),
    REPEATING(
        functionName = R.string.alarms_alarms_description_set_inexact_repeating_title,
        functionDescription = R.string.alarms_alarms_description_set_inexact_repeating_description
    ),
    ALLOW_WHILE_IDLE(
        functionName = R.string.alarms_alarms_description_set_and_allow_while_idle_title,
        functionDescription = R.string.alarms_alarms_description_set_and_allow_while_idle_description
    ),
    WINDOW(
        functionName = R.string.alarms_alarms_description_set_window_title,
        functionDescription = R.string.alarms_alarms_description_set_window_description
    )
}


/**
 * Data class that constructs an Alarm Type based in its parameters.
 *
 * It can be either ELAPSED_TIME or RTC, both in normal and WAKEUP variants
 */
internal data class AlarmType(
    /**
     * This [Boolean] parameter indicates if the Alarm Type is currently ELAPSED_TIME, if false, then it's RTC
     */
    var isElapsedTime: Boolean = true,
    /**
     * This Boolean parameter indicates if the Alarm Type is in its WAKEUP variant. It's false by default
     */
    var isWakeup: Boolean = false
) {

    /**
     * Get the [AlarmType] Int value based on the current parameters
     */
    fun getAlarmType(): Int =
        when (this.isElapsedTime) {
            true -> {
                if (this.isWakeup) AlarmManager.ELAPSED_REALTIME_WAKEUP else AlarmManager.ELAPSED_REALTIME
            }

            false -> {
                if (this.isWakeup) AlarmManager.RTC_WAKEUP else AlarmManager.RTC
            }
        }
}