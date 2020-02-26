package com.example.mindgarden.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mindgarden.R
import kotlinx.android.synthetic.main.activity_email_login.*
import kotlinx.android.synthetic.main.toolbar_mypage_main.view.*


class EmailLoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_login)

        //툴바 재활용을 위해 text 교체
        toolbar_email_login.txtSetting.text="이메일 회원가입"
        toolbar_email_login.btnBack.setOnClickListener{
            finish()
        }
    }
}
