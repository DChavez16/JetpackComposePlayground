package com.example.network.api

import com.example.model.MessageResponse
import com.example.model.Note
import com.example.model.UserTag
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


/**
 * Public interface that exposes the NoteApiService methods
 */
interface NoteApiService {

    /**
     * Create note
     * @param note [Note] to be sent to the server for storage
     * @return [MessageResponse] containg the operation results
     */
    @POST("notes")
    fun createNote(
        @Body note: Note
    ): MessageResponse


    /**
     * Read all notes
     * @return [Flow] of a [List] of [Note]
     */
    @GET("notes")
    fun getNotes(): Flow<List<Note>>

    /**
     * Read the user tags of the [Note]
     * @param noteId Id from the [Note] to get the user tags
     * @return List of [UserTag] related to the [Note], the list can be empty
     */
    @GET("notes/{id}/userTags")
    fun getNoteUserTags(
        @Path("id") noteId: Long
    ): List<UserTag>


    /**
     * Update given note
     * @param note [Note] to be updated with the new parameters
     * @return [MessageResponse] containg the operation results
     */
    @PUT("notes")
    fun updateNote(
        @Body note: Note
    ): MessageResponse


    /**
     * Delete note with the given id
     * @param noteId Id from the [Note] to delete
     * @return [MessageResponse] containg the operation results
     */
    @DELETE("notes/{id}")
    fun deleteNote(
        @Path("id") noteId: Long
    ): MessageResponse
}