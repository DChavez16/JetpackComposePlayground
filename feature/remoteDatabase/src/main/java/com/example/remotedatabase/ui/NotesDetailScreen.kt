package com.example.remotedatabase.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Discount
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.model.Note
import com.example.model.UserTag
import com.example.model.fakeNotesList
import com.example.remotedatabase.R
import com.example.ui.theme.PreviewAppTheme
import com.example.ui.ui.CompactSizeScreenThemePreview


@Composable
internal fun NotesDetailScreen(
    noteToEdit: Note,
    onMainButtonClick: (Note) -> Unit
) {
    var editableNoteTags by rememberSaveable { mutableStateOf(noteToEdit.userTags) }
}


@Composable
private fun NotesDetailScreenContent(
    noteToEdit: Note,
    noteTags: List<UserTag>,
    onNoteTagsIconButtonClicked: () -> Unit,
    onMainButtonClick: (String, String) -> Unit
) {

    var editableNoteTitle by rememberSaveable { mutableStateOf(noteToEdit.title) }
    var editableNoteBody by rememberSaveable { mutableStateOf(noteToEdit.body) }

    // Box filling all space
    // Column filling all space
    // Call to NoteTitleAndTags composable
    // Call to NoteBody composable
    // Centered tertiary FAB at the bottom
}


@Composable
private fun NoteTitleAndTags(
    noteTitle: String,
    onNoteTitleChange: (String) -> Unit,
    noteTags: List<UserTag>,
    onNoteTagsIconButtonClicked: () -> Unit
) {
    Column {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Note title text field
            TextField(
                value = noteTitle,
                onValueChange = onNoteTitleChange,
                placeholder = {
                    Text(
                        text = "Note Title",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                textStyle = MaterialTheme.typography.headlineSmall,
                singleLine = true,
                colors = TextFieldDefaults.colors().copy(
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.5f
                    ),
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.5f
                    )
                ),
                modifier = Modifier.weight(1f)
            )

            // Note tags row
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // If the note user tags list is note empty
                if (noteTags.isNotEmpty()) {
                    // Tag text from the first tag in the note user tags list
                    Text(
                        text = noteTags[0].tagText,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // If the number of tags in the note user tags list is greater than 1
                    if (noteTags.size > 1) {
                        val surfaceVariantColor = MaterialTheme.colorScheme.surfaceVariant

                        // Sets a bubble with the number of the rest of the tags in the list
                        Text(
                            text = "+${noteTags.size - 1}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.drawBehind {
                                drawCircle(
                                    color = surfaceVariantColor,
                                    radius = 25f
                                )
                            }
                        )
                    }
                }

                // Note tags icon button
                Icon(
                    imageVector = Icons.Default.Discount,
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = stringResource(R.string.remote_database_notes_detail_add_tag),
                    modifier = Modifier.size(16.dp).clickable { onNoteTagsIconButtonClicked() }
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outline
        )
    }
}


@Composable
private fun NoteBody(
    noteBody: String,
    onNoteBodyChange: (String) -> Unit
) {

}


/*
Previews
 */
@CompactSizeScreenThemePreview
@Composable
private fun NoteTitleAndTagsPreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            // With no Tags
            NoteTitleAndTags(
                noteTitle = fakeNotesList[3].title,
//                noteTitle = "",
                onNoteTitleChange = {},
                noteTags = fakeNotesList[3].userTags,
                onNoteTagsIconButtonClicked = {}
            )

            // With one tag
            NoteTitleAndTags(
                noteTitle = fakeNotesList[1].title,
                onNoteTitleChange = {},
                noteTags = fakeNotesList[1].userTags,
                onNoteTagsIconButtonClicked = {}
            )

            // With multiple tags
            NoteTitleAndTags(
                noteTitle = fakeNotesList[2].title,
                onNoteTitleChange = {},
                noteTags = fakeNotesList[2].userTags,
                onNoteTagsIconButtonClicked = {}
            )
        }
    }
}


@CompactSizeScreenThemePreview
@Composable
private fun NoteBodyPreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {

    }
}


@CompactSizeScreenThemePreview
@Composable
private fun NotesDetailScreenContentPreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {

    }
}