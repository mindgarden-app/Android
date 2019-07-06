package com.example.mindgarden.Activity

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.example.mindgarden.R
import kotlinx.android.synthetic.main.activity_main_calendar.*

class MainCalendarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_calendar)

        setWindow()
        clickText()
    }

    //PopUpWindow 사이즈 조절
    private fun setWindow(){
        val display = (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val width = (display.width * 0.8).toInt() //Display 사이즈의 20%
        val height = (display.height * 0.5).toInt()  //Display 사이즈의 20%

        //테두리 없애기
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        window.attributes.width = width
        window.attributes.height = height
    }
    fun clickText(){
        txt_1_main_calendar.setOnClickListener {
            img_1_main_calendar.visibility = View.VISIBLE
        }
        txt_2_main_calendar.setOnClickListener {
            img_2_main_calendar.visibility = View.VISIBLE
        }
        txt_3_main_calendar.setOnClickListener {
            img_3_main_calendar.visibility = View.VISIBLE
        }
        txt_4_main_calendar.setOnClickListener {
            img_4_main_calendar.visibility = View.VISIBLE
        }
        txt_5_main_calendar.setOnClickListener {
            img_5_main_calendar2.visibility = View.VISIBLE
        }
        txt_6_main_calendar.setOnClickListener {
            img_6_main_calendar2.visibility = View.VISIBLE
        }
        txt_7_main_calendar.setOnClickListener {
            img_7_main_calendar2.visibility = View.VISIBLE
        }
        txt_8_main_calendar.setOnClickListener {
            img_8_main_calendar.visibility = View.VISIBLE
        }
        txt_9_main_calendar.setOnClickListener {
            img_9_main_calendar2.visibility = View.VISIBLE
        }
        txt_10_main_calendar.setOnClickListener {
            img_10_main_calendar.visibility = View.VISIBLE
        }
        txt_11_main_calendar.setOnClickListener {
            img_11_main_calendar.visibility = View.VISIBLE
        }
        txt_12_main_calendar.setOnClickListener {
            img_12_main_calendar.visibility = View.VISIBLE
        }
    }
}
