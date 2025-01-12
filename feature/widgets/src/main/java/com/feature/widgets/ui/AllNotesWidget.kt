@file:OptIn(ExperimentalGlancePreviewApi::class)
@file:Suppress("unused")

package com.feature.widgets.ui

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
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
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.action.actionSendBroadcast
import androidx.glance.appwidget.components.CircleIconButton
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.color.ColorProvider
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.preview.ExperimentalGlancePreviewApi
import androidx.glance.preview.Preview
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import com.example.model.Note
import com.example.model.fakeNotesList
import com.feature.widgets.R
import com.feature.widgets.hiltEntryPoint.WidgetsEntryPoint
import com.feature.widgets.receiver.AllNotesReceiver
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Calendar

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

            // Initialize the update flag
            var widgetUpdates = remember { mutableStateOf(false) }

            // TODO Collect the update widget flag and set it as the Launched effect key

            // Notes recollection
            LaunchedEffect(widgetUpdates) {
                coroutineScope.launch(Dispatchers.IO) {
                    // Attempt to collect the notes from the repository
                    try {
                        Log.i(TAG, "Recollecting notes from the repository")
                        // Set the notesUiState as Loading
                        notesUiState.value = AllNotesWidgetUiState.Loading

                        // Retreive the notes from the repository
                        notesUiState.value =
                            AllNotesWidgetUiState.Success(noteRepository.getNotes())

                        // Obtain the current time in millis
                        val newUpdateTime = System.currentTimeMillis()

                        // Update the lastUpdated value
                        lastUpdated.longValue = newUpdateTime

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
                widgetIdInt = GlanceAppWidgetManager(context).getAppWidgetId(id)
            )
        }
    }
}


@Composable
private fun AllNotesWidgetContent(
    notesUiState: AllNotesWidgetUiState,
    lastUpdated: Long,
    widgetIdInt: Int = -1,
) {
    // If is Error
    if (notesUiState is AllNotesWidgetUiState.Error) {
        ErrorScreen(
            errorMessage = notesUiState.errorMessage,
            widgetIdInt = widgetIdInt
        )
    }
    // Else, is Loading or Success
    else {
        // Obtain the list of notes from the uiState and store them in cache, if uiState is not Success, do nothing
        var cachedNotes = remember { mutableStateOf(emptyList<Note>()) }
        try {
            cachedNotes.value = (notesUiState as AllNotesWidgetUiState.Success).notes
        } catch (e: Exception) {
        }

        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(color = Color(red = 255, green = 227, blue = 120))
        ) {
            // Success and Loading content
            Column(
                modifier = GlanceModifier.fillMaxSize().padding(8.dp)
            ) {
                // List of notes
                NotesList(
                    // Using the cached notes, ensures the last list of notes will always be showed in the Widget, even if the uiState is Loading
                    noteList = cachedNotes.value,
                    modifier = GlanceModifier.fillMaxSize().defaultWeight()
                )

                // Bottom row
                BottomRow(
                    isLoading = notesUiState is AllNotesWidgetUiState.Loading,
                    lastUpdateTime = lastUpdated,
                    widgetIdInt = widgetIdInt,
                    modifier = GlanceModifier.fillMaxWidth()
                )
            }

            // I the uiState is Loading, show an overlay that obscures the widget's screen
            if (notesUiState is AllNotesWidgetUiState.Loading) {
                Box(
                    content = {},
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.25f))
                )
            }
        }
    }
}

@Composable
private fun NotesList(
    noteList: List<Note>,
    modifier: GlanceModifier = GlanceModifier
) {
    // If the list of notes is empty, show a text indicating it
    if (noteList.isEmpty()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
        ) {
            Text(
                text = glanceStringResource(R.string.all_notes_widget_success_no_notes_label),
                style = TextStyle(
                    fontSize = 12.sp,
                    color = ColorProvider(day = Color.Black, night = Color.Black)
                ),
                modifier = GlanceModifier.padding(horizontal = 8.dp)
            )
        }
    }
    // Else display the list of notes
    else {
        LazyColumn(
            modifier = modifier
        ) {
            items(
                items = noteList,
                itemId = { note -> note.id },
            ) { note ->
                NoteElement(note = note)
            }
        }
    }
}

@Composable
private fun NoteElement(
    note: Note
) {
    Column(
        modifier = GlanceModifier.fillMaxWidth().padding(2.dp)
    ) {
        Text(
            text = note.title,
            style = TextStyle(
                fontSize = 14.sp,
                color = ColorProvider(day = Color.Black, night = Color.Black)
            ),
            maxLines = 1,
            modifier = GlanceModifier.fillMaxWidth().padding(horizontal = 4.dp)
        )

        Box(
            content = {},
            modifier = GlanceModifier
                .fillMaxWidth()
                .background(Color.Black)
                .height(1.dp)
        )
    }
}

@Composable
private fun BottomRow(
    isLoading: Boolean = true,
    lastUpdateTime: Long = -1,
    widgetIdInt: Int = -1,
    modifier: GlanceModifier = GlanceModifier
) {
    // Create instance of Calendar
    val calendar: Calendar = Calendar.getInstance()
    calendar.timeInMillis = lastUpdateTime

    Row(
        horizontalAlignment = Alignment.End,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(4.dp)
    ) {
        // If isLoading is false, display the last updated time and a button to reload
        if (!isLoading) {
            // Last updated time
            Text(
                text = glanceStringResource(
                    R.string.all_notes_widget_success_last_updated_label,
                    "${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}"
                ),
                style = TextStyle(
                    fontSize = 14.sp
                )
            )

            Spacer(
                modifier = GlanceModifier.width(8.dp)
            )

            // Reload button
            CircleIconButton(
                imageProvider = ImageProvider(R.drawable.baseline_cached),
                onClick = actionSendBroadcast(
                    intent = Intent().apply {
                        action = AllNotesReceiver.UPDATE_WIDGET_FLAG_ACTION
                        putExtra("widget_id_int", widgetIdInt)
                    }
                ),
                backgroundColor = null,
                contentColor = ColorProvider(day = Color.Black, night = Color.Black),
                contentDescription = glanceStringResource(R.string.all_notes_widget_success_reload_notes_button_accessibility),
                modifier = GlanceModifier.size(18.dp)
            )
        }
        // Else, display a CircularProgressIndicator
        else {
            CircularProgressIndicator(
                color = ColorProvider(day = Color.Gray, night = Color.Gray),
                modifier = GlanceModifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun ErrorScreen(
    errorMessage: String,
    widgetIdInt: Int = -1,
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
            onClick = actionSendBroadcast(
                intent = Intent().apply {
                    action = AllNotesReceiver.UPDATE_WIDGET_FLAG_ACTION
                    putExtra("widget_id_int", widgetIdInt)
                }
            ),
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
@Preview()
@Composable
private fun NoteElementPreview() {
    GlanceTheme {
        NoteElement(
            fakeNotesList[0]
        )
    }
}

@Preview(widthDp = 180, heightDp = 120)
@Composable
private fun NotesListPreview() {
    GlanceTheme {
        NotesList(
            fakeNotesList
        )
    }
}

@Preview()
@Composable
private fun BottomRowLoadingPreview() {
    GlanceTheme {
        BottomRow()
    }
}

@Preview()
@Composable
private fun BottomRowSuccessPreview() {
    GlanceTheme {
        BottomRow(
            isLoading = false,
            lastUpdateTime = System.currentTimeMillis()
        )
    }
}

@Preview(widthDp = 180, heightDp = 240)
@Composable
private fun ErrorScreenPreview() {
    GlanceTheme {
        AllNotesWidgetContent(
            notesUiState = AllNotesWidgetUiState.Error("Error message"),
            lastUpdated = -1
        )
    }
}

@Preview(widthDp = 180, heightDp = 240)
@Composable
private fun LoadingScreenPreview() {
    GlanceTheme {
        AllNotesWidgetContent(
            notesUiState = AllNotesWidgetUiState.Loading,
            lastUpdated = -1
        )
    }
}

@Preview(widthDp = 180, heightDp = 240)
@Composable
private fun SuccessScreenPreview() {
    GlanceTheme {
        AllNotesWidgetContent(
            notesUiState = AllNotesWidgetUiState.Success(fakeNotesList),
            lastUpdated = -1
        )
    }
}


// Helper functions
@Composable
private fun glanceStringResource(@StringRes id: Int, formatArgs: String = ""): String {
    val context = LocalContext.current
    return context.getString(id, formatArgs)
}
