package com.example.notification

import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.persistentWork.NotificationWorker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Duration
import javax.inject.Inject

interface NotificationRepository {
    fun showNotification()
    fun cancelNotification()
    fun isWorkRunning(): Flow<Boolean>
}


class WorkNotificationRepository @Inject constructor(
    private val workManager: WorkManager
) : NotificationRepository {

    companion object {
        const val WORK_TAG = "WorkNotification"
        const val WORK_NAME = "Work notification"
    }

    // Starts the notification task
    override fun showNotification() {
        // Creates the notification one time work request builder
        val notificationRequestBuilder = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(Duration.ofSeconds(5))
            .addTag(WORK_TAG)
            .build()

        // Enqueues the notification work request
        workManager.enqueueUniqueWork(
            /* uniqueWorkName = */ WORK_NAME,
            /* existingWorkPolicy = */ ExistingWorkPolicy.KEEP,
            /* work = */ notificationRequestBuilder
        )
    }

    // Stops the notification task
    override fun cancelNotification() {
        // Cancelling by name
        //workManager.cancelUniqueWork(WORK_NAME)

        // Cancelling by tag
        workManager.cancelAllWorkByTag(WORK_TAG)
    }

    // Returns a flow indicating if the work request is enqueued
    override fun isWorkRunning(): Flow<Boolean> =
        workManager.getWorkInfosByTagFlow(WORK_TAG).map { workInfos ->
            workInfos.any { it.state == WorkInfo.State.ENQUEUED }
        }
}