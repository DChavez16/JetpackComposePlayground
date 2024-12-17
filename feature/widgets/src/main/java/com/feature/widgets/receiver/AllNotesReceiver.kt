package com.feature.widgets.receiver

import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.feature.widgets.ui.AllNotesWidget

class AllNotesReceiver: GlanceAppWidgetReceiver() {
    override val glanceAppWidget = AllNotesWidget()
}