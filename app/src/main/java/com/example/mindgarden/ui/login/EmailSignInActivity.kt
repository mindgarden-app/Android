package com.example.mindgarden.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.mindgarden.DB.SharedPreferenceController
import com.example.mindgarden.DB.TokenController
import com.example.mindgarden.Network.ApplicationController
import com.example.mindgarden.Network.NetworkService
import com.example.mindgarden.Network.POST.PostEmailSignInResponse
import com.example.mindgarden.Network.POST.PostEmailSignUpResponse
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
        toolbar_email_login.txtSetting.text = "이메일 회원가입"
        toolbar_email_login.btnBack.setOnClickListener {
            finish()
        }
        btn_email_sign_up.setOnClickListener {
            val signUpIntent = Intent(this, EmailSignUpActivity::class.java)
            startActivity(signUpIntent)
        }

        btn_email_sign_in.setOnClickListener {
            postEmailSignInResponse()
            val mainIntent=Intent(this,MainActivity::class.java)
            startActivity(mainIntent)
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
                    Log.e("회원가입 실패", "이메일 회원가입 실패")
                }

                override fun onResponse(
                    call: Call<PostEmailSignInResponse>,
                    response: Response<PostEmailSignInResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()!!.status == 200) {
                            if (response.body()!!.success) {
                                TokenController.setAccessToken(this@EmailSignInActivity,
                                    response.body()!!.data?.get(0)?.token.toString()
                                )
                                TokenController.setRefreshToken(this@EmailSignInActivity, response.body()!!.data?.get(0)?.refreshToken.toString())
                                TokenController.setExpAccessToken(this@EmailSignInActivity,response.body()!!.data?.get(0)?.expires_in!!)
                                SharedPreferenceController.setUserMail(this@EmailSignInActivity,response.body()!!.data?.get(0)?.email.toString())
                                SharedPreferenceController.setUserName(this@EmailSignInActivity,response.body()!!.data?.get(0)?.name.toString())

                                Log.e("리프레시 토큰",TokenController.getRefreshToken(this@EmailSignInActivity))
                                Log.e("토큰",TokenController.getAccessToken(this@EmailSignInActivity))
                                Log.e("토큰 만료기한",TokenController.getExpAccessToken(this@EmailSignInActivity).toString())

                                Log.e("메일",SharedPreferenceController.getUserMail(this@EmailSignInActivity))
                                Log.e("이름",SharedPreferenceController.getUserName(this@EmailSignInActivity))
                                Log.e("회원가입 성공 메세지", response.body()!!.message)
                            } else {
                                Log.e("회원가입 메세지", response.body()!!.message)
                            }

                        }

                    }
                }

            })
    }
}
