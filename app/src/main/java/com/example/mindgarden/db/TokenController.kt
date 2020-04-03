package com.example.mindgarden.db

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import com.example.mindgarden.data.MindgardenRepository
import com.example.mindgarden.ui.login.LoginActivity
import org.koin.android.ext.android.inject
import kotlin.coroutines.coroutineContext

object TokenController {

    val TOKEN = "token"

    lateinit var preference: SharedPreferences
    lateinit var editor: SharedPreferences.Editor



    fun setAccessToken(ctx: Context, userId: String) {
        preference = ctx.getSharedPreferences(TOKEN, Context.MODE_PRIVATE)
        editor = preference.edit()
        editor.putString("access_token", userId)
        editor.commit()
    }

    fun getAccessToken(ctx: Context): String {
        preference = ctx.getSharedPreferences(TOKEN, Context.MODE_PRIVATE)
        editor = preference.edit()
        return preference.getString("access_token", "")
    }


    //사용자 -> 토큰으로 대체될 예정
    fun setRefreshToken(ctx: Context, userId: String) {
        preference = ctx.getSharedPreferences(TOKEN, Context.MODE_PRIVATE)
        editor = preference.edit()
        editor.putString("refresh_token", userId)
        editor.commit()
    }

    fun getRefreshToken(ctx: Context): String {
        preference = ctx.getSharedPreferences(TOKEN, Context.MODE_PRIVATE)
        editor = preference.edit()
        return preference.getString("refresh_token", "")
    }

    //리프레시 토큰이 만료될 경우 사용
    fun clearRefreshToken(ctx:Context) {
        preference = ctx.getSharedPreferences(TOKEN, Context.MODE_PRIVATE)

        val userPreference = ctx.getSharedPreferences("user", Context.MODE_PRIVATE)
        val userEditor = userPreference.edit()
        userEditor.clear()
        userEditor.apply()

        editor = preference.edit()
        editor.clear()
        editor.commit()
    }

    fun setStartTimeAccessToken(ctx: Context, tiemToStart: Long) {
        preference = ctx.getSharedPreferences(TOKEN, Context.MODE_PRIVATE)
        editor = preference.edit()
        editor.putLong("access_token_start_time", tiemToStart)
        editor.commit()
    }

    fun getTimeAccessToken(ctx: Context): Long {
        preference = ctx.getSharedPreferences(TOKEN, Context.MODE_PRIVATE)
        editor = preference.edit()
        return preference.getLong("access_token_start_time", 0)
    }

    fun setExpAccessToken(ctx: Context, exp: Long) {
        preference = ctx.getSharedPreferences(TOKEN, Context.MODE_PRIVATE)
        editor = preference.edit()
        editor.putLong("access_token_exp", exp)
        editor.commit()
    }

    fun getExpAccessToken(ctx: Context): Long {
        preference = ctx.getSharedPreferences(TOKEN, Context.MODE_PRIVATE)
        editor = preference.edit()
        return preference.getLong("access_token_exp", 0)
    }

    fun isValidToken(ctx: Context,repository:MindgardenRepository):Boolean{
        if (getAccessToken(ctx) == "") {
            clearRefreshToken(ctx)

            val intent = Intent(ctx, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            ctx.startActivity(intent)
            return false
        }else{
            val currentTime = System.currentTimeMillis()
            Log.e("Token Controller isValid", currentTime.toString())
            Log.e("Token Controller isValid", getTimeAccessToken(ctx).toString())

            Log.e("Token Controller isValid", (currentTime - getTimeAccessToken(ctx)).toString())
            if (getExpAccessToken(ctx) * 10 > currentTime - getTimeAccessToken(ctx)) {
                Log.e("Token Controller isValid", "is vaildate")
                return true

            } else {
                Log.e("Token Controller", "is not vaildate")
                RenewAcessTokenController.postRenewAccessToken(ctx, repository)
                return false
            }
        }
    }
}