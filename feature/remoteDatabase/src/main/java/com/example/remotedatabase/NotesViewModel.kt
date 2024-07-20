package com.example.remotedatabase

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.Note
import com.example.model.UserTag
import com.example.notes.NoteRepository
import com.example.usertag.UserTagRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject


internal sealed interface NotesUiState {
    data class Success(val notes: List<Note>): NotesUiState
    data class Error(val errorMessage: String): NotesUiState
    data object Loading: NotesUiState
}

internal sealed interface UserTagUiState {
    data class Success(val userTags: List<UserTag>): UserTagUiState
    data class Error(val errorMessage: String): UserTagUiState
    data object Loading: UserTagUiState
}


@HiltViewModel
internal class NotesViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val userTagRepository: UserTagRepository
): ViewModel() {

    // TODO Change Create, Update and Delete methods to suspend funcionts

    // Backing property of the NotesUiState to avoid state updates from other classes
    private val _notesUiState = MutableStateFlow<NotesUiState>(NotesUiState.Loading)
    // The UI collects from this StateFlow to get its state updates
    val notesUiState: StateFlow<NotesUiState> = _notesUiState

    // Backing property for UserTag list to avoid state updates from other classes
    private val _userTags = MutableStateFlow<UserTagUiState>(UserTagUiState.Loading)
    // The UI collects from this StateFlow to get its state updates
    val userTags: StateFlow<UserTagUiState> = _userTags


    // Block of code executed at ViewModel creation
    init {
        // Observe a flow of notes list obtained from the repository
        viewModelScope.launch {
            noteRepository.getNotes()
                .onStart {
                    // Sets the _notesUiState to its Loading state
                    Log.i("NotesViewModel", "Collecting notes from the remote database")
                    _notesUiState.value = NotesUiState.Loading
                }
                .catch { error ->
                    // Sets the _notesUiState to its Error state containing the error message text
                    Log.e("NotesViewModel", "Error collecting notes ${error.message}")
                    _notesUiState.value = NotesUiState.Error(error.message.toString())
                }
                .collect { notes ->
                    // Sets the _notesUiState to its Success state containing the list of Note
                    Log.i("NotesViewModel", "Notes collected succesfully")
                    _notesUiState.value = NotesUiState.Success(notes)
                }
        }

        // Observe a flow of user tag list obtainded from the repository
        viewModelScope.launch {
            userTagRepository.getUserTags()
                .onStart {
                    // Sets the _userTags to its Loading state
                    Log.i("NotesViewModel", "Collecting user tags from the remote database")
                    _userTags.value = UserTagUiState.Loading
                }
                .catch { error ->
                    // Sets the _userTags to its Error state containing the error message text
                    Log.e("NotesViewModel", "Error collecting user tags ${error.message}")
                    _userTags.value = UserTagUiState.Error(error.message.toString())
                }
                .collect { userTags ->
                    // Sets the _userTags to its Success state containing the list of UserTag
                    Log.i("NotesViewModel", "User tags collected succesfully")
                    _userTags.value = UserTagUiState.Success(userTags)
                }
        }


        /**
         * Creates a new [Note] storing it in a remote database
         * @param newNote [Note] to be send to the server for storage
         */
        fun createNote(newNote: Note) {
            Log.i("NotesViewModel", "Uploading a note to the remote database...")
            val messageResponse = noteRepository.createNote(newNote)

            Log.i("NotesViewModel", messageResponse.message)
        }

        /**
         * Creates a new [UserTag] storing it in a remote database
         * @param newUserTag [UserTag] to be send to the server for storage
         */
        fun createUserTag(newUserTag: UserTag) {
            Log.i("NotesViewModel", "Uploading an user tag to the remote database...")
            val messageResponse = userTagRepository.createUserTag(newUserTag)

            Log.i("NotesViewModel", messageResponse.message)
        }


        /**
         * Updates the given [Note] in the remote database
         * @param updatedNote [Note] to be updated in the server
         */
        fun updateNote(updatedNote: Note) {
            Log.i("NotesViewModel", "Sending the note with id ${updatedNote.id} to be updated in the remote database...")
            val messageResponse = noteRepository.updateNote(updatedNote)

            Log.i("NotesViewModel", messageResponse.message)
        }

        /**
         * Updates the given [UserTag] in the remote database
         * @param updatedUserTag [UserTag] to be updated in the server
         */
        fun updateUserTag(updatedUserTag: UserTag) {
            Log.i("NotesViewModel", "Sending the user tag with id ${updatedUserTag.id} to be updated in the remote database...")
            val messageResponse = userTagRepository.updateUserTag(updatedUserTag)

            Log.i("NotesViewModel", messageResponse.message)
        }


        /**
         * Deletes the [Note] with the given [Note.id] from the remote database
         * @param noteId [Note.id] from the [Note] to delete from the server
         */
        fun deleteNote(noteId: Long) {
            Log.i("NotesViewModel", "Deleting the note with id $noteId from the remote database...")
            val messageResponse = noteRepository.deleteNote(noteId)

            Log.i("NotesViewModel", messageResponse.message)
        }

        /**
         * Deletes the [UserTag] with the given [UserTag.id] from the remote database
         * @param userTagId [UserTag.id] from the [UserTag] to delete from the server
         */
        fun deleteUserTag(userTagId: Long) {
            Log.i("NotesViewModel", "Deleting the user tag with id $userTagId from the remote database...")
            val messageResponse = noteRepository.deleteNote(userTagId)

            Log.i("NotesViewModel", messageResponse.message)
        }
    }
}