package com.example.mindgarden.Activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.toolbar_read_diary.*
import org.jetbrains.anko.startActivity
import com.example.mindgarden_2.R


class ReadDiaryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_diary)



        //2. 목록에서 넘어왔을 경우 : 서버

        //수정버튼 -> WriteDiaryActivity로 넘어가기
        btn_save_diary_toolbar.setOnClickListener {
            startActivity<WriteDiaryActivity>()
        }
        //뒤로가기 -> DiaryListAcitivy로 이동
        btn_back_toolbar.setOnClickListener{
           startActivity<DiaryListActivity>()
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
