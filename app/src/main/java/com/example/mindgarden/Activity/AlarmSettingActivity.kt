package com.example.mindgarden.Activity

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.*
import kotlinx.android.synthetic.main.toolbar_mypage_main.*
import org.jetbrains.anko.toast
import java.util.*
import com.example.mindgarden.R
import android.util.Log
import android.widget.TimePicker
import android.app.AlarmManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.mindgarden.Service.BroadcastD


/*
[완료]특정 시간에 알림오도록 설정 완료 _ 2019.07.29
[미완]내부 DB를 통해 버튼 상태(click or not) 유지 구현하기
[미완]알림 설정 토글 버튼 상태가 false일 경우 알람 해지하기 cancleAlarm() 구현하기
[미완] 코드 다듬기
 */
class AlarmSettingActivity : AppCompatActivity() {

    @SuppressLint("ResourceAsColor")
    val cal = Calendar.getInstance()
    val CHANNEL_ID = "MINDGARDEN"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_setting)
        txtSetting.text = "알람 설정"

        //back button
        btnBack.setOnClickListener {
            val intent = Intent(this, MypageActivity::class.java)
            startActivity(intent)
            finish()
        }

        val alarmSwitch: Switch = findViewById(R.id.alarmSwitch)    //스위치 토글 버튼
        alarmSwitch.setOnCheckedChangeListener { _, isChecked ->
           alarmSwitch(isChecked)
        }


    }

    fun alarmSwitch(isChecked : Boolean){
        val btnSetTime: Button =findViewById(R.id.btnSetTime)   //시간 설정 버튼
        btnSetTime.setTextColor(Color.BLACK)

        if (isChecked) {
            //알람 설정
            btnSetTime.setOnClickListener {
                showDialog() }
        } else {
            //버튼 비활성화 + 글씨 색 바꾸기
            btnSetTime.isClickable = false
            btnSetTime.setTextColor(Color.parseColor("#c6c6c6"))
        }
    }

    //알림 시간을 설정하는 다이얼로그
    fun showDialog(){

        val builder = AlertDialog.Builder(this, R.style.AlarmDialogStyle)

        val dialogView = layoutInflater.inflate(R.layout.dialog_alarm_setting, null)

        val alarmTimePicker= dialogView.findViewById<TimePicker>(R.id.alarmTimePicker)

        var timeA : Long = 0
        var triggerTime : Long = 0

        alarmTimePicker.setOnTimeChangedListener { view, hourOfDay, minute ->
            Toast.makeText(this, hourOfDay.toString() + " : " + minute +" : " , Toast.LENGTH_LONG).show()

            cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
            cal.set(Calendar.MINUTE, minute)

            timeA  = System.currentTimeMillis()
            triggerTime = cal.timeInMillis
            Log.e("triggerTime", triggerTime.toString())

            //이미 지난 시간으로 설정했을 경우 다음날 알림으로 설정해주기
            if(timeA > triggerTime) triggerTime += 24 * 60 * 60 * 1000
        }


        builder.setView(dialogView)
            .setPositiveButton("확인") { dialogInterface, i ->
                setAlarm(triggerTime)
            }
            .setNeutralButton("취소") { dialogInterface, i ->
                /* 취소일 때 아무 액션이 없으므로 빈칸 */
            }
        var builderNew: AlertDialog = builder.show()
        builderNew.window.setBackgroundDrawableResource(R.drawable.round_layout_border)
        builderNew.show()

    }

    //알람 setting
    fun setAlarm(tgTime: Long){
        setChannel()

        Log.e("tgTime", tgTime.toString())
        var triggerTime : Long = tgTime //예약시간
        var intervalTime : Long = 24 * 60 * 60 * 1000  //24시간

        //알람이 발생했을 경우 BroadcastD에게 방송을 해주기 위해 명시
       val intent = Intent(this, BroadcastD::class.java)
        val sender : PendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)

        //알람 예약
        val am = getSystemService(Context.ALARM_SERVICE) as AlarmManager     //AlarmManager
        am.setRepeating(AlarmManager.RTC, triggerTime, intervalTime ,sender)

    }

    /*
알람 해제
fun cancelAlarm(){
    am.cancle()
}
 */
    //notification을 위한 채널설정
    fun setChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

    }

}