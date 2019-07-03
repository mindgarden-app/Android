package com.example.mindgarden

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_password.*
import org.jetbrains.anko.toast
var checkPassword: String = ""
class PasswordActivity : AppCompatActivity() {

    val REQUEST_CODE_PASSWORD_ACTIVITY = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)
        toast("넘어왔다!")

        //var isSet:Boolean=false
        val intent : Intent = Intent()
        var isSet=intent.getBooleanExtra("isSet",false)



        when(isSet){
            false-> {
                //처음 암호를 설정하는 경우

                txtPassword.text = "새 암호를 입력하세요"
                //새 암호를 입력하세요라는 문구로 바뀐 후


                //입력값 받는다
                val temp = fourPassword()
                toast("비밀번호는 " + temp)


                    password1.isSelected = false
                    password2.isSelected = false
                    password3.isSelected = false
                    password4.isSelected = false

                // 다시한번 입력하세요라는 문구호 바꾸고
                //입력값 받는다
               // txtPassword.text = "다시 입력하세요"

                val temp2 = fourPassword()
                //비교한다
                //일치하면
                if (temp == temp2) checkPassword = temp2
                    isSet=true

                val passwordIntent = Intent(this, PasswordSettingActivity::class.java)
                // 백 스페이스 누르면 다시 메인 페이지로
                passwordIntent.putExtra("isSet", isSet)
                setResult(Activity.RESULT_OK, passwordIntent)
                //finish()


            }
            true->{
                //암호 변경하는 경우
                //일단 버튼 클릭
                val temp=fourPassword()

                //기존의 암호가 맞는지 확인해야 함
                //새 암호를 저장해야함
                if(checkPassword ==temp){
                    //기존의 암호가 맞는지 확인하고
                    //암호초기화
                    //버튼 초기화
                    password1.isSelected = false
                    password2.isSelected = false
                    password3.isSelected = false
                    password4.isSelected = false

                    txtPassword.text= "새 암호를 입력하세요"
                    //새 암호를 입력하세요라는 문구로 바뀐 후
                    //입력값 받는다
                    val temp2=fourPassword()
                    password1.isSelected = false
                    password2.isSelected = false
                    password3.isSelected = false
                    password4.isSelected = false

                    // 다시한번 입력하세요라는 문구호 바꾸고
                    //입력값 받는다
                    txtPassword.text = "다시 입력하세요"

                    val temp3=fourPassword()
                    //비교한다
                    //일치하면
                    if(temp2==temp3) checkPassword=temp2

                    val passwordIntent = Intent()
                    //
                    passwordIntent.putExtra("isSet", isSet)
                    setResult(Activity.RESULT_OK, passwordIntent)

                   // finish()

                }else{//다시 눌렀을 떄 틀렸을 경우???

                }

            }
        }


    }


    fun fourPassword():String{
        var subPassword:String=""


        btn1.setOnClickListener {
           subPassword=clickBtn(subPassword,1)
        }
        btn2.setOnClickListener {
            subPassword=clickBtn(subPassword,2)
        }
        btn3.setOnClickListener {
            subPassword=clickBtn(subPassword,3)
        }

        btn4.setOnClickListener {
            subPassword=clickBtn(subPassword,4)
        }
        btn5.setOnClickListener {
            subPassword=clickBtn(subPassword,5)
        }
        btn6.setOnClickListener {
            subPassword=clickBtn(subPassword,6)
        }
        btn7.setOnClickListener {
            subPassword=clickBtn(subPassword,7)
        }
        btn8.setOnClickListener {
            subPassword=clickBtn(subPassword,8)
        }
        btn9.setOnClickListener {
            subPassword=clickBtn(subPassword,8)
        }
        btnNumBack.setOnClickListener {
            subPassword=clickBtn(subPassword,0)
        }

        return subPassword


        }


    fun clickBtn(str:String, num: Int):String{
        var password=str
        if(num !=0) {
            val length: Int = password.length
           if(length<4) {
               password += num

            when (length+1) {
                1 -> {
                    toast("길이는 구한다")
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
                }
        }
           }
            toast(password)
        }
        else{
            val length: Int = password.length
            toast(password)
            if (length != 0) {
                // 스트링이 0이 아니면 비밀번로 맨 뒤에 숫자 삭제/
                //민트색 회색으로 바꿈
                toast(password)
                val len = IntRange(0, length-2)
                password=password.substring(len)
                //그 버튼 누르면 마지막 민트색이 회색으로 바뀜
                when (length) {
                    1 -> {
                        toast(password)
                        password1.isSelected = false
                    }
                    2 -> {
                        toast(password)
                        password2.isSelected = false
                    }
                    3 -> {
                        toast(password)
                        password3.isSelected = false
                    }
                    4 -> {
                        password4.isSelected = false
                    }
                }
            }
    }
        return password
    }
}