package com.example.mindgarden.Activity

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_password.*
import org.jetbrains.anko.toast
import com.example.mindgarden.R

var checkPassword: String = ""

class PasswordActivity : AppCompatActivity() {

    val REQUEST_CODE_PASSWORD_ACTIVITY = 1000
    var subPassword: String = ""
    var firstPassword: String = ""
    var secondPassword: String = ""

    var isSet = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)
        toast("넘어왔다!")

        // TODO
        // Get isPasswordSet to isSet from innerDB

        Log.e("isSet", isSet.toString())

        setNumBtnClickListener()

        if (isSet == false) {

            //처음 암호를 설정하는 경우
            //새 암호를 입력하세요라는 문구로 바뀐 후
            txtPassword.text = "새 암호를 입력하세요"
        } else if (isSet == true) {
            //암호 변경하는 경우
            //일단 버튼 클릭
            txtPassword.text = "기존 암호를 입력하세요"

            intent.putExtra("isSet", isSet)
            setResult(Activity.RESULT_OK, intent)
        }

        btnForgetPw.setOnClickListener {
            // 통신 아마 메일로 보내줘!!!!!!!!!!일듯
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
                        if (isSet == false) {
                            if (firstPassword == "") {
                                firstPassword = subPassword
                                subPassword = ""
                                txtPassword.text = "다시 입력하세요"
                            } else {
                                secondPassword = subPassword
                                subPassword = ""
                                if (firstPassword == secondPassword) {
                                    //TODO
                                    //내부DB에 비밀번호 저장
                                    finish()
                                } else {
                                    toast("비밀번호가 다릅니다")
                                    firstPassword = ""
                                    txtPassword.text = "새 암호를 입력하세요"
                                }
                            }
                        } else {
                            var previousPassword = "1234"
                            // TODO
                            // Load previous password to previousPassword
                            if (previousPassword == subPassword) {
                                subPassword = ""
                                txtPassword.text = "새 암호를 입력하세요"
                                isSet = false
                            } else {// 기존 비
                                toast("비밀번호 틀렸어")
                                subPassword = ""
                                txtPassword.text = "다시 암호를 입력해주세요"
                                isSet = true
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
}