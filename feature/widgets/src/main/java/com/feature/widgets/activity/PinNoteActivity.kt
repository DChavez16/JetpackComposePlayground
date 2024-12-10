@file:OptIn(ExperimentalFoundationApi::class)

package com.feature.widgets.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.model.Note
import com.example.model.fakeNotesList
import com.example.notes.NoteRepository
import com.example.ui.theme.AppTheme
import com.example.ui.theme.PreviewAppTheme
import com.feature.widgets.R
import com.feature.widgets.receiver.UpdatePinnedNoteIdBroadcastReceiver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okio.IOException
import javax.inject.Inject

@AndroidEntryPoint
class PinNoteActivity : ComponentActivity() {

    @Inject
    lateinit var notesRepository: NoteRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get the widget id (as int) from the intent
        val glanceWidgetId = intent.getIntExtra("GLANCE_ID_INT_KEY", -1)

        setContent {

            // Define coroutine scope
            val coroutineScope = rememberCoroutineScope()

            // Start the pinNoteUiState as Loading
            var pinNoteUiState = remember {
                MutableStateFlow<PinNoteUiState>(PinNoteUiState.Loading)
            }

            // Notes recollection
            LaunchedEffect(Unit) {
                coroutineScope.launch(Dispatchers.IO) {
                    try {
                        // Set the pinNoteUiState as Loading
                        pinNoteUiState.value = PinNoteUiState.Loading

                        // Retreive the note with the pinnedId from the repository and set pinNoteUiState as Success
                        pinNoteUiState.value = PinNoteUiState.Success(notesRepository.getNotes())
                    } catch (e: IOException) {
                        // Set the pinNoteUiState as ConnectionError with the error message
                        pinNoteUiState.value = PinNoteUiState.ConnectionError(e.message.toString())
                    } catch (e: Exception) {
                        // Set the pinNoteUiState as ConnectionError with the error message
                        pinNoteUiState.value = PinNoteUiState.ConnectionError(e.message.toString())
                    }
                }
            }

            AppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    PinNoteContent(
                        pinNoteUiState = pinNoteUiState.collectAsState().value,
                        onNoteSelected = { newPinnedNoteId ->
                            val intent =
                                Intent(UpdatePinnedNoteIdBroadcastReceiver.UPDATE_PINNED_NOTE_ID).apply {
                                    putExtra("new_pinned_note_id", -1)
                                    putExtra("widget_id", glanceWidgetId)
                                }

                            sendBroadcast(intent)
                        },
                        innerPadding = innerPadding
                    )
                }
            }
        }
    }
}


@Composable
private fun PinNoteContent(
    pinNoteUiState: PinNoteUiState,
    onNoteSelected: (Long) -> Unit,
    innerPadding: PaddingValues
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(
                color = MaterialTheme.colorScheme.background
            )
    ) {
        when (pinNoteUiState) {
            is PinNoteUiState.Loading -> PinNoteLoadingScreen()
            is PinNoteUiState.ConnectionError -> PinNoteErrorScreen(
                errorMessage = pinNoteUiState.errorMessage
            )

            is PinNoteUiState.Success -> PinNoteSuccessScreen(
                notes = pinNoteUiState.noteList,
                onNoteSelected = onNoteSelected
            )
        }
    }
}


// Loading screen
@Composable
private fun PinNoteLoadingScreen() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        // Progress indicator
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .size(48.dp)
        )

        // Loading notes text
        Text(
            text = stringResource(R.string.pin_note_activity_loading_label),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}


// Error screen
@Composable
private fun PinNoteErrorScreen(
    errorMessage: String
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        // Error icon
        Icon(
            imageVector = Icons.Rounded.Error,
            tint = MaterialTheme.colorScheme.error,
            contentDescription = null,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .size(48.dp)
        )

        // Error message
        Text(
            text = stringResource(R.string.pin_note_activity_error_message, errorMessage),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}


// Success screen
@Composable
private fun PinNoteSuccessScreen(
    notes: List<Note>,
    onNoteSelected: (Long) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        // Pin note title
        stickyHeader(
            key = 0
        ) {
            Text(
                text = stringResource(R.string.pin_note_activity_success_title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Retreived notes
        items(
            items = notes,
            key = { note -> note.id }
        ) { note ->
            NoteListItem(
                note = note,
                onNoteClicked = onNoteSelected
            )
        }
    }
}


@Composable
private fun NoteListItem(
    note: Note,
    onNoteClicked: (Long) -> Unit,
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        ),
        modifier = Modifier
            .clickable { onNoteClicked(note.id) }
            .fillMaxWidth()
            .height(112.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(16.dp)
        ) {
            // Note title
            Text(
                text = note.title,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Note content
            Text(
                text = note.body,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


// Helper UI state
private sealed interface PinNoteUiState {
    object Loading : PinNoteUiState
    data class ConnectionError(val errorMessage: String) : PinNoteUiState
    data class Success(val noteList: List<Note>) : PinNoteUiState
}


// Previews
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PinNoteLoadingPreview() {
    PreviewAppTheme {
        PinNoteContent(
            onNoteSelected = {},
            pinNoteUiState = PinNoteUiState.Loading,
            innerPadding = PaddingValues(16.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PinNoteErrorPreview() {
    PreviewAppTheme {
        PinNoteContent(
            onNoteSelected = {},
            pinNoteUiState = PinNoteUiState.ConnectionError("Connection error"),
            innerPadding = PaddingValues(16.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PinNoteSuccessPreview() {
    PreviewAppTheme {
        PinNoteContent(
            onNoteSelected = {},
            pinNoteUiState = PinNoteUiState.Success(fakeNotesList),
            innerPadding = PaddingValues(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NoteListItemPreview() {
    PreviewAppTheme {
        NoteListItem(
            note = fakeNotesList[0],
            onNoteClicked = {}
        )
    }
}