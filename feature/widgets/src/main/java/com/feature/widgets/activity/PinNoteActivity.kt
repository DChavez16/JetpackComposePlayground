@file:OptIn(ExperimentalFoundationApi::class)

package com.feature.widgets.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
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
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = innerPadding,
        modifier = Modifier.fillMaxSize()
    ) {
        stickyHeader(
            key = 0
        ) {
            Text(
                text = stringResource(R.string.pin_note_activity_title),
                style = MaterialTheme.typography.displaySmall
            )
        }

//        items(
//            items = pinNoteUiState,
//            key = { note -> note.id }
//        ) { note ->
//            NoteListItem(
//                note = note,
//                onNoteClicked = onNoteSelected
//            )
//        }
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PinNoteContentPreview() {
    PreviewAppTheme {
        PinNoteContent(
            onNoteSelected = {},
            pinNoteUiState = PinNoteUiState.Success(fakeNotesList),
            innerPadding = PaddingValues(16.dp)
        )
    }
}