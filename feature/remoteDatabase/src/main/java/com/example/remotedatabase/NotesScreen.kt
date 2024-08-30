package com.example.remotedatabase

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.remotedatabase.util.RemoteDatabaseDestinations
import com.example.remotedatabase.util.RemoteDatabaseNavHost
import com.example.ui.theme.PreviewAppTheme
import com.example.ui.ui.CompactSizeScreenThemePreview
import com.example.ui.ui.DefaultTopAppBar


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

    val isListView = notesViewModel.isListView.collectAsState().value

    NotesScreenContent(
        currentRoute = { currentRoute },
        isListView = { isListView },
        onMenuButtonClick = onMenuButtonClick,
        onReturnButtonClick = { notesNavController.popBackStack() },
        onDeleteNoteButtonClick = {
            // Delete the current note in the remote database
            notesViewModel.deleteNote(
                noteId = notesViewModel.currentSelectedNote.value.id
            )

            // Return to the previous screen
            notesNavController.popBackStack()
        },
        onChangeViewModeButtonClick = {
            // Change the current view mode
            notesViewModel.changeViewMode()
        },
        navigateToAddNoteScreen = {
            // Navigate to the NewNote destination
            notesNavController.navigate(RemoteDatabaseDestinations.NewNote.screenRouteName)
        }
    ) { innerPadding ->
        RemoteDatabaseNavHost(
            navController = notesNavController,
            viewModelStoreOwner = { viewModelStoreOwner },
            innerPadding = { innerPadding }
        )
    }
}


@Composable
private fun NotesScreenContent(
    currentRoute: () -> String?,
    isListView: () -> Boolean,
    onMenuButtonClick: () -> Unit,
    onReturnButtonClick: () -> Unit,
    onDeleteNoteButtonClick: () -> Unit,
    onChangeViewModeButtonClick: () -> Unit,
    navigateToAddNoteScreen: () -> Unit,
    remoteDatabaseNavHost: @Composable (PaddingValues) -> Unit
) {

    // Top app bar title
    val topAppBarTitle =
        stringResource(getNavigationDestinarionFromRoute(currentRoute()).screenTitle)

    // Action button content description
    val actionButtonContentDescription = stringResource(
        getActionButtonContentDescription(currentRoute(), isListView())
    )

    // Show delete note alert dialog state
    var showAlertDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = { topAppBarTitle },
                onMenuButtonClick = onMenuButtonClick,
                onBackButtonPressed = onReturnButtonClick,
                isPrincipalScreen = { currentRoute() == RemoteDatabaseDestinations.NotesList.screenRouteName },
                actionButtonIcon = {
                    // If in the NotesList route show the view mode icon depending on the current view mode
                    // therefore, if in the EditNote route show the delete icon
                    if (currentRoute() == RemoteDatabaseDestinations.NotesList.screenRouteName) {
                        // If the view mode is in list mode, show the list icon, else, the grid icon
                        with(Icons.Rounded) {
                            if (isListView()) Icons.AutoMirrored.Rounded.List else GridView
                        }
                    } else if (currentRoute() == RemoteDatabaseDestinations.EditNote.screenRouteName) {
                        Icons.Rounded.Delete
                    } else null
                },
                onActionButtonClick = {
                    // If in the NotesList route add the change view mode method
                    // therefore, if in the EditNote route add the delete note method
                    if (currentRoute() == RemoteDatabaseDestinations.NotesList.screenRouteName) {
                        onChangeViewModeButtonClick()
                    } else if (currentRoute() == RemoteDatabaseDestinations.EditNote.screenRouteName) {
                        // Show the delete note alert dialog
                        showAlertDialog = true
                    }
                },
                actionButtonContentDescription = { actionButtonContentDescription },
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = currentRoute() == "notesList",
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                FloatingActionButton(
                    onClick = {
                        navigateToAddNoteScreen()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = stringResource(R.string.remote_database_notes_list_write_new_note_button_label)
                    )
                }
            }
        }
    ) { innerPadding ->

        // If showAlertDialog is true, show the delete note alert dialog
        if (showAlertDialog) {
            DeleteNoteAlertDialog(
                onDeleteButtonClick = onDeleteNoteButtonClick,
                onDismiss = { showAlertDialog = false }
            )
        }

        remoteDatabaseNavHost(innerPadding)
    }
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
                onClick = onDeleteButtonClick
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


private fun getNavigationDestinarionFromRoute(route: String?) =
    RemoteDatabaseDestinations.entries.find {
        it.screenRouteName == route
    } ?: RemoteDatabaseDestinations.NotesList


private fun getActionButtonContentDescription(route: String?, isListView: Boolean) =
// If in the NotesList route add the view mode content description depending on the current view mode
    // else add the delete note content description
    if (route == RemoteDatabaseDestinations.NotesList.screenRouteName) {
        // If the view mode is in list mode, add the list view content description, else, add the grid view content description
        if (isListView) R.string.remote_database_notes_list_change_to_grid_layout else R.string.remote_database_notes_list_change_to_grid_layout
    } else {
        R.string.remote_database_edit_note_delete_note_button
    }


/*
Preview
 */
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