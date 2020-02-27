package com.example.mindgarden.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mindgarden.R
import kotlinx.android.synthetic.main.activity_email_sign_up.*
import kotlinx.android.synthetic.main.activity_email_sign_up.edt_email
import kotlinx.android.synthetic.main.toolbar_write_diary.view.*

class EmailSignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_sign_up)

        toolbar_email_sign_up.txt_date_toolbar_write_diary.text = "이메일 회원가입"
        edt_email.setOnFocusChangeListener { view, b ->
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(edt_email.text).matches()) {
                txt_check_email.setTextColor(getColor(R.color.colorWhite))
            } else {
                txt_check_email.setTextColor(getColor(R.color.colorPrimaryMint))
            }
        }


        toolbar_email_sign_up.btn_save_diary_toolbar.setOnClickListener{
            if(canEnroll()) {
                val enrolledIntent = Intent(this, EmailSignInActivity::class.java)
                startActivity(enrolledIntent)
            }

        }

    }

    private fun canEnroll(): Boolean {
        //유효성 판단하는 함수
        return edt_email.toString().isNotEmpty()&&edt_name.toString().isNotEmpty()&&(edt_password.text.toString()==edt_password_check.text.toString())
    }

}
