package com.example.mindgarden.Activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebView
import com.example.mindgarden.DB.SharedPreferenceController
import com.example.mindgarden.DB.TokenController
import com.example.mindgarden.Network.ApplicationController
import com.example.mindgarden.Network.NetworkService
import com.example.mindgarden.R
import com.google.gson.JsonParser
import im.delight.android.webview.AdvancedWebView
import org.jetbrains.anko.startActivity

class WebviewLoginActivity : AppCompatActivity() {
    val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    @SuppressLint("SetJavaScriptEnabled")
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_webview_login)

            val myWebView = findViewById<AdvancedWebView>(R.id.webView) as AdvancedWebView

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
                            if (it.endsWith("http://13.125.190.74:3000/auth/login/success")) {
                                myWebView.visibility = View.GONE
                                myWebView.loadUrl("javascript:window.Android.getResponse(document.getElementsByTagName('pre')[0].innerHTML);")

                                //암호설정을 안 한 경우
                                if(SharedPreferenceController.getPassword(this@WebviewLoginActivity)==""){
                                    val loginIntent= Intent(this@WebviewLoginActivity, MainActivity::class.java)
                                    loginIntent.putExtra("whereFrom","login")
                                    startActivity(loginIntent)
                                }

                                //암호설정을 한 경우
                                else{
                                    val loginIntent= Intent(this@WebviewLoginActivity, PasswordActivity::class.java)
                                    // 암호변겅을 누르면
                                    loginIntent.putExtra("whereFrom","login")
                                    startActivity(loginIntent)
                                }

                            }
                            //로그인 실패시
                            else if(it.endsWith("http://13.125.190.74:3000/auth/login/fail")){
                                myWebView.visibility = View.GONE
                                startActivity<LoginActivity>()
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
            myWebView.loadUrl("http://13.125.190.74:3000/auth/login/kakao")

        }
    inner class MyJavaScriptInterface{
        @Suppress()
        @JavascriptInterface
        fun getResponse(response1: String) {
            val  response=response1.toString()
            val json = JsonParser().parse(response).asJsonObject

          if (json == null || !json.has("data")) {
        setResult(Activity.RESULT_CANCELED)
    }
    else {
        val temp =json["data"]!!.asJsonObject
       // val temp2=temp["userIdx"].asInt
        val temp3=temp["email"].asString
        val temp4=temp["name"].asString
        val temp5=temp["refreshToken"].asString
        val temp6=temp["token"].asString

        TokenController.setAccessToken(this@WebviewLoginActivity,temp6)
              //TODO 엑세스토큰 받은 시간 저장하기
              //TODO 엑세스토큰 만료기한도 받기

        TokenController.setRefreshToken(this@WebviewLoginActivity,temp5)
        SharedPreferenceController.setUserMail(this@WebviewLoginActivity,temp3)
        SharedPreferenceController.setUserName(this@WebviewLoginActivity,temp4)

        Log.e("accessToken",TokenController.getAccessToken(this@WebviewLoginActivity))
        setResult(Activity.RESULT_OK, Intent().apply {
            putExtra("userId", temp6)
        })
    }
        }
    }
}
