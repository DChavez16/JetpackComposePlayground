package com.feature.widgets.receiver

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.feature.widgets.ui.IndividualNoteWidget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


//https://stackoverflow.com/questions/71381707/how-to-create-an-instance-of-room-dao-or-repository-or-viewmodel-in-glanceappwid/72168589#72168589
//&
//https://stackoverflow.com/questions/75511282/display-data-from-database-room-in-widget-glance-using-jetpack-compose

private const val TAG = "IndividualNoteReceiver"

// Receiver for the 'IndividualNoteWidget'
class IndividualNoteReceiver : GlanceAppWidgetReceiver() {

    // IndividualNoteWidget instance
    override val glanceAppWidget: GlanceAppWidget = IndividualNoteWidget()

    companion object {
        // Preferences keys
        val PINNED_NOTE_ID = longPreferencesKey("selected_note_id")

        // Intent actions
        const val UPDATE_INDIVIDUAL_NOTE_WIDGET = "updateIndividualNoteWidget"
    }

    // Define a coroutine scope
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    // Callback when the Widget is updated
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    // Receive actions
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        when (intent.action) {
            // Update the pinned note id of the specified Individual Note Widget
            UpdatePinnedNoteIdBroadcastReceiver.UPDATE_PINNED_NOTE_ID -> {
                Log.i(TAG, "Intent received with action: ${intent.action}")

                // Retreving the parameters from the intent
                val newPinnedNoteId = intent.getLongExtra("new_pinned_note_id", -1)
                val widgetIdInt = intent.getIntExtra("widget_id_int", -1)

                Log.i(TAG, "Pinning note with id $newPinnedNoteId to widget with id: $widgetIdInt")

                // Update the pinned note id in the Widget parameters
                updatePinnedNoteId(
                    context = context,
                    widgetIdInt = widgetIdInt,
                    newPinnedNoteId = newPinnedNoteId
                )
            }

            // Plain update of the specified Individual Note Widget
            UPDATE_INDIVIDUAL_NOTE_WIDGET -> {
                Log.i(TAG, "Reloading Widget from the composition error screen")

                // Retreiving the parameters of the intent
                val widgetIdInt = intent.getIntExtra("widget_id_int", -1)

                // Get widget to update id
                val widgetId = GlanceAppWidgetManager(context).getGlanceIdBy(widgetIdInt)

                // Updates the specified Individual Note Widget
                Log.i(TAG, "Updating widget with id: $widgetIdInt")
                updateIndividualNoteWidget(context = context, widgetId = widgetId)
            }
        }
    }

    // Update the pinned note id in the given's Widget parameters
    private fun updatePinnedNoteId(
        context: Context,
        widgetIdInt: Int,
        newPinnedNoteId: Long,
    ) {
        coroutineScope.launch {
            // Get widget to update id
            val widgetId = GlanceAppWidgetManager(context).getGlanceIdBy(widgetIdInt)

            // Update the pinned note id in the Widget parameters
            updateAppWidgetState(context, PreferencesGlanceStateDefinition, widgetId) { prefs ->
                Log.i(
                    TAG,
                    "Updating parameters of the widget with id: $widgetIdInt, setting new pinned note id: $newPinnedNoteId"
                )
                prefs.toMutablePreferences().apply {
                    this[PINNED_NOTE_ID] = newPinnedNoteId
                }
            }

            // Updates the specified Individual Note Widget
            Log.i(TAG, "Updating widget with id: $widgetIdInt")
            updateIndividualNoteWidget(context = context, widgetId = widgetId)
        }
    }


    // Update the Individual Note Widget
    private fun updateIndividualNoteWidget(
        context: Context,
        widgetId: GlanceId
    ) {
        coroutineScope.launch {
            // Refresh the widget
            IndividualNoteWidget().update(context, widgetId)
        }
    }
}

class UpdatePinnedNoteIdBroadcastReceiver : BroadcastReceiver() {

    companion object {
        const val UPDATE_PINNED_NOTE_ID = "updatePinnedNoteId"
        private const val TAG = "UpdatePinnedNoteIdBroadcastReceiver"
    }

    override fun onReceive(context: Context?, intent: Intent?) {

        Log.i(TAG, "Broadcast received with action: ${intent?.action}")

        if (intent?.action == UPDATE_PINNED_NOTE_ID) {
            // Get the new pinned note id and the widget id from the intent
            val newPinnedNoteId = intent.getLongExtra("new_pinned_note_id", -1)
            val widgetId = intent.getIntExtra("widget_id_int", -1)

            Log.i(TAG, "Pinning note with id $newPinnedNoteId to widget with id: $widgetId")

            // Creating intent for the 'IndividualNoteReceiver' and setting the parameters
            val widgetReceiverIntent = Intent(context, IndividualNoteReceiver::class.java).apply {
                action = UPDATE_PINNED_NOTE_ID
                putExtra("new_pinned_note_id", newPinnedNoteId)
                putExtra("widget_id_int", widgetId)
            }

            // Sending broadcast to the 'IndividualNoteReceiver'
            context?.sendBroadcast(widgetReceiverIntent)
        }
    }
}