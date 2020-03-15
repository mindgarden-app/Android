package com.example.mindgarden.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView
import com.example.mindgarden.R
import kotlinx.android.synthetic.main.activity_main_calendar.*
import java.util.*


@Suppress("DEPRECATION")
class MainCalendarActivity : AppCompatActivity() {

    private val cal = Calendar.getInstance()
    private val currentCal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_calendar)

        init()
    }
    private fun init(){
        initDate()
        setWindow()
        yearClick()
        monthClickControl()
    }

    private fun initDate(){
        val date = intent.getStringExtra("toolbarDate")
        cal.set(Calendar.YEAR, date.substring(0,4).toInt())
        txtYearMainCalendar.text = date.substring(0,4)
        cal.set(Calendar.MONTH, (date.substring(6,8).toInt()-1))
        initMonthBackground()
    }

    //year
    private fun setYear(){
        txtYearMainCalendar.text = cal.get(Calendar.YEAR).toString()
    }

    private fun yearClick(){
        btnRightMainCalendar.setOnClickListener {
            yearMoveControl(0)
            monthClickControl()
        }
        btnLeftMainCalendar.setOnClickListener {
            yearMoveControl(1)
            monthClickControl()
        }
    }

    private fun yearMoveControl(rl: Int){
        when(rl){
            0->{
                if(!isCurrentYear()){
                    cal.add(Calendar.YEAR, 1)
                    setYear()
                }
            }
            1->{
                if(txtYearMainCalendar.text != "2019"){
                    cal.add(Calendar.YEAR, -1)
                    setYear()
                }
            }
        }
    }

    private fun isCurrentYear(): Boolean{
        return txtYearMainCalendar.text == currentCal.get(Calendar.YEAR).toString()
    }

    //month
    private fun initMonthBackground(){
        ll_main_calendar.findViewWithTag<TextView>(cal.get(Calendar.MONTH).toString()).setBackgroundResource(R.drawable.round_btn)
    }

    private fun monthClick(tv: TextView){
        tv.setOnClickListener {
            tv.setBackgroundResource(R.drawable.round_btn)
            val monthText = tv.text.toString()
            cal.set(Calendar.MONTH, monthText.toInt())
            intentToFragment()
        }
    }

    private fun monthClickControl(){
        resetClick()

        if(isCurrentYear()) setMonthClick(0,currentCal.get(Calendar.MONTH))
        else{
            if(cal.get(Calendar.YEAR) == 2019) setMonthClick(6,11)
            else setMonthClick(0, 11)
        }
    }

    private fun setMonthClick(start: Int, end: Int){
        for(i in start..end){
            ll_main_calendar.findViewWithTag<TextView>("$i").isClickable = true
            monthClick(ll_main_calendar.findViewWithTag("$i"))
        }
    }

    private fun resetClick(){
        ll_main_calendar.findViewWithTag<TextView>(cal.get(Calendar.MONTH).toString()).setBackgroundResource(0)

        for(i in 0..11) {
            ll_main_calendar.findViewWithTag<TextView>("$i").isClickable = false
        }
    }

    private fun intentToFragment(){
        Intent(this, MainFragment::class.java).apply {
            putExtra("year", cal.get(Calendar.YEAR))
            putExtra("month", ((cal.get(Calendar.MONTH))-1))
            setResult(Activity.RESULT_OK,this)
            finish()
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
}
