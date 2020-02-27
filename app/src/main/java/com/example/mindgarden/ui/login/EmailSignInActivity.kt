package com.example.mindgarden.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mindgarden.R
import kotlinx.android.synthetic.main.activity_email_login.*
import kotlinx.android.synthetic.main.toolbar_mypage_main.view.*


class EmailSignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_login)

        //툴바 재활용을 위해 text 교체
        toolbar_email_login.txtSetting.text="이메일 회원가입"
        toolbar_email_login.btnBack.setOnClickListener{
            finish()
        }
        btn_email_sign_up.setOnClickListener {
            val signUpIntent= Intent(this,EmailSignUpActivity::class.java)
            startActivity(signUpIntent)
        }
    }
}
