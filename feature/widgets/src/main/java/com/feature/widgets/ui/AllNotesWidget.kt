@file:OptIn(ExperimentalGlancePreviewApi::class)

package com.feature.widgets.ui

import android.content.Context
import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.ColorFilter
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.components.CircleIconButton
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.color.ColorProvider
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.preview.ExperimentalGlancePreviewApi
import androidx.glance.preview.Preview
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import com.example.model.Note
import com.feature.widgets.R
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
            var lastUpdated = remember { mutableLongStateOf(-1) }

            // Notes recollection
            LaunchedEffect(lastUpdated) {
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
                lastUpdated = lastUpdated.longValue,
                updateNotesList = {
                    coroutineScope.launch(Dispatchers.IO) {
                        Log.i(TAG, "Updating the list of notes...")

                        // Obtain the current time in millis
                        val newUpdateTime = System.currentTimeMillis()

                        // Update the lastUpdated value
                        lastUpdated.longValue = newUpdateTime
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
    updateNotesList: () -> Unit
) {
    // If is Error
    if (notesUiState is AllNotesWidgetUiState.Error) {
        ErrorScreen(
            errorMessage = notesUiState.errorMessage,
            updateNotesList = updateNotesList
        )
    }
    // Else, is Loading or Success
    else {
        /* TODO Add a Box with a Column and another Box (overlay) inside (both fit the available space)
        Column with two elements
            List of notes which fit all the available space
            Row that:
                If uiState is Success:
                    show the last time the list was updated AND an icon button to update the list
                Else uiState is Loading:
                    Shows a circular progress indicator

        Box that adds a dark background when uiState is Loading

         * The list should keep showing the elements even when the widget is updating (uiState as Loading)
         ** When uiState is Loading, add an overlay that obscures the Widget
         */
    }
}


@Composable
private fun ErrorScreen(
    errorMessage: String,
    updateNotesList: () -> Unit
) {
    Column(
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = GlanceModifier
            .fillMaxSize()
            .background(color = Color(red = 191, green = 170, blue = 90))
    ) {
        // Error icon
        Image(
            provider = ImageProvider(R.drawable.baseline_error),
            contentDescription = null,
            colorFilter = ColorFilter.tint(ColorProvider(day = Color.Red, night = Color.Red)),
            modifier = GlanceModifier.size(36.dp)
        )

        // Error message
        Text(
            text = glanceStringResource(
                R.string.all_notes_widget_error_message_label,
                errorMessage
            ),
            style = TextStyle(
                fontSize = 12.sp,
                color = ColorProvider(day = Color.Black, night = Color.Black),
                textAlign = TextAlign.Center
            ),
            modifier = GlanceModifier.padding(8.dp)
        )

        // Retry button
        CircleIconButton(
            imageProvider = ImageProvider(R.drawable.baseline_cached),
            onClick = updateNotesList,
            backgroundColor = GlanceTheme.colors.primary,
            contentColor = GlanceTheme.colors.onPrimary,
            contentDescription = glanceStringResource(R.string.all_notes_widget_error_retry_connection_button_accessibility)
        )
    }
}


// Helper UI State
private sealed interface AllNotesWidgetUiState {
    data class Success(val notes: List<Note>) : AllNotesWidgetUiState
    data class Error(val errorMessage: String) : AllNotesWidgetUiState
    data object Loading : AllNotesWidgetUiState
}


// Previews
@Preview(widthDp = 180, heightDp = 240)
@Composable
private fun ErrorScreenPreview() {
    GlanceTheme {
        AllNotesWidgetContent(
            notesUiState = AllNotesWidgetUiState.Error("Error message"),
            lastUpdated = -1,
            updateNotesList = {}
        )
    }
}


// Helper functions
@Composable
private fun glanceStringResource(@StringRes id: Int, formatArgs: String = ""): String {
    val context = LocalContext.current
    return context.getString(id, formatArgs)
}