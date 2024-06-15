package com.example.preferences.di

import com.example.preferences.LocalPreferencesRepository
import com.example.preferences.PreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
abstract class PreferencesRepositoryModule {

    /**
     * Binds a [LocalPreferencesRepository] as a [PreferencesRepository] implementation
      */
    @Binds
    abstract fun bindLocalPreferencesRepository(
        localPreferencesRepository: LocalPreferencesRepository
    ): PreferencesRepository
}