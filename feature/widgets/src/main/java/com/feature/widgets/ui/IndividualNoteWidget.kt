package com.feature.widgets.ui

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
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
import com.feature.widgets.receiver.IndividualNoteReceiver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
                                Log.d(
                                    TAG,
                                    "PINNED_NOTE_ID preferences before changes ${this[IndividualNoteReceiver.PINNED_NOTE_ID]}"
                                )
                                this[IndividualNoteReceiver.PINNED_NOTE_ID] =
                                    this[IndividualNoteReceiver.PINNED_NOTE_ID]?.plus(1) ?: -1
                                Log.d(
                                    TAG,
                                    "PINNED_NOTE_ID preferences changed to ${this[IndividualNoteReceiver.PINNED_NOTE_ID]}"
                                )
                            }
                        }

                        IndividualNoteWidget().updateAll(context)
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
        "" -> WidgetErrorContent("No pinned note", updatePinnedNote)
        null -> WidgetErrorContent("Connection error", updatePinnedNote)
        else -> WidgetSuccessContent(noteTitle, updatePinnedNote)
    }
}

@Composable
private fun WidgetErrorContent(
    errorMessage: String,
    onUpdatePinnedNoteClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = GlanceModifier
            .background(color = Color.Red)
            .fillMaxSize()
            .clickable(onUpdatePinnedNoteClick)
    ) {
        Text(
            text = errorMessage,
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