package com.example.mindgarden.Service

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import com.example.mindgarden.Activity.MainActivity
import com.example.mindgarden.R


class BroadcastD() : BroadcastReceiver(){
    val CHANNEL_ID = "MINDGARDEN"

    override fun onReceive(context: Context?, intent: Intent?) {
        //푸시 알림 설정
        Log.e("broadcast", "broadcastOK")

        val pendingIntent = PendingIntent.getActivity(context, 0, Intent(context, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(context!!, CHANNEL_ID)
            .setSmallIcon(R.drawable.img_weather11_none)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.img_weather11_none))
            .setContentTitle("마인드 가든")
            .setContentText("오늘의 정원을 가꿀 시간이에요.")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("당신의 이야기를 들려주세요"))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(0, builder.build())
        }
    }
}
