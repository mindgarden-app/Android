package com.example.mindgarden

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.mindgarden.R


class SplashActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)



        val intent = Intent(this, LoginActivity::class.java)
        //스플래시 화면에서 로그인 화면으로 넘어가는 걸로 구현

        startActivity(intent)

        finish()

    }

}