package com.mindgarden.mindgarden.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.mindgarden.mindgarden.db.TokenController
import com.mindgarden.mindgarden.R
import com.mindgarden.mindgarden.data.MindgardenRepository
import com.mindgarden.mindgarden.ui.base.BaseActivity
import com.mindgarden.mindgarden.ui.diary.WriteDiaryActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject


class MainActivity  : BaseActivity(R.layout.activity_main) {

    private val repository : MindgardenRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.e("Main: accessToken",TokenController.getAccessToken(this))
        Log.e("accessToken_exp",TokenController.getExpAccessToken(this).toString())
        Log.e("accessToken_startTime",TokenController.getTimeAccessToken(this).toString())

        configureMainTab()

        TokenController.isValidToken(this,repository)




        Log.e("Main: accessToken",TokenController.getAccessToken(this))
        Log.e("accessToken_exp",TokenController.getExpAccessToken(this).toString())
        Log.e("accessToken_startTime",TokenController.getTimeAccessToken(this).toString())


        btn_write.setOnClickListener {
                startActivityForResult(Intent(this, WriteDiaryActivity::class.java),1100)
        }

        WriteDiaryActivity.CHECK = false
    }

    private fun configureMainTab() {
        vp_main.adapter = MainPagerAdapter(supportFragmentManager, 2)
        vp_main.offscreenPageLimit = 2
        tl_main_category.setupWithViewPager(vp_main)

        val navCategoryMainLayout: View = (this.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
            .inflate(R.layout.navigation_category_main, null, false)
        tl_main_category.getTabAt(0)!!.customView = navCategoryMainLayout.
            findViewById(R.id.rl_nav_category_main_home)
       // tl_main_category.getTabAt(1)!!.customView = navCategoryMainLayout.
         //   findViewById(R.id.rl_nav_category_main_write) as RelativeLayout
       tl_main_category.getTabAt(1)!!.customView = navCategoryMainLayout.
           findViewById(R.id.rl_nav_category_main_list)
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
