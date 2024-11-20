package com.feature.widgets.ui

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.glance.background
import androidx.glance.color.ColorProvider
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import com.feature.widgets.activity.PinNoteActivity
import com.feature.widgets.receiver.IndividualNoteReceiver
import com.feature.widgets.receiver.IndividualNoteRefreshCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Thread.sleep

private const val TAG = "IndividualNoteWidget"

class IndividualNoteWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {

        Log.i(TAG, "AllNotesWidget started")

        provideContent {
            val coroutineScope = rememberCoroutineScope()

            val prefs = currentState<Preferences>()
            val noteTitle = prefs[IndividualNoteReceiver.PINNED_NOTE_TITLE]
            Log.d(TAG, "Note title: $noteTitle")

            IndividualNoteWidgetContent(
                noteTitle = noteTitle,
                updatePinnedNote = {
                    coroutineScope.launch(Dispatchers.IO) {
                        updateAppWidgetState(
                            context,
                            PreferencesGlanceStateDefinition,
                            id
                        ) { prefs ->
                            prefs.toMutablePreferences().apply {
                                this[IndividualNoteReceiver.PINNED_NOTE_ID] =
                                    this[IndividualNoteReceiver.PINNED_NOTE_ID]?.plus(1) ?: -1
                            }
                        }
                    }
                }
            )
        }
    }
}


@Composable
private fun IndividualNoteWidgetContent(
    noteTitle: String?,
    updatePinnedNote: () -> Unit
) {
    when (noteTitle) {
        "" -> WidgetNoPinnedNoteContent()
        null -> WidgetConnectionErrorContent()
        else -> WidgetSuccessContent(noteTitle, updatePinnedNote)
    }
}

@Composable
private fun WidgetConnectionErrorContent(updatePinnedNote: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = GlanceModifier
            .background(color = Color.Red)
            .fillMaxSize()
            .clickable(actionRunCallback<IndividualNoteRefreshCallback>())
    ) {
        Text(
            text = "Connection error",
            style = TextStyle(
                color = ColorProvider(day = Color.White, night = Color.White),
                textAlign = TextAlign.Center
            )
        )
    }
}

@Composable
private fun WidgetNoPinnedNoteContent() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = GlanceModifier
            .background(color = Color.Red)
            .fillMaxSize()
            .clickable(actionStartActivity<PinNoteActivity>())
    ) {
        Text(
            text = "No pinned note",
            style = TextStyle(
                color = ColorProvider(day = Color.White, night = Color.White),
                textAlign = TextAlign.Center
            )
        )
    }
}

@Composable
fun WidgetSuccessContent(
    noteTitle: String,
    onUpdatePinnedNoteClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = GlanceModifier
            .background(color = Color.Green)
            .fillMaxSize()
            .clickable(onUpdatePinnedNoteClick)
    ) {
        Text(
            text = "Success!!\nNote title: $noteTitle",
            style = TextStyle(
                color = ColorProvider(day = Color.Black, night = Color.Black)
            )
        )
    }
}