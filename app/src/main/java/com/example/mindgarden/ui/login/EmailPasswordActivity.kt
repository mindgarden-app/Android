package com.example.mindgarden.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.example.mindgarden.R
import kotlinx.android.synthetic.main.activity_email_login.*
import kotlinx.android.synthetic.main.activity_email_password.*
import kotlinx.android.synthetic.main.toolbar_mypage_main.view.*

class EmailPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_password)

        toolbar_email_password.txtSetting.text = "이메일 로그인"
        toolbar_email_password.btnBack.setOnClickListener {
            finish()
        }
        btn_email_password_find.setOnClickListener {
            //통신
            //통신안에서 성공하면 다음 뷰로 넘어가고 실패하면 팝업 띄우기
        }

        edt_email_password_forgot.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(edt_email_password_forgot.text).matches()) {
                    txt_check_email_password_forgot.visibility = View.INVISIBLE
                    btn_email_password_find.isEnabled=true
                    btn_email_password_find.setBackgroundResource(R.drawable.green_border_square)
                    btn_email_password_find.setTextColor(getColor(R.color.colorWhite))
                } else {
                    txt_check_email_password_forgot.visibility = View.VISIBLE
                    btn_email_password_find.isEnabled=false
                    btn_email_password_find.setBackgroundResource(R.drawable.grid_border)
                    btn_email_password_find.setTextColor(getColor(R.color.colorPrimaryMint))
                }

            }
        })
    }
}
