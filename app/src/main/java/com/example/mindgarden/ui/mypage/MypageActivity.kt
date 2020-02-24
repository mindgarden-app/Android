package com.example.mindgarden.ui.mypage

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.widget.TextView
import com.example.mindgarden.DB.SharedPreferenceController
import com.example.mindgarden.DB.TokenController
import com.example.mindgarden.Network.ApplicationController
import com.example.mindgarden.Network.Delete.DeleteUserResponse
import com.example.mindgarden.Network.NetworkService
import com.example.mindgarden.R
import com.example.mindgarden.DB.RenewAcessTokenController
import com.example.mindgarden.ui.alarm.AlarmSettingActivity
import com.example.mindgarden.ui.login.LoginActivity
import com.example.mindgarden.ui.password.PasswordSettingActivity
import kotlinx.android.synthetic.main.activity_mypage.*
import kotlinx.android.synthetic.main.toolbar_mypage_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response




class MypageActivity : AppCompatActivity() {

    val networkService: NetworkService by lazy{
        ApplicationController.instance.networkService
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage)

        userName.text=SharedPreferenceController.getUserName(this)
        userMail.text=SharedPreferenceController.getUserMail(this)

        //Toolbar
        btnBack.setOnClickListener {
            //뒤로가기
            onBackPressed()
        }

        //RelativeLayout
        btnLogout.setOnClickListener {
            //TODO TokenController.clearRefreshToken(this)
            //TODO 로그아웃 코드 추가해야함 웹쿠키
            val intent = Intent(this, LoginActivity::class.java)
            //로그아웃 누르면 다시 일단 로그인 페이지로
            startActivity(intent)
            finish()
        }
        btnDelete.setOnClickListener{
            var dlg = AlertDialog.Builder(this, R.style.MyAlertDialogStyle)

            dlg.setMessage("계정 삭제는 이메일로 문의해주세요.\n\n"+"mindgarden2019@gmail.com")


            dlg.setNeutralButton("                             확인         ", null)




            var dlgNew: AlertDialog = dlg.show()
            var messageText:TextView? = dlgNew.findViewById(android.R.id.message)
            messageText!!.gravity= Gravity.CENTER



            dlgNew.window.setBackgroundDrawableResource(R.drawable.round_layout_border)

            dlgNew.show()

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
private fun deleteUserResponse() {
if(!TokenController.isValidToken(this)){
RenewAcessTokenController.postRenewAccessToken(this)
}

val deleteDiaryListResponse = networkService.deleteUserResponse(
TokenController.getAccessToken(this))
Log.e("delete", "delete")

deleteDiaryListResponse.enqueue(object: Callback<DeleteUserResponse> {
override fun onFailure(call: Call<DeleteUserResponse>, t: Throwable) {

}
override fun onResponse(call: Call<DeleteUserResponse>, response: Response<DeleteUserResponse>) {

    if (response.isSuccessful) {
        if (response.body()!!.status == 200) {
           Log.e("Delete User",response.body()!!.message)

        }
    }
}


})
}
}