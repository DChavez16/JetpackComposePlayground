package com.feature.widgets.receiver

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.feature.widgets.ui.AllNotesWidget
import com.feature.widgets.ui.IndividualNoteWidget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


//https://stackoverflow.com/questions/71381707/how-to-create-an-instance-of-room-dao-or-repository-or-viewmodel-in-glanceappwid/72168589#72168589
//&
//https://stackoverflow.com/questions/75511282/display-data-from-database-room-in-widget-glance-using-jetpack-compose

private const val TAG = "IndividualNoteReceiver"

class AllNotesReceiver: GlanceAppWidgetReceiver() {

    // AllNotesWidget instance
    override val glanceAppWidget = AllNotesWidget()

    companion object {
        // Preferences keys
        val LAST_UPDATE_LONG = longPreferencesKey("last_update_long")

        // Intent action
        const val UPDATE_LAST_UPDATE_ACTION = "update_last_update_action"

        // Intent parameters
        const val LAST_UPDATE_PARAM = "last_update_param"
        const val WIDGET_ID_PARAM = "widget_id_param"
    }

    // Define a coroutine scope
    val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        if(intent.action == UPDATE_LAST_UPDATE_ACTION) {
            Log.i(TAG, "Intent received with action: ${intent.action}")

            // Retreiving parameters from the intent
            val lastUpdateParam = intent.getLongExtra(LAST_UPDATE_PARAM, -1)
            val widgetId = intent.getIntExtra(WIDGET_ID_PARAM, -1)

            Log.i(TAG, "Updating widget with id: $widgetId at time $lastUpdateParam")

            // Update the last update time in the Widget parameters
            updateLastUpdateTime(
                context = context,
                widgetId = widgetId,
                newLastUpdateTime = lastUpdateParam
            )
        }
    }

    fun updateLastUpdateTime(
        context: Context,
        widgetId: Int,
        newLastUpdateTime: Long
    ) {
        coroutineScope.launch {
            // Get widget to update id
            val widgetId = GlanceAppWidgetManager(context).getGlanceIdBy(widgetId)

            // Update the last update time in the Widget parameters
            updateAppWidgetState(context, PreferencesGlanceStateDefinition, widgetId) { prefs ->
                Log.i(TAG, "Updating parameters of the widget with id: $widgetId, setting new last update time: $newLastUpdateTime")
                prefs.toMutablePreferences().apply {
                    this[LAST_UPDATE_LONG] = newLastUpdateTime
                }
            }

            Log.i(TAG, "Updating widget with id: $widgetId")

            // Refresh the widget
            AllNotesWidget().update(context, widgetId)
        }
    }
}