package com.mindgarden.mindgarden.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.mindgarden.mindgarden.R
import com.mindgarden.mindgarden.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_email_send_password.*
import kotlinx.android.synthetic.main.toolbar_mypage_main.view.*

class EmailSendPasswordActivity : BaseActivity(R.layout.activity_email_send_password) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_send_password)

        val email=intent.getStringExtra("email")

        toolbar_email_send_password.txtSetting.text="비밀번호 재설정"
        toolbar_email_send_password.btnBack.setOnClickListener {
            finish()
        }

        btn_resend_email.setOnClickListener {
            val sendPasswordIntent=Intent(this,EmailPasswordActivity::class.java)
            startActivity(sendPasswordIntent)
        }
        txt_submit_email.text=email+getString(R.string.txtEmailPasswordSendSuccess)

        btn_go_to_login.setOnClickListener {
            val goBackLogin= Intent(this,EmailSignInActivity::class.java)
            goBackLogin.putExtra("email",email)
            startActivity(goBackLogin)
            Log.e("",txt_submit_email.text.toString())
            //finish()
        }

    }


}
