package com.example.mindgarden.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import com.example.mindgarden.DB.SharedPreferenceController
import com.example.mindgarden.DB.TokenController
import com.example.mindgarden.Network.ApplicationController
import com.example.mindgarden.Network.NetworkService
import com.example.mindgarden.Network.POST.PostEmailSignInResponse
import com.example.mindgarden.R
import com.example.mindgarden.ui.main.MainActivity
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_email_login.*
import kotlinx.android.synthetic.main.toolbar_mypage_main.view.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EmailSignInActivity : AppCompatActivity() {
    val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_login)

        //툴바 재활용을 위해 text 교체
        toolbar_email_login.txtSetting.text = "이메일 로그인"
        toolbar_email_login.btnBack.setOnClickListener {
            finish()
        }
        //로그인하기
        btn_email_sign_in.setOnClickListener {
            postEmailSignInResponse()
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


    fun postEmailSignInResponse() {
        var jsonObject = JSONObject()

        jsonObject.put("email", edt_email_sign_in.text.toString())
        jsonObject.put("password", edt_password_sign_in.text.toString())


        val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject

        val postEmailSignInResponse = networkService.postEmailSignIn(gsonObject)
        postEmailSignInResponse.enqueue(
            object : Callback<PostEmailSignInResponse> {
                override fun onFailure(call: Call<PostEmailSignInResponse>, t: Throwable) {
                    Log.e("로그인 실패", "이메일 로그인 실패")
                    txt_check_email_password.visibility = View.VISIBLE
                }

                override fun onResponse(
                    call: Call<PostEmailSignInResponse>,
                    response: Response<PostEmailSignInResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()!!.status == 200) {
                            if (response.body()!!.success) {
                                TokenController.setAccessToken(
                                    this@EmailSignInActivity,
                                    response.body()!!.data?.get(0)?.token.toString()
                                )
                                TokenController.setRefreshToken(
                                    this@EmailSignInActivity,
                                    response.body()!!.data?.get(0)?.refreshToken.toString()
                                )
                                TokenController.setExpAccessToken(
                                    this@EmailSignInActivity,
                                    response.body()!!.data?.get(0)?.expires_in!!
                                )
                                SharedPreferenceController.setUserMail(
                                    this@EmailSignInActivity,
                                    response.body()!!.data?.get(0)?.email.toString()
                                )
                                SharedPreferenceController.setUserName(
                                    this@EmailSignInActivity,
                                    response.body()!!.data?.get(0)?.name.toString()
                                )
                                //로그인의 성공하면 메인으로 이동
                                val mainIntent =
                                    Intent(this@EmailSignInActivity, MainActivity::class.java)
                                startActivity(mainIntent)

                                Log.e(
                                    "리프레시 토큰",
                                    TokenController.getRefreshToken(this@EmailSignInActivity)
                                )
                                Log.e(
                                    "토큰",
                                    TokenController.getAccessToken(this@EmailSignInActivity)
                                )
                                Log.e(
                                    "토큰 만료기한",
                                    TokenController.getExpAccessToken(this@EmailSignInActivity).toString()
                                )
                                Log.e(
                                    "메일",
                                    SharedPreferenceController.getUserMail(this@EmailSignInActivity)
                                )
                                Log.e(
                                    "이름",
                                    SharedPreferenceController.getUserName(this@EmailSignInActivity)
                                )
                                Log.e("회원가입 성공 메세지", response.body()!!.message)


                            } else {
                                Log.e("회원가입 메세지", response.body()!!.message)
                                txt_check_email_password.visibility = View.VISIBLE
                            }

                        }


                    }
                }

            })
    }
}
