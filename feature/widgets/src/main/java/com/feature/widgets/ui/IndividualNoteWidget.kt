package com.feature.widgets.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.text.Text


class IndividualNoteWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {

        provideContent {
            IndividualNoteWidgetContent()
        }
    }
}


@Composable
private fun IndividualNoteWidgetContent(

) {
    Text("Individual Note Widget")
}