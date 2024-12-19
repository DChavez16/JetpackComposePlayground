package com.feature.widgets.ui

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import com.example.model.Note
import com.feature.widgets.hiltEntryPoint.WidgetsEntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

private const val TAG = "AllNotesWidget"

class AllNotesWidget() : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {

        Log.i(TAG, "AllNotesWidget started")

        // Hilt entry point
        val notesEntryPoint = EntryPointAccessors
            .fromApplication(context.applicationContext, WidgetsEntryPoint::class.java)

        // Obtain a NoteRepository instance from the entry point
        val noteRepository = notesEntryPoint.getNoteRepository()

        provideContent {
            // Define the coroutine scope
            val coroutineScope = rememberCoroutineScope()

            // Start the notesUiState as Loading
            var notesUiState = remember {
                MutableStateFlow<AllNotesWidgetUiState>(AllNotesWidgetUiState.Loading)
            }

            // Initiallize the last update time
            var lastUpdated: Long = -1

            // Update Widget flag
            var updateWidgetFlag = false

            // Notes recollection
            LaunchedEffect(updateWidgetFlag) {
                coroutineScope.launch(Dispatchers.IO) {
                    // Attempt to collect the notes from the repository
                    try {
                        Log.i(TAG, "Recollecting notes from the repository")
                        // Set the notesUiState as Loading
                        notesUiState.value = AllNotesWidgetUiState.Loading

                        // Retreive the notes from the repository
                        notesUiState.value =
                            AllNotesWidgetUiState.Success(noteRepository.getNotes())

                        Log.i(TAG, "Notes succesfully retrieved")

                        // TODO Obtain the current time in Long
                    } catch (e: IOException) {
                        Log.e(TAG, "IO Exception error: ${e.message}")
                        // Set the notesUiState as Error with the error message
                        notesUiState.value = AllNotesWidgetUiState.Error(e.message.toString())
                    } catch (e: Exception) {
                        Log.e(TAG, "Exception error: ${e.message}")
                        notesUiState.value = AllNotesWidgetUiState.Error(e.message.toString())
                    }
                }
            }

            AllNotesWidgetContent(
                notesUiState = notesUiState.collectAsState().value,
                lastUpdated = lastUpdated,
                updateNotesList = { newUpdateTime ->
                    coroutineScope.launch() {
                        Log.i(TAG, "Updating the list of notes...")

                        updateWidgetFlag = !updateWidgetFlag
                    }
                }
            )
        }
    }
}


@Composable
private fun AllNotesWidgetContent(
    notesUiState: AllNotesWidgetUiState,
    lastUpdated: Long,
    updateNotesList: (Long) -> Unit
) {

}


// Helper UI State
private sealed interface AllNotesWidgetUiState {
    data class Success(val notes: List<Note>) : AllNotesWidgetUiState
    data class Error(val errorMessage: String) : AllNotesWidgetUiState
    data object Loading : AllNotesWidgetUiState
}