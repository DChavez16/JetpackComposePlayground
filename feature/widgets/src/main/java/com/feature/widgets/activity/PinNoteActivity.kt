package com.feature.widgets.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.model.Note
import com.example.model.fakeNotesList
import com.example.ui.theme.AppTheme
import com.example.ui.theme.PreviewAppTheme
import com.feature.widgets.receiver.UpdatePinnedNoteIdBroadcastReceiver

class PinNoteActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val glanceWidgetId = intent.getIntExtra("GLANCE_ID_INT_KEY", -1)

        setContent {
            AppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PinNoteContent(
                        onNoteSelected = { newPinnedNoteId ->
                            val intent =
                                Intent(UpdatePinnedNoteIdBroadcastReceiver.UPDATE_PINNED_NOTE_ID).apply {
                                    putExtra("new_pinned_note_id", -1)
                                    putExtra("widget_id", glanceWidgetId)
                                }

                            sendBroadcast(intent)
                        },
                        noteList = emptyList(),
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


@Composable
private fun PinNoteContent(
    onNoteSelected: (Long) -> Unit,
    noteList: List<Note>,
    modifier: Modifier = Modifier
) {

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
        modifier = Modifier.clickable { onNoteClicked(note.id) }.fillMaxWidth().height(112.dp)
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


/// Previews
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
            noteList = fakeNotesList
        )
    }
}