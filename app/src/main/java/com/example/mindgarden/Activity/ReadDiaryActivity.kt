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
import android.app.Activity
import android.support.v4.app.FragmentManager
import android.support.v4.view.PagerAdapter
import com.example.mindgarden.Fragment.MainFragment


class ReadDiaryActivity : AppCompatActivity() {

        private var pager : ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_diary)

        val intent : Intent = getIntent()

        //수정버튼 -> WriteDiaryActivity로 넘어가기
            btn_modify_diary_toolbar.setOnClickListener {
                startActivity<WriteDiaryActivity>()
            }


        //뒤로가기 -> DiaryListAcitivy로 이동
        btn_back_toolbar.setOnClickListener{
                setResult(Activity.RESULT_OK)
                finish()
        }

        /*
        이미지가 있다면
         img_gallary_read_diary.visibility = View.VISIBLE
         서버에 받아온 이미지 넣어주기
         */
    }


}
