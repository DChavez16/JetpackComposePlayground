package com.example.usertag.di

import com.example.usertag.RemoteUserTagRepository
import com.example.usertag.UserTagRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class UserTagRepositoryModule {

    /**
     * Binds a [RemoteUserTagRepository] implementation of [UserTagRepository]
     */
    @Binds
    abstract fun bindUserTagRepository(
        remoteUserTagRepository: RemoteUserTagRepository
    ): UserTagRepository
}