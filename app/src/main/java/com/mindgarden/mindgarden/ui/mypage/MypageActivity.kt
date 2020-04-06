package com.mindgarden.mindgarden.ui.mypage

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.mindgarden.mindgarden.db.SharedPreferenceController
import com.mindgarden.mindgarden.db.TokenController
import com.mindgarden.mindgarden.R
import com.mindgarden.mindgarden.data.MindgardenRepository
import com.mindgarden.mindgarden.ui.alarm.AlarmSettingActivity
import com.mindgarden.mindgarden.ui.base.BaseActivity
import com.mindgarden.mindgarden.ui.login.LoginActivity
import com.mindgarden.mindgarden.ui.password.PasswordSettingActivity
import kotlinx.android.synthetic.main.activity_mypage.*
import kotlinx.android.synthetic.main.toolbar_mypage_main.*
import org.koin.android.ext.android.inject


class MypageActivity : BaseActivity(R.layout.activity_mypage) {

    private val repository: MindgardenRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userName.text = SharedPreferenceController.getUserName(this)
        userMail.text = SharedPreferenceController.getUserMail(this)

        //Toolbar
        btnBack.setOnClickListener {
            //뒤로가기
            onBackPressed()
        }

        //RelativeLayout
        btnLogout.setOnClickListener {
            TokenController.clearRefreshToken(this)

            val intent = Intent(this, LoginActivity::class.java)
            //로그아웃 누르면 다시 일단 로그인 페이지로
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }
        btnDelete.setOnClickListener {
            showDialog()

            //TODO 웹쿠키 지우고 내부 디비 지우고 계정삭제

            /*
            deleteUserResponse()
            TokenController.clearRefreshToken(this)



            val Intent= Intent(this, WebviewLoginActivity::class.java)
            Intent.putExtra("whyOpen","deleteUser")
            startActivity(Intent)
            */

        }

            //LinerLayout 버튼들
       btnPasswordSetting.setOnClickListener {
           val intent = Intent(this, PasswordSettingActivity::class.java)
           //암호 설정버튼 누르면 암호 액티비티로 넘어가는 것으로 구현

            startActivity(intent)

            finish()
        }

        alarmSetting.setOnClickListener {
            val intent3 = Intent(this, AlarmSettingActivity::class.java)
// 알람 설정하는 페이즈로 넘어감

            startActivity(intent3)

            finish()
        }
    }

    fun showDialog() {
        var dlg = AlertDialog.Builder(this, R.style.NewDialogStyle)
        dlg.setTitle(" ")
            .setMessage("\n계정 삭제는 이메일로 문의해주세요.\n" + "mindgarden2019@gmail.com")
            .setPositiveButton("\n확인", null)

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

        button.typeface = ResourcesCompat.getFont(this, R.font.notosanscjkr_medium)
        button.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f)
    }

    private fun deleteUser() {
        TokenController.isValidToken(this,repository)

        repository
            .deleteUser(TokenController.getAccessToken(this),
                {
                    Log.e("Delete User", it.message)
                },
                {
                    //에러처리
                })
    }

}