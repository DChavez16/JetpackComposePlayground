package com.example.notes

import com.example.model.MessageResponse
import com.example.model.Note
import com.example.network.api.NoteApiService
import javax.inject.Inject


interface NoteRepository {
    suspend fun createNote(note: Note): MessageResponse
    suspend fun getNotes(): List<Note>
    suspend fun updateNote(note: Note): MessageResponse
    suspend fun deleteNote(noteId: Long): MessageResponse
}


class RemoteNoteRepository @Inject constructor(
    private val noteApiService: NoteApiService
) : NoteRepository {

    /**
     * Calls the Api service to store the given [Note] in the remote database
     * @param note Note to be sent to the server for storage
     * @return [MessageResponse] containg the operation results
     */
    override suspend fun createNote(note: Note): MessageResponse = noteApiService.createNote(note)

    /**
     * Calls the Api Service to get a [List] of [Note]
     * @return List of Note
     */
    override suspend fun getNotes(): List<Note> =
        noteApiService.getNotes().map { note ->
            note.copy(
                userTags = noteApiService.getNoteUserTags(note.id)
            )
        }

    /**
     * Calls the Api Service to update the given [Note] in the remote database
     * @param note Note to be updated with the new parameters
     * @return [MessageResponse] containg the operation results
     */
    override suspend fun updateNote(note: Note): MessageResponse = noteApiService.updateNote(note)

    /**
     * Calls the Api Service to update the [Note] with the given [noteId] from the remote database
     * @param noteId Id from the [Note] to delete
     * @return [MessageResponse] containg the operation results
     */
    override suspend fun deleteNote(noteId: Long): MessageResponse = noteApiService.deleteNote(noteId)
}