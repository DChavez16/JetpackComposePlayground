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
}