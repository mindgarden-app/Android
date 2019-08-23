package com.example.mindgarden.Activity

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.mindgarden.DB.SharedPreferenceController
import com.example.mindgarden.DB.TokenController
import com.example.mindgarden.Network.ApplicationController
import com.example.mindgarden.Network.GET.GetForgetPasswordResponse
import com.example.mindgarden.Network.NetworkService
import kotlinx.android.synthetic.main.activity_password.*
import org.jetbrains.anko.toast
import com.example.mindgarden.R
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.jetbrains.anko.ctx
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Suppress("NAME_SHADOWING")
class PasswordActivity : AppCompatActivity() {

    val networkService: NetworkService by lazy{
        ApplicationController.instance.networkService
    }

    val REQUEST_CODE_PASSWORD_ACTIVITY = 1000
    var subPassword: String = ""
    var firstPassword: String = ""
    var secondPassword: String = ""
    var forgetPassword:String=""

    var isSet = true

    var previousPassword:String=""
    var whereFrom:String =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)

        val toast: Toast = Toast(ctx)
        val inflater: LayoutInflater = LayoutInflater.from(ctx)
        val toastView: View = inflater.inflate(R.layout.toast, null)
        val toastText: TextView = toastView.findViewById(R.id.toastText)

        txtPassword.text = "비밀번호를 입력해주세요."
        btnForgetPw.visibility= View.INVISIBLE
        btnForgetPw.isEnabled=false

        val intent = getIntent()
        intent.getStringExtra("whereFrom")?.let {
            whereFrom = it
            Log.e("from", whereFrom)
            if (whereFrom == "login") {
                txtPassword.text = "비밀번호를 입력해주세요."
                btnForgetPw.visibility= View.VISIBLE
                btnForgetPw.isEnabled=true
            }

        }

        toastText.setText(whereFrom.toString())
        toastText.gravity = Gravity.CENTER
        toast.view = toastView
        toast.show()
        btnForgetPw.setOnClickListener {
            getForgetPasswordResponse(TokenController.getAccessToken(this))
            Log.e("통신 후 받아온 비밀번호",forgetPassword)

            //TODO  다이얼로그 띄워서 확인버튼 누르면 인텐트로 보냄
            var dlg = AlertDialog.Builder(this, R.style.MyAlertDialogStyle)
            dlg.setMessage("메일로 발송된 번호를 입력하세요")
            fun do_p() {
                Log.e("다이얼로그",SharedPreferenceController.getPassword(this@PasswordActivity))
            }

            val dlg_listener = DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> do_p()
                }
            }

            dlg.setPositiveButton("확인", null)

            var dlgNew: AlertDialog = dlg.show()
            var messageText: TextView? = dlgNew.findViewById(android.R.id.message)
            messageText!!.gravity = Gravity.CENTER
            dlgNew.window.setBackgroundDrawableResource(R.drawable.round_layout_border)

            dlgNew.show()

            Log.e("메일로 4자리 받았어요!", SharedPreferenceController.getPassword(this))
            Log.e("다이얼로그",SharedPreferenceController.getPassword(this@PasswordActivity))

        }


        previousPassword = SharedPreferenceController.getPassword(this)
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
            if (whereFrom != "login") {
                txtPassword.text = "기존 암호를 입력하세요"
                btnForgetPw.visibility= View.VISIBLE
                btnForgetPw.isEnabled=true}

            //intent.putExtra("isSet", isSet)
            setResult(Activity.RESULT_OK, intent)
        }

    }


    fun getForgetPasswordResponse(accessToken:String){


        val getForgetPasswordResponse: Call<GetForgetPasswordResponse> =
            networkService.getForgetPasswordResponse("application/json",accessToken)
        getForgetPasswordResponse.enqueue(object:Callback<GetForgetPasswordResponse>{
            override fun onFailure(call: Call<GetForgetPasswordResponse>, t: Throwable) {
                Log.e("Fail: send email",t.toString())
            }

            override fun onResponse(call: Call<GetForgetPasswordResponse>, response: Response<GetForgetPasswordResponse>
            ) {
                if(response.isSuccessful){
                    if(response.body()!!.status==200){
                        Log.e("응답 받아오는 데이터","$response")

                        val tep2=response.body()!!.data
                        Log.e("비밀번호의 임시값은",tep2)
                        forgetPassword=tep2
                        SharedPreferenceController.setPassword(this@PasswordActivity,tep2)
                        Log.e("임시비밀번호로 데베에 저장",SharedPreferenceController.getPassword(this@PasswordActivity))
                        previousPassword=SharedPreferenceController.getPassword(this@PasswordActivity)
                        subPassword=""
                        setNumBtnClickListener()


                    }
                }
            }
        })

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
        val toast: Toast = Toast(ctx)
        val inflater: LayoutInflater = LayoutInflater.from(ctx)
        val toastView: View = inflater.inflate(R.layout.toast, null)
        val toastText: TextView = toastView.findViewById(R.id.toastText)

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
                            btnForgetPw.visibility= View.INVISIBLE
                            btnForgetPw.isEnabled=false
                            if (firstPassword == "") {
                                //첫번쨰 비밀번호를 입력한다
                                firstPassword = subPassword
                                subPassword = ""
                                txtPassword.text = "다시 입력하세요"
                                btnForgetPw.visibility= View.INVISIBLE
                                btnForgetPw.isEnabled=false
                            } else {
                                secondPassword = subPassword
                                subPassword = ""
                                if (firstPassword == secondPassword) {
                                    //TODO
                                    //내부DB에 비밀번호 저장
                                    SharedPreferenceController.setPassword(this, firstPassword)
                                    Log.e("바뀐 비밀번호",firstPassword)
                                    finish()
                                } else {
                                    toastText.setText("비밀번호가 다릅니다")
                                    toastText.gravity = Gravity.CENTER
                                    toast.view = toastView
                                    toast.show()
                                    firstPassword = ""
                                    txtPassword.text = "새 암호를 입력하세요"
                                    btnForgetPw.visibility= View.INVISIBLE
                                    btnForgetPw.isEnabled=false
                                }
                            }
                        } else {

                            // 암호설정이 되어있는 경우
                            //previousPassword = "1234"
                            if (whereFrom == "login") {

                                Log.e("암호",subPassword)
                                Log.e("저장된 암호",previousPassword)
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
                                    btnForgetPw.visibility= View.INVISIBLE
                                    btnForgetPw.isEnabled=false
                                } else {// 기존 비
                                    subPassword = ""
                                    txtPassword.text = "다시 암호를 입력해주세요"
                                    btnForgetPw.visibility= View.INVISIBLE
                                    btnForgetPw.isEnabled=false
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
