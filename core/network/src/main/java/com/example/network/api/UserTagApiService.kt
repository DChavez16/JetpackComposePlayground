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
 * Public interface that exposes the UserTagApiService methods
 */
interface UserTagApiService {

    /**
     * Create user tag
     * @param userTag [UserTag] to be send to the server for storage
     * @return [MessageResponse] containg the operation results
     */
    @POST("tags")
    fun createUserTag(
        @Body userTag: UserTag
    ): MessageResponse


    /**
     * Read all user tags
     * @return [Flow] of a [List] of [UserTag]
     */
    @GET("tags")
    fun getUserTags(): Flow<List<UserTag>>

    /**
     * Read the notes of the [UserTag]
     * @param userTagId Id from the [UserTag] to get the notes
     * @return List of [Note] related to the [UserTag], the list can be empty
     */
    @GET("tags/{id}/notes")
    fun getUserTagNotes(
        @Path("id") userTagId: Long
    ): List<Note>


    /**
     * Update given user tag
     * @param userTag [UserTag] to be updated with the new parameters
     * @return [MessageResponse] containg the operation results
     */
    @PUT("tags")
    fun deleteUserTag(
        @Body userTag: UserTag
    ): MessageResponse


    /**
     * Delete user tag with the given id
     * @param userTagId Id from the [UserTag] to delete
     * @return [MessageResponse] containg the operation results
     */
    @DELETE("tags/{id}")
    fun deleteUserTag(
        @Path("id") userTagId: Long
    ): MessageResponse
}