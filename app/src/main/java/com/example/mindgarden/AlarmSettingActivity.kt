package com.example.mindgarden

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import kotlinx.android.synthetic.main.toolbar_mypage_main.*
import org.jetbrains.anko.toast

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
        val setTime: Button =findViewById(R.id.setTime)
        alarmSwitch.setOnCheckedChangeListener { _, isChecked ->
            setTime.setTextColor(Color.BLACK)
            if (isChecked) {
                // The toggle is enabled
                setTime.setOnClickListener {
                //이 부분에 타임피커 다이얼 로그
                clickTimePicker(setTime)
                }
                toast("ddksk")
                ///팝업창
                //color에 있는 검정색 어떻게 쓰는지 모르겠어
            } else {
                // The toggle is disabled
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun clickTimePicker(view: View) {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR)
        val minute = c.get(Calendar.MINUTE)

        val tpd = TimePickerDialog(this,TimePickerDialog.OnTimeSetListener(function = { view, h, m ->

            Toast.makeText(this, h.toString() + " : " + m +" : " , Toast.LENGTH_LONG).show()

        }),hour,minute,false)

        tpd.show()
    }
}
