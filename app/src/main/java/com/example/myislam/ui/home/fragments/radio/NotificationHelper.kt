package com.example.myislam.ui.home.fragments.radio

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service.NOTIFICATION_SERVICE
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.myislam.R
import com.example.myislam.ui.home.HomeActivity
import com.example.myislam.utils.Constants.CHANNEL_ID
import com.example.myislam.utils.Constants.CHANNEL_NAME
import com.example.myislam.utils.Constants.RADIO_SERVICE_ID
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class NotificationHelper @Inject constructor(
    @ApplicationContext private val applicationContext: Context
) {

    private val notificationManager by lazy {
        (applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
    }


    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            notificationManager.createNotificationChannel(channel)
        }
    }

    fun createNotification(context: Context, customContent: RemoteViews): Notification {
        val notificationIntent = Intent(context, HomeActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            1,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat
            .Builder(context, CHANNEL_ID)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.radio)
            .setCustomContentView(customContent)
            .setOnlyAlertOnce(true)
            .setSilent(true)

        return notificationBuilder.build()
    }

    fun updateNotification(notification: Notification) {
        notificationManager.notify(RADIO_SERVICE_ID, notification)
    }
}
