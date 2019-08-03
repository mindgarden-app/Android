package com.example.mindgarden.Activity

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.example.mindgarden.Adapter.MainPagerAdapter
import com.example.mindgarden.DB.SharedPreferenceController
import com.example.mindgarden.DB.TokenController
import com.example.mindgarden.Fragment.MainFragment
import com.example.mindgarden.R
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast


class MainActivity  : AppCompatActivity(), MainFragment.OnDataPass  {

    var check : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Log.e("Main: userID", SharedPreferenceController.getUserID(this).toString())
        Log.e("Main: accessToken",TokenController.getAccessToken(this))
        configureMainTab()

        btn_write.setOnClickListener {
          // Log.e("mainActivity", check.toString())
           // if(check == 2) {
            startActivityForResult<WriteDiaryActivity>(1100)
            //}else if({
            //  toast("일기를 이미 썼다")
           //}
        }
    }

    override fun checkPass(c: Int) {
        check = c
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
                val vp = findViewById<ViewPager>(R.id.vp_main)
                vp.setCurrentItem(1,true)
            }
        }
    }
}
