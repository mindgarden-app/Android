package com.example.mindgarden

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.Switch
import kotlinx.android.synthetic.main.toolbar_mypage_main.*
import org.jetbrains.anko.toast

class PasswordSettingActivity : AppCompatActivity() {
    val REQUEST_CODE_PASSWORD_SETTING_ACTIVITY=1000

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_setting)
        txtSetting.text = "암호 설정"

        btnBack.setOnClickListener {
            val intent = Intent(this, MypageActivity::class.java)
            // 백 스페이스 누르면 다시 메인 페이지로

            startActivity(intent)

            finish()
        }
        val passwordSwitch: Switch = findViewById(R.id.passwordSwitch)
        val changePassword: Button =findViewById(R.id.changePassword)
        passwordSwitch.setOnCheckedChangeListener  { _, isChecked ->


            if (isChecked) {
                // The toggle is enabled
                changePassword.setTextColor(Color.BLACK)

                val passwordIntent = Intent(this, PasswordActivity::class.java)
                // 암호 처음 설정합니다!
                passwordIntent.putExtra("isSet",false)
                startActivityForResult(passwordIntent,REQUEST_CODE_PASSWORD_SETTING_ACTIVITY)

                // val intent = Intent(this, PasswordActivity::class.java)
                //로그인 해야되는데 마이페이지로 넘어가는 걸로 구현(임시)



                //이 부분에 암호 설정하는 액티비티로 넘어감

            }
            toast("ddksk")

            //color에 있는 검정색 어떻게 쓰는지 모르겠어
        }


        changePassword.setOnClickListener {
            val passwordIntent2 = Intent(this, PasswordActivity::class.java)
            // 암호변겅을 누르면
            passwordIntent2.putExtra("isSet",true)
            startActivityForResult(passwordIntent2,REQUEST_CODE_PASSWORD_SETTING_ACTIVITY)
            finish()

        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_CODE_PASSWORD_SETTING_ACTIVITY){
            if(resultCode == Activity.RESULT_OK){
                val isSet= data!!.getBooleanExtra("isSet",false)
                /*
                // without Anko
                Toast.makeText(this, "End time: ${e_time}", Toast.LENGTH_SHORT).show()
                */
                // with Anko
                toast("End time: ${isSet}")
            }
        }
    }
}
