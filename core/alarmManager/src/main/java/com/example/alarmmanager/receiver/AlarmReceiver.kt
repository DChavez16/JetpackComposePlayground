package com.example.alarmmanager.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.alarmmanager.R

const val TAG = "AlarmReceiver"

class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        Log.d(TAG, "Alarm received")

        // Create a Notification Manager
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        // Build the notification to show
        val notification = NotificationCompat.Builder(context, "alarm_channel")
            .setContentTitle("Alarm triggered")
            .setContentText("The alarm has been triggered succesfully")
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            .setSmallIcon(R.drawable.jetpack_compose_icon)
            .build()

        // Show the notification
        notificationManager.notify(1, notification)
    }
}