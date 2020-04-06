package com.mindgarden.mindgarden.ui.login

import android.os.Bundle
import com.mindgarden.mindgarden.R
import com.mindgarden.mindgarden.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_privacy_statement.*
import kotlinx.android.synthetic.main.toolbar_mypage_main.view.*

class PrivacyStatementActivity : BaseActivity(R.layout.activity_privacy_statement) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        toolbar_privacy_statement.btnBack.setOnClickListener {
            finish()
        }
        toolbar_privacy_statement.txtSetting.text="개인정보처리방침"
    }

}
