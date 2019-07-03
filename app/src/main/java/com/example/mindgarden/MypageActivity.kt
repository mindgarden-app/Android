package com.example.mindgarden

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_mypage.*
import kotlinx.android.synthetic.main.toolbar_mypage_main.*

class MypageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage)

        //Toolbar
        btnBack.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            //백 스페이스 누르면 다시 일단 로그인 페이지로

            startActivity(intent)

            finish()
        }

        //RelativeLayout
        btnLogout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            //로그아웃 누르면 다시 일단 로그인 페이지로

            startActivity(intent)

            finish()
        }

        //LinerLayout 버튼들
        btnPasswordSetting.setOnClickListener {
            val intent = Intent(this, PasswordSettingActivity::class.java)
            //암호 설정버튼 누르면 암호 액티비티로 넘어가는 것으로 구현

            startActivity(intent)

            finish()
        }

        fontSetting.setOnClickListener {
         val intent3=Intent(this,FontSettingActivity::class.java)
            startActivity(intent3)
            finish()
        }
        alarmSetting.setOnClickListener {
            val intent3 = Intent(this, AlarmSettingActivity::class.java)
            // 알람 설정하는 페이즈로 넘어감

            startActivity(intent3)

            finish()
        }
        premium.setOnClickListener {
            val level : TextView = findViewById(R.id.level)
            level.setBackgroundResource(R.drawable.mint_border_fill)

        }
    }
}
