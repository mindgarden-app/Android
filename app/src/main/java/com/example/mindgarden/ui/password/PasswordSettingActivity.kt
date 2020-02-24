package com.example.mindgarden.ui.password

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.Switch
import com.example.mindgarden.ui.mypage.MypageActivity
import com.example.mindgarden.DB.SharedPreferenceController
import com.example.mindgarden.R
import kotlinx.android.synthetic.main.toolbar_mypage_main.*

class PasswordSettingActivity : AppCompatActivity() {
    val REQUEST_CODE_PASSWORD_SETTING_ACTIVITY=1000
    lateinit var  passwordSwitch: Switch
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

        passwordSwitch =findViewById (R.id.passwordSwitch)
        passwordSwitch.isChecked = SharedPreferenceController.getPasswordSwitchState(this)

        pwSwitchState(passwordSwitch.isChecked)

        passwordSwitch.setOnCheckedChangeListener  { _, isChecked ->
                pwSwitchState(isChecked)
        }

    }

    fun pwSwitchState(isChecked:Boolean){
        val changePassword : Button =findViewById(R.id.changePassword)
        changePassword.setTextColor(Color.BLACK)

        if (isChecked) {
            //처음 스위치를 눌렀을 경우
            if(SharedPreferenceController.getPasswordIsSet(this) != 100){
                val passwordIntent = Intent(this, PasswordActivity::class.java)
                // 암호 처음 설정합니다!
                passwordIntent.putExtra("from","passwordSetting")
                startActivity(passwordIntent)
            }

            changePassword.setOnClickListener {
                val passwordIntent2 = Intent(this, PasswordActivity::class.java)
                // 암호변겅을 누르면
                passwordIntent2.putExtra("isSet",false)
                startActivityForResult(passwordIntent2,REQUEST_CODE_PASSWORD_SETTING_ACTIVITY)
                finish()

            }
        }else{
            changePassword.isClickable = false
            SharedPreferenceController.setPassword(this,"")
            changePassword.setTextColor(Color.parseColor("#c6c6c6"))
        }

    }
    //switch 상태 저장
    override fun onPause() {
        super.onPause()
        SharedPreferenceController.setPasswordSwitchState(this ,passwordSwitch.isChecked,100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_CODE_PASSWORD_SETTING_ACTIVITY){
            if(resultCode == Activity.RESULT_OK){
                val isSet= data!!.getBooleanExtra("isSet",false)
                Log.e("End time", isSet.toString())
            }
        }
    }
}
