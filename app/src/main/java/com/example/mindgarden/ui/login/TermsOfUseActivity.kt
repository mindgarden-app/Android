package com.example.mindgarden.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mindgarden.R
import kotlinx.android.synthetic.main.activity_terms_of_use.*
import kotlinx.android.synthetic.main.toolbar_mypage_main.view.*

class TermsOfUseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_of_use)

        toolbar_terms_of_use.btnBack.setOnClickListener {
            finish()
        }
        toolbar_terms_of_use.txtSetting.text="이용약관"
    }
}
