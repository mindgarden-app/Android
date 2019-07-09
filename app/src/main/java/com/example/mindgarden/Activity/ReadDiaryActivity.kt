package com.example.mindgarden.Activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.util.Log
import com.example.mindgarden.Fragment.DiaryListFragment
import kotlinx.android.synthetic.main.toolbar_read_diary.*
import org.jetbrains.anko.startActivity
import com.example.mindgarden.R
import org.jetbrains.anko.support.v4.fragmentTabHost
import android.R.attr.fragment
import android.support.v4.app.Fragment
import android.R.attr.fragment
import android.support.v4.app.FragmentManager
import com.example.mindgarden.Fragment.MainFragment





class ReadDiaryActivity : AppCompatActivity() {

        private var pager : ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_diary)

        val intent : Intent = getIntent()
        val requestCode : Int = intent.getIntExtra("requestCode", 0)

        Log.e("requestCode" , requestCode.toString())

        //수정버튼 -> WriteDiaryActivity로 넘어가기
            btn_modify_diary_toolbar.setOnClickListener {
                startActivity<WriteDiaryActivity>()
            }


        //뒤로가기 -> DiaryListAcitivy로 이동
        btn_back_toolbar.setOnClickListener{

            //var mFragment: Fragment? = null
            //val fragmentManager = supportFragmentManager
            //fragmentManager.beginTransaction().replace(R.id.frame_container, DiaryListFragment).commit()
        }

        /*
        이미지가 있다면
         img_gallary_read_diary.visibility = View.VISIBLE
         서버에 받아온 이미지 넣어주기
         */
    }

    fun sharedPreference(){
        //writeActivity에서 넘어왔는지 확인
        var requestCode : Int
        val intent : Intent = Intent()
        requestCode = intent.getIntExtra("reauestCode", 0)
        if(requestCode == 1000) {
            //내부 DB에서 내용 받아와서 보여주기

        }
    }

}
