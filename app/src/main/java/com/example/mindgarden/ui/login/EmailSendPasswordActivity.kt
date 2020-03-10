package com.example.mindgarden.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mindgarden.R
import kotlinx.android.synthetic.main.activity_email_send_password.*
import kotlinx.android.synthetic.main.toolbar_mypage_main.view.*

class EmailSendPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_send_password)

        toolbar_email_send_password.txtSetting.text="비밀번호 재설정"
        toolbar_email_send_password.btnBack.setOnClickListener {
            finish()
        }

        btn_resend_email.setOnClickListener {
            finish()
        }
        txt_submit_email.text=intent.getStringExtra("email")+getString(R.string.txtEmailPasswordSendSuccess)

    }
}
