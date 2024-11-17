package com.feature.widgets.ui

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.updateAll
import androidx.glance.background
import androidx.glance.color.ColorProvider
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import com.example.model.Note
import com.example.notes.RemoteNoteRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.IOException


class AllNotesWidget() : GlanceAppWidget() {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface RepositoryEntryPoint {
        fun getRepository(): RemoteNoteRepository
    }

    override suspend fun provideGlance(context: Context, id: GlanceId) {

        Log.i("AllNotesWidget", "AllNotesWidget started")

        val notesEntryPoint = EntryPointAccessors.fromApplication(
            context.applicationContext, RepositoryEntryPoint::class.java,
        )

        provideContent {
            val coroutineScope = rememberCoroutineScope()
            val noteRepository = notesEntryPoint.getRepository()
            var notesUiState =
                remember { MutableStateFlow<AllNotesWidgetUiState>(AllNotesWidgetUiState.Loading) }

            LaunchedEffect(Unit) {
                coroutineScope.launch {
                    try {
                        notesUiState.value = AllNotesWidgetUiState.Loading
                        Log.i("AllNotesWidget", "Retreiving notes")
                        notesUiState.value =
                            AllNotesWidgetUiState.Success(noteRepository.getNotes())
                        Log.i("AllNotesWidget", "Notes succesfully retrieved")
                    } catch (e: IOException) {
                        notesUiState.value = AllNotesWidgetUiState.Error(e.message.toString())
                        Log.e("AllNotesWidget", "IO Exception error: ${e.message}")
                    } catch (e: Exception) {
                        notesUiState.value = AllNotesWidgetUiState.Error(e.message.toString())
                        Log.e("AllNotesWidget", "Exception error: ${e.message}")
                    }
                }
            }

            AllNotesWidgetContent(
                notesUiState = notesUiState.collectAsState().value,
                retryButtonClicked = {
                    coroutineScope.launch {
                        Log.d("AllNotesWidget", "retryButtonClicked")
                        AllNotesWidget().updateAll(context)
                    }
                }
            )
        }
    }
}


@Composable
private fun AllNotesWidgetContent(
    notesUiState: AllNotesWidgetUiState,
    retryButtonClicked: () -> Unit
) {
    when (notesUiState) {
        is AllNotesWidgetUiState.Loading -> WidgetLoadingContent(retryButtonClicked)
        is AllNotesWidgetUiState.Error -> WidgetErrorContent(
            errorMessage = notesUiState.errorMessage,
            retryButtonClicked = retryButtonClicked
        )

        is AllNotesWidgetUiState.Success -> WidgetSuccessContent(notesUiState.notes, retryButtonClicked)
    }
}

@Composable
private fun WidgetLoadingContent(
    retryButtonClicked: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = GlanceModifier
            .background(color = Color.Yellow)
            .fillMaxSize()
            .clickable(retryButtonClicked)
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
private fun WidgetErrorContent(errorMessage: String, retryButtonClicked: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = GlanceModifier
            .background(color = Color.Red)
            .fillMaxSize()
            .clickable(retryButtonClicked)
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
private fun WidgetSuccessContent(notesList: List<Note>, retryButtonClicked: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = GlanceModifier
            .background(color = Color.Green)
            .fillMaxSize()
            .clickable(retryButtonClicked)
    ) {
        Text(
            text = "Success!!\nItems: ${notesList.size}",
            style = TextStyle(
                color = ColorProvider(day = Color.Black, night = Color.Black)
            )
        )
    }
}


private sealed interface AllNotesWidgetUiState {
    data class Success(val notes: List<Note>) : AllNotesWidgetUiState
    data class Error(val errorMessage: String) : AllNotesWidgetUiState
    data object Loading : AllNotesWidgetUiState
}