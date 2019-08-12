package com.example.mindgarden

import android.content.Context
import android.util.Log
import com.example.mindgarden.DB.TokenController
import com.example.mindgarden.Network.ApplicationController
import com.example.mindgarden.Network.NetworkService
import com.example.mindgarden.Network.POST.PostAccessTokenResponse
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


        val postRenewAccessToken= networkService.postRenewAccessToken(TokenController.getRefreshToken(ctx))

        postRenewAccessToken.enqueue(object: Callback<PostAccessTokenResponse>{
            override fun onFailure(call: Call<PostAccessTokenResponse>, t: Throwable) {
              Log.e("Renew token failed", t.toString())            }

            override fun onResponse(call: Call<PostAccessTokenResponse>, response: Response<PostAccessTokenResponse>) {
                if(response.isSuccessful) {
                    if (response.body()!!.status == 200) {
                        Log.e("Renew token", response.body()!!.message)

                        //엑세스 토큰 재발급
                        val temp= response.body()!!.data!![0].toString()
                        Log.e("Renew token: ", temp)

                        //재발급 받은 토큰을 저장
                        TokenController.setAccessToken(ctx,temp)
                        //재발급 받은 시간을 저장
                        TokenController.setStartTimeAccessToken(ctx,System.currentTimeMillis())

                    }
                }
            }
        })
    }

}