package com.example.mindgarden.db

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat.startActivity
import android.util.Log
import com.example.mindgarden.ui.login.LoginActivity
import com.example.mindgarden.network.ApplicationController
import com.example.mindgarden.network.NetworkService
import com.example.mindgarden.network.POST.PostRenewAccessTokenResponse
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object RenewAcessTokenController {

    val networkService: NetworkService by lazy{
        ApplicationController.instance.networkService
    }
    var exp_start_time:Long = 0
    var exp_in:Int=0
    var token:String=""

    fun postRenewAccessToken(ctx:Context){
        var jsonObject = JSONObject()
        val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject
        Log.e("Renew token","in Renew access token")

        val postRenewAccessTokenResponse=
            networkService.postRenewAccessTokenResponse(TokenController.getRefreshToken(ctx),gsonObject)
        Log.e("Renew token","in Renew access token2")

        postRenewAccessTokenResponse.enqueue(object: Callback<PostRenewAccessTokenResponse>{

            override fun onFailure(call: Call<PostRenewAccessTokenResponse>, t: Throwable) {
              Log.e("Renew token failed", t.toString())
            }

            override fun onResponse(call: Call<PostRenewAccessTokenResponse>, response: Response<PostRenewAccessTokenResponse>) {
                Log.e("Renew token success", "Renew token success ")
                if(response.isSuccessful) {
                    if (response.body()!!.status == 200) {

                        //리프레시 토큰 유효
                         if(response.body()!!.success==true) {
                             //엑세스 토큰 재발급
                             Log.e("Renew token", response.body()!!.message)
                             val temp = response.body()!!.data!![0].token
                             Log.e("Renew token: ", temp)

                             //재발급 받은 토큰을 저장
                             TokenController.setAccessToken(ctx, temp)
                             //재발급 받은 시간을 저장
                             TokenController.setStartTimeAccessToken(ctx, System.currentTimeMillis())
                         }
                        //리프레시 토큰 만료
                        else{
                             //리프레시 토큰 값 지워주고
                             TokenController.clearRefreshToken(ctx)

                            //로그인 화면으로 이동
                             var loginIntent= Intent(ctx, LoginActivity::class.java)
                             startActivity(ctx,loginIntent, Bundle())



                         }
                    }
                }
            }
        })
    }

}