package com.example.mindgarden.Activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.example.mindgarden.R
import kotlinx.android.synthetic.main.activity_main_calendar.*
import java.util.*

class MainCalendarActivity : AppCompatActivity() {

    private var btn_left : ImageView? = null
    private var btn_right: ImageView? = null
    private var txt_year : TextView? = null
    private var year : String = ""  //툴바 년
    private var month : String = "" //툴바 월
    val cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_calendar)

        //현재 MainFragment에 있는 년, 월(FY, FM ) -> Calendar에 있는 년, 월(CY, CM)에 셋팅
        val intent : Intent = getIntent()
        year = intent.getStringExtra("year")
        month = intent.getStringExtra("month")


        btn_right =findViewById(R.id.btn_right_main_calendar) as ImageView
        btn_left = findViewById(R.id.btn_left_main_calendar) as ImageView
        txt_year = findViewById(R.id.txt_year_main_calendar) as TextView
        setWindow()

        //년도 설정
        txt_year?.setText(year)

    }

    override fun onResume() {
        super.onResume()

        canBeFuture()

        btn_left?.setOnClickListener {
            year = (year.toInt() - 1).toString()
            txt_year?.setText(year)
            canBeFuture()
        }

        btn_right?.setOnClickListener {
            year = (year.toInt() + 1).toString()
            txt_year?.setText(year)
            canBeFuture()

        }

        clickText()


    }
    private fun canBeFuture(){
        if(year == cal.get(Calendar.YEAR).toString()){
            btn_right?.isEnabled = false
        }else{
            btn_right?.isEnabled = true
        }

    }
    //PopUpWindow 사이즈 조절
    private fun setWindow(){
        val display = (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val width = (display.width * 0.65).toInt() //Display 사이즈의 20%
        val height = (display.height * 0.45).toInt()  //Display 사이즈의 20%

        //테두리 없애기
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        window.attributes.width = width
        window.attributes.height = height
    }

    fun clickText(){
        clickState(txt_1_main_cal)
        clickState(txt_2_main_cal)
        clickState(txt_3_main_cal)
        clickState(txt_4_main_cal)
        clickState(txt_5_main_cal)
        clickState(txt_6_main_cal)
        clickState(txt_7_main_cal)
        clickState(txt_8_main_cal)
        clickState(txt_9_main_cal)
        clickState(txt_10_main_cal)
        clickState(txt_11_main_cal)
        clickState(txt_12_main_cal)
    }

    fun clickState(tv: TextView){
        tv.setOnClickListener(object : View.OnClickListener {
            private var state = false
            override fun onClick(v: View) {
                if (state) {
                    state = false
                    tv.setBackgroundResource(0)
                } else {
                    state = true
                    tv.setBackgroundResource(R.drawable.round_btn)
                    intentToMain(tv)

                }
            }
        })
    }

    fun intentToMain(tv: TextView){
        val intent : Intent = Intent()
        intent.putExtra("month", tv.text.toString())
        intent.putExtra("year", year)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

}
