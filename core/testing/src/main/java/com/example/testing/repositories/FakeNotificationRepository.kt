package com.example.testing.repositories

import com.example.notification.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeNotificationRepository : NotificationRepository {

    private var isWorkRunningFlow = MutableStateFlow(false)

    override fun showNotification() {
        isWorkRunningFlow.value = true
    }

    override fun cancelNotification() {
        isWorkRunningFlow.value = false
    }

    override fun isWorkRunning(): Flow<Boolean> =
        isWorkRunningFlow

    fun simulateWorkCompletion() {
        isWorkRunningFlow.value = false
    }

}