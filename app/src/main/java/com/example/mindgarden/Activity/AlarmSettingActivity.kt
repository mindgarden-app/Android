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


class AlarmSettingActivity : AppCompatActivity() {

    @SuppressLint("ResourceAsColor")
    val c = Calendar.getInstance()
    var hour = c.get(Calendar.HOUR)
    var minute = c.get(Calendar.MINUTE)
    var time : Long  = 0
    val CHANNEL_ID = "MINDGARDEN"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_setting)
        txtSetting.text = "알람 설정"

        btnBack.setOnClickListener {
            val intent = Intent(this, MypageActivity::class.java)
            // 백 스페이스 누르면 다시 메인 페이지로

            startActivity(intent)

            finish()
        }

        val alarmSwitch: Switch = findViewById(R.id.alarmSwitch)
        val btnSetTime: Button =findViewById(R.id.btnSetTime)

        alarmSwitch.setOnCheckedChangeListener { _, isChecked ->
            btnSetTime.setTextColor(Color.BLACK)
            if (isChecked) {
                // The toggle is enabled
                btnSetTime.setOnClickListener {
                  showDialog()
                }
                toast("ddksk")
                ///팝업창
            } else {
                //버튼 비활성화 + 글씨 색 바꾸기
                btnSetTime.isClickable = false
                val color : String = "#c6c6c6"
                btnSetTime.setTextColor(Color.parseColor(color))
            }
        }
    }


    fun showDialog(){

        val builder = AlertDialog.Builder(this, R.style.AlarmDialogStyle)

        val dialogView = layoutInflater.inflate(R.layout.dialog_alarm_setting, null)

        val alarmTimePicker= dialogView.findViewById<TimePicker>(R.id.alarmTimePicker)
        builder.setView(dialogView)

        //val dialog = builder.create()

        //dialog.getWindow().setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        builder.setView(dialogView)
            .setPositiveButton("확인") { dialogInterface, i ->

                setAlarm(time)
                Log.e("time", time.toString())

            }
            .setNeutralButton("취소") { dialogInterface, i ->
                /* 취소일 때 아무 액션이 없으므로 빈칸 */
            }
        var builderNew: AlertDialog = builder.show()
        builderNew.window.setBackgroundDrawableResource(R.drawable.round_layout_border)
        builderNew.show()
        //builder.show()
        alarmTimePicker.setOnTimeChangedListener{
                view,hourOfDay,minute->Toast.makeText(this, hourOfDay.toString() + " : " + minute +" : " , Toast.LENGTH_LONG).show()
                time = getPickerTime(alarmTimePicker)
        }

    }

    /*
    알람 해제
     */

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

    //알람 매니저 setting
    fun setAlarm(time : Long){
        setChannel()

        //AlarmManager에게 알림 행위 등록
        val am = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        //알람이 발생했을 경우 BroadcastD에게 방송을 해주기위해 명시시
       val intent = Intent(this, BroadcastD::class.java)
        val sender : PendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)

        //알람 예약
        //RTC_WAKEUP 대기 상태일 경우 단말기를 활성화 시킴
        am.set(AlarmManager.RTC_WAKEUP, time, sender)

    }
    //Push 알림 울릴 시간 설정
    private fun getPickerTime(timePicker: TimePicker) : Long {
        hour = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.hour
        } else {
            timePicker.currentHour
        }

       minute = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            timePicker.minute
        }else{
            timePicker.currentMinute
        }


        val hourToMs1 = HourAMPM() * 3600000
        val minuteToMs = minute * 6000

        val a = (hourToMs1 + minuteToMs).toLong()

        val milliseconds : Long = a
        return milliseconds
    }

    //시간이 AM, PM인지 판별
    fun HourAMPM() : Int {
        val pmHour = hour +12
        val amHour = hour

        return if(hour>11) pmHour else amHour
    }

    // AM, PM 문자 반환 (토스트에 쓰고 있음 추후 지우자)
    private fun getAMPM(hour:Int):String{
        return if(hour>11)"PM" else "AM"
    }

    // 추후 지워도 될듯
    private fun getHourAMPM(hour:Int):Int{
        // Return the hour value for AM PM time format
        var modifiedHour = if (hour>11)hour-12 else hour
        if (modifiedHour == 0){modifiedHour = 12}
        return modifiedHour
    }


    // Custom method to set time picker time
    private fun setPickerTime(timePicker: TimePicker){
        // Get random time
        val hour = randomInRange(0,23)
        val minute = randomInRange(0,59)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.hour = hour
            timePicker.minute = minute
        }else{
            timePicker.currentHour = hour
            timePicker.currentMinute = minute
        }
    }

    // Custom method to get a random number from the provided range
    private fun randomInRange(min:Int, max:Int):Int{
        //define a new Random class
        val r = Random()

        //get the next random number within range
        // Including both minimum and maximum number
        return r.nextInt((max - min) + 1) + min;
    }

}