package com.feature.widgets.ui

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.color.ColorProvider
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import com.example.model.Note
import com.feature.widgets.HiltEntryPoint.WidgetsEntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.MutableStateFlow

private const val TAG = "IndividualNoteWidget"

class IndividualNoteWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {

        Log.i(TAG, "AllNotesWidget started")

        val notesEntryPoint = EntryPointAccessors.fromApplication(
            context.applicationContext, WidgetsEntryPoint::class.java,
        )

        provideContent {
            val coroutineScope = rememberCoroutineScope()
            val noteRepository = notesEntryPoint.getNoteRepository()
            var notesUiState = remember {
                MutableStateFlow<IndividualNoteWidgetUiState>(IndividualNoteWidgetUiState.Loading)
            }

            // TODO Add in note repository a way to store a single note id using DataStore
            // TODO OR
            // TODO Store it in the widgets preferences

            IndividualNoteWidgetContent(
                notesUiState = notesUiState.collectAsState().value,
            )
        }
    }
}


@Composable
private fun IndividualNoteWidgetContent(
    notesUiState: IndividualNoteWidgetUiState
) {
    when (notesUiState) {
        is IndividualNoteWidgetUiState.Loading -> WidgetLoadingContent()
        is IndividualNoteWidgetUiState.Error -> WidgetErrorContent(
            errorMessage = notesUiState.errorMessage
        )

        is IndividualNoteWidgetUiState.Success -> WidgetSuccessContent(
            note = notesUiState.notes
        )
    }
}

@Composable
private fun WidgetLoadingContent(

) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = GlanceModifier
            .background(color = Color.Yellow)
            .fillMaxSize()
    ) {
        Text(
            text = "Loading...",
            style = TextStyle(
                color = ColorProvider(day = Color.Black, night = Color.Black)
            )
        )
    }
}

@Composable
private fun WidgetErrorContent(
    errorMessage: String
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = GlanceModifier
            .background(color = Color.Red)
            .fillMaxSize()
    ) {
        Text(
            text = "Error!!\n$errorMessage",
            style = TextStyle(
                color = ColorProvider(day = Color.White, night = Color.White),
                textAlign = TextAlign.Center
            )
        )
    }
}

@Composable
fun WidgetSuccessContent(note: Note) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = GlanceModifier
            .background(color = Color.Green)
            .fillMaxSize()
    ) {
        Text(
            text = "Success!!\nNote title: ${note.title}",
            style = TextStyle(
                color = ColorProvider(day = Color.Black, night = Color.Black)
            )
        )
    }
}


// Helper UI State
private sealed interface IndividualNoteWidgetUiState {
    data class Success(val notes: Note) : IndividualNoteWidgetUiState
    data class Error(val errorMessage: String) : IndividualNoteWidgetUiState
    data object Loading : IndividualNoteWidgetUiState
}