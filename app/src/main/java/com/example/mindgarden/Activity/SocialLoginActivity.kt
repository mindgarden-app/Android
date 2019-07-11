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
import com.example.mindgarden.Network.ApplicationController
import com.example.mindgarden.Network.Get.GetLoginResponse
import com.example.mindgarden.Network.NetworkService
import com.example.mindgarden.R
import com.google.gson.JsonParser
import im.delight.android.webview.AdvancedWebView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SocialLoginActivity : AppCompatActivity() {
    val networkService: NetworkService by lazy {
        ApplicationController.instance.networkService
    }

    @SuppressLint("SetJavaScriptEnabled")
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_social_login)

            val myWebView = findViewById<AdvancedWebView>(R.id.webView) as AdvancedWebView

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                WebView.setWebContentsDebuggingEnabled(true)
            }
            myWebView.apply {
                setListener(this@SocialLoginActivity, object : AdvancedWebView.Listener {
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
                            if (it.endsWith("http://13.125.190.74:3000/auth/login/success")) {
                                myWebView.visibility = View.GONE
                                myWebView.loadUrl("javascript:window.Android.getResponse(document.getElementsByTagName('pre')[0].innerHTML);")


                                val loginIntent= Intent(this@SocialLoginActivity, PasswordActivity::class.java)
                                // 암호변겅을 누르면
                                loginIntent.putExtra("whereFrom","login")
                                startActivity(loginIntent)
                            }
                        }
                    }
                    override fun onPageStarted(url: String?, favicon: Bitmap?) {
                        Log.e("로그인", "웹뷰 시작한당 $url")
                    }

                    override fun onExternalPageRequest(url: String?) {
                        Log.e("로그인", "웹뷰 리퀘스트 $url")
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

            Log.e("get response $json \nfrom $response1","get response $json \nfrom $response1")
            Log.e("response","$response")
            /* val getLoginResponse = networkService.getLoginResponse("application/json", json)

           getLoginResponse.enqueue(object: Callback<GetLoginResponse> {
               override fun onFailure(call: Call<GetLoginResponse>, t: Throwable) {
                   Log.e("login", t.toString())
               }
               override fun onResponse(call: Call<GetLoginResponse>, response: Response<GetLoginResponse>) {
                   if (response.isSuccessful) {
                       if (response.body()!!.status == 200) {
                           val tmp: Int = response.body()!!.data!!.userIdx
                           Log.e("json", tmp.toString())
                           SharedPreferenceController.setUserID(this@SocialLoginActivity, tmp)
                           Log.e("userID", SharedPreferenceController.getUserID(this@SocialLoginActivity).toString())
                           //데베에 저장
                       }
                   }
               }
           })*/
          if (json == null || !json.has("data")) {
        setResult(Activity.RESULT_CANCELED)
    }
    else {
        val temp =json["data"]!!.asJsonObject
        val temp2=temp["userIdx"].asInt
        SharedPreferenceController.setUserID(this@SocialLoginActivity,temp2)
        Log.e("userID",SharedPreferenceController.getUserID(this@SocialLoginActivity).toString())
        setResult(Activity.RESULT_OK, Intent().apply {
            putExtra("userId", temp2)
        })
    }
            // finish()
        }
    }
}
