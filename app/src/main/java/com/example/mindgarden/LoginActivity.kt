package com.example.mindgarden

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

      btnLogin.setOnClickListener {
          val intent = Intent(this, MypageActivity::class.java)
          //로그인 해야되는데 마이페이지로 넘어가는 걸로 구현(임시)

          startActivity(intent)

          finish()
      }
    }
}
