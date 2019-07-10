package com.example.mindgarden.Activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.example.mindgarden.Adapter.MainPagerAdapter
import com.example.mindgarden.DB.SharedPreferenceController
import com.example.mindgarden.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.e("여긴 메인이야 userID", SharedPreferenceController.getUserID(this).toString())
        configureMainTab()

    }

    private fun configureMainTab() {
        vp_main.adapter = MainPagerAdapter(supportFragmentManager, 3)
        vp_main.offscreenPageLimit = 2
        tl_main_category.setupWithViewPager(vp_main)

        val navCategoryMainLayout: View = (this.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
            .inflate(R.layout.navigation_category_main, null, false)
        tl_main_category.getTabAt(0)!!.customView = navCategoryMainLayout.
            findViewById(R.id.rl_nav_category_main_home) as RelativeLayout
        tl_main_category.getTabAt(1)!!.customView = navCategoryMainLayout.
            findViewById(R.id.rl_nav_category_main_write) as RelativeLayout
       tl_main_category.getTabAt(2)!!.customView = navCategoryMainLayout.
           findViewById(R.id.rl_nav_category_main_list) as RelativeLayout
    }
}
