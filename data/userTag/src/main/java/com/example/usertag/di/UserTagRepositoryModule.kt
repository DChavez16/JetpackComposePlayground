package com.example.usertag.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
 @InstallIn(ViewModelComponent::class)
abstract class UserTagRepositoryModule {

    /**
     * Binds a RemoteUserTagRepository implementation of UserTagRepository
     */
    // TODO Bind a RemoteUserTagRepository implementation of UserTagRepository
}