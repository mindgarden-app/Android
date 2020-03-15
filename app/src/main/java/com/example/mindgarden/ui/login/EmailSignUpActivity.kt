package com.example.mindgarden.ui.login

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.mindgarden.R
import com.example.mindgarden.data.MindgardenRepository
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_email_sign_up.*
import kotlinx.android.synthetic.main.activity_email_sign_up.edt_email
import kotlinx.android.synthetic.main.toolbar_write_diary.view.*
import org.json.JSONObject
import org.koin.android.ext.android.inject
import java.util.regex.Pattern

class EmailSignUpActivity : AppCompatActivity() {
    private val repository: MindgardenRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_sign_up)

        toolbar_email_sign_up.txt_date_toolbar_write_diary.text = "이메일 회원가입"
        toolbar_email_sign_up.txt_save_toolbar.text = "등록"



        toolbar_email_sign_up.btn_save_diary_toolbar.setOnClickListener {
            if(canEnroll()){
                postEmailSignUp()
            }

//            val enrolledIntent = Intent(this, EmailSignInActivity::class.java)
//            startActivity(enrolledIntent)


        }
        //뒤로가기 버튼 누를시 signinactivity로 이동
        toolbar_email_sign_up.btn_back_toolbar.setOnClickListener {
            finish()
        }


        //Email
        edt_email.setOnFocusChangeListener { view, b ->

            hasFocus(view, b) //포커스 잡혀있으면 green  border로 바꾸기

        }
        //실시간 이메일인지  아닌지 확인
        edt_email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edt_email.text).matches()) {
                    txt_check_email.visibility = View.VISIBLE
                    txt_check_email.setTextColor(getColor(R.color.colorRed))
                } else {
                    txt_check_email.visibility = View.INVISIBLE

                }
            }
        })

        //이름
        edt_name.setOnFocusChangeListener { view, b ->
            hasFocus(view, b)

        }


        //비밀번호
        edt_password.setOnFocusChangeListener { view, b ->
            hasFocus(view, b)

        }
        edt_password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (isValidPassword()) {
                    txt_check_password.visibility = View.INVISIBLE

                } else {
                    txt_check_password.visibility = View.VISIBLE
                }
            }

        })


        //비밀번호 화인
        edt_password_check.setOnFocusChangeListener { view, b ->
            hasFocus(view, b)

        }

        edt_password_check.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (edt_password.text.toString() != edt_password_check.text.toString()) {
                    txt_check_password_again.visibility = View.VISIBLE
                    txt_check_password_again.setTextColor(getColor(R.color.colorRed))

                } else {
                    txt_check_password_again.visibility = View.INVISIBLE

                }
            }
        })

        checkBox_agree.setOnClickListener {

            if(checkBox_agree.isChecked){
                checkBox_agree.buttonTintList=getColorStateList(R.color.colorPrimaryMint)
            }else{
                checkBox_agree.buttonTintList=getColorStateList(R.color.colorBlockGray)
            }
        }



        //개인정보처리방침으로 이동
        btn_privacy_statement.setOnClickListener {
            val privacyIntent = Intent(this, PrivacyStatementActivity::class.java)
            startActivity(privacyIntent)
        }

        //이용약관으로 이동
        btn_terms_of_use.setOnClickListener {
            val termsOfUseIntent = Intent(this, TermsOfUseActivity::class.java)
            startActivity(termsOfUseIntent)
        }


    }


    //포커스 잡혔을 떄는 그린, 안잡혔을 때는 회색
    fun hasFocus(view: View, b: Boolean) {
        if (b) {
            view.setBackgroundResource(R.drawable.grid_border)
        } else {
            view.setBackgroundResource(R.drawable.gray_border_square)
        }
    }

    private fun canEnroll():Boolean {
        //빈칸이 다 없어야 하며
        //빈칸이 있을시에 포커스 옮겨줌

        if (txt_check_email.visibility == View.VISIBLE || edt_email.text.toString().isEmpty()) {
            edt_email.requestFocus()
            return false

        } else if (edt_name.text.toString().isEmpty()) {
            edt_name.requestFocus()
            return false
        } else if (txt_check_password.visibility == View.VISIBLE || edt_password.text.toString().isEmpty()) {
            edt_password.requestFocus()
            return false
        } else if (txt_check_password_again.visibility == View.VISIBLE || edt_password_check.text.toString().isEmpty()) {
            edt_password_check.requestFocus()
            return false
        } else if (!checkBox_agree.isChecked) {

            currentFocus.clearFocus()
            checkBox_agree.isChecked=true
            return false
        }else{
            return true
        }




    }

    //영문 숫자 8자 이상
    fun isValidPassword(): Boolean {
        return Pattern.matches(
            "^.*(?=.{8,20})(?=.*[0-9])(?=.*[a-zA-Z]).*\$",
            edt_password.text.toString()
        )
    }


    //회원가입 통신
    private fun postEmailSignUp() {
        var jsonObject = JSONObject()

        jsonObject.put("email", edt_email.text.toString())
        jsonObject.put("password", edt_password.text.toString())
        jsonObject.put("name", edt_name.text.toString())

        val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject

        val loginButtonClick = { dialog: DialogInterface, which: Int ->
          finish()
        }

        repository
            .postEmailSignUp(gsonObject,
                {
                    if (it.status == 200) {
                        if (it.success) {
                            Log.e("회원가입 성공 메세지", it.message)
                            //팝업 띄우기
                            var dlg = AlertDialog.Builder(this, R.style.MyAlertDialogStyle2)
                            dlg.setMessage("가입이 완료되었습니다.")
                                //.setNeutralButton("                              로그인하기                            ",loginButtonClick)
                            dlg.setPositiveButton("로그인하기", loginButtonClick)

                            var dlgNew: AlertDialog = dlg.show()
                            var messageText: TextView? = dlgNew.findViewById(android.R.id.message)
                            messageText!!.gravity = Gravity.CENTER

                            dlgNew.window.setBackgroundDrawableResource(R.drawable.round_layout_border)

                            dlgNew.show()

                            //버튼 가운데 정렬
                            val button : Button = dlgNew.getButton(AlertDialog.BUTTON_POSITIVE)
                            val parent : LinearLayout = button.parent as LinearLayout
                            parent.gravity = Gravity.CENTER_HORIZONTAL
                            val leftSpacer : View = parent.getChildAt(1)
                            leftSpacer.visibility = View.GONE
                        } else {
                            Log.e("회원가입 메세지", it.message)

                            var dlg = AlertDialog.Builder(this, R.style.MyAlertDialogStyle2)
                            dlg.setMessage("이미 등록된 메일입니다.")
                                //.setNeutralButton("                닫기        ",null)
                                //.setPositiveButton("       로그인하기          ",loginButtonClick)
                            dlg.setNegativeButton("닫기", null)
                            dlg.setPositiveButton("로그인하기", loginButtonClick)

                            var dlgNew: AlertDialog = dlg.show()
                            var messageText: TextView? = dlgNew.findViewById(android.R.id.message)
                            messageText!!.gravity = Gravity.CENTER
//                            var button1=dlgNew.getButton(0)
//                            button1.setTextColor(getColor(R.color.colorBlockGray))
//                            var button2=dlgNew.getButton(1)
//                            button2.setTextColor(getColor(R.color.colorPrimaryMint))

                            dlgNew.window.setBackgroundDrawableResource(R.drawable.round_layout_border)

                            dlgNew.show()

                            //2개 버튼 비율 같게 정렬 & 하나의 버튼 글씨 색 변경
                            val btnPositive = dlgNew.getButton(AlertDialog.BUTTON_POSITIVE);
                            val btnNegative = dlgNew.getButton(AlertDialog.BUTTON_NEGATIVE);
                            btnNegative.setTextColor(getColor(R.color.colorBlack2b))

                            val layoutParams : LinearLayout.LayoutParams = btnPositive.layoutParams as LinearLayout.LayoutParams
                            layoutParams.weight = 10f;
                            btnPositive.setLayoutParams(layoutParams);
                            btnNegative.setLayoutParams(layoutParams);
                        }
                    }
                },
                {
                    //에러처리
                }
            )
    }
}
