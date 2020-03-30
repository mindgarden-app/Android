package com.example.mindgarden.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mindgarden.R
import com.example.mindgarden.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_privacy_statement.*
import kotlinx.android.synthetic.main.toolbar_mypage_main.view.*
import kotlinx.android.synthetic.main.toolbar_write_diary.view.*

class PrivacyStatementActivity : BaseActivity(R.layout.activity_privacy_statement) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        toolbar_privacy_statement.btnBack.setOnClickListener {
            finish()
        }
        toolbar_privacy_statement.txtSetting.text="개인정보처리방침"
    }

}
