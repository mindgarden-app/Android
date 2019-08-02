package com.example.mindgarden.Login

import android.content.Context
import android.content.SharedPreferences

object TokenController {

    val ACCESS_TOKEN_START_TIME="unique_strng5"
    val ACCESS_TOKEN_EXP="unique_strng6"

    fun setStartTimeAccessToken(ctx: Context, tiemToStart:Long){
        val  preference: SharedPreferences =ctx.getSharedPreferences(ACCESS_TOKEN_START_TIME,Context.MODE_PRIVATE)
        val  editor:SharedPreferences.Editor=preference.edit()
        editor.putLong("access_token_start_time",tiemToStart)
        editor.commit()
    }
    fun getTimeAccessToken(ctx:Context): Long {
        val preference:SharedPreferences=ctx.getSharedPreferences(ACCESS_TOKEN_START_TIME,Context.MODE_PRIVATE)
        return preference.getLong("access_token_start_time",0)
    }
    fun setExpAccessToken(ctx: Context, exp:Long){
        val  preference: SharedPreferences =ctx.getSharedPreferences(ACCESS_TOKEN_EXP,Context.MODE_PRIVATE)
        val  editor:SharedPreferences.Editor=preference.edit()
        editor.putLong("access_token_exp",exp)
        editor.commit()
    }
    fun getExpAccessToken(ctx:Context): Long {
        val preference:SharedPreferences=ctx.getSharedPreferences(ACCESS_TOKEN_EXP,Context.MODE_PRIVATE)
        return preference.getLong("access_token",0)
    }

    fun isValidToken(ctx:Context):Boolean{
        val currentTime=System.currentTimeMillis()

        //TODO 만료기간이 지났나 안지났나 확인하기

       if(getExpAccessToken(ctx) >currentTime- getTimeAccessToken(
               ctx
           )
       ){
           return true
       }
       else{
           return false
       }


    }
}