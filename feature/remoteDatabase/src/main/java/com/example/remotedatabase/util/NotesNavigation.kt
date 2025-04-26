package com.example.remotedatabase.util

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.example.model.Note
import com.example.remotedatabase.NotesViewModel
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


// Remote database NavHost Composable function
@Composable
internal fun RemoteDatabaseNavHost(
    navController: NavHostController,
    viewModelStoreOwner: () -> ViewModelStoreOwner,
    innerPadding: () -> PaddingValues
) {

    // Creates a ViewModel instance binded to viewModelStoreOwner
    val notesViewModel: NotesViewModel = hiltViewModel(viewModelStoreOwner())

    val notesUiState by notesViewModel.notesUiState.collectAsState()
    val isListView by notesViewModel.isListView.collectAsState()

    NavHost(
        navController = navController,
        startDestination = RemoteDatabaseDestinations.NotesList.screenRouteName,
        modifier = Modifier.padding(innerPadding())
    ) {
        // Notes List destination
        composable(
            route = RemoteDatabaseDestinations.NotesList.screenRouteName
        ) {
            Log.i(LOG_TAG, "NotesListScreen destination selected in the NavHost")

            NotesListScreen(
                notesUiState = notesUiState,
                isListViewMode = { isListView },
                onNoteClick = { selectedNote ->
                    // Set the selecteNote as the current selected note
                    notesViewModel.changeCurrentSelectedNote(selectedNote.id)

                    // Navigate to the EditNote destination
                    navController.navigate(RemoteDatabaseDestinations.EditNote.screenRouteName)
                },
                onErrorMessageRetryButtonClick = notesViewModel::getNotes,
                viewModelStoreOwner = viewModelStoreOwner()
            )
        }

        // New Note destination
        composable(
            route = RemoteDatabaseDestinations.NewNote.screenRouteName
        ) {
            Log.i(LOG_TAG, "NoteDetalScreen as 'new note' destination selected in the NavHost")

            NotesDetailScreen(
                noteToEdit = Note(),
                onMainButtonClick = {
                    // Creating a new note
                    notesViewModel.createNote(it)

                    // Returning to the NotesList destination
                    navController.navigate(RemoteDatabaseDestinations.NotesList.screenRouteName)
                },
                viewModelStoreOwner = viewModelStoreOwner()
            )
        }

        // Edit Note destination
        composable(
            route = RemoteDatabaseDestinations.EditNote.screenRouteName
        ) {
            Log.i(LOG_TAG, "NoteDetalScreen as 'edit note' destination selected in the NavHost")

            NotesDetailScreen(
                noteToEdit = notesViewModel.currentSelectedNote.collectAsState().value,
                onMainButtonClick = { updatedNote ->
                    // Updating the note
                    notesViewModel.updateNote(updatedNote)

                    // Returning to the NotesList destination
                    navController.navigate(RemoteDatabaseDestinations.NotesList.screenRouteName)
                },
                viewModelStoreOwner = viewModelStoreOwner()
            )
        }
    }
}

fun NavGraphBuilder.remoteDatabaseGraph(
    navController: NavHostController,
    onMenuButtonClick: () -> Unit
) {
    navigation(
        route = "remoteDatabase",
        startDestination = RemoteDatabaseDestinations.NotesList.screenRouteName
    ) {
        composable(
            route = RemoteDatabaseDestinations.NotesList.screenRouteName,
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "$URI/notes"
                }
            )
        ) {
//            val parentEntry = remember { navController.getBackStackEntry("remoteDatabase") }
//            val viewModel = hiltViewModel<NotesViewModel>(parentEntry)

            // TODO Refactor NotesListScreen and add it here

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Text("Notes list")

                Button(
                    onClick = onMenuButtonClick
                ) {
                    Text("Open menu")
                }

                Row {
                    Button(
                        onClick = {
                            navController.navigate(RemoteDatabaseDestinations.NewNote.screenRouteName)
                        }
                    ) {
                        Text("To New Note")
                    }

                    Button(
                        onClick = {
                            navController.navigate(RemoteDatabaseDestinations.EditNote.screenRouteName)
                        }
                    ) {
                        Text("To Edit Note")
                    }
                }
            }
        }

        composable(
            route = RemoteDatabaseDestinations.NewNote.screenRouteName
        ) {

            // TODO Refactor NotesDetailScreen and add it here

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Text("New note")

                Button(
                    onClick = {
                        navController.popBackStack()
                    }
                ) {
                    Text("Return")
                }
            }
        }

        composable(
            route = RemoteDatabaseDestinations.EditNote.screenRouteName,
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "$URI/notes/editNote"
                }
            )
        ) {

            // TODO Refactor NotesDetailScreen and add it here

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Text("Edit note")

                Button(
                    onClick = {
                        navController.popBackStack()
                    }
                ) {
                    Text("Return")
                }
            }
        }
    }
}