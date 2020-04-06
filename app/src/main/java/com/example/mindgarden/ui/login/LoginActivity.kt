package com.example.mindgarden.ui.login

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import com.google.android.material.tabs.TabLayout
import androidx.core.app.ActivityCompat
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.example.mindgarden.db.TokenController
import com.example.mindgarden.R
import com.example.mindgarden.data.MindgardenRepository
import com.example.mindgarden.db.RenewAcessTokenController
import com.example.mindgarden.db.SharedPreferenceController
import com.example.mindgarden.ui.base.BaseActivity
import com.example.mindgarden.ui.main.MainActivity
import com.example.mindgarden.ui.password.PasswordActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.android.ext.android.inject

class LoginActivity : BaseActivity(R.layout.activity_login) {
    private val repository: MindgardenRepository by inject()

    private val PERMISSION_CALLBACK_CONSTANT = 101
    private val REQUEST_PERMISSION_SETTING = 101
    private var permissionsRequired = arrayOf(
        Manifest.permission.WAKE_LOCK,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    private var permissionStatus: SharedPreferences? = null
    private var sentToSettings = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureMainTab()


        permissionStatus = getSharedPreferences("permissionStatus", Context.MODE_PRIVATE)
        requestPermission()
        //setupPermissions(permissionsRequired[0])
        //setupPermissions(permissionsRequired[1])
        //setupPermissions(permissionsRequired[2])

        btnWebLogin.setOnClickListener {


//            val loginIntent = Intent(this, WebviewLoginActivity::class.java)
//            startActivity(loginIntent)

            var dlg = AlertDialog.Builder(this, R.style.MyAlertDialogStyleNOTICE)
            dlg.setMessage("카카오 서비스가 업데이트 중이오니\n" +
                    "기존 사용자분들께서는\n" +
                    "mindgarden2019@gmail.com으로\n" +
                    "기존 이메일을 제출해주시면\n" +
                    "해결 도와드리도록 하겠습니다.")
                //.setNeutralButton("                                 확인                            ",null)
            dlg.setPositiveButton("확인", null)

            var dlgNew: AlertDialog = dlg.show()
            var messageText: TextView? = dlgNew.findViewById(android.R.id.message)
            messageText!!.gravity = Gravity.LEFT

            dlgNew.window.setBackgroundDrawableResource(R.drawable.round_layout_border)

            dlgNew.show()

            //버튼 가운데 정렬
            val button : Button = dlgNew.getButton(AlertDialog.BUTTON_POSITIVE)
            val parent : LinearLayout = button.parent as LinearLayout
            parent.gravity = Gravity.CENTER_HORIZONTAL
            val leftSpacer : View = parent.getChildAt(1)
            leftSpacer.visibility = View.GONE

            //settings.domStorageEnabled = true
        }
        btnEmailLogin.setOnClickListener {


            val emailLoginIntent=Intent(this,EmailSignInActivity::class.java)
            startActivity(emailLoginIntent)
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

    override fun onResume() {
        super.onResume()

        //토큰있으면 메인으로 돌려줘
        /*if(TokenController.getAccessToken(this)!=""){
            val alreadyLoginIntent = Intent(this,MainActivity::class.java)
            startActivity(alreadyLoginIntent)
        }*/

        //암호 작동
        if(TokenController.getAccessToken(this)!=""){
            val currentTime = System.currentTimeMillis()

            if (TokenController.getExpAccessToken(this) * 1000 > currentTime - TokenController.getTimeAccessToken(this)) {
                if(SharedPreferenceController.getPassword(this) != "") {
                    val passwordIntent = Intent(this, PasswordActivity::class.java)
                    passwordIntent.putExtra("whereFrom","login")
                    startActivity(passwordIntent)
                    finish()
                } else {
                    val alreadyLoginIntent = Intent(this,MainActivity::class.java)
                    startActivity(alreadyLoginIntent)
                    finish()
                }
            } else {
                RenewAcessTokenController.postRenewAccessToken(this, repository)

                if(SharedPreferenceController.getPassword(this) != "") {
                    val passwordIntent = Intent(this, PasswordActivity::class.java)
                    passwordIntent.putExtra("whereFrom","login")
                    startActivity(passwordIntent)
                    finish()
                } else {
                    val alreadyLoginIntent = Intent(this,MainActivity::class.java)
                    startActivity(alreadyLoginIntent)
                    finish()
                }
            }

            /*if(SharedPreferenceController.getPassword(this) != "") {
                val passwordIntent = Intent(this, PasswordActivity::class.java)
                passwordIntent.putExtra("whereFrom","login")
                startActivity(passwordIntent)
                finish()
            } else {
                val alreadyLoginIntent = Intent(this,MainActivity::class.java)
                startActivity(alreadyLoginIntent)
                finish()
            }*/
        } /*else {
            if (TokenController.getRefreshToken(this) != "") {
                if(SharedPreferenceController.getPassword(this) != "") {
                    val passwordIntent = Intent(this, PasswordActivity::class.java)
                    passwordIntent.putExtra("whereFrom","login")
                    startActivity(passwordIntent)
                    finish()
                } else {
                    val alreadyLoginIntent = Intent(this,MainActivity::class.java)
                    startActivity(alreadyLoginIntent)
                    finish()
                }
            }
        }*/
    }

    private fun requestPermission() {
        val toast: Toast = Toast(this)
        val inflater: LayoutInflater = LayoutInflater.from(this)
        val toastView: View = inflater.inflate(R.layout.toast, null)
        val toastText: TextView = toastView.findViewById(R.id.toastText)

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
                    toastText.setText("Go to Permissions to Grant")
                    toastText.gravity = Gravity.CENTER
                    toast.view = toastView
                    toast.show()
                }
                builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
                builder.show()
            } else {
                Log.e("per3", permissionsRequired[2])
                //just request the permission
                ActivityCompat.requestPermissions(
                    this,
                    permissionsRequired,
                    PERMISSION_CALLBACK_CONSTANT
                )
            }
            val editor = permissionStatus!!.edit()
            editor.putBoolean(permissionsRequired[0], true)
            editor.commit()

            //   txtPermissions.setText("Permissions Required")


        } else {
            //You already have the permission, just go ahead.
//            toastText.setText("Allowed All Permissions")
//            toastText.gravity = Gravity.CENTER
//            toast.view = toastView
//            toast.show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val toast: Toast = Toast(this)
        val inflater: LayoutInflater = LayoutInflater.from(this)
        val toastView: View = inflater.inflate(R.layout.toast, null)
        val toastText: TextView = toastView.findViewById(R.id.toastText)

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
                toastText.setText("Allowed All Permissions")
                toastText.gravity = Gravity.CENTER
                toast.view = toastView
                toast.show()
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    permissionsRequired[0]
                )
                || ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsRequired[1])
            ) {

                getAlertDialog()
            } else {
                toastText.setText("Unable to Get Permission")
                toastText.gravity = Gravity.CENTER
                toast.view = toastView
                toast.show()
            }
        }
    }

    private fun getAlertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Need Multiple Permissions")
        builder.setMessage("This app needs permissions.")
        builder.setPositiveButton("Grant") { dialog, i ->
            dialog.cancel()
            ActivityCompat.requestPermissions(
                this,
                permissionsRequired,
                PERMISSION_CALLBACK_CONSTANT
            )
        }
        builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
        builder.show()
    }

    override fun onPostResume() {
        super.onPostResume()

        val toast: Toast = Toast(this)
        val inflater: LayoutInflater = LayoutInflater.from(this)
        val toastView: View = inflater.inflate(R.layout.toast, null)
        val toastText: TextView = toastView.findViewById(R.id.toastText)

        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    permissionsRequired[0]
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                //Got Permission
                toastText.setText("Allowed All Permissions")
                toastText.gravity = Gravity.CENTER
                toast.view = toastView
                toast.show()
            }
        }
    }

    private fun configureMainTab() {

        vpLoginSlider.adapter = SliderLoginPagerAdapter(supportFragmentManager, 4)
        vpLoginSlider.offscreenPageLimit = 3
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
        tlLoginIndicator.getTabAt(3)!!.customView =
            navIndicatorLoginLayout.findViewById(R.id.imgNavIndicatorLogin4) as ImageView

        tlLoginIndicator.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
                p0!!.customView!!.isSelected = false
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                p0!!.customView!!.isSelected = true
            }
        })
    }
}