package com.example.mindgarden.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.mindgarden.R
import com.example.mindgarden.data.MindgardenRepository
import com.example.mindgarden.ui.base.BaseActivity
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_email_send_password.*
import kotlinx.android.synthetic.main.toolbar_mypage_main.view.*
import org.json.JSONObject
import org.koin.android.ext.android.inject

class EmailSendPasswordActivity : BaseActivity(R.layout.activity_email_send_password) {


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
