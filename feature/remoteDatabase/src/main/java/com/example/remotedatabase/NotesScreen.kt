package com.example.remotedatabase

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController


@Composable
fun NotesScreen(
    onMenuButtonClick: () -> Unit
) {

    // Nav controller and current back stack entry to observe the current route
    val notesNavController = rememberNavController()
    // Observes the notesNavControlles BackstackEntry as State
    val currentBackStackEntry by notesNavController.currentBackStackEntryAsState()
    // Current route based on the current back stack entry
    val currentRoute: String? = currentBackStackEntry?.destination?.route

    // Stores the current ViewModelStoreOwner
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current)
    // Create a ViewModel instance binded to viewModelStoreOwner
    val notesViewModel: NotesViewModel = hiltViewModel(viewModelStoreOwner)

    NotesScreenContent(
        currentRoute = { currentRoute },
        onMenuButtonClick = onMenuButtonClick,
        setNoteToEditById = {},
        setNewNote = {},
        returnToPreviousScreen = {},
        navigateToAddNoteScreen = {}
    ) {

    }
}


@Composable
private fun NotesScreenContent(
    currentRoute: () -> String?,
    onMenuButtonClick: () -> Unit,
    setNoteToEditById: (Long) -> Unit,
    setNewNote: () -> Unit,
    returnToPreviousScreen: () -> Unit,
    navigateToAddNoteScreen: () -> Unit,
    remoteDatabaseNavHost: @Composable (PaddingValues) -> Unit
) {

}