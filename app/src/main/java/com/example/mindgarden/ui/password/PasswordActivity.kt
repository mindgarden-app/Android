package com.example.mindgarden.ui.password

import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import com.example.mindgarden.ui.main.MainActivity
import com.example.mindgarden.db.SharedPreferenceController
import com.example.mindgarden.db.TokenController
import kotlinx.android.synthetic.main.activity_password.*
import com.example.mindgarden.R
import com.example.mindgarden.data.MindgardenRepository
import com.example.mindgarden.db.RenewAcessTokenController
import org.koin.android.ext.android.inject


@Suppress("NAME_SHADOWING")
class PasswordActivity : AppCompatActivity() {

    private val repository : MindgardenRepository by inject()

    val REQUEST_CODE_PASSWORD_ACTIVITY = 1000
    var subPassword: String = ""
    var firstPassword: String = ""
    var secondPassword: String = ""
    var forgetPassword:String=""

    var isSet = true

    lateinit var builderNew: android.app.AlertDialog

    var previousPassword:String=""
    var whereFrom:String =""
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //screen size
        val configuration : Configuration = resources.configuration
        configuration.fontScale = 1f
        val metrics : DisplayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        metrics.scaledDensity = configuration.fontScale * metrics.density
        configuration.densityDpi = resources.displayMetrics.xdpi.toInt()
        baseContext.resources.updateConfiguration(configuration, metrics)

        setContentView(R.layout.activity_password)

        val toast: Toast = Toast(this)
        val inflater: LayoutInflater = LayoutInflater.from(this)
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


        btnForgetPw.setOnClickListener {

            /*val builder = android.app.AlertDialog.Builder(this, R.style.AlarmDialogStyle)
            val dialogView = layoutInflater.inflate(R.layout.dialog_password_forget, null)
            builder.setView(dialogView)
            var mail:String =SharedPreferenceController.getUserMail(this)

            dialogView.txt_user_mail.text= (if (mail != null) mail+"으로\n새로운 비밀번호를 보내겠습니까?" else throw NullPointerException("Expression 'mail' must not be null"))

            builderNew= builder.show()
            builderNew.window.setBackgroundDrawableResource(R.drawable.round_layout_border)
            builderNew.show()


            //크기조절
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(builderNew.window.attributes)
            lp.width = 800
            val window = builderNew.window
            window.attributes = lp*/

            showDialog()
        }


        previousPassword = SharedPreferenceController.getPassword(this)
        // TODO
        // Get isPasswordSet to isSet from innerDB


        Log.e("isSet", previousPassword)

        setNumBtnClickListener()

        if (previousPassword == "") {
            //처음 암호를 설정하는 경우
            txtPassword.text = "비밀번호를 입력해주세요."
        } else if (previousPassword != "") {
            //암호 변경하는 경우
            //일단 버튼 클릭
            if (whereFrom != "login") {
                txtPassword.text = "기존 비밀번호를 입력해주세요."
                btnForgetPw.visibility= View.VISIBLE
                btnForgetPw.isEnabled=true}

            //intent.putExtra("isSet", isSet)
            setResult(Activity.RESULT_OK, intent)
        }

    }

    fun showDialog() {
        var builder = AlertDialog.Builder(this, R.style.NewDialogStyle)
        builder.setTitle(" ")

        var mail: String = SharedPreferenceController.getUserMail(this)
        if (mail != null) {
            builder.setMessage("\n" + mail + "으로\n새로운 비밀번호를 보내겠습니까?")
        } else {
            throw NullPointerException("Expression 'mail' must not be null")
        }

        builder.setNegativeButton("\n취소", null)
            .setPositiveButton("\n메일 보내기") {
                    dlgInterface: DialogInterface?, which: Int ->
                mClick()
                null
            }

        var dlg: AlertDialog = builder.show()

        messageModi(dlg)

        dlg.window.setBackgroundDrawableResource(R.drawable.round_layout_border)

        dlg.show()

        buttonModi(dlg)
    }

    fun messageModi(dlg : AlertDialog) {
        var messageText: TextView? = dlg.findViewById(android.R.id.message)
        messageText!!.gravity = Gravity.CENTER
        messageText!!.typeface = ResourcesCompat.getFont(this, R.font.notosanscjkr_medium)
        messageText!!.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f)
        messageText!!.setTextColor(getColor(R.color.colorBlack2b))
    }

    fun buttonModi(dlg : AlertDialog) {
        val btnNegative = dlg.getButton(AlertDialog.BUTTON_NEGATIVE);
        val btnPositive = dlg.getButton(AlertDialog.BUTTON_POSITIVE);

        btnNegative.gravity = Gravity.CENTER
        btnNegative.typeface = ResourcesCompat.getFont(this, R.font.notosanscjkr_regular)
        btnNegative.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f)
        btnNegative.setTextColor(getColor(R.color.colorBlack2b))

        btnPositive.gravity = Gravity.CENTER
        btnPositive.typeface = ResourcesCompat.getFont(this, R.font.notosanscjkr_medium)
        btnPositive.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f)

        val layoutParams : LinearLayout.LayoutParams = btnPositive.layoutParams as LinearLayout.LayoutParams
        layoutParams.weight = 10f;
        btnNegative.setLayoutParams(layoutParams);
        btnPositive.setLayoutParams(layoutParams);
    }

    /*fun mClick(v : View){
        when (v.id){
            R.id.btn_ok_password_forget -> {
                //메일보내기 눌렀을때
                getForgetPassword(TokenController.getAccessToken(this))
                Log.e("통신 후 받아온 비밀번호",forgetPassword)
                fun do_p() {
                    Log.e("다이얼로그",SharedPreferenceController.getPassword(this@PasswordActivity))

                    Log.e("메일로 4자리 받았어요!", SharedPreferenceController.getPassword(this))
                    Log.e("다이얼로그",SharedPreferenceController.getPassword(this@PasswordActivity))
                }
                builderNew.dismiss()

            }
            R.id.btn_cancel_password_forget->{
                builderNew.dismiss()
            }
        }

    }*/

    fun mClick() {
        //메일보내기 눌렀을때
        getForgetPassword(TokenController.getAccessToken(this))

        Log.e("통신 후 받아온 비밀번호", forgetPassword)

        fun do_p() {
            Log.e("다이얼로그", SharedPreferenceController.getPassword(this@PasswordActivity))

            Log.e("메일로 4자리 받았어요!", SharedPreferenceController.getPassword(this))
            Log.e("다이얼로그", SharedPreferenceController.getPassword(this@PasswordActivity))
        }
    }

    private fun getForgetPassword(accessToken:String){
        if(!TokenController.isValidToken(this)){
            Log.e("Main Activity token opposite state",(!TokenController.isValidToken(this)).toString())
            RenewAcessTokenController.postRenewAccessToken(this,repository)
        }

        repository
            .getForgetPassword(TokenController.getAccessToken(this),
                {
                    //Log.e("응답 받아오는 데이터","$response")

                    val tep2= it.data
                    Log.e("비밀번호의 임시값은",tep2)
                    forgetPassword=tep2
                    SharedPreferenceController.setPassword(this@PasswordActivity,tep2)
                    Log.e("임시비밀번호로 데베에 저장",SharedPreferenceController.getPassword(this@PasswordActivity))
                    previousPassword=SharedPreferenceController.getPassword(this@PasswordActivity)
                    subPassword=""
                    setNumBtnClickListener()
                },
                {
                    //에러처리
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
        val toast: Toast = Toast(this)
        val inflater: LayoutInflater = LayoutInflater.from(this)
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
                                txtPassword.text = "한 번 더 입력해주세요."
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
                                    toastText.setText("비밀번호가 다릅니다.")
                                    toastText.gravity = Gravity.CENTER
                                    toast.view = toastView
                                    toast.show()
                                    firstPassword = ""

                                    //새 암호 입력
                                    txtPassword.text = "새 비밀번호를 입력해주세요."
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
                                } else {
                                    //수정
                                    toastText.setText("비밀번호가 다릅니다.")
                                    toastText.gravity = Gravity.CENTER
                                    toast.view = toastView
                                    toast.show()

                                    subPassword = ""
                                }
                            }
                                else{
                                // TODO

                                // Load previous password to previousPassword
                                // 암호변경 하는 경우
                                if (previousPassword == subPassword) {
                                    subPassword = ""

                                    //새 암호 입력
                                    txtPassword.text = "새 비밀번호를 입력해주세요."
                                    previousPassword = ""
                                    btnForgetPw.visibility= View.INVISIBLE
                                    btnForgetPw.isEnabled=false
                                } else {// 기존 비
                                    subPassword = ""
                                    txtPassword.text = "한 번 더 입력해주세요."
                                    btnForgetPw.visibility= View.INVISIBLE
                                    btnForgetPw.isEnabled=false

                                    toastText.setText("비밀번호가 다릅니다.")
                                    toastText.gravity = Gravity.CENTER
                                    toast.view = toastView
                                    toast.show()

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
