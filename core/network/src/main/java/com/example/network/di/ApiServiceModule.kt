package com.example.network.di

import com.example.network.api.NoteApiService
import com.example.network.api.UserTagApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit


@Module
@InstallIn(SingletonComponent::class)
object ApiServiceModule {

    /**
     * Provides a Retrofit object
     */
    @Provides
    fun providesRetrofit(): Retrofit {
        // Base URL to be used by retrofit
        val baseUrl = "http://192.168.0.9:8080"

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    /**
     * Provides a retrofitService instance as a NoteApiService
     */
    @Provides
    fun providesNoteApiService(
        retrofit: Retrofit
    ): NoteApiService = retrofit.create(NoteApiService::class.java)

    /**
     * Provides a retrofitService instance as a UserTagApiService
     */
    @Provides
    fun provideUserTagApiService(
        retrofit: Retrofit
    ): UserTagApiService = retrofit.create(UserTagApiService::class.java)
}