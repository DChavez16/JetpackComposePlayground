package com.feature.widgets.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.text.Text


class AllNotesWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {

        provideContent {
            AllNotesWidgetContent()
        }
    }
}


@Composable
private fun AllNotesWidgetContent(

) {
    Text("All Notes Widget")
}