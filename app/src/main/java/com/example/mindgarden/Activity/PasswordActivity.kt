package com.example.mindgarden.Activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.mindgarden.DB.SharedPreferenceController
import kotlinx.android.synthetic.main.activity_password.*
import org.jetbrains.anko.toast
import com.example.mindgarden.R


@Suppress("NAME_SHADOWING")
class PasswordActivity : AppCompatActivity() {

    val REQUEST_CODE_PASSWORD_ACTIVITY = 1000
    var subPassword: String = ""
    var firstPassword: String = ""
    var secondPassword: String = ""

    var isSet = true

    var previousPassword:String=""
    var whereFrom:String =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)
        toast("넘어왔다!")
        txtPassword.text="비밀번호를 입력해주세요."

        val intent=getIntent()
       intent.getStringExtra("whereFrom")?.let {
           whereFrom = it
           Log.e("from", whereFrom)
           if(whereFrom=="login"){
           txtPassword.text="비밀번호를 입력해주세요."
       }

       }

        toast(whereFrom.toString())

        SharedPreferenceController.setPassword(this,"1234")
        //SharedPreferenceController.setPassword(this,"")
        previousPassword=SharedPreferenceController.getPassword(this)
        // TODO
        // Get isPasswordSet to isSet from innerDB


        Log.e("isSet", previousPassword)

        setNumBtnClickListener()

        if (previousPassword == "") {

            //처음 암호를 설정하는 경우
            //새 암호를 입력하세요라는 문구로 바뀐 후
            txtPassword.text = "새 암호를 입력하세요"
        } else if (previousPassword != "") {
            //암호 변경하는 경우
            //일단 버튼 클릭
           if(whereFrom!="login") txtPassword.text = "기존 암호를 입력하세요"

            //intent.putExtra("isSet", isSet)
            setResult(Activity.RESULT_OK, intent)
        }


    }


    fun setNumBtnClickListener() {
        btn1.setOnClickListener {
            clickBtn(1)
        }
        btn2.setOnClickListener {
            clickBtn(2)
        }
        btn3.setOnClickListener {
            clickBtn(3)
        }
        btn4.setOnClickListener {
            clickBtn(4)
        }
        btn5.setOnClickListener {
            clickBtn(5)
        }
        btn6.setOnClickListener {
            clickBtn(6)
        }
        btn7.setOnClickListener {
            clickBtn(7)
        }
        btn8.setOnClickListener {
            clickBtn(8)
        }
        btn9.setOnClickListener {
            clickBtn(9)
        }
        btn0.setOnClickListener {
            clickBtn(0)
        }
        btnNumBack.setOnClickListener {
            clickBtn(-1)
        }
    }


    fun clickBtn(num: Int) {
        if (num != -1) {
            val length: Int = subPassword.length
            if (length < 4) {
                subPassword += num

                when (length + 1) {
                    1 -> {
                        password1.isSelected = !password1.isSelected
                    }
                    2 -> {
                        password2.isSelected = !password2.isSelected
                    }
                    3 -> {
                        password3.isSelected = !password3.isSelected
                    }
                    4 -> {
                        password4.isSelected = !password4.isSelected
                        password1.isSelected = false
                        password2.isSelected = false
                        password3.isSelected = false
                        password4.isSelected = false
                        if (previousPassword == "") {
                            if (firstPassword == "") {
                                //첫번쨰 비밀번호를 입력한다
                                firstPassword = subPassword
                                subPassword = ""
                                txtPassword.text = "다시 입력하세요"
                            } else {
                                secondPassword = subPassword
                                subPassword = ""
                                if (firstPassword == secondPassword) {
                                    //TODO
                                    //내부DB에 비밀번호 저장
                                    SharedPreferenceController.setPassword(this, firstPassword)
                                    finish()
                                } else {
                                    toast("비밀번호가 다릅니다")
                                    firstPassword = ""
                                    txtPassword.text = "새 암호를 입력하세요"
                                }
                            }
                        } else {

                            // 암호설정이 되어있는 경우
                            //previousPassword = "1234"
                            if (whereFrom == "login") {
                                //txtPassword.text="비밀번호를 입력하세요."
                                if (previousPassword == subPassword){
                                    Log.e("login","이제 메인으로 넘어갈거야")
                                    val intent2 = Intent(this, MainActivity::class.java)
                                    startActivity(intent2)
                                    finish()
                                }
                            }
                                else{
                                // TODO
                                // Load previous password to previousPassword
                                // 암호변경 하는 경우
                                if (previousPassword == subPassword) {
                                    subPassword = ""
                                    txtPassword.text = "새 암호를 입력하세요"
                                    previousPassword = ""
                                } else {// 기존 비
                                    toast("비밀번호 틀렸어")
                                    subPassword = ""
                                    txtPassword.text = "다시 암호를 입력해주세요"
                                    //isSet = true
                                    //finish()}
                                    /* while(previousPassword != subPassword){
                                    subPassword =""
                                    txtPassword.text = "다시 암호를 입력하세요"

                                subPassword = ""
                                txtPassword.text = "새 암호를 입력하세요"
                                isSet = false*/
                                }
                        }
                        }
                    }
                    }
                }
            } else {
                val length: Int = subPassword.length
                if (length != 0) {
                    // 스트링이 0이 아니면 비밀번로 맨 뒤에 숫자 삭제/
                    //민트색 회색으로 바꿈
                    val len = IntRange(0, length - 2)
                    subPassword = subPassword.substring(len)
                    //그 버튼 누르면 마지막 민트색이 회색으로 바뀜
                    when (length) {
                        1 -> {
                            password1.isSelected = false
                        }
                        2 -> {
                            password2.isSelected = false
                        }
                        3 -> {
                            password3.isSelected = false
                        }
                        4 -> {
                            password4.isSelected = false
                        }
                    }
                }
            }
        }
    }
