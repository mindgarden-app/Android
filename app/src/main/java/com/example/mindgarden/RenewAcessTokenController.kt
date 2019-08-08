package com.example.mindgarden

import android.content.Context
import com.example.mindgarden.DB.TokenController
import com.example.mindgarden.Network.ApplicationController
import com.example.mindgarden.Network.NetworkService
import com.google.gson.JsonObject
import org.json.JSONObject

object RenewAcessTokenController {

    val networkService: NetworkService by lazy{
        ApplicationController.instance.networkService
    }
    var exp_start_time:Long = 0
    var exp_in:Int=0
    var token:String=""

    fun getRenewAccessToken(ctx:Context){

        val jsonObject = JsonObject()
        val getRenewAccessToken= networkService.getRenewAccessToken(TokenController.getRefreshToken(ctx),jsonObject)
    }

}