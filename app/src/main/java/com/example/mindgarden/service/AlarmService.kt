package com.example.mindgarden.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.mindgarden.R
import com.example.mindgarden.broadcastReceiver.BroadcastD
import com.example.mindgarden.ui.alarm.AlarmSettingActivity

class AlarmService(): Service() {
    val CHANNEL_ID = "MINDGARDEN"
    var alarmTime : Long = 0

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null) {
            return Service.START_STICKY
        } else {
            setChannel()

            if (intent.getIntExtra("index", 0) == 0) {
                alarmTime = intent.getLongExtra("alarmTime", 0)
                setAlarm()
            } else if (intent.getIntExtra("index", 0) == 1) {
                //alarmCancel()
            }
        }

        Log.e("intent_time", alarmTime.toString())

        //setChannel()
        //setAlarm()

        return super.onStartCommand(intent, flags, startId)
    }

    //notification을 위한 채널 설정
    fun setChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            //Register the channel with the system
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            Log.e("service", "channelOk")
        }
    }

    //알람 설정
    fun setAlarm() {
        //알람이 발생했을 경우 BroadcastD에게 방송을 해주기 위해 명시
        val intent = Intent(this, BroadcastD::class.java)

        val pendingIntent : PendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)

        //알람 예약
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager     //AlarmManager
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarmTime, 24 * 60 * 60 * 1000, pendingIntent)
        //alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent)

        Log.e("service", "alarmOk")
    }
}