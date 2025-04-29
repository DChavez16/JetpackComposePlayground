package com.example.remotedatabase.util

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.example.model.Note
import com.example.remotedatabase.ui.NotesViewModel
import com.example.remotedatabase.R
import com.example.remotedatabase.ui.NotesDetailScreen
import com.example.remotedatabase.ui.NotesListScreen


private const val LOG_TAG = "RemoteDatabaseNavHost"

const val URI = "https://www.compose-playground.com"

// Remote database destination enum class
internal enum class RemoteDatabaseDestinations(
    @StringRes val screenTitle: Int,
    val screenRouteName: String
) {
    NotesList(
        screenTitle = R.string.remote_database_notes_list_screen_title,
        screenRouteName = "notesList"
    ),
    NewNote(
        screenTitle = R.string.remote_database_new_note_screen_title,
        screenRouteName = "newNote"
    ),
    EditNote(
        screenTitle = R.string.remote_database_edit_note_screen_title,
        screenRouteName = "editNote"
    )
}

fun NavGraphBuilder.remoteDatabaseGraph(
    navController: NavHostController,
    graphRoute: String,
    onMenuButtonClick: () -> Unit
) {
    /* TODO Change to follow an Offline-first approach. There are two options ->
        * Create a `useCases` package inside this module:
            - Create a new module is not needed
            - The use case is limited to ONLY this module
        * Create a useCases module where all the app use cases are stored:
            - A new module creation and configuration is needed
            - The use case will be usable by all modules
            - Not all features need use cases, and would interact directly with the needed repository in the data modules (use cases would seem unnecessary)
     */

    navigation(
        route = graphRoute,
        startDestination = RemoteDatabaseDestinations.NotesList.screenRouteName
    ) {
        // Notes List destination
        composable(
            route = RemoteDatabaseDestinations.NotesList.screenRouteName,
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "$URI/notes"
                }
            )
        ) {
            Log.i(LOG_TAG, "NotesListScreen destination selected in the NavHost")

            // Creates viewModel using the parent entry
            val parentEntry = remember { navController.getBackStackEntry(graphRoute) }
            val notesViewModel = hiltViewModel<NotesViewModel>(parentEntry)

            // Notes List uiState
            val notesUiState by notesViewModel.notesUiState.collectAsState()
            // List view mode state
            val isListView by notesViewModel.isListView.collectAsState()

            NotesListScreen(
                notesUiState = notesUiState,
                isListViewMode = { isListView },
                onAddNoteCLick = {
                    // Navigate to the NewNote destination
                    navController.navigate(RemoteDatabaseDestinations.NewNote.screenRouteName)
                },
                onNoteClick = { selectedNote ->
                    // Set the selecteNote as the current selected note
                    notesViewModel.changeCurrentSelectedNote(selectedNote.id)

                    // Navigate to the EditNote destination
                    navController.navigate(RemoteDatabaseDestinations.EditNote.screenRouteName)
                },
                onMenuButtonClick = onMenuButtonClick,
                onChangeViewModeButtonCLick = notesViewModel::changeViewMode,
                onErrorMessageRetryButtonClick = notesViewModel::getNotes,
                parentNavBackStackEntry = parentEntry
            )
        }

        // New Note destination
        composable(
            route = RemoteDatabaseDestinations.NewNote.screenRouteName
        ) {
            Log.i(LOG_TAG, "NoteDetalScreen as 'new note' destination selected in the NavHost")

            // Creates viewModel using the parent entry
            val parentEntry = remember { navController.getBackStackEntry(graphRoute) }
            val notesViewModel = hiltViewModel<NotesViewModel>(parentEntry)

            // Top App Bar title
            val topAppBarTitle = stringResource(R.string.remote_database_new_note_screen_title)

            NotesDetailScreen(
                noteToEdit = Note(),
                topAppBarTitle = topAppBarTitle,
                onMainButtonClick = {
                    // Creating a new note
                    notesViewModel.createNote(it)

                    // Returning to the NotesList destination
                    navController.popBackStack()
                },
                onReturnButtonClick = { navController.popBackStack() },
                parentNavBackStackEntry = parentEntry
            )
        }

        // Edit Note destination
        composable(
            route = RemoteDatabaseDestinations.EditNote.screenRouteName,
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "$URI/notes/editNote"
                }
            )
        ) {
            Log.i(LOG_TAG, "NoteDetalScreen as 'edit note' destination selected in the NavHost")

            // TODO Implementate deep linnking from Widget

            // Creates viewModel using the parent entry
            val parentEntry = remember { navController.getBackStackEntry(graphRoute) }
            val notesViewModel = hiltViewModel<NotesViewModel>(parentEntry)

            // Top App Bar title
            val topAppBarTitle = stringResource(R.string.remote_database_edit_note_screen_title)

            // Current selected note state
            val currentSelectedNote by notesViewModel.currentSelectedNote.collectAsState()

            NotesDetailScreen(
                noteToEdit = currentSelectedNote,
                topAppBarTitle = topAppBarTitle,
                onMainButtonClick = {
                    // Updates the current note
                    notesViewModel.updateNote(it)

                    // Returning to the NotesList destination
                    navController.popBackStack()
                },
                onReturnButtonClick = { navController.popBackStack() },
                parentNavBackStackEntry = parentEntry,
                showDeleteActionButton = true,
                onDeleteNote = {
                    // TODO Fix note deletion not succeding AND causing the app to crash
                    // Delete the current note in the remote database
                    notesViewModel.deleteNote(noteId = currentSelectedNote.id)

                    // Returning to the NotesList destination
                    navController.popBackStack()
                },
            )
        }
    }
}