package com.example.mindgarden.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.mindgarden.Network.ApplicationController
import com.example.mindgarden.Network.NetworkService
import com.example.mindgarden.Network.POST.PostEmailSignUpResponse
import com.example.mindgarden.R
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_email_sign_up.*
import kotlinx.android.synthetic.main.activity_email_sign_up.edt_email
import kotlinx.android.synthetic.main.toolbar_write_diary.view.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EmailSignUpActivity : AppCompatActivity() {
    val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }

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


        toolbar_email_sign_up.btn_save_diary_toolbar.setOnClickListener {
            if (canEnroll()) {
                postEmailSignUpResponse()
                val enrolledIntent = Intent(this, EmailSignInActivity::class.java)
                startActivity(enrolledIntent)
            }

        }

    }

    private fun canEnroll(): Boolean {
        //유효성 판단하는 함수
        return edt_email.toString().isNotEmpty() && edt_name.toString().isNotEmpty() && (edt_password.text.toString() == edt_password_check.text.toString())
    }

    fun postEmailSignUpResponse() {
        var jsonObject = JSONObject()

        jsonObject.put("email", edt_email.text.toString())
        jsonObject.put("password", edt_password.text.toString())
        jsonObject.put("name", edt_name.text.toString())

        val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject

        val postEmailSignUpResponse = networkService.postEmailSignUp(gsonObject)
        postEmailSignUpResponse.enqueue(object : Callback<PostEmailSignUpResponse> {
            override fun onFailure(call: Call<PostEmailSignUpResponse>, t: Throwable) {
                Log.e("회원가입 실패", "이메일 회원가입 실패")
            }

            override fun onResponse(
                call: Call<PostEmailSignUpResponse>,
                response: Response<PostEmailSignUpResponse>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == 200) {
                        if (response.body()!!.success) {
                            Log.e("회원가입 성공 메세지", response.body()!!.message)
                        }
                        else{
                            Log.e("회원가입 메세지",response.body()!!.message)
                        }

                    }

                }
            }

        })
    }
}
