package com.example.remotedatabase.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.model.Note
import com.example.model.fakeNotesList
import com.example.ui.theme.PreviewAppTheme
import com.example.ui.ui.CompactSizeScreenThemePreview


@Composable
internal fun NotesListScreen(

) {

}


@Composable
private fun NotesListScreenContent(

) {

}


@Composable
private fun NoteScreenItem(
    note: Note,
    showTags: () -> Boolean = { false },
    onNoteClick: (Note) -> Unit,
    isListViewMode: () -> Boolean,
    onTagsClick: (Long) -> Unit = {}
) {

    // Item modifier dependning on the current list view mode
    val itemModifier = if (isListViewMode()) {
        // When in list view mode, fill the width to the container and set a fixed height of 80dp
        Modifier
            .fillMaxWidth()
            .height(80.dp)
    } else {
        // When in grid view mode, set a width with min 144dp and max 164dp, and set a fixed height of 163dp
        Modifier
            .widthIn(min = 144.dp, max = 164.dp)
            .height(163.dp)
    }

    Box(
        modifier = itemModifier
    ) {
        // Note element card
        Card(
            shape = RoundedCornerShape(size = 12.dp),
            colors = CardDefaults.cardColors().copy(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            ),
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = Modifier
                .fillMaxSize()
                .clickable { onNoteClick(note) }
        ) {
            // If the view mode is list view, set the column's vertical arrangement as SpaceBetween, else set space of 4dp between elements
            val itemVerticalArrangement =
                if (isListViewMode()) Arrangement.SpaceBetween
                else Arrangement.spacedBy(4.dp)

            // Column of notes content
            Column(
                verticalArrangement = itemVerticalArrangement,
                modifier = Modifier.fillMaxSize().padding(12.dp)
            ) {
                // Note title
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth()
                )

                // If the view mode IS NOT list view (With grid view enabled), show text body
                if (!isListViewMode()) {
                    Text(
                        text = note.body,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                }

                // Note tags row
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { onTagsClick(note.id) }
                        .align(Alignment.End)
                ) {
                    // If the note user tags list is note empty and showTags is false
                    if (note.userTags.isNotEmpty() && !showTags()) {
                        // Tag text from the first tag in the note user tags list
                        Text(
                            text = note.userTags[0].tagText,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        // If the number of tags in the note user tags list is greater than 1
                        if (note.userTags.size > 1) {
                            val surfaceVariantColor = MaterialTheme.colorScheme.surfaceVariant

                            Spacer(Modifier.width(8.dp))

                            // Sets a bubble with the number of the rest of the tags in the list
                            Text(
                                text = "+${note.userTags.size - 1}",
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
                }
            }
        }

        // If showTags is true, show an expanded list of tags
        if (showTags()) {
            Surface(
                color = MaterialTheme.colorScheme.surfaceContainerHighest,
                shadowElevation = 6.dp,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .wrapContentSize(
                        align = Alignment.BottomEnd,
                        unbounded = true
                    )
                    .padding(end = 12.dp, bottom = 12.dp)
                    .clickable { onTagsClick(note.id) }
            ) {
                Column(
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier.padding(4.dp)
                ) {
                    for (noteUserTag in note.userTags) {
                        Text(
                            text = noteUserTag.tagText,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        if (noteUserTag != note.userTags.last()) {
                            Spacer(Modifier.height(4.dp))
                        }
                    }
                }
            }
        }
    }
}


/*
Previews
 */
@CompactSizeScreenThemePreview
@Composable
private fun NoteScreenItemListViewModePreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(12.dp)
        ) {
            // With no tags
            NoteScreenItem(
                note = fakeNotesList[3],
                onNoteClick = {},
                isListViewMode = { true }
            )

            // With one tag
            NoteScreenItem(
                note = fakeNotesList[1],
                onNoteClick = {},
                isListViewMode = { true }
            )

            // With multiple tags
            NoteScreenItem(
                note = fakeNotesList[2],
                onNoteClick = {},
                isListViewMode = { true }
            )

            // With expanded tags
            NoteScreenItem(
                note = fakeNotesList[2],
                showTags = { true },
                onNoteClick = {},
                isListViewMode = { true },
                onTagsClick = {}
            )
        }
    }
}


@CompactSizeScreenThemePreview
@Composable
private fun NoteScreenItemGridViewModePreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(12.dp)
        ) {
            // With no tags
            NoteScreenItem(
                note = fakeNotesList[3],
                onNoteClick = {},
                isListViewMode = { false }
            )

            // With one tag
            NoteScreenItem(
                note = fakeNotesList[1],
                onNoteClick = {},
                isListViewMode = { false }
            )

            // With multiple tags
            NoteScreenItem(
                note = fakeNotesList[2],
                onNoteClick = {},
                isListViewMode = { false }
            )

            // With expanded tags
            NoteScreenItem(
                note = fakeNotesList[2],
                showTags = { true },
                onNoteClick = {},
                isListViewMode = { false },
                onTagsClick = {}
            )
        }
    }
}