@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.remotedatabase.ui

import android.content.Context
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Note
import com.example.model.UserTag
import com.example.model.fakeNotesList
import com.example.model.fakeUserTagsList
import com.example.remotedatabase.R
import com.example.ui.theme.PreviewAppTheme
import com.example.ui.ui.CompactSizeScreenThemePreview
import dagger.hilt.android.qualifiers.ApplicationContext


@Composable
internal fun NotesListScreen(

) {

}


@Composable
private fun NotesListScreenContent(
    notes: () -> List<Note>,
    onNoteClick: (Note) -> Unit,
    isListViewMode: () -> Boolean,
    filterTags: () -> List<UserTag>,
    onTagFiltersButtonClick: () -> Unit,
    onClearTagFilterClick: (Long) -> Unit
) {

    // State that indicates which note will have its tags list expanded, -1L means none
    var noteWithExpandedTags by rememberSaveable { mutableLongStateOf(-1L) }

    // State of a list that will hold the notes list with the search filter applied
    var filteredNotesList by rememberSaveable { mutableStateOf(notes()) }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
    ) {
        // Note search tools
        NoteSearchTools(
            filterTags = filterTags,
            onNoteSearch = { noteSearchText ->
                filteredNotesList = notes().filter { note ->
                    note.title.contains(noteSearchText, ignoreCase = true) ||
                            note.body.contains(noteSearchText, ignoreCase = true)
                }
            },
            onTagFiltersButtonClick = onTagFiltersButtonClick,
            onClearTagFilterClick = onClearTagFilterClick
        )

        /*
            If in List View Mode, use 1 fixed column an show the notes list items in list view mode,
            else set adaptative columns and show the notes list items in grid view mode
         */
        LazyVerticalGrid(
            columns = if (isListViewMode()) GridCells.Fixed(1) else GridCells.Adaptive(144.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .widthIn(min = 320.dp, max = 623.dp)
        ) {
            items(
                items = filteredNotesList,
                key = { note -> note.id }
            ) { note ->
                NoteScreenItem(
                    note = note,
                    showTags = { note.id == noteWithExpandedTags },
                    onNoteClick = onNoteClick,
                    isListViewMode = isListViewMode,
                    onTagsClick = { noteId ->
                        // If the obtained noteId is the same as the current noteWithExpandedTags, set it to -1, else, set the new noteId
                        noteWithExpandedTags = if (noteId == noteWithExpandedTags) -1 else noteId
                    }
                )
            }
        }
    }
}


@Composable
private fun NoteSearchTools(
    filterTags: () -> List<UserTag>,
    onNoteSearch: (String) -> Unit,
    onTagFiltersButtonClick: () -> Unit,
    onClearTagFilterClick: (Long) -> Unit
) {

    var noteSearchText by rememberSaveable { mutableStateOf("") }

    // Note search / Filter button and Filtered tags
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Note search and Filter button
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Note search
            SearchBar(
                query = noteSearchText,
                onQueryChange = { newNoteSearchText -> noteSearchText = newNoteSearchText },
                onSearch = { onNoteSearch(noteSearchText) },
                active = false,
                onActiveChange = {},
                placeholder = {
                    Text(
                        text = stringResource(R.string.remote_database_notes_list_search_tab_label),
                        fontSize = 16.sp,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                trailingIcon = {
                    // If the noteSearchText is empty, show the search icon, else a clear icon
                    Icon(
                        imageVector = with(Icons.Rounded) {
                            if (noteSearchText.isEmpty()) Search else Clear
                        },
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .clickable {
                                if (noteSearchText.isNotEmpty()) {
                                    // Clean the current search bar text content
                                    noteSearchText = ""
                                    // Trigger the onNoteSearch function with an empty string
                                    onNoteSearch(noteSearchText)
                                }
                            }
                    )
                },
                colors = SearchBarDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                content = {},
                modifier = Modifier
                    .weight(1f)
                    .offset(y = (-4).dp)
            )

            // Filter button
            Icon(
                imageVector = Icons.Rounded.FilterList,
                contentDescription = stringResource(R.string.remote_database_notes_list_open_tags_filter),
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        onTagFiltersButtonClick()
                    }
            )
        }

        // Filtered tags
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Create an input chip for each uesr tag in the filterTags list
            items(
                items = filterTags(),
                key = { userTag -> userTag.id }
            ) { filterTag ->
                InputChip(
                    selected = false,
                    onClick = {},
                    label = {
                        Text(
                            text = filterTag.tagText,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Clear,
                            contentDescription = null,
                            modifier = Modifier
                                .clickable {
                                    onClearTagFilterClick(filterTag.id)
                                }
                        )
                    },
                    colors = InputChipDefaults.inputChipColors().copy(
                        containerColor = Color.Transparent,
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        trailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    border = InputChipDefaults.inputChipBorder(
                        enabled = true,
                        selected = false,
                        borderColor = MaterialTheme.colorScheme.outline
                    )
                )
            }
        }
    }
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
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

                // ONLY IF the number of user tags of the note is greater than 1, add a clickable to expand the tags list
                val noteTagsRowModifier = if (note.userTags.size > 1) {
                    Modifier.clickable { onTagsClick(note.id) }
                } else Modifier

                // Note tags row
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = noteTagsRowModifier
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
@Composable
@CompactSizeScreenThemePreview
//@Preview
private fun NotesListScreenContentPreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            NotesListScreenContent(
                notes = { fakeNotesList },
                onNoteClick = {},
                isListViewMode = { true },
                filterTags = {
                    listOf(
                        fakeUserTagsList[1],
                        fakeUserTagsList[2]
                    )
                },
                onTagFiltersButtonClick = {},
                onClearTagFilterClick = {}
            )
        }
    }
}


@Composable
@CompactSizeScreenThemePreview
private fun NoteSearchToolsPreview() {
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
            NoteSearchTools(
                filterTags = {
                    listOf(
                        fakeUserTagsList[1],
                        fakeUserTagsList[2]
                    )
                },
                onNoteSearch = {},
                onTagFiltersButtonClick = {},
                onClearTagFilterClick = {}
            )
        }
    }
}


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