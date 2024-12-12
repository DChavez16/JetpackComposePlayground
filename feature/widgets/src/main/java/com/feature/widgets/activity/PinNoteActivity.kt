@file:OptIn(ExperimentalFoundationApi::class)

package com.feature.widgets.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material.icons.rounded.Replay
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.model.Note
import com.example.model.fakeNotesList
import com.example.notes.RemoteNoteRepository
import com.example.ui.theme.AppTheme
import com.example.ui.theme.PreviewAppTheme
import com.example.ui.ui.CompactSizeScreenThemePreview
import com.feature.widgets.R
import com.feature.widgets.receiver.UpdatePinnedNoteIdBroadcastReceiver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okio.IOException
import javax.inject.Inject

private const val TAG = "PinNoteActivityTag"

@AndroidEntryPoint
class PinNoteActivity : ComponentActivity() {

    @Inject
    lateinit var notesRepository: RemoteNoteRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i(TAG, "PinNoteActivity created")

        // Get the widget id (as int) from the intent
        val glanceWidgetId = intent.getIntExtra("GLANCE_ID_INT_KEY", -1)

        setContent {
            // Define coroutine scope
            val coroutineScope = rememberCoroutineScope()

            // Start the pinNoteUiState as Loading
            var pinNoteUiState = remember {
                MutableStateFlow<PinNoteUiState>(PinNoteUiState.Loading)
            }

            // Flag used as a key to recompose the LaunchedEffect that retreives the notes from the repository
            var retryConnectionFlag = remember { mutableStateOf(false) }

            // Notes recollection
            LaunchedEffect(retryConnectionFlag.value) {
                coroutineScope.launch(Dispatchers.IO) {
                    try {
                        Log.i(TAG, "Retreiving notes from the repository")
                        // Set the pinNoteUiState as Loading
                        pinNoteUiState.value = PinNoteUiState.Loading

                        // Retreive the note with the pinnedId from the repository and set pinNoteUiState as Success
                        pinNoteUiState.value = PinNoteUiState.Success(notesRepository.getNotes())
                        Log.i(TAG, "Notes successfully retreived")
                    } catch (e: IOException) {
                        // Set the pinNoteUiState as ConnectionError with the error message
                        pinNoteUiState.value = PinNoteUiState.ConnectionError(e.message.toString())
                        Log.e(TAG, "IOException error: ${e.message}")
                    } catch (e: Exception) {
                        // Set the pinNoteUiState as ConnectionError with the error message
                        pinNoteUiState.value = PinNoteUiState.ConnectionError(e.message.toString())
                        Log.e(TAG, "Exception error: ${e.message}")
                    }
                }
            }

            val isDarkTheme = isSystemInDarkTheme()

            AppTheme(
                isDarkTheme = { isDarkTheme }
            ) {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    PinNoteContent(
                        pinNoteUiState = pinNoteUiState.collectAsState().value,
                        onNoteSelected = { newPinnedNoteId ->
                            Log.i(
                                TAG,
                                "Pinning note with id $newPinnedNoteId to widget with id $glanceWidgetId"
                            )

                            val intent = Intent(
                                this,
                                UpdatePinnedNoteIdBroadcastReceiver::class.java
                            ).apply {
                                action =
                                    UpdatePinnedNoteIdBroadcastReceiver.UPDATE_PINNED_NOTE_ID
                                putExtra("new_pinned_note_id", newPinnedNoteId)
                                putExtra("widget_id_int", glanceWidgetId)
                            }

                            // TODO Force the widget update
                            // TODO End this activity after the broadcast is sent

                            this.sendBroadcast(intent)
                        },
                        innerPadding = innerPadding,
                        retryConnection = {
                            retryConnectionFlag.value = !retryConnectionFlag.value
                        }
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
    innerPadding: PaddingValues,
    retryConnection: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.background
            )
            .padding(innerPadding)
    ) {
        when (pinNoteUiState) {
            is PinNoteUiState.Loading -> PinNoteLoadingScreen()
            is PinNoteUiState.ConnectionError -> PinNoteErrorScreen(
                errorMessage = pinNoteUiState.errorMessage,
                retryConnection = retryConnection
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
    errorMessage: String,
    retryConnection: () -> Unit
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
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        // Retry button
        IconButton(
            onClick = retryConnection,
            colors = IconButtonDefaults.iconButtonColors().copy(
                contentColor = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier.padding(top = 12.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Replay,
                contentDescription = stringResource(R.string.pin_note_activity_error_retry_connection_accessibility)
            )
        }
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
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Pin note title
        stickyHeader(
            key = 0
        ) {
            Text(
                text = stringResource(R.string.pin_note_activity_success_title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 8.dp)
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
@CompactSizeScreenThemePreview
@Composable
fun PinNoteLoadingPreview() {
    PreviewAppTheme(darkTheme = isSystemInDarkTheme()) {
        PinNoteContent(
            onNoteSelected = {},
            pinNoteUiState = PinNoteUiState.Loading,
            innerPadding = PaddingValues(0.dp)
        )
    }
}

@CompactSizeScreenThemePreview
@Composable
fun PinNoteErrorPreview() {
    PreviewAppTheme(darkTheme = isSystemInDarkTheme()) {
        PinNoteContent(
            onNoteSelected = {},
            pinNoteUiState = PinNoteUiState.ConnectionError("Connection error"),
            innerPadding = PaddingValues(0.dp)
        )
    }
}

@CompactSizeScreenThemePreview
@Composable
fun PinNoteSuccessPreview() {
    PreviewAppTheme(darkTheme = isSystemInDarkTheme()) {
        PinNoteContent(
            onNoteSelected = {},
            pinNoteUiState = PinNoteUiState.Success(fakeNotesList),
            innerPadding = PaddingValues(0.dp)
        )
    }
}

@Preview
@Composable
fun NoteListItemPreview() {
    PreviewAppTheme {
        NoteListItem(
            note = fakeNotesList[0],
            onNoteClicked = {}
        )
    }
}