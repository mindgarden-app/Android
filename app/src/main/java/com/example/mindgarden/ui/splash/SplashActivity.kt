package com.example.mindgarden.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mindgarden.ui.login.LoginActivity
import java.lang.Exception


class SplashActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        try {
            Thread.sleep(2000)
            val intent = Intent(this, LoginActivity::class.java)
            //스플래시 화면에서 로그인 화면으로 넘어가는 걸로 구현

            startActivity(intent)

            finish()
        } catch (e: Exception) {
        }
    }
}