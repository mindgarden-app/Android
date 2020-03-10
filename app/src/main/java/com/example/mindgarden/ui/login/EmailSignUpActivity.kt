package com.example.mindgarden.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.example.mindgarden.R
import com.example.mindgarden.data.MindgardenRepository
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_email_sign_up.*
import kotlinx.android.synthetic.main.activity_email_sign_up.edt_email
import kotlinx.android.synthetic.main.toolbar_write_diary.view.*
import org.json.JSONObject
import org.koin.android.ext.android.inject
import java.util.regex.Pattern

class EmailSignUpActivity : AppCompatActivity() {
    private val repository: MindgardenRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_sign_up)

        toolbar_email_sign_up.txt_date_toolbar_write_diary.text = "이메일 회원가입"
        toolbar_email_sign_up.txt_save_toolbar.text = "등록"



        toolbar_email_sign_up.btn_save_diary_toolbar.setOnClickListener {
            canEnroll()
            postEmailSignUp()
//            val enrolledIntent = Intent(this, EmailSignInActivity::class.java)
//            startActivity(enrolledIntent)


        }
        //뒤로가기 버튼 누를시 signinactivity로 이동
        toolbar_email_sign_up.btn_back_toolbar.setOnClickListener {
            finish()
        }


        //Email
        edt_email.setOnFocusChangeListener { view, b ->

            hasFocus(view, b) //포커스 잡혀있으면 green  border로 바꾸기

        }
        //실시간 이메일인지  아닌지 확인
        edt_email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edt_email.text).matches()) {
                    txt_check_email.visibility = View.VISIBLE
                    txt_check_email.setTextColor(getColor(R.color.colorRed))
                } else {
                    txt_check_email.visibility = View.INVISIBLE

                }
            }
        })

        //이름
        edt_name.setOnFocusChangeListener { view, b ->
            hasFocus(view, b)

        }


        //비밀번호
        edt_password.setOnFocusChangeListener { view, b ->
            hasFocus(view, b)

        }
        edt_password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (isValidPassword()) {
                    txt_check_password.visibility = View.INVISIBLE

                } else {
                    txt_check_password.visibility = View.VISIBLE
                }
            }

        })


        //비밀번호 화인
        edt_password_check.setOnFocusChangeListener { view, b ->
            hasFocus(view, b)

        }

        edt_password_check.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (edt_password.text.toString() != edt_password_check.text.toString()) {
                    txt_check_password_again.visibility = View.VISIBLE
                    txt_check_password_again.setTextColor(getColor(R.color.colorRed))

                } else {
                    txt_check_password_again.visibility = View.INVISIBLE

                }
            }
        })

        checkBox_agree.setOnClickListener {

            if(checkBox_agree.isChecked){
                checkBox_agree.buttonTintList=getColorStateList(R.color.colorPrimaryMint)
            }else{
                checkBox_agree.buttonTintList=getColorStateList(R.color.colorBlockGray)
            }
        }



        //개인정보처리방침으로 이동
        btn_privacy_statement.setOnClickListener {
            val privacyIntent = Intent(this, PrivacyStatementActivity::class.java)
            startActivity(privacyIntent)
        }

        //이용약관으로 이동
        btn_terms_of_use.setOnClickListener {
            val termsOfUseIntent = Intent(this, TermsOfUseActivity::class.java)
            startActivity(termsOfUseIntent)
        }


    }


    //포커스 잡혔을 떄는 그린, 안잡혔을 때는 회색
    fun hasFocus(view: View, b: Boolean) {
        if (b) {
            view.setBackgroundResource(R.drawable.grid_border)
        } else {
            view.setBackgroundResource(R.drawable.gray_border_square)
        }
    }

    private fun canEnroll() {
        //빈칸이 다 없어야 하며
        //빈칸이 있을시에 포커스 옮겨줌
        if (txt_check_email.visibility == View.VISIBLE || edt_email.text.toString().isEmpty()) {
            edt_email.requestFocus()
        } else if (edt_name.text.toString().isEmpty()) {
            edt_name.requestFocus()
        } else if (txt_check_password.visibility == View.VISIBLE || edt_password.text.toString().isEmpty()) {
            edt_password.requestFocus()
        } else if (txt_check_password_again.visibility == View.VISIBLE || edt_password_check.text.toString().isEmpty()) {
            edt_password_check.requestFocus()
        } else if (!checkBox_agree.isChecked) {
            currentFocus.clearFocus()
            checkBox_agree.isChecked=true
        }
    }

    //영문 숫자 8자 이상
    fun isValidPassword(): Boolean {
        return Pattern.matches(
            "^.*(?=.{8,20})(?=.*[0-9])(?=.*[a-zA-Z]).*\$",
            edt_password.text.toString()
        )
    }


    //회원가입 통신
    private fun postEmailSignUp() {
        var jsonObject = JSONObject()

        jsonObject.put("email", edt_email.text.toString())
        jsonObject.put("password", edt_password.text.toString())
        jsonObject.put("name", edt_name.text.toString())

        val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject

        repository
            .postEmailSignUp(gsonObject,
                {
                    if (it.status == 200) {
                        if (it.success) {
                            Log.e("회원가입 성공 메세지", it.message)
                        } else {
                            Log.e("회원가입 메세지", it.message)
                        }
                    }
                },
                {
                    //에러처리
                }
            )
    }
}
