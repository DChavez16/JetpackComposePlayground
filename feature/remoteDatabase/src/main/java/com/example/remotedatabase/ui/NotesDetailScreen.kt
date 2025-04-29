@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.remotedatabase.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Discount
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import com.example.model.Note
import com.example.model.UserTag
import com.example.model.fakeNotesList
import com.example.remotedatabase.R
import com.example.ui.theme.PreviewAppTheme
import com.example.ui.ui.CompactSizeScreenThemePreview
import com.example.ui.ui.DefaultTopAppBar
import com.example.ui.ui.ExpandedSizeScreenThemePreview
import kotlinx.coroutines.launch


private const val LOG_TAG = "NotesDetailScreen"

@Composable
internal fun NotesDetailScreen(
    noteToEdit: Note,
    topAppBarTitle: String,
    onMainButtonClick: (Note) -> Unit,
    onReturnButtonClick: () -> Unit,
    parentNavBackStackEntry: NavBackStackEntry,
    showDeleteActionButton: Boolean = false,
    onDeleteNote: () -> Unit = {},
) {

    // Note's user tags wichh can be edited in the TagsBottomSheet
    var editableNoteTags by rememberSaveable { mutableStateOf(noteToEdit.userTags) }

    // NotesDetailScreen coroutine scope
    val notesDetailScreenCoroutineScope = rememberCoroutineScope()

    // Modal bottom sheet state to expand or hide it
    val modalBottomSheetState = rememberModalBottomSheetState()
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }

    // Snackbar host state
    val snackbarHostState = remember { SnackbarHostState() }
    // Snackbar host invalid title message
    val invalidTitleMessage = stringResource(R.string.remote_database_notes_detail_snackbar_invalid_title)

    // Show delete note alert dialog state
    var showAlertDialog by rememberSaveable { mutableStateOf(false) }

    // Action button content description
    val actionButtonContentDescription = stringResource(R.string.remote_database_edit_note_delete_note_button)

    Log.i(LOG_TAG, "NotesDetailScreen started")

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = { topAppBarTitle },
                onMenuButtonClick = {},
                onBackButtonPressed = onReturnButtonClick,
                isPrincipalScreen = { false },
                actionButtonIcon = { if (showDeleteActionButton) Icons.Rounded.Delete else null },
                onActionButtonClick = {
                    // Show the delete note alert dialog
                    showAlertDialog = true
                },
                actionButtonContentDescription = { actionButtonContentDescription }
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        // Screen content
        NotesDetailScreenContent(
            noteToEdit = { noteToEdit },
            noteTags = { editableNoteTags },
            onNoteTagsIconButtonClicked = {
                // Start the expand animation of the bottom sheet and set showBottomSheet to true
                notesDetailScreenCoroutineScope.launch {
                    modalBottomSheetState.expand()
                }.invokeOnCompletion {
                    showBottomSheet = true
                }
            },
            onMainButtonClick = { noteTitle, noteBody ->
                // If the note title is empty, show the snackbar indicating that it can't be empty, else continue
                if(noteTitle.isEmpty()) {
                    notesDetailScreenCoroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = invalidTitleMessage
                        )
                    }
                }
                else {
                    // Create a note to upload using noteToEdit as base
                    val noteToUpload = noteToEdit.copy(
                        title = noteTitle,
                        body = noteBody,
                        userTags = editableNoteTags
                    )

                    Log.i(LOG_TAG, "Uploading note: $noteToUpload")

                    // Return to the caller the note with the edited title, body and tags
                    onMainButtonClick(noteToUpload)
                }
            },
            modifier = Modifier.padding(innerPadding)
        )

        // If Bottom Sheet is visible
        if(showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    // Start the hide animation of the bottom sheet and set showBottomSheet to false
                    notesDetailScreenCoroutineScope.launch {
                        modalBottomSheetState.hide()
                    }.invokeOnCompletion {
                        showBottomSheet = false
                    }
                },
                sheetState = modalBottomSheetState,
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            ) {
                TagsBottomSheet(
                    selectedUserTags = { editableNoteTags },
                    filterMode = { false },
                    onMainButtonClick = { newUserTags ->
                        // Replace the current editableUserTags with the obtained from TagsBottomSheet
                        editableNoteTags = newUserTags

                        // Start the hide animation of the bottom sheet and set showBottomSheet to false
                        notesDetailScreenCoroutineScope.launch {
                            modalBottomSheetState.hide()
                        }.invokeOnCompletion {
                            showBottomSheet = false
                        }
                    },
                    notesViewModel = hiltViewModel(parentNavBackStackEntry)
                )
            }
        }

        // If the Alert Dialog is visible
        if(showAlertDialog) {
            DeleteNoteAlertDialog(
                onDeleteButtonClick = onDeleteNote,
                onDismiss = { showAlertDialog = false }
            )
        }
    }
}


@Composable
private fun NotesDetailScreenContent(
    noteToEdit: () -> Note,
    noteTags: () -> List<UserTag>,
    onNoteTagsIconButtonClicked: () -> Unit,
    onMainButtonClick: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {

    var editableNoteTitle by rememberSaveable { mutableStateOf(noteToEdit().title) }
    var editableNoteBody by rememberSaveable { mutableStateOf(noteToEdit().body) }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Column with the title, tags and body of the note, occupying all the screen
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(start = 16.dp, end = 16.dp, top = 16.dp)
        ) {
            // Note title and tags
            NoteTitleAndTags(
                noteTitle = { editableNoteTitle },
                noteTags = noteTags,
                onNoteTitleChange = { newNoteTitle -> editableNoteTitle = newNoteTitle },
                onNoteTagsIconButtonClicked = onNoteTagsIconButtonClicked

            )

            // Note body
            NoteBody(
                noteBody = { editableNoteBody },
                onNoteBodyChange = { newNoteBody -> editableNoteBody = newNoteBody }
            )
        }

        // Save note FAB at the bottom
        FloatingActionButton(
            onClick = {
                onMainButtonClick(editableNoteTitle, editableNoteBody)
            },
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 4.dp,
                pressedElevation = 2.dp,
                hoveredElevation = 6.dp,
                focusedElevation = 6.dp
            ),
            content = {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = stringResource(R.string.remote_database_notes_detail_save_note_button_label)
                )
            },
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 20.dp)
        )
    }
}


@Composable
private fun NoteTitleAndTags(
    noteTitle: () -> String,
    noteTags: () -> List<UserTag>,
    onNoteTitleChange: (String) -> Unit,
    onNoteTagsIconButtonClicked: () -> Unit
) {
    Column(
        modifier = Modifier.widthIn(min = 260.dp, max = 623.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Note title text field
            TextField(
                value = noteTitle(),
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
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onNoteTagsIconButtonClicked() }
            ) {
                // If the note user tags list is note empty
                if (noteTags().isNotEmpty()) {
                    // Tag text from the first tag in the note user tags list
                    Text(
                        text = noteTags()[0].tagText,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // If the number of tags in the note user tags list is greater than 1
                    if (noteTags().size > 1) {
                        val surfaceVariantColor = MaterialTheme.colorScheme.surfaceVariant

                        // Sets a bubble with the number of the rest of the tags in the list
                        Text(
                            text = "+${noteTags().size - 1}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.drawBehind {
                                drawCircle(
                                    color = surfaceVariantColor,
                                    radius = 10.dp.toPx()
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
                    modifier = Modifier
                        .size(16.dp)
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
    noteBody: () -> String,
    onNoteBodyChange: (String) -> Unit
) {
    val outlineVariantColor = MaterialTheme.colorScheme.outlineVariant
    val verticalScroll = rememberScrollState()

    // Note's body text field
    TextField(
        value = noteBody(),
        onValueChange = onNoteBodyChange,
        textStyle = MaterialTheme.typography.bodyLarge,
        singleLine = false,
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
        modifier = Modifier
            .widthIn(min = 260.dp, max = 623.dp)
            .fillMaxSize()
            .verticalScroll(state = verticalScroll)
            .drawBehind {
                // Line drawns to give a sensation of a notebook
                val numberOfLines = (size.height.toDp() / 24).value.toInt()

                for (line in 1..numberOfLines) {
                    drawLine(
                        color = outlineVariantColor,
                        start = Offset(
                            x = 12.dp.toPx(),
                            y = 24.dp
                                .toPx()
                                .times(line.toFloat())
                                .plus(9.dp.toPx())
                        ),
                        end = Offset(
                            x = size.width - 12.dp.toPx(),
                            y = 24.dp
                                .toPx()
                                .times(line.toFloat())
                                .plus(9.dp.toPx())
                        ),
                        strokeWidth = 1.dp.toPx()
                    )
                }
            }
    )
}


@Composable
private fun DeleteNoteAlertDialog(
    onDeleteButtonClick: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        title = {
            Text(
                text = stringResource(R.string.remote_database_edit_note_delete_note_alert_dialog_header)
            )
        },
        text = {
            Text(
                text = stringResource(R.string.remote_database_edit_note_delete_note_alert_dialog_message)
            )
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    onDeleteButtonClick()
                    onDismiss()
                }
            ) {
                Text(
                    text = stringResource(R.string.remote_database_edit_note_delete_note_alert_dialog_confirm_button_label),
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    text = stringResource(R.string.remote_database_edit_note_delete_note_alert_dialog_cancel_button_label),
                    fontWeight = FontWeight.Bold
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    )
}


/*
Previews
 */
@CompactSizeScreenThemePreview
@Composable
private fun NotesDetailScreenContentPreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)

        ) {
            NotesDetailScreenContent(
                noteToEdit = { fakeNotesList[0] },
                noteTags = { fakeNotesList[0].userTags },
                onNoteTagsIconButtonClicked = {},
                onMainButtonClick = { _: String, _: String ->}
            )
        }
    }
}


@ExpandedSizeScreenThemePreview
@Composable
private fun NotesDetailScreenContentExpandedPreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)

        ) {
            NotesDetailScreenContent(
                noteToEdit = { fakeNotesList[0] },
                noteTags = { fakeNotesList[0].userTags },
                onNoteTagsIconButtonClicked = {},
                onMainButtonClick = { _: String, _: String ->}
            )
        }
    }
}


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
                noteTitle = { fakeNotesList[3].title },
//                noteTitle = { "" },
                noteTags = { fakeNotesList[3].userTags },
                onNoteTitleChange = {},
                onNoteTagsIconButtonClicked = {}
            )

            // With one tag
            NoteTitleAndTags(
                noteTitle = { fakeNotesList[1].title },
                noteTags = { fakeNotesList[1].userTags },
                onNoteTitleChange = {},
                onNoteTagsIconButtonClicked = {}
            )

            // With multiple tags
            NoteTitleAndTags(
                noteTitle = { fakeNotesList[2].title },
                noteTags = { fakeNotesList[2].userTags },
                onNoteTitleChange = {},
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            NoteBody(
                noteBody = { fakeNotesList[0].body },
                onNoteBodyChange = {}
            )
        }
    }
}


@CompactSizeScreenThemePreview
@Composable
private fun DeleteNoteAlertDialogPreview() {
    PreviewAppTheme(
        darkTheme = isSystemInDarkTheme()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            DeleteNoteAlertDialog(
                onDeleteButtonClick = {},
                onDismiss = {}
            )
        }
    }
}