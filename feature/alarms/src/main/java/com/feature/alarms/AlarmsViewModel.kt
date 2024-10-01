package com.feature.alarms

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

const val ALARM_INTENT_ACTION = ""
const val TAG = "AlarmsViewModel"


@HiltViewModel
internal class AlarmsViewModel @Inject constructor(
    private val alarmManager: AlarmManager,
    @ApplicationContext private val context: Context
) : ViewModel() {

    // TODO Add log for each method

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

    // TODO Add an alarm target time picker backing property for AlarmType ELAPSED_TIME
    // TODO Add an alarm target date picker backing property for AlarmType RTC


    init {
        verifyAlarmsNotActive()
    }

    /**
     * Verify if an alarm with the [ALARM_INTENT_ACTION] is currently active and returns a nullable [PendingIntent]
     *
     * @return A nullable [PendingIntent] if an alarm is active, null otherwise
     */
    private fun getActiveAlarmPendingIntent(): PendingIntent? {
        // Creates an Intent with the ALARM_INTENT_ACTION
        val alarmIntent = Intent(ALARM_INTENT_ACTION)
        Log.i(TAG, "Obtaining an active alarm pending intent")
        // Obtains a PendingIntent from a broadcast
        val pendingIntent = PendingIntent.getBroadcast(
            /* context = */ context,
            /* requestCode = */ 0,
            /* intent = */ alarmIntent,
            /* flags = */ PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        Log.i(TAG, if(pendingIntent != null) "Active alarm found" else "No active alarm found")

        return pendingIntent
    }

    /**
     * Verify if there is there is currently an alarm active, if not, _isAlarmRunning is set to false, true otherwise
     */
    private fun verifyAlarmsNotActive() {
        // Attemps to call a pending intent to verify if an alarm is active, if is not null, _isAlarmRunning is set to true, false otherwise
        _isAlarmExact.value = getActiveAlarmPendingIntent() != null
    }

    /**
     * Change the current [_isAlarmExact] to the opposite one
     */
    fun changeAlarmInvokeTimeType() {
        _isAlarmExact.value = !_isAlarmExact.value
    }

    /**
     * Change the current [_exactAlarmInvokeType] to the new [ExactAlarmsInvokeType] given
     *
     * @param newExactAlarmInvokeType The new [ExactAlarmsInvokeType] to be set
     */
    fun changeExactAlarmInvokeType(newExactAlarmInvokeType: ExactAlarmsInvokeType) {
        _exactAlarmInvokeType.value = newExactAlarmInvokeType
    }

    /**
     * Change the current [_inexactAlarmInvokeType] to the new [InexactAlarmsInvokeType] given
     *
     * @param newInexactAlarmInvokeType The new [InexactAlarmsInvokeType] to be set
     */
    fun changeInexactAlarmInvokeType(newInexactAlarmInvokeType: InexactAlarmsInvokeType) {
        _inexactAlarmInvokeType.value = newInexactAlarmInvokeType
    }

    /**
     * Change the [_alarmType]'s invoke time type value to the opposite one
     */
    fun changeAlarmTypeInvokeTimeType() {
        _alarmType.value = _alarmType.value.changeInvokeTimeType()
    }

    /**
     * Change the [_alarmType]'s device awake value to the opposite one
     */
    fun changeAlarmTypeDeviceAwake() {
        _alarmType.value = _alarmType.value.changeWakeupType()
    }

    // TODO Add method to change the alarm target time for ELAPSED_TIME
    // TODO Add method to change the alarm target date for RTC

    /**
     * Manually cancels the active alarm if there is any
     */
    fun cancelAlarm() {
        // Get active alarm pending intent
        val activeAlarmPendingIntent = getActiveAlarmPendingIntent() ?: return

        alarmManager.cancel(activeAlarmPendingIntent)
    }

    /**
     * Starts a new alarm with the current values. If one is already running, it will be canceled
     */
    fun startAlarm() {
        // TODO Cancel active alarm if there is any

        // TODO Set an exact or inexact alarm
        // TODO Start an alarm with the given parameters
        // TODO Set the alarm TYPE as ELAPSED_TIME or RTC (with WAKEUP variant if needed). Use the alarm target time (for ELAPSED_TIME) or alarm target date (for RTC)
    }
}


/*
    Helper enum classes that represent the invoke alarm types
 */
// Invoke types for exact alarms
internal enum class ExactAlarmsInvokeType {
    NORMAL,
    REPEATING,
    ALLOW_WHILE_IDLE,
    ALARM_CLOCK
}

// Invoke types for inexact alarms
internal enum class InexactAlarmsInvokeType {
    NORMAL,
    REPEATING,
    ALLOW_WHILE_IDLE,
    WINDOW
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
     * Change the current [isElapsedTime] invoke time type value to the opposite one (From ELAPSED_TIME to RTC or vice versa)
     *
     * @return The updated [AlarmType] instance
     */
    fun changeInvokeTimeType(): AlarmType {
        this.isElapsedTime = !this.isElapsedTime

        return this
    }

    /**
     * Change the current [isWakeup] device awake value to the opposite one (From normal to WAKEUP or vice versa)
     *
     * @return The updated [AlarmType] instance
     */
    fun changeWakeupType(): AlarmType {
        this.isWakeup = !this.isWakeup

        return this
    }
}