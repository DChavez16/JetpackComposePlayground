package com.feature.widgets.receiver

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.state.getAppWidgetState
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.example.model.Note
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

// Receiver for the 'IndividualNoteWidget'
@AndroidEntryPoint
class IndividualNoteReceiver : GlanceAppWidgetReceiver() {

    // IndividualNoteWidget instance
    override val glanceAppWidget: GlanceAppWidget = IndividualNoteWidget()

    // Preferences keys
    companion object {
        val PINNED_NOTE_ID = intPreferencesKey("selected_note_id")
        val PINNED_NOTE_TITLE = stringPreferencesKey("selected_note_title")
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

        // Retreive pinned note
        retreivePinnedNote(context)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        // Retreive pinned note
        retreivePinnedNote(context)
    }

    // Function to retreive the pinned note
    fun retreivePinnedNote(context: Context) {
        coroutineScope.launch {
            // Get the id of the widget
            val glanceId =
                GlanceAppWidgetManager(context).getGlanceIds(glanceAppWidget.javaClass).first()
            Log.d("IndividualNoteReceiver", "Retreiving pinned note for widget with id: $glanceId")

            glanceId.let {
                // Get the pinned note id
                val prefs = getAppWidgetState(context, PreferencesGlanceStateDefinition, it)
                val pinnedNoteId = prefs[PINNED_NOTE_ID]
                Log.d("IndividualNoteReceiver", "Pinned note id: $pinnedNoteId")

                // Obtain the note with the given id from the repository
                val note: Note? =
                    noteRepository.getNotes().find { it.id == (pinnedNoteId?.toLong() ?: -1) }
                Log.d("IndividualNoteReceiver", "Retreived note id: ${note?.id ?: "null"}")

                // Sets the note title as the PINNED_NOTE_TITLE in the Widget's preferences
                updateAppWidgetState(context, PreferencesGlanceStateDefinition, it) { prefs ->
                    prefs.toMutablePreferences().apply {
                        this[PINNED_NOTE_TITLE] = note?.title ?: ""
                        Log.d(
                            "IndividualNoteReceiver",
                            "PINNED_NOTE_TITLE preferences changed to ${note?.title ?: ""}"
                        )
                    }
                }
            }
        }
    }
}


// Receiver for the 'AllNotesWidget'
class AllNotesReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = AllNotesWidget()
}