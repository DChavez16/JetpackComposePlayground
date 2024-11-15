package com.feature.widgets.receiver

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.feature.widgets.ui.AllNotesWidget
import com.feature.widgets.ui.IndividualNoteWidget


// Receiver for the 'IndividualNoteWidget'
class IndividualNoteReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = IndividualNoteWidget()
}


// Receiver for the 'AllNotesWidget'
class AllNotesReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = AllNotesWidget()
}