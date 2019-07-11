package com.example.mindgarden.Activity

import android.app.Activity
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
import com.example.mindgarden.Network.ApplicationController
import com.example.mindgarden.Network.NetworkService
import com.example.mindgarden.Network.POST.PostWriteDiaryResponse
import com.example.mindgarden.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_write_diary.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    val networkService: NetworkService by lazy{
        ApplicationController.instance.networkService
    }
    var userIdx = 7 //유저 인덱스
    var status :Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configureMainTab()


        btn_write.setOnClickListener {
            if (status == false) {
                toast("이미 일기를 등록하셨습니다.")
            }else{
                startActivityForResult<WriteDiaryActivity>(1100)
            }
        }


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
                intent = getIntent()
                intent.getBooleanExtra("status", true)
                Log.e("status", status.toString())
                //3번으로 바꿔주기
                val vp = findViewById<ViewPager>(R.id.vp_main)
                vp.setCurrentItem(1,true)

            }
        }
    }
}
