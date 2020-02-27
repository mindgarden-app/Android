package com.example.mindgarden.DB

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

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
        editor.apply()
    }

    fun setStartTimeAccessToken(ctx: Context, tiemToStart:Long){
        val  preference: SharedPreferences =ctx.getSharedPreferences(ACCESS_TOKEN_START_TIME,Context.MODE_PRIVATE)
        val  editor:SharedPreferences.Editor=preference.edit()
        editor.putLong("access_token_start_time",tiemToStart)
        editor.commit()
    }
    fun getTimeAccessToken(ctx: Context): Long {
        val preference:SharedPreferences=ctx.getSharedPreferences(ACCESS_TOKEN_START_TIME,Context.MODE_PRIVATE)
        return preference.getLong("access_token_start_time",0)
    }

    fun setExpAccessToken(ctx: Context, exp:Long){
        val  preference: SharedPreferences =ctx.getSharedPreferences(ACCESS_TOKEN_EXP,Context.MODE_PRIVATE)
        val  editor:SharedPreferences.Editor=preference.edit()
        editor.putLong("access_token_exp",exp)
        editor.commit()
    }

    fun getExpAccessToken(ctx: Context): Long {
        val preference:SharedPreferences=ctx.getSharedPreferences(ACCESS_TOKEN_EXP,Context.MODE_PRIVATE)
        return preference.getLong("access_token_exp",0)
    }

    fun isValidToken(ctx:Context):Boolean{
        val currentTime=System.currentTimeMillis()
        Log.e("Token Controller isValid",currentTime.toString())
        Log.e("Token Controller isValid", getTimeAccessToken(ctx).toString())

        Log.e("Token Controller isValid",(currentTime-getTimeAccessToken(ctx)).toString())
       if(getExpAccessToken(ctx)*1000 >currentTime- getTimeAccessToken(ctx)){
           Log.e("Token Controller isValid","is vaildate")
           return true
       }
       else{
           Log.e("Token Controller","is not vaildate")
           return false
       }


    }
}