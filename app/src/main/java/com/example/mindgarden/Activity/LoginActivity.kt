package com.example.mindgarden.Activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.TabLayout
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.example.mindgarden.R
import com.example.mindgarden.Adapter.SliderLoginPagerAdapter
import im.delight.android.webview.AdvancedWebView
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private val PERMISSION_CALLBACK_CONSTANT = 101
    private val REQUEST_PERMISSION_SETTING = 101
    private var permissionsRequired = arrayOf(Manifest.permission.WAKE_LOCK, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
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