package com.example.remotedatabase.util

import androidx.annotation.StringRes
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