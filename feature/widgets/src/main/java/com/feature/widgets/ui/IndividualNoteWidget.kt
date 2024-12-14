@file:OptIn(ExperimentalGlancePreviewApi::class)
@file:Suppress("unused")

package com.feature.widgets.ui

import android.content.Context
import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.components.CircleIconButton
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.color.ColorProvider
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.preview.ExperimentalGlancePreviewApi
import androidx.glance.preview.Preview
import androidx.glance.text.FontStyle
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.example.model.Note
import com.example.model.fakeNotesList
import com.feature.widgets.R
import com.feature.widgets.activity.PinNoteActivity
import com.feature.widgets.hiltEntryPoint.WidgetsEntryPoint
import com.feature.widgets.receiver.IndividualNoteReceiver
import com.feature.widgets.ui.IndividualNoteWidgetUiState.ConnectionError
import com.feature.widgets.ui.IndividualNoteWidgetUiState.Loading
import com.feature.widgets.ui.IndividualNoteWidgetUiState.NoPinnedNote
import com.feature.widgets.ui.IndividualNoteWidgetUiState.NoteNotFound
import com.feature.widgets.ui.IndividualNoteWidgetUiState.Success
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okio.IOException

// - NOTE RECOLLECTION USE CASE
// Get pinned note id from preferences
//  If the pinned id is null (-1), display 'no pinned note screen'
//  Else Try to get the note with the pinned id from the repository, display 'loading screen' in the meantime
//      If note collection failed, display 'connection error screen', show button to retry connection (aka, update widget method)
//      Else display 'success screen' if the note exists, or 'note no longer exists screen' otherwise

// - NOTE PINNING USE CASE
// If the pinned note id is -1 ('no pinned note screen' is displayed), show button to pin a note
// Else, if 'success screen is displayed', show button to change pinned note alongside the current pinned note, start 'PinNoteActivity'; there, use 'updatePinnedNote action'

private const val TAG = "IndividualNoteWidget"

class IndividualNoteWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {

        Log.i(TAG, "AllNotesWidget started")

        // Hilt entry point
        val notesEntryPoint = EntryPointAccessors
            .fromApplication(context.applicationContext, WidgetsEntryPoint::class.java)

        // Obtain a NoteRepository instance from the entry point
        val noteRepository = notesEntryPoint.getNoteRepository()

        provideContent {
            // Define coroutine scope
            val coroutineScope = rememberCoroutineScope()

            // Get the preferences from the current widget
            val prefs = currentState<Preferences>()

            // Get the pinned note id from the preferences, set -1 if null
            val pinnedNoteId = prefs[IndividualNoteReceiver.PINNED_NOTE_ID] ?: -1

            // Start the notesUiState as Loading
            var notesUiState = remember {
                MutableStateFlow<IndividualNoteWidgetUiState>(Loading)
            }

            // Pinned note recollection
            LaunchedEffect(pinnedNoteId) {
                coroutineScope.launch(Dispatchers.IO) {
                    // If the pinned note is -1
                    if (pinnedNoteId.toInt() == -1) {
                        // Set the notesUiState as NoPinnedNote
                        notesUiState.value = NoPinnedNote
                        Log.i(TAG, "No pinned note id saved")
                    } else {
                        // Attempt to collect the note with the pinnedNoteId from the repository
                        try {
                            Log.i(TAG, "Recollecting note with the pinned note id ($pinnedNoteId)")
                            // Set the notesUiState as Loading
                            notesUiState.value = Loading

                            // Retreive the note with the pinnedId from the repository
                            val retreivedNote: Note? =
                                noteRepository.getNotes().find { it.id == pinnedNoteId.toLong() }

                            // If the retreived note is null
                            notesUiState.value = if (retreivedNote == null) {
                                Log.i(TAG, "The note with the pinned note id no longer exists")
                                // Set the notesUiState as NoNoteFound
                                NoteNotFound
                            } else {
                                Log.i(
                                    TAG,
                                    "Note with the pinned note id found and succesfully retreived"
                                )
                                // set the notesUiState as Success with the retreived note
                                Success(retreivedNote)
                            }
                        } catch (e: IOException) {
                            Log.e(TAG, "IOException error: ${e.message}")
                            // Set the notesUiState as ConnectionError with the error message
                            notesUiState.value =
                                ConnectionError(e.message.toString())
                        } catch (e: Exception) {
                            Log.e(TAG, "Exception error: ${e.message}")
                            // Set the notesUiState as ConnectionError with the error message
                            notesUiState.value =
                                ConnectionError(e.message.toString())
                        }
                    }
                }
            }

            // Individual note Widget content
            IndividualNoteWidgetContent(
                noteUiState = notesUiState.collectAsState().value,
                glanceId = GlanceAppWidgetManager(context).getAppWidgetId(id),
                updateWidget = {
                    coroutineScope.launch {
                        IndividualNoteWidget().update(context, id)
                    }
                }
            )
        }
    }
}


@Composable
private fun IndividualNoteWidgetContent(
    noteUiState: IndividualNoteWidgetUiState,
    glanceId: Int,
    updateWidget: () -> Unit = {}
) {

    // Action parameter key to pair with glanceId
    val glanceIdKey = ActionParameters.Key<Int>("GLANCE_ID_INT_KEY")

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(color = Color(red = 255, green = 227, blue = 120))
    ) {
        // Widget header if the state isn't NoPinnedNote
        if (noteUiState !is NoPinnedNote) {
            Row(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .background(color = Color(red = 249, green = 208, blue = 59))
            ) {
                CircleIconButton(
                    imageProvider = ImageProvider(R.drawable.thumb_tack_2_plain),
                    contentDescription = null,
                    onClick = actionStartActivity<PinNoteActivity>(actionParametersOf(glanceIdKey to glanceId)),
                    enabled = noteUiState is Success,
                    backgroundColor = null,
                    contentColor = ColorProvider(day = Color.Black, night = Color.Black),
                    modifier = GlanceModifier.height(24.dp)
                )
            }
        }

        // Content
        when (noteUiState) {
            is Loading -> LoadingScreen()
            is NoPinnedNote -> NoPinnedNoteScreen(glanceId)
            is NoteNotFound -> NoteNotFoundScreen(glanceId)
            is ConnectionError -> ConnectionErrorScreen(noteUiState.errorMessage, updateWidget)
            is Success -> SuccessScreen(glanceId, noteUiState.note)
        }
    }
}

@Composable
private fun NoPinnedNoteScreen(
    glanceId: Int
) {

    // Action parameter key to pair with glanceId
    val glanceIdKey = ActionParameters.Key<Int>("GLANCE_ID_INT_KEY")

    Column(
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = GlanceModifier
            .background(color = Color(red = 191, green = 170, blue = 90))
            .fillMaxSize()
    ) {
        Text(
            text = glanceStringResource(R.string.individual_note_widget_no_pinned_note_label),
            style = TextStyle(
                color = ColorProvider(day = Color.Black, night = Color.Black)
            ),
            modifier = GlanceModifier.padding(8.dp)
        )

        CircleIconButton(
            imageProvider = ImageProvider(R.drawable.baseline_push_pin),
            onClick = actionStartActivity<PinNoteActivity>(actionParametersOf(glanceIdKey to glanceId)),
            backgroundColor = GlanceTheme.colors.primary,
            contentColor = GlanceTheme.colors.onPrimary,
            contentDescription = glanceStringResource(R.string.individual_note_widget_pin_note_button_accessibility)
        )
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = GlanceModifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.25f))
    ) {
        CircularProgressIndicator(
            color = ColorProvider(day = Color.Gray, night = Color.Gray)
        )
    }
}

@Composable
private fun NoteNotFoundScreen(
    glanceId: Int
) {

    // Action parameter key to pair with glanceId
    val glanceIdKey = ActionParameters.Key<Int>("GLANCE_ID_INT_KEY")

    Column(
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = GlanceModifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.25f))
    ) {
        Text(
            text = glanceStringResource(R.string.individual_note_widget_note_not_found_label),
            style = TextStyle(
                color = ColorProvider(day = Color.Black, night = Color.Black)
            ),
            modifier = GlanceModifier.padding(8.dp)
        )

        CircleIconButton(
            imageProvider = ImageProvider(R.drawable.baseline_push_pin),
            onClick = actionStartActivity<PinNoteActivity>(actionParametersOf(glanceIdKey to glanceId)),
            backgroundColor = GlanceTheme.colors.primary,
            contentColor = GlanceTheme.colors.onPrimary,
            contentDescription = glanceStringResource(R.string.individual_note_widget_pin_note_button_accessibility)
        )
    }
}

@Composable
private fun ConnectionErrorScreen(
    errorMessage: String,
    retryConnection: () -> Unit
) {
    Column(
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = GlanceModifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.25f))
    ) {
        Text(
            text = "${glanceStringResource(R.string.individual_note_widget_connection_error_label)} $errorMessage",
            style = TextStyle(
                color = ColorProvider(day = Color.Black, night = Color.Black)
            ),
            maxLines = 2,
            modifier = GlanceModifier.padding(8.dp)
        )

        CircleIconButton(
            imageProvider = ImageProvider(R.drawable.baseline_cached),
            onClick = retryConnection,
            backgroundColor = GlanceTheme.colors.primary,
            contentColor = GlanceTheme.colors.onPrimary,
            contentDescription = glanceStringResource(R.string.individual_note_widget_retry_connection_button_accessibility)
        )
    }
}

@Composable
private fun SuccessScreen(
    glanceId: Int,
    note: Note
) {
    Text(
        text = note.title,
        style = TextStyle(
            fontSize = 28.sp,
            fontStyle = FontStyle.Italic,
            color = ColorProvider(day = Color.Black, night = Color.Black)
        ),
        modifier = GlanceModifier.fillMaxSize().padding(12.dp)
    )
}


// Helper UI State
private sealed interface IndividualNoteWidgetUiState {
    data class Success(val note: Note) : IndividualNoteWidgetUiState
    data class ConnectionError(val errorMessage: String) : IndividualNoteWidgetUiState
    data object NoPinnedNote : IndividualNoteWidgetUiState
    data object NoteNotFound : IndividualNoteWidgetUiState
    data object Loading : IndividualNoteWidgetUiState
}


@Preview(widthDp = 200, heightDp = 200)
@Composable
private fun NoPinnedNoteScreenPreview() {
    GlanceTheme {
        IndividualNoteWidgetContent(
            noteUiState = NoPinnedNote,
            glanceId = 1
        )
    }
}

@Preview(widthDp = 200, heightDp = 200)
@Composable
private fun LoadingScreenPreview() {
    GlanceTheme {
        IndividualNoteWidgetContent(
            noteUiState = Loading,
            glanceId = 1
        )
    }
}

@Preview(widthDp = 200, heightDp = 200)
@Composable
private fun NoteNotFoundScreenPreview() {
    GlanceTheme {
        IndividualNoteWidgetContent(
            noteUiState = NoteNotFound,
            glanceId = 1
        )
    }
}

@Preview(widthDp = 200, heightDp = 200)
@Composable
private fun ConnectionErrorScreenPreview() {
    GlanceTheme {
        IndividualNoteWidgetContent(
            noteUiState = ConnectionError("Connection error"),
            glanceId = 1
        )
    }
}

@Preview(widthDp = 200, heightDp = 200)
@Composable
private fun SuccessScreenPreview() {
    GlanceTheme {
        IndividualNoteWidgetContent(
            noteUiState = Success(fakeNotesList[0]),
            glanceId = 1
        )
    }
}


// Helper functions
@Composable
private fun glanceStringResource(@StringRes id: Int): String {
    val context = LocalContext.current
    return context.getString(id)
}