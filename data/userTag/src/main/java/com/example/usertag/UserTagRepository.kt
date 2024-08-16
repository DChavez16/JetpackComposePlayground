package com.example.usertag

import com.example.model.MessageResponse
import com.example.model.UserTag
import com.example.network.api.UserTagApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


interface UserTagRepository {
    suspend fun createUserTag(userTag: UserTag): MessageResponse
    fun getUserTags(): Flow<List<UserTag>>
    suspend fun updateUserTag(userTag: UserTag): MessageResponse
    suspend fun deleteUserTag(userTagId: Long): MessageResponse
}


class RemoteUserTagRepository @Inject constructor(
    private val userTagApiService: UserTagApiService
) : UserTagRepository {

    /**
     * Calls the Api service to store the given [UserTag] in the remote database
     * @param userTag UserTag to be send to the server for storage
     * @return [MessageResponse] containg the operation results
     */
    override suspend fun createUserTag(userTag: UserTag): MessageResponse =
        userTagApiService.createUserTag(userTag)

    /**
     * Call the Api service to get a [Flow] of a [List] of [UserTag]
     * @return Flow of a List of UserTag
     */
    override fun getUserTags(): Flow<List<UserTag>> = userTagApiService.getUserTags()

    /**
     * Calls the Api service to update the given [UserTag] in the remote database
     * @param userTag UserTag to be updated with the new parameters
     * @return [MessageResponse] containg the operation results
     */
    override suspend fun updateUserTag(userTag: UserTag): MessageResponse =
        userTagApiService.updateUserTag(userTag)

    /**
     * Calls the Api Service to update the [UserTag] with the given [userTagId] from the remote database
     * @param userTagId Id from the [UserTag] to delete
     * @return [MessageResponse] containg the operation results
     */
    override suspend fun deleteUserTag(userTagId: Long): MessageResponse =
        userTagApiService.deleteUserTag(userTagId)
}