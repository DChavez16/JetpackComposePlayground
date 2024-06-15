package com.example.notification

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class NotificationRepositoryModule {

    /**
     * Binds a [WorkNotificationRepository] implementation of [NotificationRepository]
     */
    @Binds
    abstract fun bindWorkNotificationRepository(
        workNotificationRepository: WorkNotificationRepository
    ): NotificationRepository
}