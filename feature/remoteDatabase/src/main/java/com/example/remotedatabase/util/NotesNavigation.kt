package com.example.remotedatabase.util

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.remotedatabase.R


// Remote database destination enum class
internal enum class RemoteDatabaseDestinations(
    @StringRes val screenTitle: Int,
    val screenRouteName: String
) {
    NotesList(
        screenTitle = R.string.remote_database_notes_list_screen_title,
        screenRouteName = ""
    ),
    AddNote(
        screenTitle = R.string.remote_database_add_note_screen_title,
        screenRouteName = ""
    ),
    EditNote(
        screenTitle = R.string.remote_database_edit_note_screen_title,
        screenRouteName = ""
    )
}


// Remote database NavHost Composable function
@Composable
internal fun RemoteDatabaseNavHost(
    navController: NavHostController,
    viewModelStoreOwner: () -> ViewModelStoreOwner,
    innerPadding: () -> PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = RemoteDatabaseDestinations.NotesList.screenRouteName,
        modifier = Modifier.padding(innerPadding())
    ) {

    }
}