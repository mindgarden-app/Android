package com.example.mindgarden.Activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.example.mindgarden.R
import com.example.mindgarden.Adapter.SliderLoginPagerAdapter
import com.example.mindgarden.DB.SharedPreferenceController
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class LoginActivity : AppCompatActivity() {
    private val RECORD_REQUEST_CODE = 101
    val REQUEST_CODE_LOGIN_ACTIVITY=100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        configureMainTab()
        setupPermissions()

        btnLogin.setOnClickListener {

            val loginIntent= Intent(this, PasswordActivity::class.java)
            // 암호변겅을 누르면
            loginIntent.putExtra("whereFrom","login")
            startActivity(loginIntent)
            //startActivityForResult(passwordIntent2,REQUEST_CODE_LOGIN_ACTIVITY)
            finish()
            //로그인 해야되는데 마이페이지로 넘어가는 걸로 구현(임시)
            // 일단 로그인 받아오면?>>>
            //  startActivity<PasswordActivity>("from" to  "login")
        }
    }
    private fun setupPermissions() {
        //스토리지 읽기 퍼미션을 permission 변수에 담는다
        val permission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.READ_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.e("TAG", "Permission to record denied")
            makeRequest()
        }
    }
    private fun makeRequest() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            RECORD_REQUEST_CODE)
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {

        when(requestCode){
            RECORD_REQUEST_CODE ->{
//                if(grantResults.isNotEmpty()
//                            && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    toast("권한거부됨");

                }else{
                 toast("권한 자 ㄹ있어")
                    Log.i("tag", "Permission has been granted by user")
                }
                return
            }
        }
    }


    fun postLoginResponse(u_id:String,u_pw:String){
        SharedPreferenceController.setUserID(this,u_id)
    }
    private fun configureMainTab() {

        vpLoginSlider.adapter = SliderLoginPagerAdapter(supportFragmentManager, 3)
        vpLoginSlider.offscreenPageLimit = 2
        tlLoginIndicator.setupWithViewPager(vpLoginSlider)

        val navIndicatorLoginLayout: View =
            (this.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
                .inflate(R.layout.navigation_indicator_login, null, false)
        tlLoginIndicator.getTabAt(0)!!.customView =
            navIndicatorLoginLayout.findViewById(R.id.imgNavIndicatorLogin1) as ImageView
        tlLoginIndicator.getTabAt(1)!!.customView =
            navIndicatorLoginLayout.findViewById(R.id.imgNavIndicatorLogin2) as ImageView
        tlLoginIndicator.getTabAt(2)!!.customView =
            navIndicatorLoginLayout.findViewById(R.id.imgNavIndicatorLogin3) as ImageView

        tlLoginIndicator.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
                p0!!.customView!!.isSelected=false
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                p0!!.customView!!.isSelected=true
            }
        })
    }
}