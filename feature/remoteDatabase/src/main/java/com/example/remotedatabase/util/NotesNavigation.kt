package com.example.remotedatabase.util

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.model.Note
import com.example.remotedatabase.NotesViewModel
import com.example.remotedatabase.R
import com.example.remotedatabase.ui.NotesDetailScreen
import com.example.remotedatabase.ui.NotesListScreen


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



    NavHost(
        navController = navController,
        startDestination = RemoteDatabaseDestinations.NotesList.screenRouteName,
        modifier = Modifier.padding(innerPadding())
    ) {
        // Notes List destination
        composable(
            route = RemoteDatabaseDestinations.NotesList.screenRouteName
        ) {
            Log.i("NotesListScreen", "NotesListScreen created")

            NotesListScreen()
        }

        // New Note destination
        composable(
            route = RemoteDatabaseDestinations.NewNote.screenRouteName
        ) {
            Log.i("NoteDetailScreen", "NoteDetalScreen as 'new note' variant created")

            NotesDetailScreen(
                noteToEdit = Note(),
                onMainButtonClick = { TODO("Trigger ViewModel's createNote() method with the given note") }
            )
        }

        // Edit Note destination
        composable(
            route = RemoteDatabaseDestinations.EditNote.screenRouteName
        ) {
            Log.i("NoteDetailScreen", "NoteDetalScreen as 'edit note' variant created")

            NotesDetailScreen(
                noteToEdit = notesViewModel.currentSelectedNote.collectAsState().value,
                onMainButtonClick = { TODO("Trigger ViewModel's updateNote() method with the given note") }
            )
        }
    }
}