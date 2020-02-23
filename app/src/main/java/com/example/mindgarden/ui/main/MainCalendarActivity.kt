package com.example.mindgarden.ui.main

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


/*
[미완] 코드 다듬기 필요 _ for문 맘에안듬
 */
class MainCalendarActivity : AppCompatActivity() {

    private lateinit var btn_left : ImageView
    private lateinit var btn_right: ImageView
    private lateinit var txt_year : TextView
    private var year : String = ""  //툴바 년
    private var month : String = "" //툴바 달
    private var currentMonth : Int = 0//현재 달
    val cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_calendar)

        //현재 MainFragment에 있는 년, 월(FY, FM ) -> Calendar에 있는 년, 월(CY, CM)에 셋팅
        val intent : Intent = getIntent()
        year = intent.getStringExtra("year")
        month = intent.getStringExtra("month")

        //현재달 설정
        currentMonth = cal.get(Calendar.MONTH) + 1


        btn_right =findViewById(R.id.btn_right_main_calendar) as ImageView
        btn_left = findViewById(R.id.btn_left_main_calendar) as ImageView
        txt_year = findViewById(R.id.txt_year_main_calendar) as TextView
        setWindow()

        //년도 설정
        txt_year.setText(year)

    }

    override fun onResume() {
        super.onResume()

        btnRightControl()

        btn_left.setOnClickListener {
            year = (year.toInt() - 1).toString()
            txt_year.setText(year)
            btnRightControl()
        }

        btn_right.setOnClickListener {
            year = (year.toInt() + 1).toString()
            txt_year.setText(year)
            btnRightControl()
        }

    }
    private fun btnRightControl(){
        if(year == cal.get(Calendar.YEAR).toString()){
            btn_right.isEnabled = false
            Log.e("btnRight", "ok")
            monthClickControl(true)
        }else{
            btn_right.isEnabled = true
            monthClickControl(false)
        }
    }

    private fun monthClickControl(currentYear : Boolean){
        for(i in 1..12){
            val tag = ll_main_calendar.findViewWithTag<TextView>("$i")
            val calMonth = tag.text.toString().toInt()

            if(currentYear == true){   //현재 년도일 경우
                if(calMonth <= currentMonth){	//현재달보다 달력에서의 달이 작아야함
                    for(i in 1..currentMonth){
                        val tv = ll_main_calendar.findViewWithTag<TextView>("$i")
                        clickState(tv)
                    }

                    if(currentMonth != 12){
                      for(i in currentMonth+1..12){
                          val tv = ll_main_calendar.findViewWithTag<TextView>("$i")
                          resetClick(tv)
                      }
                    }
                }
            }
            else{ //현재 년보다 작은값일 경우
                clickState(tag)
            }

        }
    }

    //PopUpWindow 사이즈 조절
    private fun setWindow(){
        val display = (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val width = (display.width * 0.69).toInt() //Display 사이즈의 20%
        val height = (display.height * 0.45).toInt()  //Display 사이즈의 20%

        //테두리 없애기
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        window.attributes.width = width
        window.attributes.height = height
    }

    //눌렀을때의 동작
    fun clickState(tv: TextView){
        tv.isClickable = true
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

    fun resetClick(tv: TextView){
        tv.isClickable = false
    }

    fun intentToMain(tv: TextView){
        val intent : Intent = Intent()
        intent.putExtra("month", tv.text.toString())
        intent.putExtra("year", year)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

}
