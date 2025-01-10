package com.feature.widgets.receiver

import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.feature.widgets.ui.AllNotesWidget


//https://stackoverflow.com/questions/71381707/how-to-create-an-instance-of-room-dao-or-repository-or-viewmodel-in-glanceappwid/72168589#72168589
//&
//https://stackoverflow.com/questions/75511282/display-data-from-database-room-in-widget-glance-using-jetpack-compose

class AllNotesReceiver: GlanceAppWidgetReceiver() {

    // AllNotesWidget instance
    override val glanceAppWidget = AllNotesWidget()

    // TODO Add receiver to update widget
}