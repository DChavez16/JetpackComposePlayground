package com.feature.widgets.hiltEntryPoint

import com.example.notes.RemoteNoteRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface WidgetsEntryPoint {
    fun getNoteRepository(): RemoteNoteRepository
}