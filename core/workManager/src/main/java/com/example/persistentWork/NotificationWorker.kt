package com.example.persistentWork

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.workmanager.R

// Notification constants
const val CHANNEL_ID = "WORK_NOTIFICATION"
const val CHANNEL_NAME = "Work Notification Example"
const val NOTIFICATION_ID = 1

class NotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {

        makeNotification(
            title = "Work Notification Example",
            text = "This is a work notification example",
            context = applicationContext
        )

        return Result.success()
    }
}

fun makeNotification(
    title: String,
    text: String,
    context: Context
) {
    // Create the NotificationChannel only on API +26
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR_0_1) {
        // Creates the Notification Channel
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        with(channel) {
            vibrationPattern = LongArray(0)
        }

        // Creates the NotificationManager and assigns it the created NotificationChannel
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    // Builds the notification
    val workNotificationBuilder = Notification.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.jetpack_compose_icon)
        .setContentTitle(title)
        .setContentText(text)
        .setAutoCancel(true)

    // Checks if the notification permission has been granted
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.
        return
    }

    // Displays the notification
    NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, workNotificationBuilder.build())
}