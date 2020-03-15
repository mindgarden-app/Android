package com.example.mindgarden.ui.main

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.example.mindgarden.db.TokenController
import com.example.mindgarden.R
import com.example.mindgarden.data.MindgardenRepository
import com.example.mindgarden.db.RenewAcessTokenController
import com.example.mindgarden.ui.diary.ModifyDiaryActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject


class MainActivity  : AppCompatActivity() {

    private val repository : MindgardenRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toast: Toast = Toast(this)
        val inflater: LayoutInflater = LayoutInflater.from(this)
        val toastView: View = inflater.inflate(R.layout.toast, null)
        val toastText: TextView = toastView.findViewById(R.id.toastText)

        //Log.e("Main: userID", SharedPreferenceController.getUserID(this).toString())

        Log.e("Main: accessToken",TokenController.getAccessToken(this))
        Log.e("accessToken_exp",TokenController.getExpAccessToken(this).toString())
        Log.e("accessToken_startTime",TokenController.getTimeAccessToken(this).toString())

        configureMainTab()

        if(!TokenController.isValidToken(this)){
            Log.e("Main Activity token opposite state",(!TokenController.isValidToken(this)).toString())
            RenewAcessTokenController.postRenewAccessToken(this,repository)
        }

        Log.e("Main: accessToken",TokenController.getAccessToken(this))
        Log.e("accessToken_exp",TokenController.getExpAccessToken(this).toString())
        Log.e("accessToken_startTime",TokenController.getTimeAccessToken(this).toString())


        btn_write.setOnClickListener {
                startActivityForResult(Intent(this, ModifyDiaryActivity::class.java),1100)
        }

        ModifyDiaryActivity.CHECK = false
    }

    private fun configureMainTab() {
        vp_main.adapter = MainPagerAdapter(supportFragmentManager, 2)
        vp_main.offscreenPageLimit = 2
        tl_main_category.setupWithViewPager(vp_main)

        val navCategoryMainLayout: View = (this.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
            .inflate(R.layout.navigation_category_main, null, false)
        tl_main_category.getTabAt(0)!!.customView = navCategoryMainLayout.
            findViewById(R.id.rl_nav_category_main_home) as RelativeLayout
       // tl_main_category.getTabAt(1)!!.customView = navCategoryMainLayout.
         //   findViewById(R.id.rl_nav_category_main_write) as RelativeLayout
       tl_main_category.getTabAt(1)!!.customView = navCategoryMainLayout.
           findViewById(R.id.rl_nav_category_main_list) as RelativeLayout
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1100){
            if(resultCode == Activity.RESULT_OK)
            {
                //3번으로 바꿔주기
                Log.e("MainA", "restart2")
                val vp = findViewById<ViewPager>(R.id.vp_main)
                vp.setCurrentItem(1,true)
            }
        }
    }
}
