package com.example.notes.di

import com.example.notes.NoteRepository
import com.example.notes.RemoteNoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
abstract class NoteRepositoryModule {

    /**
     * Binds a [RemoteNoteRepository] implementation of [NoteRepository]
     */
    @Binds
    abstract fun bindNoteRepository(
        remoteNoteRepository: RemoteNoteRepository
    ): NoteRepository
}