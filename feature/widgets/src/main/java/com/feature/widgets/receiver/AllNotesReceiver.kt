package com.feature.widgets.receiver

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.feature.widgets.ui.AllNotesWidget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


//https://stackoverflow.com/questions/71381707/how-to-create-an-instance-of-room-dao-or-repository-or-viewmodel-in-glanceappwid/72168589#72168589
//&
//https://stackoverflow.com/questions/75511282/display-data-from-database-room-in-widget-glance-using-jetpack-compose

private const val TAG = "AllNotesReceiver"

class AllNotesReceiver: GlanceAppWidgetReceiver() {

    // AllNotesWidget instance
    override val glanceAppWidget = AllNotesWidget()

    // Preferences keys
    companion object {
        val WIDGET_UPDATE_FLAG_KEY = booleanPreferencesKey("widget_update_flag")
        const val UPDATE_WIDGET_FLAG_ACTION = "update_widget_flag"
    }

    // Define a coroutine scope
    val coroutineScope = CoroutineScope(Dispatchers.IO)

    // Receive actions
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        // If the action is to update the widget flag
        if(intent.action == UPDATE_WIDGET_FLAG_ACTION) {
            Log.i(TAG, "Intent received with action: ${intent.action}")

            // Retreiving the parameters from the intent
            val widgetId = intent.getIntExtra("widget_id_int", -1)

            Log.i(TAG, "Updating widget with id: $widgetId")

            // Update the widget flag
            triggerWidgetUpdate(
                context = context,
                widgetIdInt = widgetId
            )
        }
    }

    // Trigger flag for widget update
    private fun triggerWidgetUpdate(context: Context, widgetIdInt: Int) {
        coroutineScope.launch {
            // Get widget to update id
            val widgetId = GlanceAppWidgetManager(context).getGlanceIdBy(widgetIdInt)

            // Update the widget flag
            updateAppWidgetState(context, PreferencesGlanceStateDefinition, widgetId) { prefs ->
                Log.i(TAG, "Updating parameters for widget with id: $widgetIdInt")

                prefs.toMutablePreferences().apply {
                    this[WIDGET_UPDATE_FLAG_KEY] = this[WIDGET_UPDATE_FLAG_KEY]?.not() != false
                }
            }

            Log.i(TAG, "Updating widget with id: $widgetIdInt")

            // Refresh the widget
            AllNotesWidget().update(context, widgetId)
        }
    }
}