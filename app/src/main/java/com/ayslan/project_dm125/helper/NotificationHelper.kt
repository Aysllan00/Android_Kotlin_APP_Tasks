package com.ayslan.project_dm125.helper

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import androidx.core.app.NotificationManagerCompat
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.PendingIntentCompat
import androidx.core.content.ContextCompat
import com.ayslan.project_dm125.R
import com.ayslan.project_dm125.activity.MainActivity

class NotificationHelper(private val context: Context) {

    private val notificationManager = NotificationManagerCompat.from(context)

    companion object{
        const val CHANNEL_ID = "daily_notification"
    }

    init {
        val channelName = context.getString(R.string.daily_notification)
        val channel = NotificationChannel(CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        channel.description = context.getString(R.string.daily_notification_on)

        notificationManager.createNotificationChannel(channel)
    }

    fun showNotification(title: String, text: String){

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntentCompat.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT, false)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        if(ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED){
            notificationManager.notify(1, notification)
        }

    }
}