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
                                Log.e("webview","성공페이지 화면 뜸")

                                myWebView.loadUrl("javascript:window.Android.getResponse(document.getElementsByTagName('pre')[0].innerHTML);")
                                myWebView.visibility = View.GONE
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
            val  response=response1
            val json = JsonParser().parse(response).asJsonObject

    if (json == null || !json.has("data")) {
        setResult(Activity.RESULT_CANCELED)
    }
    else {
        val temp =json["data"]!!.asJsonArray
        val temp2 =temp[0].asJsonObject
       // val temp2=temp["userIdx"].asInt
        val exp=temp2["expires_in"].asInt
        val email=temp2["email"].asString
        val name=temp2["name"].asString
        val refreshToken=temp2["refreshToken"].asString
        val accessToken=temp2["token"].asString

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
        SharedPreferenceController.setUserMail(this@WebviewLoginActivity,email)
        SharedPreferenceController.setUserName(this@WebviewLoginActivity,name)

        Log.e("Webview_accessToken",TokenController.getAccessToken(this@WebviewLoginActivity))
        setResult(Activity.RESULT_OK, Intent().apply {
            putExtra("userId", accessToken)
        })
    }
        }
    }
}
