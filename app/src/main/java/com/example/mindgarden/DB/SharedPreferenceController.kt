package com.example.mindgarden.DB

import android.content.Context
import android.content.SharedPreferences

object SharedPreferenceController {
    val MY_ACCOUNT="unique_string"
    val PASSWORD="unique_string2"
    val USER_NAME="unique_strng3"
    val USER_MAIL="unique_strng4"

    fun setUserID(ctx: Context,userId:Int){
        val  preference: SharedPreferences=ctx.getSharedPreferences(MY_ACCOUNT,Context.MODE_PRIVATE)
        val  editor:SharedPreferences.Editor=preference.edit()
        editor.putInt("u_id",userId)
        editor.commit()
    }
    fun getUserID(ctx:Context):Int{
        val preference:SharedPreferences=ctx.getSharedPreferences(MY_ACCOUNT,Context.MODE_PRIVATE)
        return preference.getInt("u_id",-9)
    }
    fun clearUserID(ctx:Context){
        val preference:SharedPreferences=ctx.getSharedPreferences(MY_ACCOUNT,Context.MODE_PRIVATE)
        val editor:SharedPreferences.Editor=preference.edit()
        //로그아웃하면 앱 내 비밀번호도 없어진다!
        editor.clear()
        editor.commit()
    }
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
}
