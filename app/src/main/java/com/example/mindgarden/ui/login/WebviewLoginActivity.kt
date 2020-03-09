package com.example.mindgarden.ui.login

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.example.mindgarden.db.TokenController
import com.example.mindgarden.ui.main.MainActivity
import com.example.mindgarden.ui.password.PasswordActivity
import com.example.mindgarden.db.SharedPreferenceController
import com.example.mindgarden.R
import com.google.gson.JsonParser
import im.delight.android.webview.AdvancedWebView


class WebviewLoginActivity : AppCompatActivity() {

    var whyOpen:String =""


    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview_login)
        val myWebView = findViewById<AdvancedWebView>(R.id.webView)
        val intent = getIntent()

        intent.getStringExtra("whyOpen")?.let {
            whyOpen=it
            Log.e("WebView Whyopen",whyOpen)
            if(whyOpen=="deleteUser"){
                TokenController.clearRefreshToken(this)
                Log.e("Webview whyOpen",TokenController.getRefreshToken(this))
                myWebView.clearHistory()
                myWebView.clearCache(true)

                this.deleteDatabase("webView.db")
                this.deleteDatabase("webViewCache.db")

                myWebView.visibility=View.VISIBLE
                myWebView.loadUrl("http://15.165.86.150:3000/auth/login/kakao")


                Log.e("WebView Whyopen","delete Cookies")
            }
        }
        Log.e("Webview","having refreshToken??"+TokenController.getRefreshToken(this))
        if(TokenController.getRefreshToken(this)!=""){

            Log.e("Webview","having refreshToken")

            if(SharedPreferenceController.getPassword(this)!=""){
                Log.e("Webview","to PassActivity")
                val loginIntent= Intent(this@WebviewLoginActivity, PasswordActivity::class.java)

                loginIntent.putExtra("whereFrom","login")
                startActivity(loginIntent)
            }
            else{
                Log.e("Webview","to MainActivity")
                //val loginIntent= Intent(this@WebviewLoginActivity, MainActivity::class.java)
                //loginIntent.putExtra("whereFrom","login")
                //startActivity(loginIntent)
            }
        }
        Log.e("Webview","웹뷰")



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true)
        }
        myWebView.apply {

            setListener(this@WebviewLoginActivity, object : AdvancedWebView.Listener {
                override fun onDownloadRequested(
                    url: String?,
                    suggestedFilename: String?,
                    mimeType: String?,
                    contentLength: Long,
                    contentDisposition: String?,
                    userAgent: String?
                ) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onPageError(errorCode: Int, description: String?, failingUrl: String?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onPageFinished(url: String?) {


                    url?.let {

                        //로그인 성공시
                        if (it.endsWith("http://15.165.86.150:3000/auth/login/success")) {

                            whyOpen=intent.getStringExtra("whyOpen")

                            Log.e("WebView WhyOpen Success",whyOpen)
                            Log.e("webview","성공페이지 화면 뜸")
                            if(whyOpen=="deleteUser"){
                                Log.e("webview","계정 삭제")
                                myWebView.visibility=View.VISIBLE
                                myWebView.clearHistory()
                                myWebView.clearCache(true)
                                myWebView.clearFormData()


                                //  myWebView.loadUrl("http://13.125.190.74:3000/auth/login/kakao")

                            }

                            else {

                                myWebView.loadUrl("javascript:window.Android.getResponse(document.getElementsByTagName('pre')[0].innerHTML);")
                                myWebView.visibility = View.INVISIBLE
                                //암호설정을 안 한 경우
                                if (SharedPreferenceController.getPassword(this@WebviewLoginActivity) == "") {
                                    Log.e("webView main으로 넘어가기전",TokenController.getAccessToken(this@WebviewLoginActivity))
                                    Log.e("webView main으로 넘어가기전",TokenController.getRefreshToken(this@WebviewLoginActivity))
                                    Log.e("webView main으로 넘어가기전",SharedPreferenceController.getUserName(this@WebviewLoginActivity))
                                    Log.e("webView main으로 넘어가기전",SharedPreferenceController.getUserMail(this@WebviewLoginActivity))
                                    val loginIntent =
                                        Intent(this@WebviewLoginActivity, MainActivity::class.java)
                                    loginIntent.putExtra("whereFrom", "login")
                                    startActivity(loginIntent)

                                }

                                //암호설정을 한 경우
                                else {
                                    val loginIntent =
                                        Intent(this@WebviewLoginActivity, PasswordActivity::class.java)

                                    loginIntent.putExtra("whereFrom", "login")
                                    startActivity(loginIntent)
                                }
                            }
                        }
                        //로그인 실패시
                        else if(it.endsWith("http://15.165.86.150:3000/auth/login/fail")){
                            myWebView.visibility = View.INVISIBLE
                            startActivity(Intent(context, LoginActivity::class.java))
                        }
                    }

                }
                override fun onPageStarted(url: String?, favicon: Bitmap?) {
                }

                override fun onExternalPageRequest(url: String?) {
                }
            })

            settings.javaScriptEnabled = true
            addJavascriptInterface(MyJavaScriptInterface(), "Android")

        }
        myWebView.loadUrl("http://15.165.86.150:3000/auth/login/kakao")

    }
    inner class MyJavaScriptInterface{
        @Suppress()
        @JavascriptInterface
        fun getResponse(response1: String) {
            val  response=response1
            val json = JsonParser().parse(response).asJsonObject

            if (json == null || !json.has("data")) {
                setResult(Activity.RESULT_CANCELED)
            }
            else {
                val temp =json["data"]!!.asJsonArray
                val temp2 =temp[0].asJsonObject
                // val temp2=temp["userIdx"].asInt
                val exp=temp2["expires_in"].asInt //이것도 얘기해봐야함
                //val email=temp2["email"].asString
                val name=temp2["name"].asString
                val refreshToken=temp2["refreshToken"].asString
                val accessToken=temp2["token"].asString
                // val userIdx=temp2["userIdx"].asString
                //  Log.e("userIdx",userIdx)

                TokenController.setAccessToken(this@WebviewLoginActivity,accessToken)

                //TODO 엑세스토큰 받은 시간 저장하기
                TokenController.setStartTimeAccessToken(this@WebviewLoginActivity,System.currentTimeMillis())
                Log.e("Webview_accessToken_startTime",TokenController.getTimeAccessToken(this@WebviewLoginActivity).toString())

                //TODO 엑세스토큰 만료기한도 받기
                TokenController.setExpAccessToken(this@WebviewLoginActivity,exp.toLong())
                Log.e("Webview_accessToken_exp",exp.toString())

                Log.e("Webview_accessToken_exp_in_DB",TokenController.getExpAccessToken(this@WebviewLoginActivity).toString())


                //리프레시 토큰 저장하기
                TokenController.setRefreshToken(this@WebviewLoginActivity,refreshToken)
                TokenController.getRefreshToken(this@WebviewLoginActivity)

                //SharedPreferenceController.setUserMail(this@WebviewLoginActivity,email)
                SharedPreferenceController.setUserName(this@WebviewLoginActivity,name)

                Log.e("Webview_accessToken",TokenController.getAccessToken(this@WebviewLoginActivity))
                setResult(Activity.RESULT_OK, Intent().apply {
                    putExtra("userId", accessToken)
                })
            }
        }
    }
}


