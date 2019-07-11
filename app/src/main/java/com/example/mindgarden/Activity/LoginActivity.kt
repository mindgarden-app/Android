package com.example.mindgarden.Activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.TabLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.ImageView
import android.widget.Toast
import com.example.mindgarden.R
import com.example.mindgarden.Adapter.SliderLoginPagerAdapter
import com.example.mindgarden.DB.SharedPreferenceController
import com.example.mindgarden.Network.ApplicationController
import com.example.mindgarden.Network.Get.GetLoginResponse
import com.example.mindgarden.Network.NetworkService
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import im.delight.android.webview.AdvancedWebView
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.logging.Logger

class LoginActivity : AppCompatActivity() {
    private val PERMISSION_CALLBACK_CONSTANT = 101
    private val REQUEST_PERMISSION_SETTING = 101
    private var permissionsRequired = arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private var permissionStatus: SharedPreferences? = null
    private var sentToSettings = false




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        configureMainTab()

        permissionStatus = getSharedPreferences("permissionStatus", Context.MODE_PRIVATE)
        requestPermission()
        //setupPermissions(permissionsRequired[0])
        //setupPermissions(permissionsRequired[1])
        //setupPermissions(permissionsRequired[2])
        val myWebView = findViewById<AdvancedWebView>(R.id.webView) as AdvancedWebView
        val settings = myWebView.settings

        btnLogin.setOnClickListener {
            val loginIntent= Intent(this, SocialLoginActivity::class.java)
            // 암호변겅을 누르면

            startActivity(loginIntent)

            //settings.domStorageEnabled = true

        }

        //getLoginResponse()

        // val url = "http://13.125.190.74:3000/auth/login/success"


        /*val loginIntent= Intent(this, PasswordActivity::class.java)
            // 암호변겅을 누르면
            loginIntent.putExtra("whereFrom","login")
            startActivity(loginIntent)
            //startActivityForResult(passwordIntent2,REQUEST_CODE_LOGIN_ACTIVITY)
            finish()
            //로그인 해야되는데 마이페이지로 넘어가는 걸로 구현(임시)
            // 일단 로그인 받아오면?>>>
            //  startActivity<PasswordActivity>("from" to  "login")
            */

    }


    /*private fun getLoginResponse(){
        var jsonObject = JSONObject()
        val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject

        val getLoginResponse = networkService.getLoginResponse(
            "application/json", gsonObject)
        getLoginResponse.enqueue(object:Callback<GetLoginResponse>{
            override fun onFailure(call: Call<GetLoginResponse>, t: Throwable) {
                Log.e("login", t.toString())
            }

            override fun onResponse(call:  Call<GetLoginResponse>, response: Response<GetLoginResponse>)  {

                if (response.isSuccessful) {
                    if (response.body()!!.status == 200) {
                        val tmp: Int = response.body()!!.data!!
                        Log.e("json",tmp.toString())
                        SharedPreferenceController.setUserID(this@LoginActivity,tmp)
                        Log.e("userID",SharedPreferenceController.getUserID(this@LoginActivity).toString())
                        //데베에 저장
                    }
                }
            }
        })
    }*/
                private fun requestPermission() {
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            permissionsRequired[0]
                        ) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(
                            this,
                            permissionsRequired[1]
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsRequired[0])
                            || ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsRequired[1])
                        ) {
                            //Show Information about why you need the permission
                            getAlertDialog()
                            Log.e("per1", permissionsRequired[0])
                        } else if (permissionStatus!!.getBoolean(permissionsRequired[0], false)) {
                            Log.e("per2", permissionsRequired[1])
                            //Previously Permission Request was cancelled with 'Dont Ask Again',
                            // Redirect to Settings after showing Information about why you need the permission
                            val builder = AlertDialog.Builder(this)
                            builder.setTitle("Need Multiple Permissions")
                            builder.setMessage("This app needs permissions.")
                            builder.setPositiveButton("Grant") { dialog, which ->
                                dialog.cancel()
                                sentToSettings = true
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                val uri = Uri.fromParts("package", packageName, null)
                                intent.data = uri
                                startActivityForResult(intent, REQUEST_PERMISSION_SETTING)
                                Toast.makeText(applicationContext, "Go to Permissions to Grant ", Toast.LENGTH_LONG)
                                    .show()
                            }
                            builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
                            builder.show()
                        } else {
                            Log.e("per3", permissionsRequired[2])
                            //just request the permission
                            ActivityCompat.requestPermissions(this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT)
                        }
                        val editor = permissionStatus!!.edit()
                        editor.putBoolean(permissionsRequired[0], true)
                        editor.commit()

                        //   txtPermissions.setText("Permissions Required")


        } else {
            //You already have the permission, just go ahead.
            Toast.makeText(applicationContext, "Allowed All Permissions", Toast.LENGTH_LONG).show()
        }
    }
    /*private fun setupPermissions(requestPermission: String) {
        //스토리지 읽기 퍼미션을 permission 변수에 담는다
        val permission = ContextCompat.checkSelfPermission(this,requestPermission)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.e("TAG", "Permission to record denied")
    makeRequest(requestPermission)
}

}


private fun makeRequest(requestPermission: String) {
    ActivityCompat.requestPermissions(this,
        arrayOf( requestPermission),
        RECORD_REQUEST_CODE)
}

*/
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            var allgranted = false
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true
                } else {
                    allgranted = false
                    break
                }
            }

            if (allgranted) {
                Toast.makeText(applicationContext, "Allowed All Permissions", Toast.LENGTH_LONG).show()
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsRequired[0])
                || ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsRequired[1])
            ) {

                getAlertDialog()
            } else {
                Toast.makeText(applicationContext, "Unable to get Permission", Toast.LENGTH_LONG).show()
            }
        }
    }
        private fun getAlertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Need Multiple Permissions")
        builder.setMessage("This app needs permissions.")
        builder.setPositiveButton("Grant") { dialog, i->
            dialog.cancel()
            ActivityCompat.requestPermissions(this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT)
        }
        builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
        builder.show()
    }

    override fun onPostResume() {
        super.onPostResume()
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                Toast.makeText(applicationContext, "Allowed All Permissions", Toast.LENGTH_LONG).show()
            }
        }
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