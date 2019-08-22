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
import com.example.mindgarden.R
import android.util.Log
import android.widget.TimePicker
import android.app.AlarmManager
import android.content.SharedPreferences
import com.example.mindgarden.BroadCastReceiver.BroadcastD
import com.example.mindgarden.DB.SharedPreferenceController
import org.jetbrains.anko.toast


/*
[완료]특정 시간에 알림오도록 설정 완료 _ 2019.07.29
[완료] 토글버튼 상태 유지하기 _2010.07_30
[완료]알림 설정 토글 버튼 상태가 false일 경우 알람 해지하기 cancleAlarm() 구현하기 _2010.07_30
[완료] 코드 다듬기 _2010.07_30
[수정필요] 1분차이남
 */
class AlarmSettingActivity : AppCompatActivity() {

    @SuppressLint("ResourceAsColor")
    val cal = Calendar.getInstance()
    val CHANNEL_ID = "MINDGARDEN"
    lateinit var alarmSwitch: Switch
    lateinit var alarmManager: AlarmManager
    var triggerTime : Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_setting)
        txtSetting.text = "알림 설정"

        //back button
        btnBack.setOnClickListener {
            val intent = Intent(this, MypageActivity::class.java)
            startActivity(intent)
            finish()
        }

        alarmSwitch = findViewById (R.id.alarmSwitch)    //스위치 토글 버튼
        alarmSwitch.isChecked = SharedPreferenceController.getAlarmState(this)

        btnSetTimeState(alarmSwitch.isChecked)

        alarmSwitch.setOnCheckedChangeListener { _, isChecked ->
            btnSetTimeState(isChecked)
            if(!isChecked) alarmCancle()    //알람취소
        }
    }

    //switch 상태 저장
    override fun onPause() {
        super.onPause()
        SharedPreferenceController.setAlarmState(this ,alarmSwitch.isChecked)
    }

    //시간설정 버튼
    fun btnSetTimeState(isChecked : Boolean){
        val btnSetTime: Button = findViewById(R.id.btnSetTime)   //시간 설정 버튼
        btnSetTime.setTextColor(Color.BLACK)

        if (isChecked) {
            //알람 설정
            btnSetTime.setOnClickListener {
                showDialog()
            }

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

        //시간설정
        alarmTimePicker.setOnTimeChangedListener { view, hourOfDay, minute ->
            alarmTimePicker(hourOfDay, minute)
        }

        builder.setView(dialogView)
            .setPositiveButton("확인") { dialogInterface, i ->
                //알람설정
                setAlarm(triggerTime)
            }
            .setNeutralButton("취소") { dialogInterface, i ->
                /* 취소일 때 아무 액션이 없으므로 빈칸 */
            }
        var builderNew: AlertDialog = builder.show()
        builderNew.window.setBackgroundDrawableResource(R.drawable.round_layout_border)
        builderNew.show()

    }

    //시간설정
    fun alarmTimePicker(hourOfDay: Int, minute : Int){

        cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
        cal.set(Calendar.MINUTE, minute)

        triggerTime = cal.timeInMillis

        //이미 지난 시간으로 설정했을 경우 다음날 알림으로 설정해주기
        if(System.currentTimeMillis() > triggerTime){
            triggerTime += 24 * 60 * 60 * 1000 //24시간후
        }
    }

    //알람설정
    fun setAlarm(tgTime: Long){
        setChannel()

        //알람이 발생했을 경우 BroadcastD에게 방송을 해주기 위해 명시
       val intent = Intent(this, BroadcastD::class.java)
        val pendingIntent : PendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)

        //알람 예약
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager     //AlarmManager
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, tgTime, 24 * 60 * 60 * 1000 ,pendingIntent)

    }

    //알람 해제
    fun alarmCancle(){
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager     //AlarmManager

        val intent = Intent(this, BroadcastD::class.java)
        val pendingIntent : PendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)

        alarmManager.cancel(pendingIntent)
        toast("알람이 해제되었습니다.")
    }

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