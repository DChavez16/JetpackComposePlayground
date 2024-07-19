package com.example.notes

import com.example.model.MessageResponse
import com.example.model.Note
import com.example.network.api.NoteApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


interface NoteRepository {
    fun createNote(note: Note): MessageResponse
    fun getNotes(): Flow<List<Note>>
    fun updateNote(note: Note): MessageResponse
    fun deleteNote(noteId: Long): MessageResponse
}


class RemoteNoteRepository(
    private val noteApiService: NoteApiService
) : NoteRepository {

    /**
     * Calls the Api service to store the given [Note] in the remote database
     * @param note Note to be sent to the server for storage
     * @return [MessageResponse] containg the operation results
     */
    override fun createNote(note: Note): MessageResponse = noteApiService.createNote(note)

    /**
     * Calls the Api Service to get a [Flow] of a [List] of [Note]
     * @return Flow of a List of Note
     */
    override fun getNotes(): Flow<List<Note>> =
        noteApiService.getNotes().map { notesList ->
            notesList.map { note ->
                val noteUserTags = noteApiService.getNoteUserTags(note.id)

                note.copy(userTags = noteUserTags)

//                val newNote = note.copy(userTags = noteUserTags)

//                newNote
            }
        }

    /**
     * Calls the Api Service to update the given [Note] in the remote database
     * @param note Note to be updated with the new parameters
     * @return [MessageResponse] containg the operation results
     */
    override fun updateNote(note: Note): MessageResponse = noteApiService.updateNote(note)

    /**
     * Calls the Api Service to update the [Note] with the given [noteId] from the remote database
     * @param noteId Id from the [Note] to delete
     * @return [MessageResponse] containg the operation results
     */
    override fun deleteNote(noteId: Long): MessageResponse = noteApiService.deleteNote(noteId)
}