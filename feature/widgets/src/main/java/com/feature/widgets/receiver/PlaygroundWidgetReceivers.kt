package com.feature.widgets.receiver

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.ActionCallback
import com.example.notes.RemoteNoteRepository
import com.feature.widgets.ui.AllNotesWidget
import com.feature.widgets.ui.IndividualNoteWidget
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        if(intent.action == UpdatePinnedNoteIdCallback.UPDATE_PINNED_NOTE_ID) {
            // Update the pinned note id in the Widget parameters
            updatePinnedNoteId(
                context,
                intent.getIntExtra("widget_id_int", -1),
                intent.getLongExtra("new_pinned_note_id", -1)
            )
        }
    }

    fun updatePinnedNoteId(
        context: Context,
        widgetIdInt: Int,
        newPinnedNoteId: Long,
    ) {
        // Update the pinned note id in the Widget parameters
        

        // Refresh the widget

    }
}

class UpdatePinnedNoteIdCallback : ActionCallback {

    companion object {
        const val UPDATE_PINNED_NOTE_ID = "updatePinnedNoteId"
    }

    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {

        val intent = Intent(context, IndividualNoteReceiver::class.java).apply {
            action = UPDATE_PINNED_NOTE_ID
        }

        intent.putExtra("new_pinned_note_id", parameters[ActionParameters.Key<Long>("new_pinned_note_id")])
        intent.putExtra("widget_id_int", parameters[ActionParameters.Key<Int>("widget_id_int")])

        context.sendBroadcast(intent)
    }
}


// Receiver for the 'AllNotesWidget'
class AllNotesReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = AllNotesWidget()
}