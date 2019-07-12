package com.example.mindgarden.Activity

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.toolbar_mypage_main.*
import com.example.mindgarden.R


class FontSettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_font_setting)
        txtSetting.text = "글자 크기 설정"

        btnBack.setOnClickListener {
            val intent = Intent(this, MypageActivity::class.java)
            // 백 스페이스 누르면 다시 메인 페이지로

            startActivity(intent)

            finish()
        }
       /* fontSizeSetting.setOnClickListener {
            showdialog(this)
        }*/
    }
        fun showdialog(view: View) {

            val alertDialog = AlertDialog.Builder(this)
                //set icon
                .setIcon(android.R.drawable.ic_dialog_alert)
                //set title
                .setTitle("Are you sure to Exit")
                //set message
                .setMessage("If yes then application will close")
                //set positive button
                .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, i ->
                    //set what would happen when positive button is clicked
                    finish()
                })
                //set negative button
                .setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface, i ->
                    //set what should happen when negative button is clicked
                    Toast.makeText(applicationContext, "Nothing Happened", Toast.LENGTH_LONG).show()
                })
                .show()
        }

}