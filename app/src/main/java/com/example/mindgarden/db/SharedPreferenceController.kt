package com.example.mindgarden.db

import android.content.Context
import android.content.SharedPreferences

object SharedPreferenceController {

    val PASSWORD="unique_string2"
    val USER_NAME="unique_strng3"
    val USER_MAIL="unique_strng4"
    val ALARMSTATE ="unique_strng5"
    val PASSWORDSWITCHSTATE = "unique_strng5"


    //사용자 비밀번호
    fun setPassword(ctx: Context,pw:String){
        val  preference: SharedPreferences=ctx.getSharedPreferences(PASSWORD,Context.MODE_PRIVATE)
        val  editor:SharedPreferences.Editor=preference.edit()
        editor.putString("PW",pw)
        editor.apply()
    }
    fun getPassword(ctx:Context):String{
        val preference:SharedPreferences=ctx.getSharedPreferences(PASSWORD,Context.MODE_PRIVATE)
        return preference.getString("PW","")
    }
    //사용자 이름
    fun  setUserName(ctx: Context,userName:String){
        val  preference: SharedPreferences=ctx.getSharedPreferences(USER_NAME,Context.MODE_PRIVATE)
        val  editor:SharedPreferences.Editor=preference.edit()
        editor.putString("u_name",userName)
        editor.commit()
    }
    fun getUserName(ctx:Context):String{
        val preference:SharedPreferences=ctx.getSharedPreferences(USER_NAME,Context.MODE_PRIVATE)
        return preference.getString("u_name","")
    }
    // 사용자 메일
    fun setUserMail(ctx: Context,userMail:String){
        val  preference: SharedPreferences=ctx.getSharedPreferences(USER_MAIL,Context.MODE_PRIVATE)
        val  editor:SharedPreferences.Editor=preference.edit()
        editor.putString("u_mail",userMail)
        editor.commit()
    }
    fun getUserMail(ctx:Context):String{
        val preference:SharedPreferences=ctx.getSharedPreferences(USER_MAIL,Context.MODE_PRIVATE)
        return preference.getString("u_mail","")
    }

    fun setAlarmState(ctx: Context, state : Boolean){
        val preference : SharedPreferences = ctx.getSharedPreferences(ALARMSTATE, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = preference.edit()
        editor.putBoolean("alarmState", state)
        editor.commit()
    }

    fun getAlarmState(ctx: Context):Boolean{
        val preference : SharedPreferences = ctx.getSharedPreferences(ALARMSTATE, Context.MODE_PRIVATE)
        return preference.getBoolean("alarmState", false)
    }

    fun setPasswordSwitchState(ctx: Context, state : Boolean, initValue : Int){
        val preference : SharedPreferences = ctx.getSharedPreferences(PASSWORDSWITCHSTATE, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = preference.edit()
        editor.putBoolean("passwordSwitchState", state)
        editor.putInt("initValue", initValue)
        editor.commit()
    }

    fun getPasswordSwitchState(ctx: Context):Boolean{
        val preference : SharedPreferences = ctx.getSharedPreferences(PASSWORDSWITCHSTATE, Context.MODE_PRIVATE)
        return preference.getBoolean("passwordSwitchState", false)
    }

    fun getPasswordIsSet(ctx: Context):Int{
        val preference : SharedPreferences = ctx.getSharedPreferences(PASSWORDSWITCHSTATE, Context.MODE_PRIVATE)
        return preference.getInt("initValue", 0)
    }

}
