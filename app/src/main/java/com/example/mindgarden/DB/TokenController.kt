package com.example.mindgarden.DB

import android.content.Context
import android.content.SharedPreferences

object TokenController {

    val ACCESS_TOKEN="unique_string"

    val ACCESS_TOKEN_START_TIME="unique_strng5"
    val ACCESS_TOKEN_EXP="unique_strng6"
    val REFRESH_TOKEN="unique_string7"

    fun setAccessToken(ctx: Context, userId:String){
        val  preference: SharedPreferences=ctx.getSharedPreferences(ACCESS_TOKEN,Context.MODE_PRIVATE)
        val  editor:SharedPreferences.Editor=preference.edit()
        editor.putString("access_token",userId)
        editor.commit()
    }
    fun getAccessToken(ctx:Context):String{
        val preference: SharedPreferences = ctx.getSharedPreferences(ACCESS_TOKEN, Context.MODE_PRIVATE)
        return preference.getString("access_token", "")
    }


    //사용자 -> 토큰으로 대체될 예정
    fun setRefreshToken(ctx: Context, userId:String){
        val  preference: SharedPreferences=ctx.getSharedPreferences( REFRESH_TOKEN,Context.MODE_PRIVATE)
        val  editor:SharedPreferences.Editor=preference.edit()
        editor.putString("refresh_token",userId)
        editor.commit()
    }
    fun getRefreshToken(ctx:Context):String {
        val preference: SharedPreferences = ctx.getSharedPreferences( REFRESH_TOKEN, Context.MODE_PRIVATE)
        return preference.getString("refresh_token", "")
    }

    //리프레시 토큰이 만료될 경우 사용
    fun clearRefreshToken(ctx:Context){
        val preference:SharedPreferences=ctx.getSharedPreferences(ACCESS_TOKEN,Context.MODE_PRIVATE)
        val editor:SharedPreferences.Editor=preference.edit()
        editor.clear()
        editor.commit()
    }

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
        return preference.getLong("access_token_exp",0)
    }

    fun isValidToken(ctx:Context):Boolean{
        val currentTime=System.currentTimeMillis()

        //TODO 만료기간이 지났나 안지났나 확인하기

       if(getExpAccessToken(ctx) >currentTime- getTimeAccessToken(ctx)){
           return true
       }
       else{
           return false
       }


    }
}