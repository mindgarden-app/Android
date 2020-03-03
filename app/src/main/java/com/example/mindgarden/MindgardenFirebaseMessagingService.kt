package com.example.mindgarden

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.mindgarden.ui.login.LoginActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*

class MindgardenFirebaseMessagingService : FirebaseMessagingService(){

    //토큰 생성 모니터링
    //새 토큰 생성시 호출
    override fun onNewToken(token: String) {
        Log.e("FCM Log" ,"Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.

        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.
        Log.d("FCM Log", "sendRegistrationTokenToServer($token)")
    }


    override fun onMessageReceived(rm: RemoteMessage) {
        super.onMessageReceived(rm)
        rm.data.isNotEmpty().let {
            Log.d("FCM Log", "Message data payload: " + rm.data)
            if (/* Check if data needs to be processed by long running job */ true) {
               // scheduleJob()
            } else {
                // Handle message within 10 seconds
                handleNow()
            }

            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT)

            val channelId = getString(R.string.channel_id)

            val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.icn_pup_topbar)
                .setLargeIcon(BitmapFactory.decodeResource(applicationContext.resources, R.drawable.icn_puptree))
                .setContentTitle("MindGarden")
                .setContentText("마음의 정원을 돌볼 시간입니다.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Since android Oreo notification channel is needed.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT)
                notificationManager.createNotificationChannel(channel)
            }

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
        }

        rm.notification?.let {
            Log.d("FCM Log", "Message Notification Body: ${it.body}")
        }

    }


    private fun handleNow() {
        Log.d("FCM Log", "Short lived task is done.")
    }

    private fun sendNotification(messageBody: String) {
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT)

        val channelId = getString(R.string.channel_id)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.icn_pup_topbar)
            .setLargeIcon(BitmapFactory.decodeResource(applicationContext.resources, R.drawable.icn_puptree))
            .setContentTitle("MindGarden")
            .setContentText("마음의 정원을 돌볼 시간입니다.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }



}