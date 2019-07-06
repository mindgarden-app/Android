package com.example.mindgarden

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.*
import kotlinx.android.synthetic.main.toolbar_mypage_main.*
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk27.coroutines.onTimeChanged
import org.jetbrains.anko.toast
import java.util.*

class AlarmSettingActivity : AppCompatActivity() {

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_setting)
        txtSetting.text = "얼람 설정"

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
                    //TODO
                    val c = Calendar.getInstance()
                    val hour = c.get(Calendar.HOUR)
                    val minute = c.get(Calendar.MINUTE)

                    val dialogView = layoutInflater.inflate(R.layout.dialog_alarm_setting, null)
                    val builder = AlertDialog.Builder(this)

                    val color= getColor(R.color.colorPrimaryMint)
                    val alarmTimePicker= dialogView.findViewById<TimePicker>(R.id.alarmTimePicker)






                  alarmTimePicker.setOnTimeChangedListener{
                            view,hourOfDay,minute->Toast.makeText(this, hourOfDay.toString() + " : " + minute +" : " , Toast.LENGTH_LONG).show()
                        //toast("Time(HH:MM)"+ ${getHourAMPM(hourOfDay)} + ": $minute ${getAMPM(hourOfDay)}")
                    }
                     builder.setView(dialogView)
                        .setPositiveButton("확인") { dialogInterface, i ->

                            /* 확인일 때 main의 View의 값에 dialog View에 있는 값을 적용 */

                        }
                        .setNegativeButton("취소") { dialogInterface, i ->
                            /* 취소일 때 아무 액션이 없으므로 빈칸 */
                        }
                        .show()

                //이 부분에 타임피커 다이얼 로그

               // clickTimePicker(btnSetTime)
                }
                toast("ddksk")
                ///팝업창
                //color에 있는 검정색 어떻게 쓰는지 모르겠어
            } else {
                // The toggle is disabled
            }
        }
    }

    // Custom method to get time picker current time as string
    private fun getPickerTime(timePicker: TimePicker):String{
        val hour = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.hour
        } else {
            timePicker.currentHour
        }

        val minute = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            timePicker.minute
        }else{
            timePicker.currentMinute
        }

        return "${getHourAMPM(hour)} : $minute ${getAMPM(hour)}"
    }


    // Custom method to get AM PM value from provided hour
    private fun getAMPM(hour:Int):String{
        return if(hour>11)"PM" else "AM"
    }


    // Custom method to get hour for AM PM time format
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

   /* @RequiresApi(Build.VERSION_CODES.N)
    fun clickTimePicker(view: View) {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR)
        val minute = c.get(Calendar.MINUTE)

        val tpd = TimePickerDialog(this,TimePickerDialog.OnTimeSetListener(function = { view, h, m ->


            Toast.makeText(this, h.toString() + " : " + m +" : " , Toast.LENGTH_LONG).show()

        }),hour,minute,false)

        tpd.show()
    }
*/

}