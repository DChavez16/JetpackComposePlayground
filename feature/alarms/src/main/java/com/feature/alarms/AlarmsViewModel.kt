package com.feature.alarms

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.util.Log
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
    @ApplicationContext private val context: Context,
    calendar: Calendar? = Calendar.getInstance()
) : ViewModel() {

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
    private val _alarmTargetTimeMiliseconds = MutableStateFlow( calendar?.timeInMillis ?: 0L)
    val alarmTargetTimeMiliseconds: StateFlow<Long> = _alarmTargetTimeMiliseconds

    // Define a Flow for alarmtTargetTimeWindowMilliseconds and its backing property
    private val _alarmTargetTimeWindowLenghtMilliseconds = MutableStateFlow(calendar?.timeInMillis ?: 0L)
    val alarmTargetTimeWindowLenghtMilliseconds: StateFlow<Long> = _alarmTargetTimeWindowLenghtMilliseconds


    init {
        Log.d(TAG, "AlarmsViewModel Started")

        // Verify if there is an active alarm
        verifyAlarmsNotActive()
    }

    /**
     * Verify if an alarm with the [ALARM_INTENT_ACTION] is currently active and returns a nullable [PendingIntent]
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
        Log.i(TAG, if(pendingIntent != null) "Active alarm found" else "No active alarm found")

        return pendingIntent
    }

    /**
     * Verify if there is there is currently an alarm active, if not, _isAlarmRunning is set to false, true otherwise
     */
    private fun verifyAlarmsNotActive() {
        Log.d(TAG, "Verifying if there is an active alarm")
        // Attemps to call a pending intent to verify if an alarm is active, if is not null, _isAlarmRunning is set to true, false otherwise
        _isAlarmRunning.value = getActiveAlarmPendingIntent() != null
        Log.d(TAG, "There is currently ${if(_isAlarmRunning.value) "an" else "no"} active alarm")
    }

    /**
     * Change the current [_exactAlarmInvokeType] to the new [ExactAlarmsInvokeType] given
     *
     * @param newExactAlarmInvokeType The new [ExactAlarmsInvokeType] to be set
     */
    fun changeExactAlarmInvokeType(newExactAlarmInvokeType: ExactAlarmsInvokeType) {
        _exactAlarmInvokeType.value = newExactAlarmInvokeType
        Log.d(TAG, "Exact alarm invoke time type changed to ${_exactAlarmInvokeType.value.functionName}")
    }

    /**
     * Change the current [_inexactAlarmInvokeType] to the new [InexactAlarmsInvokeType] given
     *
     * @param newInexactAlarmInvokeType The new [InexactAlarmsInvokeType] to be set
     */
    fun changeInexactAlarmInvokeType(newInexactAlarmInvokeType: InexactAlarmsInvokeType) {
        _inexactAlarmInvokeType.value = newInexactAlarmInvokeType
        Log.d(TAG, "Inexact alarm invoke time type changed to ${_inexactAlarmInvokeType.value.functionName}")
    }

    /**
     * Change the [_alarmType]'s invoke time type value to the opposite one
     */
    fun changeAlarmTypeInvokeTimeType() {
        _alarmType.value = _alarmType.value.changeInvokeTimeType()
        Log.d(TAG, "Alarm type changed to ${if(_alarmType.value.isElapsedTime) "ELAPSED_TIME" else "RTC"}")
    }

    /**
     * Change the [_alarmType]'s device awake value to the opposite one
     */
    fun changeAlarmTypeDeviceAwake() {
        _alarmType.value = _alarmType.value.changeWakeupType()
        Log.d(TAG, "Alarm type changed to ${if(_alarmType.value.isWakeup) "WAKEUP" else "NORMAL"}")
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
    fun startInexactAlarm() {
        // TODO Add logs

        // Cancel active alarm if there is any
        cancelAlarm()

        // Create a PendingIntent for the alarm
        val alarmIntent: PendingIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        }

        // If the Alarm type is ELAPSED_TIME
        if(alarmType.value.isElapsedTime) {

            // TODO Add logs

            val selectedAlarmType = if(_alarmType.value.isWakeup) AlarmManager.ELAPSED_REALTIME_WAKEUP else AlarmManager.ELAPSED_REALTIME

            // Start the correct alarm based in the exact alarm invoke type
            when(_inexactAlarmInvokeType.value) {
                InexactAlarmsInvokeType.NORMAL -> {
                    alarmManager.set(
                        /* type = */ selectedAlarmType,
                        /* triggerAtMillis = */ alarmTargetTimeMiliseconds.value,
                        /* operation = */ alarmIntent
                    )
                }
                InexactAlarmsInvokeType.REPEATING -> {
                    alarmManager.setInexactRepeating(
                        /* type = */ selectedAlarmType,
                        /* triggerAtMillis = */ alarmTargetTimeMiliseconds.value,
                        /* intervalMillis = */ AlarmManager.INTERVAL_HALF_HOUR,
                        /* operation = */ alarmIntent
                    )
                }
                InexactAlarmsInvokeType.ALLOW_WHILE_IDLE -> {
                    alarmManager.setAndAllowWhileIdle(
                        /* type = */ selectedAlarmType,
                        /* triggerAtMillis = */ alarmTargetTimeMiliseconds.value,
                        /* operation = */ alarmIntent
                    )
                }
                InexactAlarmsInvokeType.WINDOW -> {
                    alarmManager.setWindow(
                        /* type = */ selectedAlarmType,
                        /* windowStartMillis = */ alarmTargetTimeMiliseconds.value,
                        /* windowLengthMillis = */ alarmTargetTimeWindowLenghtMilliseconds.value,
                        /* operation = */ alarmIntent
                    )
                }
            }
        } // Else (is RTC)
        else {
            // TODO Complete this section

            val selectedAlarmType = if(_alarmType.value.isWakeup) AlarmManager.RTC_WAKEUP else AlarmManager.RTC

            // Transform the alarm's target milliseconds into a Calendar
            val rtcCalendar = Calendar.getInstance()
            rtcCalendar.timeInMillis = alarmTargetTimeMiliseconds.value

            // Transform the alarm's target window milliseconds into a Calendar
            val rtcCalendarWindowMilliseconds = Calendar.getInstance()
            rtcCalendarWindowMilliseconds.timeInMillis = alarmTargetTimeWindowLenghtMilliseconds.value

            // Start the correct alarm based in the exact alarm invoke type
            when(_inexactAlarmInvokeType.value) {
                InexactAlarmsInvokeType.NORMAL -> {

                }
                InexactAlarmsInvokeType.REPEATING -> {

                }
                InexactAlarmsInvokeType.ALLOW_WHILE_IDLE -> {

                }
                InexactAlarmsInvokeType.WINDOW -> {

                }
            }
        }

        // Verfy if there is an alarm running
        verifyAlarmsNotActive()
    }

    /**
     * Starts a new exact alarm with the current values. If one is already running, it will be canceled
     */
    fun startExactAlarm() {
        // TODO Add logs

        // Cancel active alarm if there is any
        cancelAlarm()

        // Create a PendingIntent for the alarm
        val alarmIntent: PendingIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        }

        // If the Alarm type is ELAPSED_TIME
        if(alarmType.value.isElapsedTime) {
            // TODO Add logs

            val selectedAlarmType = if(_alarmType.value.isWakeup) AlarmManager.ELAPSED_REALTIME_WAKEUP else AlarmManager.ELAPSED_REALTIME

            // Start the correct alarm based in the inexact alarm invoke type
            when(_exactAlarmInvokeType.value) {
                ExactAlarmsInvokeType.NORMAL -> {
                    alarmManager.setExact(
                        selectedAlarmType,
                        alarmTargetTimeMiliseconds.value,
                        alarmIntent
                    )
                }
                ExactAlarmsInvokeType.REPEATING -> {
                    alarmManager.setRepeating(
                        /* type = */ selectedAlarmType,
                        /* triggerAtMillis = */ alarmTargetTimeMiliseconds.value,
                        /* intervalMillis = */ AlarmManager.INTERVAL_HALF_HOUR,
                        /* operation = */ alarmIntent
                    )
                }
                ExactAlarmsInvokeType.ALLOW_WHILE_IDLE -> {
                    alarmManager.setExactAndAllowWhileIdle(
                        /* type = */ selectedAlarmType,
                        /* triggerAtMillis = */ alarmTargetTimeMiliseconds.value,
                        /* operation = */ alarmIntent
                    )
                }
                ExactAlarmsInvokeType.ALARM_CLOCK -> {
                    alarmManager.setAlarmClock(
                        /* info = */ AlarmManager.AlarmClockInfo(
                            /* triggerTime = */ alarmTargetTimeMiliseconds.value,
                            /* showIntent = */ alarmIntent
                        ),
                        /* operation = */ alarmIntent
                    )
                }
            }
        } // Else (is RTC)
        else {
            // TODO Complete this section

            val selectedAlarmType = if(_alarmType.value.isWakeup) AlarmManager.RTC_WAKEUP else AlarmManager.RTC

            // Transform the alarm's target milliseconds into a Calendar
            val rtcCalendar = Calendar.getInstance()
            rtcCalendar.timeInMillis = alarmTargetTimeMiliseconds.value

            // Transform the alarm's target window milliseconds into a Calendar
            val rtcCalendarWindowMilliseconds = Calendar.getInstance()
            rtcCalendarWindowMilliseconds.timeInMillis = alarmTargetTimeWindowLenghtMilliseconds.value

            // Start the correct alarm based in the inexact alarm invoke type
            when(_exactAlarmInvokeType.value) {
                ExactAlarmsInvokeType.NORMAL -> {

                }
                ExactAlarmsInvokeType.REPEATING -> {

                }
                ExactAlarmsInvokeType.ALLOW_WHILE_IDLE -> {

                }
                ExactAlarmsInvokeType.ALARM_CLOCK -> {

                }
            }
        }

        // Verfy if there is an alarm running
        verifyAlarmsNotActive()
    }
}


/*
    Helper enum classes that represent the invoke alarm types
 */
// Invoke types for exact alarms
internal enum class ExactAlarmsInvokeType(val functionName: String) {
    NORMAL("setExact()"),
    REPEATING("setRepeating"),
    ALLOW_WHILE_IDLE("setExactAndAllowWhileIdle()"),
    ALARM_CLOCK("setAlarmClock()")
}

// Invoke types for inexact alarms
internal enum class InexactAlarmsInvokeType(val functionName: String) {
    NORMAL("set()"),
    REPEATING("setInexactRepeating()"),
    ALLOW_WHILE_IDLE("setAndAllowWhileIdle()"),
    WINDOW("setWindow()")
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