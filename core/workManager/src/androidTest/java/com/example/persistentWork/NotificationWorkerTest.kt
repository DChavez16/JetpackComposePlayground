package com.example.persistentWork

import android.content.Context
import android.util.Log
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.Configuration
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.impl.utils.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

const val WORK_TAG = "WorkNotification"
const val WORK_NAME = "Work notification"

@RunWith(AndroidJUnit4::class)
class NotificationWorkerTest {

    @Before
    fun setup() {
        val context = getApplicationContext<Context>()
        val config = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setExecutor(SynchronousExecutor())
            .build()

        // Initialize WorkManager for instrumentation tests
        WorkManagerTestInitHelper.initializeTestWorkManager(context, config)
    }



    // Starts work and confirms it completed succesfully after 5 seconds
    @Test
    fun `notification worker completed succesfully`() {
        // Create work request
        val request = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(5, TimeUnit.SECONDS)
            .addTag(WORK_TAG)
            .build()

        // Creates workManager and testDriver instances
        val workManager = WorkManager.getInstance(getApplicationContext())
        val testDriver = WorkManagerTestInitHelper.getTestDriver(getApplicationContext())
        // Enqueue work request
        workManager.enqueueUniqueWork(
            /* uniqueWorkName = */ WORK_NAME,
            /* existingWorkPolicy = */ androidx.work.ExistingWorkPolicy.KEEP,
            /* work = */ request
        ).result.get()

        // Set initial delay as met
        testDriver?.setInitialDelayMet(request.id)

        // Get WorkInfo
        val workInfo = workManager.getWorkInfoById(request.id).get()

        // Assert the work state is SUCCEEDED
        assertEquals(
            /* expected = */ workInfo.state,
            /* actual = */ (WorkInfo.State.SUCCEEDED)
        )
    }


    // Starts work and cancels it
    @Test
    fun `cancel notification worker after start`() {
        // Create work request
        val request = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(5, TimeUnit.SECONDS)
            .addTag(WORK_TAG)
            .build()

        // Creates workManager instance
        val workManager = WorkManager.getInstance(getApplicationContext())
        // Enqueue work request
        workManager.enqueueUniqueWork(
            /* uniqueWorkName = */ WORK_NAME,
            /* existingWorkPolicy = */ androidx.work.ExistingWorkPolicy.KEEP,
            /* work = */ request
        ).result.get()

        // Cancel notification work
        workManager.cancelAllWorkByTag(WORK_TAG)

        // Get WorkInfo
        val workInfo = workManager.getWorkInfoById(request.id).get()

        // Assert the work state is CANCELLED
        assertEquals(
            /* expected = */ workInfo.state,
            /* actual = */ (WorkInfo.State.CANCELLED)
        )
    }
}