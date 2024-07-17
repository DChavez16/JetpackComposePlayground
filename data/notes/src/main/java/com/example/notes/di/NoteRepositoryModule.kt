package com.example.notes.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
 @InstallIn(ViewModelComponent::class)
abstract class NoteRepositoryModule {

    /**
     * Binds a RemoteNoteRepository implementation of NoteRepository
     */
    // TODO Bind a RemoteNoteRepository implementation of NoteRepository
}