package com.mindgarden.mindgarden.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import com.mindgarden.mindgarden.R
import com.mindgarden.mindgarden.data.MindgardenRepository
import com.mindgarden.mindgarden.ui.base.BaseActivity
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_email_password.*
import kotlinx.android.synthetic.main.toolbar_mypage_main.view.*
import org.json.JSONObject
import org.koin.android.ext.android.inject

class EmailPasswordActivity : BaseActivity(R.layout.activity_email_password) {
    private val repository: MindgardenRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        toolbar_email_password.txtSetting.text = "비밀번호 재설정"
        toolbar_email_password.btnBack.setOnClickListener {
            finish()
        }
        btn_email_password_find.setOnClickListener {
            //통신
            //통신안에서 성공하면 다음 뷰로 넘어가고 실패하면 팝업 띄우기
            postEmailSendPassword(edt_email_password_forgot.text.toString())
        }

        edt_email_password_forgot.setOnFocusChangeListener { view, b ->
            if (b) {
                view.setBackgroundResource(R.drawable.grid_border)
            } else {
                view.setBackgroundResource(R.drawable.gray_border_square)
            }
        }
        edt_email_password_forgot.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(edt_email_password_forgot.text).matches()) {
                    txt_check_email_password_forgot.visibility = View.INVISIBLE
                    btn_email_password_find.isEnabled = true
                    btn_email_password_find.setBackgroundResource(R.drawable.green_border_square)
                    btn_email_password_find.setTextColor(getColor(R.color.colorWhite))
                } else {
                    txt_check_email_password_forgot.visibility = View.VISIBLE
                    btn_email_password_find.isEnabled = false
                    btn_email_password_find.setBackgroundResource(R.drawable.grid_border)
                    btn_email_password_find.setTextColor(getColor(R.color.colorPrimaryMint))
                }

            }
        })
    }


    private fun postEmailSendPassword(email: String) {
        var jsonObject = JSONObject()
        //TODO 수정 필요 함수파라메터 useIdx에서 accessToken으로 바꿈
        //TODO 수정 필요 jsonObject.put("userIdx", userIdx)

        jsonObject.put("email", email)

        val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject

        repository
            .postEmailSendPassword(gsonObject,
                {
                    if (it.message != "메일 전송 성공") {
                        Log.e("email",it.message)

                        showDialog()
                    }
                    else{
                        val sendPasswordIntent = Intent(this, EmailSendPasswordActivity::class.java)
                        sendPasswordIntent.putExtra("email", edt_email_password_forgot.text.toString())
                        startActivity(sendPasswordIntent)
                        //finish()

                    }
                },
                {
                    //에러처리
                })
    }

    fun showDialog() {
        var dlg = AlertDialog.Builder(this, R.style.NewDialogStyle)
        dlg.setMessage("\n\n존재하지 않는 메일 주소입니다.").setPositiveButton("\n다시 입력하기", null)

        var dlgNew: AlertDialog = dlg.show()

        messageModi(dlgNew)

        dlgNew.window.setBackgroundDrawableResource(R.drawable.round_layout_border)

        dlgNew.show()

        buttonModi(dlgNew)
    }

    fun messageModi(dlgNew : AlertDialog) {
        var messageText: TextView? = dlgNew.findViewById(android.R.id.message)
        messageText!!.gravity = Gravity.CENTER
        messageText!!.typeface = ResourcesCompat.getFont(this, R.font.notosanscjkr_medium)
        messageText!!.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f)
        messageText!!.setTextColor(getColor(R.color.colorBlack2b))
    }

    fun buttonModi(dlgNew : AlertDialog) {
        val button : Button = dlgNew.getButton(AlertDialog.BUTTON_POSITIVE)
        val parent : LinearLayout = button.parent as LinearLayout
        parent.gravity = Gravity.CENTER_HORIZONTAL
        val leftSpacer : View = parent.getChildAt(1)
        leftSpacer.visibility = View.GONE

        button.typeface = ResourcesCompat.getFont(this, R.font.notosanscjkr_regular)
        button.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f)
    }
}
