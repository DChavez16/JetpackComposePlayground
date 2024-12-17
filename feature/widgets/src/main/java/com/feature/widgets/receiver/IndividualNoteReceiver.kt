package com.feature.widgets.receiver

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.example.notes.RemoteNoteRepository
import com.feature.widgets.ui.AllNotesWidget
import com.feature.widgets.ui.IndividualNoteWidget
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


//https://stackoverflow.com/questions/71381707/how-to-create-an-instance-of-room-dao-or-repository-or-viewmodel-in-glanceappwid/72168589#72168589
//&
//https://stackoverflow.com/questions/75511282/display-data-from-database-room-in-widget-glance-using-jetpack-compose

private const val TAG = "IndividualNoteReceiver"

// Receiver for the 'IndividualNoteWidget'
@AndroidEntryPoint
class IndividualNoteReceiver : GlanceAppWidgetReceiver() {

    // IndividualNoteWidget instance
    override val glanceAppWidget: GlanceAppWidget = IndividualNoteWidget()

    // Preferences keys
    companion object {
        val PINNED_NOTE_ID = longPreferencesKey("selected_note_id")
    }

    // Inject NoteRepository
    @Inject
    lateinit var noteRepository: RemoteNoteRepository

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

        if (intent.action == UpdatePinnedNoteIdBroadcastReceiver.UPDATE_PINNED_NOTE_ID) {
            Log.i(TAG, "Intent received with action: ${intent.action}")

            // Retreving the parameters from the intent
            val newPinnedNoteId = intent.getLongExtra("new_pinned_note_id", -1)
            val widgetId = intent.getIntExtra("widget_id_int", -1)

            Log.i(TAG, "Pinning note with id $newPinnedNoteId to widget with id: $widgetId")

            // Update the pinned note id in the Widget parameters
            updatePinnedNoteId(
                context = context,
                widgetIdInt = widgetId,
                newPinnedNoteId = newPinnedNoteId
            )
        }
    }

    // Update the pinned note id in the given's Widget parameters
    fun updatePinnedNoteId(
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

            Log.i(TAG, "Updating widget with id: $widgetIdInt")

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