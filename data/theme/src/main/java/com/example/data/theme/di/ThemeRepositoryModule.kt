package com.example.data.theme.di

import com.example.data.theme.LocalThemeRepository
import com.example.data.theme.ThemeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
abstract class ThemeRepositoryModule {

    /**
     * Binds a [LocalThemeRepository] as a [ThemeRepository] implementation
     */
    @Binds
    abstract fun bindLocalThemeRepository(
        localThemeRepository: LocalThemeRepository
    ): ThemeRepository
}