package com.example.alarmmanager.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.alarmmanager.R

const val TAG = "AlarmReceiver"
const val NOTIFICATION_ID = 2
const val CHANNEL_ID = "ALARM_NOTIFICATION"
const val CHANNEL_NAME = "Alarm Notification Example"

class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        Log.d(TAG, "Alarm received")

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

        // Create a Notification Manager
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        // Build the notification to show
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Alarm triggered")
            .setContentText("The alarm has been triggered succesfully")
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            .setSmallIcon(R.drawable.jetpack_compose_icon)
            .build()

        // Show the notification
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
    }
}