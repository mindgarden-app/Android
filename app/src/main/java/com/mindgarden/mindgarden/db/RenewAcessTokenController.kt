package com.mindgarden.mindgarden.db

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import com.mindgarden.mindgarden.ui.login.LoginActivity
import com.mindgarden.mindgarden.data.MindgardenRepository
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.json.JSONObject

object RenewAcessTokenController {

    var exp_start_time:Long = 0
    var exp_in:Int=0
    var token:String=""

    fun postRenewAccessToken(ctx: Context, repository: MindgardenRepository){
        var jsonObject = JSONObject()
        val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject
        Log.e("Renew token","in Renew access token")

        repository
            .postRenewAccessToken(
                TokenController.getRefreshToken(ctx),gsonObject,

                {
                    //리프레시 토큰 유효
                    if (it.status == 200) {
                        if (it.success) {
                            //엑세스 토큰 재발급
                            Log.e("Renew token", it.message)
                            val temp = it.data!![0].token
                            Log.e("Renew token: ", temp)

                            //재발급 받은 토큰을 저장
                            TokenController.setAccessToken(ctx, temp)
                            //재발급 받은 시간을 저장
                            TokenController.setStartTimeAccessToken(ctx, System.currentTimeMillis())
                        }//리프레시 토큰 만료
                        else{
                            //리프레시 토큰 값 지워주고
                            TokenController.clearRefreshToken(ctx)


                        }
                    }
                },
                {
                    Log.e("Renew token failed", it)
                })
    }

}