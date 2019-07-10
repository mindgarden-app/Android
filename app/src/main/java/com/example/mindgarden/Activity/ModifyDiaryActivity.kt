package com.example.mindgarden.Activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.mindgarden.R
import kotlinx.android.synthetic.main.toolbar_write_diary.*

class ModifyDiaryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_diary)

        txt_save_toolbar.setText("완료")

        btn_back_toolbar.setOnClickListener {
            onBackPressed()
        }
       btn_save_diary_toolbar.setOnClickListener {  }

    }
}
