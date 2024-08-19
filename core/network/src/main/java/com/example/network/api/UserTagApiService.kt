package com.example.network.api

import com.example.model.MessageResponse
import com.example.model.UserTag
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
    @POST("/tags")
    suspend fun createUserTag(
        @Body userTag: UserTag
    ): MessageResponse


    /**
     * Read all user tags
     * @return [List] of [UserTag]
     */
    @GET("/tags")
    suspend fun getUserTags(): List<UserTag>


    /**
     * Update given user tag
     * @param userTag [UserTag] to be updated with the new parameters
     * @return [MessageResponse] containg the operation results
     */
    @PUT("/tags")
    suspend fun updateUserTag(
        @Body userTag: UserTag
    ): MessageResponse


    /**
     * Delete user tag with the given id
     * @param userTagId Id from the [UserTag] to delete
     * @return [MessageResponse] containg the operation results
     */
    @DELETE("/tags/{id}")
    suspend fun deleteUserTag(
        @Path("id") userTagId: Long
    ): MessageResponse
}