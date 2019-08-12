package com.example.mindgarden

import android.content.Context
import android.util.Log
import com.example.mindgarden.DB.TokenController
import com.example.mindgarden.Network.ApplicationController
import com.example.mindgarden.Network.NetworkService
import com.example.mindgarden.Network.POST.PostRenewAccessTokenResponse
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
        Log.e("Renew token","in Renew access token")
        val postRenewAccessTokenResponse= networkService.postRenewAccessTokenResponse(TokenController.getRefreshToken(ctx))
        Log.e("Renew token","in Renew access token2")
        postRenewAccessTokenResponse.enqueue(object: Callback<PostRenewAccessTokenResponse>{

            override fun onFailure(call: Call<PostRenewAccessTokenResponse>, t: Throwable) {
              Log.e("Renew token failed", t.toString())            }

            override fun onResponse(call: Call<PostRenewAccessTokenResponse>, response: Response<PostRenewAccessTokenResponse>) {
                if(response.isSuccessful) {
                    if (response.body()!!.status == 200) {
                        Log.e("Renew token", response.body()!!.message)

                        //엑세스 토큰 재발급
                        val temp= response.body()!!.data!![0].token
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