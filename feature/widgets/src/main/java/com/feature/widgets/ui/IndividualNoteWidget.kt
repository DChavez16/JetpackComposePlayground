@file:OptIn(ExperimentalGlancePreviewApi::class)

package com.feature.widgets.ui

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.Preferences
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.ImageProvider
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.components.CircleIconButton
import androidx.glance.appwidget.components.SquareIconButton
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
import androidx.glance.layout.size
import androidx.glance.preview.ExperimentalGlancePreviewApi
import androidx.glance.preview.Preview
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.example.model.Note
import com.example.model.fakeNotesList
import com.feature.widgets.HiltEntryPoint.WidgetsEntryPoint
import com.feature.widgets.R
import com.feature.widgets.activity.PinNoteActivity
import com.feature.widgets.receiver.IndividualNoteReceiver
import com.feature.widgets.ui.IndividualNoteWidgetUiState.ConnectionError
import com.feature.widgets.ui.IndividualNoteWidgetUiState.Loading
import com.feature.widgets.ui.IndividualNoteWidgetUiState.NoPinnedNote
import com.feature.widgets.ui.IndividualNoteWidgetUiState.Success
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okio.IOException

// - NOTE RECOLLECTION USE CASE
// Get pinned note id from preferences
//  If the pinned id is -1, display 'no pinned note screen'
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
            val pinnedNoteId = prefs[IndividualNoteReceiver.PINNED_NOTE_ID] ?: 0

            // Start the notesUiState as Loading
            var notesUiState = remember {
                MutableStateFlow<IndividualNoteWidgetUiState>(Loading)
            }

            // Pinned note recollection
            LaunchedEffect(Unit) {
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
                                // Set the notesUiState as NoPinnedNote
                                NoPinnedNote
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
                pinnedNoteId = pinnedNoteId,
                noteUiState = notesUiState.collectAsState().value,
                glanceId = GlanceAppWidgetManager(context).getAppWidgetId(id)
            )
        }
    }
}


@Composable
private fun IndividualNoteWidgetContent(
    pinnedNoteId: Long,
    noteUiState: IndividualNoteWidgetUiState,
    glanceId: Int
) {
    if (pinnedNoteId.toInt() == -1) NoPinnedNoteScreen(glanceId = glanceId)
    else Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(color = Color(red = 255, green = 227, blue = 120))
    ) {
        // Widget header
        Row(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = GlanceModifier
                .fillMaxWidth()
                .background(color = Color(red = 249, green = 208, blue = 59))
        ) {
            Text(
                text = "Individual Note Widget",
                style = TextStyle(
                    color = ColorProvider(day = Color.White, night = Color.White)
                )
            )
        }

        // Content
        when (noteUiState) {
            is Loading -> LoadingScreen()
            is NoPinnedNote -> NoteNotFoundScreen()
            is ConnectionError -> ConnectionErrorScreen(noteUiState.errorMessage)
            is Success -> SuccessScreen(noteUiState.note)
        }
    }
}

@Composable
private fun NoPinnedNoteScreen(
    glanceId: Int
) {

    val glanceIdKey = ActionParameters.Key<Int>("GLANCE_ID_INT_KEY")

    Column(
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = GlanceModifier
            .background(color = Color.Gray)
            .fillMaxSize()
    ) {
        Text(
            text = "No pinned note id saved",
            style = TextStyle(
                color = ColorProvider(day = Color.Black, night = Color.Black)
            )
        )

        Button(
            text = "Pin note",
            onClick = {
                actionStartActivity<PinNoteActivity>(
                    actionParametersOf(glanceIdKey to glanceId)
                )
            }
        )
    }
}

@Composable
private fun LoadingScreen() {
    Column(
        modifier = GlanceModifier.fillMaxSize().background(Color.Black.copy(alpha = 0.25f))
    ) {
        Row(
            horizontalAlignment = Alignment.End,
            modifier = GlanceModifier
                .fillMaxWidth()
        ) {
            CircleIconButton(
                imageProvider = ImageProvider(R.drawable.baseline_arrow_drop_down),
                contentDescription = null,
                onClick = { /*TODO*/ },
                enabled = false,
                backgroundColor = null,
                contentColor = ColorProvider(day = Color.Black, night = Color.Black),
                modifier = GlanceModifier.size(width = 30.dp, height = 24.dp)
            )
        }
    }
}

@Composable
private fun NoteNotFoundScreen() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = GlanceModifier
            .fillMaxSize()
    ) {
        Text(
            text = "Note not found",
            style = TextStyle(
                color = ColorProvider(day = Color.Black, night = Color.Black)
            )
        )
    }
}

@Composable
private fun ConnectionErrorScreen(errorMessage: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = GlanceModifier
            .fillMaxSize()
    ) {
        Text(
            text = "Error: $errorMessage",
            style = TextStyle(
                color = ColorProvider(day = Color.Black, night = Color.Black)
            )
        )
    }
}

@Composable
private fun SuccessScreen(note: Note) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = GlanceModifier
            .fillMaxSize()
    ) {
        Text(
            text = "Note title: ${note.title}",
            style = TextStyle(
                color = ColorProvider(day = Color.Black, night = Color.Black)
            )
        )
    }
}


// Helper UI State
private sealed interface IndividualNoteWidgetUiState {
    data class Success(val note: Note) : IndividualNoteWidgetUiState
    data class ConnectionError(val errorMessage: String) : IndividualNoteWidgetUiState
    data object NoPinnedNote : IndividualNoteWidgetUiState
    data object Loading : IndividualNoteWidgetUiState
}


@Preview(widthDp = 200, heightDp = 200)
@Composable
private fun NoPinnedNoteScreenPreview() {
    GlanceTheme {
        IndividualNoteWidgetContent(
            pinnedNoteId = -1,
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
            pinnedNoteId = 1,
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
            pinnedNoteId = 1,
            noteUiState = NoPinnedNote,
            glanceId = 1
        )
    }
}

@Preview(widthDp = 200, heightDp = 200)
@Composable
private fun ConnectionErrorScreenPreview() {
    GlanceTheme {
        IndividualNoteWidgetContent(
            pinnedNoteId = 1,
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
            pinnedNoteId = 1,
            noteUiState = Success(fakeNotesList[0]),
            glanceId = 1
        )
    }
}