package com.mindgarden.mindgarden.db

import android.content.Context
import android.content.SharedPreferences

object SharedPreferenceController {


    val ALARMSTATE ="unique_strng5"
    val PASSWORDSWITCHSTATE="passwordswitchstate"

    val USER="user"

    lateinit var  preference: SharedPreferences
    lateinit var  editor:SharedPreferences.Editor



    //사용자 비밀번호
    fun setPassword(ctx: Context,pw:String){
        preference=ctx.getSharedPreferences(USER,Context.MODE_PRIVATE)
        editor=preference.edit()
        editor.putString("PW",pw)
        editor.apply()
    }
    fun getPassword(ctx:Context):String{
        preference=ctx.getSharedPreferences(USER,Context.MODE_PRIVATE)
        editor=preference.edit()
        return preference.getString("PW","")
    }
    //사용자 이름
    fun  setUserName(ctx: Context,userName:String){
        preference=ctx.getSharedPreferences(USER,Context.MODE_PRIVATE)
        editor=preference.edit()
        editor.putString("u_name",userName)
        editor.commit()
    }
    fun getUserName(ctx:Context):String{
        preference=ctx.getSharedPreferences(USER,Context.MODE_PRIVATE)
        editor=preference.edit()
        return preference.getString("u_name","")
    }
    // 사용자 메일
    fun setUserMail(ctx: Context,userMail:String){
        preference=ctx.getSharedPreferences(USER,Context.MODE_PRIVATE)
        editor=preference.edit()
        editor.putString("u_mail",userMail)
        editor.commit()
    }
    fun getUserMail(ctx:Context):String{
        preference=ctx.getSharedPreferences(USER,Context.MODE_PRIVATE)
        editor=preference.edit()
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
