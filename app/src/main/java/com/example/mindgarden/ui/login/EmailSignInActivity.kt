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
import com.example.mindgarden.db.SharedPreferenceController
import com.example.mindgarden.db.TokenController
import com.example.mindgarden.ui.base.BaseActivity
import com.example.mindgarden.ui.main.MainActivity
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_email_login.*
import kotlinx.android.synthetic.main.toolbar_mypage_main.view.*
import org.json.JSONObject
import org.koin.android.ext.android.inject


class EmailSignInActivity : BaseActivity(R.layout.activity_email_login) {

    private val repository: MindgardenRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //툴바 재활용을 위해 text 교체
        toolbar_email_login.txtSetting.text = "이메일 로그인"
        toolbar_email_login.btnBack.setOnClickListener {
            finish()
        }
        edt_email_sign_in.setText(intent.getStringExtra("email"))
        Log.e("name",SharedPreferenceController.getUserName(this))
        Log.e("mail",SharedPreferenceController.getUserMail(this))
        Log.e("access",TokenController.getAccessToken(this))
        Log.e("refresh",TokenController.getRefreshToken(this))

        //로그인하기
        btn_email_sign_in.setOnClickListener {
            postEmailSignIn()
        }
        btn_email_password_forgot.setOnClickListener {
            val passwordIntent = Intent(this, EmailPasswordActivity::class.java)
            startActivity(passwordIntent)
        }

        //회원가입으로 이동
        btn_email_sign_up.setOnClickListener {
            val signUpIntent = Intent(this, EmailSignUpActivity::class.java)
            startActivity(signUpIntent)
        }


        btn_email_sign_in.setOnClickListener {
            canLogin()
            postEmailSignIn()
        }

        //누를시에 그린 border로 변화
        edt_email_sign_in.setOnFocusChangeListener { view, b -> hasFocus(view,b) }
        //누를시에 그린 border로 변화
        edt_email_sign_in.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                canLogin()
            }
        })



        //누를시에 그린 border로 변화
        edt_password_sign_in.setOnFocusChangeListener { view, b -> hasFocus(view,b) }
        edt_password_sign_in.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                canLogin()

            }
        })

    }

    private fun canLogin() {
        //이메일이랑 비밀번호 채워져있을 때 버튼 색 바꿔주기
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(edt_email_sign_in.text).matches() && edt_password_sign_in.text.toString().isNotEmpty()) {
            txt_check_email_password.visibility = View.INVISIBLE
            btn_email_sign_in.isEnabled = true
            btn_email_sign_in.setBackgroundResource(R.drawable.green_border_square)
            btn_email_sign_in.setTextColor(getColor(R.color.colorWhite))
        } else {
            btn_email_sign_in.isEnabled = false
            txt_check_email_password.visibility = View.VISIBLE
            btn_email_sign_in.setBackgroundResource(R.drawable.grid_border)
            btn_email_sign_in.setTextColor(getColor(R.color.colorPrimaryMint))
        }
    }
    fun hasFocus(view: View, b: Boolean) {
        if (b) {
            view.setBackgroundResource(R.drawable.grid_border)
        } else {
            view.setBackgroundResource(R.drawable.gray_border_square)
        }
    }

    private fun postEmailSignIn() {
        var jsonObject = JSONObject()

        jsonObject.put("email", edt_email_sign_in.text.toString())
        jsonObject.put("password", edt_password_sign_in.text.toString())


        val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject

        repository
            .postEmailSignIn(gsonObject,
                {
                    if (it.status == 200) {
                        if (it.success) {
                            TokenController.setAccessToken(this@EmailSignInActivity, it.data?.get(0)?.token.toString()
                            )
                            TokenController.setRefreshToken(this@EmailSignInActivity, it.data?.get(0)?.refreshToken.toString()
                            )
                            TokenController.setExpAccessToken(this@EmailSignInActivity, it.data?.get(0)?.expires_in!!
                            )
                            TokenController.setStartTimeAccessToken(
                                this@EmailSignInActivity,
                                System.currentTimeMillis()
                            )

                            SharedPreferenceController.setUserMail(this@EmailSignInActivity, it.data?.get(0)?.email.toString()
                            )
                            SharedPreferenceController.setUserName(this@EmailSignInActivity, it.data?.get(0)?.name.toString()
                            )
                            val mainIntent = Intent(this, MainActivity::class.java)
                            startActivity(mainIntent)

                            Log.e("리프레시 토큰", TokenController.getRefreshToken(this@EmailSignInActivity))
                            Log.e("토큰", TokenController.getAccessToken(this@EmailSignInActivity))
                            Log.e("토큰 만료기한", TokenController.getExpAccessToken(this@EmailSignInActivity).toString())

                            Log.e("메일", SharedPreferenceController.getUserMail(this@EmailSignInActivity))
                            Log.e("이름", SharedPreferenceController.getUserName(this@EmailSignInActivity))
                            Log.e("회원가입 성공 메세지", it.message)
                        }
                        else {
                            Log.e("회원가입 메세지", it.message)
                            txt_check_email_password.visibility=View.VISIBLE
                            txt_check_email_password.text=it.message
                        }
                    }

                },
                {

                    //에러처리
                })
    }
}
