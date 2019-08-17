package com.example.mindgarden.Activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import com.example.mindgarden.DB.SharedPreferenceController
import com.example.mindgarden.DB.TokenController
import com.example.mindgarden.Network.ApplicationController
import com.example.mindgarden.Network.Delete.DeleteDiaryListResponse
import com.example.mindgarden.Network.Delete.DeleteUserResponse
import com.example.mindgarden.Network.NetworkService
import com.example.mindgarden.R
import com.example.mindgarden.RenewAcessTokenController
import kotlinx.android.synthetic.main.activity_mypage.*
import kotlinx.android.synthetic.main.toolbar_mypage_main.*
import org.jetbrains.anko.ctx
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MypageActivity : AppCompatActivity() {

    val networkService: NetworkService by lazy{
        ApplicationController.instance.networkService
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage)

        userName.text=SharedPreferenceController.getUserName(this)
        userMail.text=SharedPreferenceController.getUserMail(this)

        //Toolbar
        btnBack.setOnClickListener {
            //뒤로가기
            onBackPressed()
        }

        //RelativeLayout
        btnLogout.setOnClickListener {
            TokenController.clearRefreshToken(this)
            //TODO 로그아웃 코드 추가해야함

            val intent = Intent(this, LoginActivity::class.java)
            //로그아웃 누르면 다시 일단 로그인 페이지로

            startActivity(intent)

            finish()
        }
        btnDelete.setOnClickListener{
            deleteUserResponse()
            TokenController.clearRefreshToken(this)
        }

        //LinerLayout 버튼들
        btnPasswordSetting.setOnClickListener {
            val intent = Intent(this, PasswordSettingActivity::class.java)
            //암호 설정버튼 누르면 암호 액티비티로 넘어가는 것으로 구현

            startActivity(intent)

            finish()
        }

        alarmSetting.setOnClickListener {
            val intent3 = Intent(this, AlarmSettingActivity::class.java)
            // 알람 설정하는 페이즈로 넘어감

            startActivity(intent3)

            finish()
        }
    }
    private fun deleteUserResponse() {
        if(!TokenController.isValidToken(ctx)){
            RenewAcessTokenController.postRenewAccessToken(ctx)
        }

        val deleteDiaryListResponse = networkService.deleteUserResponse(
            TokenController.getAccessToken(ctx))
        Log.e("delete", "delete")

        deleteDiaryListResponse.enqueue(object: Callback<DeleteUserResponse> {
            override fun onFailure(call: Call<DeleteUserResponse>, t: Throwable) {

            }
            override fun onResponse(call: Call<DeleteUserResponse>, response: Response<DeleteUserResponse>) {

                if (response.isSuccessful) {
                    if (response.body()!!.status == 200) {
                       Log.e("Delete User",response.body()!!.message)

                    }
                }
            }


        })
    }
}
