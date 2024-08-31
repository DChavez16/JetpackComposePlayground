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
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject


internal sealed interface NotesUiState {
    data class Success(val notes: List<Note>) : NotesUiState
    data class Error(val errorMessage: String) : NotesUiState
    data object Loading : NotesUiState
}

internal sealed interface UserTagUiState {
    data class Success(val userTags: List<UserTag>) : UserTagUiState
    data class Error(val errorMessage: String) : UserTagUiState
    data object Loading : UserTagUiState
}


@HiltViewModel
internal class NotesViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val userTagRepository: UserTagRepository
) : ViewModel() {

    // Backing property of the NotesUiState to avoid state updates from other classes
    private val _notesUiState = MutableStateFlow<NotesUiState>(NotesUiState.Loading)
    // The UI collects from this StateFlow to get its state updates
    val notesUiState: StateFlow<NotesUiState> = _notesUiState

    // Backing property for UserTag list to avoid state updates from other classes
    private val _userTags = MutableStateFlow<UserTagUiState>(UserTagUiState.Loading)
    // The UI collects from this StateFlow to get its state updates
    val userTags: StateFlow<UserTagUiState> = _userTags

    // Backing property for the current selected note to avoid state updates from other classes
    private val _currentSelectedNote = MutableStateFlow(Note())
    // The UI collects from this StateFlow to get its state updates
    val currentSelectedNote: StateFlow<Note> = _currentSelectedNote

    // Backing property to know if the current view mode is the list view to avoid state updates from other classes
    private val _isListView = MutableStateFlow(true)
    // The UI collects from this StateFlow to get its state updates
    val isListView: StateFlow<Boolean> = _isListView


    // Block of code executed at ViewModel creation
    init {
        // Get a list of notes from the repository
        getNotes()

        // Get a list of user notes from the repository
        getUserTags()
    }

    /**
     * Attemps to a [List] of [Note] from the repository, stores the result in the [_notesUiState]
     */
    fun getNotes() {
        viewModelScope.launch {
            // Sets the _notesUiState to its Loading state
            _notesUiState.value = NotesUiState.Loading
            Log.i("NotesViewModel", "Collecting notes from the remote database")

            try {
                // Try to get a list of notes from the repository and assign it to the _notesUiState Success state
                _notesUiState.value = NotesUiState.Success(noteRepository.getNotes())
                Log.i("NotesViewModel", "Notes collected succesfully")
            } catch (e: IOException) {
                // Sets the _notesUiState to its Error state containing the IO error message text
                _notesUiState.value = NotesUiState.Error(e.message.toString())
                Log.e("NotesViewModel", "IO Error collecting notes: ${e.message}")
            } catch (e: Exception) {
                // Sets the _notesUiState to its Error state containing the error message text
                _notesUiState.value = NotesUiState.Error(e.message.toString())
                Log.e("NotesViewModel", "Error collecting notes: ${e.message}")
            }
        }
    }

    /**
     * Attemps to a [List] of [UserTag] from the repository, stores the result in the [_userTags]
     */
    fun getUserTags() {
        viewModelScope.launch {
            // Sets the _userTags to its Loading state
            _userTags.value = UserTagUiState.Loading
            Log.i("NotesViewModel", "Collecting user tags from the remote database")

            try {
                // Try to get a list of user tags from the repository and assign it to the _userTags Success state
                _userTags.value = UserTagUiState.Success(userTagRepository.getUserTags())
                Log.i("NotesViewModel", "User tags collected succesfully")
            } catch (e: IOException) {
                // Sets the _userTags to its Error state containing the IO error message text
                _userTags.value = UserTagUiState.Error(e.message.toString())
                Log.e("NotesViewModel", "IO Error collecting user tags: ${e.message}")
            } catch (e: Exception) {
                // Sets the _userTags to its Error state containing the error message text
                _userTags.value = UserTagUiState.Error(e.message.toString())
                Log.e("NotesViewModel", "Error collecting user tags: ${e.message}")
            }
        }
    }

    /**
     * Creates a new [Note] storing it in a remote database
     * @param newNote [Note] to be send to the server for storage
     */
    fun createNote(newNote: Note) {
        viewModelScope.launch {
            Log.i("NotesViewModel", "Uploading a note to the remote database...")
            val messageResponse = noteRepository.createNote(newNote)

            Log.i("NotesViewModel", messageResponse.message)
        }
    }

    /**
     * Creates a new [UserTag] storing it in a remote database
     * @param newUserTag [UserTag] to be send to the server for storage
     */
    fun createUserTag(newUserTag: UserTag) {
        viewModelScope.launch {
            Log.i("NotesViewModel", "Uploading an user tag to the remote database...")
            val messageResponse = userTagRepository.createUserTag(newUserTag)

            Log.i("NotesViewModel", messageResponse.message)
        }
    }


    /**
     * Updates the given [Note] in the remote database
     * @param updatedNote [Note] to be updated in the server
     */
    fun updateNote(updatedNote: Note) {
        viewModelScope.launch {
            Log.i(
                "NotesViewModel",
                "Sending the note with id ${updatedNote.id} to be updated in the remote database..."
            )
            val messageResponse = noteRepository.updateNote(updatedNote)

            Log.i("NotesViewModel", messageResponse.message)
        }
    }

    /**
     * Updates the given [UserTag] in the remote database
     * @param updatedUserTag [UserTag] to be updated in the server
     */
    fun updateUserTag(updatedUserTag: UserTag) {
        viewModelScope.launch {
            Log.i(
                "NotesViewModel",
                "Sending the user tag with id ${updatedUserTag.id} to be updated in the remote database..."
            )
            val messageResponse = userTagRepository.updateUserTag(updatedUserTag)

            Log.i("NotesViewModel", messageResponse.message)
        }
    }


    /**
     * Deletes the [Note] with the given [Note.id] from the remote database
     * @param noteId [Note.id] from the [Note] to delete from the server
     */
    fun deleteNote(noteId: Long) {
        viewModelScope.launch {
            Log.i("NotesViewModel", "Deleting the note with id $noteId from the remote database...")
            val messageResponse = noteRepository.deleteNote(noteId)

            Log.i("NotesViewModel", messageResponse.message)
        }
    }

    /**
     * Deletes the [UserTag] with the given [UserTag.id] from the remote database
     * @param userTagId [UserTag.id] from the [UserTag] to delete from the server
     */
    fun deleteUserTag(userTagId: Long) {
        viewModelScope.launch {
            Log.i(
                "NotesViewModel",
                "Deleting the user tag with id $userTagId from the remote database..."
            )
            val messageResponse = userTagRepository.deleteUserTag(userTagId)

            Log.i("NotesViewModel", messageResponse.message)
        }
    }

    fun changeViewMode() {
        _isListView.value = !_isListView.value
    }

    fun changeCurrentSelectedNote(note: Note) {
        _currentSelectedNote.value = note
    }
}