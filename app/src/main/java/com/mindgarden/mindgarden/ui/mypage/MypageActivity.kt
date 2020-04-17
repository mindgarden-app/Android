package com.mindgarden.mindgarden.ui.mypage

import android.content.DialogInterface
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
        val deleteUserButtonClick = { dialog: DialogInterface, which: Int ->
            deleteUser()
        }
        var dlg = AlertDialog.Builder(this, R.style.NewDialogStyle)
        dlg.setTitle("")
            .setMessage("  계정을 삭제하시겠습니까?  ")
            .setNegativeButton("취소", null)
            .setPositiveButton("삭제하기",deleteUserButtonClick)

        var dlgNew: AlertDialog = dlg.show()

        messageModi(dlgNew)

        dlgNew.window.setBackgroundDrawableResource(R.drawable.round_layout_border)

        dlgNew.show()

        twoButtonModi(dlgNew)
    }

    fun messageModi(dlgNew : AlertDialog) {
        var messageText: TextView? = dlgNew.findViewById(android.R.id.message)
        messageText!!.gravity = Gravity.CENTER
        messageText!!.typeface = ResourcesCompat.getFont(this, R.font.notosanscjkr_medium)
        messageText!!.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f)
        messageText!!.setTextColor(getColor(R.color.colorBlack2b))
    }


    fun twoButtonModi(dlgNew : AlertDialog) {
        val btnPositive = dlgNew.getButton(AlertDialog.BUTTON_POSITIVE);
        val btnNegative = dlgNew.getButton(AlertDialog.BUTTON_NEGATIVE);

        btnNegative.typeface = ResourcesCompat.getFont(this, R.font.notosanscjkr_medium)
        btnNegative.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f)
        btnNegative.setTextColor(getColor(R.color.colorBlack2b))

        btnPositive.typeface = ResourcesCompat.getFont(this, R.font.notosanscjkr_medium)
        btnPositive.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f)
        btnPositive.setTextColor(getColor(R.color.colorRed))

        val layoutParams : LinearLayout.LayoutParams = btnPositive.layoutParams as LinearLayout.LayoutParams
        layoutParams.weight = 10f;
        btnPositive.setLayoutParams(layoutParams);
        btnNegative.setLayoutParams(layoutParams);
    }
    private fun deleteUser() {
        TokenController.isValidToken(this,repository)

        repository
            .deleteUser(TokenController.getAccessToken(this),
                {
                    if(it.success){
                        TokenController.clearRefreshToken(this)
                    }
                    Log.e("Delete User", it.message)
                },
                {
                    //에러처리
                })
    }

}
