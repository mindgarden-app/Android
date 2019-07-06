package com.example.mindgarden.Activity

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.mindgarden.R
import kotlinx.android.synthetic.main.activity_main_calendar.*
import kotlinx.android.synthetic.main.toolbar_diary_list.*
import kotlinx.android.synthetic.main.toolbar_main_calendar.*
import java.util.*


class MainCalendarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_calendar)

        setWindow()
       clickText()

        val cal = Calendar.getInstance()
        var year = cal.get(Calendar.YEAR).toString()

        val btn_left = findViewById<ImageView>(R.id.btn_left_toolbar_main_calendar)
        val btn_right = findViewById<ImageView>(R.id.btn_right_toolbar_main_calendar)
        val txt_year = findViewById<TextView>(R.id.txt_year_toolbar_main_calendar
        )
        btn_left.setOnClickListener {
                year = (year.toInt() - 1).toString()

               txt_year.setText(year)
        }
        btn_right.setOnClickListener {
            year = (year.toInt() + 1).toString()

            txt_year.setText(year)
        }

    }

    //PopUpWindow 사이즈 조절
    private fun setWindow(){
        val display = (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val width = (display.width * 0.7).toInt() //Display 사이즈의 20%
        val height = (display.height * 0.4).toInt()  //Display 사이즈의 20%

        //테두리 없애기
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        window.attributes.width = width
        window.attributes.height = height
    }

    fun clickText(){
        txt_1_main_cal.setOnClickListener(object : View.OnClickListener {
            private var state = false
            override fun onClick(v: View) {
                if (state) {
                    state = false
                    txt_1_main_cal.setBackgroundResource(0)
                } else {
                    state = true
                    txt_1_main_cal.setBackgroundResource(R.drawable.round_btn)

                }
            }
        })

        txt_2_main_cal.setOnClickListener(object : View.OnClickListener {
            private var state = false
            override fun onClick(v: View) {
                if (state) {
                    state = false
                    txt_2_main_cal.setBackgroundResource(0)
                } else {
                    state = true
                    txt_2_main_cal.setBackgroundResource(R.drawable.round_btn)

                }
            }
        })

        txt_3_main_cal.setOnClickListener(object : View.OnClickListener {
            private var state = false
            override fun onClick(v: View) {
                if (state) {
                    state = false
                    txt_3_main_cal.setBackgroundResource(0)
                } else {
                    state = true
                    txt_3_main_cal.setBackgroundResource(R.drawable.round_btn)

                }
            }
        })

        txt_4_main_cal.setOnClickListener(object : View.OnClickListener {
            private var state = false
            override fun onClick(v: View) {
                if (state) {
                    state = false
                    txt_4_main_cal.setBackgroundResource(0)
                } else {
                    state = true
                    txt_4_main_cal.setBackgroundResource(R.drawable.round_btn)

                }
            }
        })
        txt_5_main_cal.setOnClickListener(object : View.OnClickListener {
            private var state = false
            override fun onClick(v: View) {
                if (state) {
                    state = false
                    txt_5_main_cal.setBackgroundResource(0)
                } else {
                    state = true
                    txt_5_main_cal.setBackgroundResource(R.drawable.round_btn)

                }
            }
        })

        txt_6_main_cal.setOnClickListener(object : View.OnClickListener {
            private var state = false
            override fun onClick(v: View) {
                if (state) {
                    state = false
                    txt_6_main_cal.setBackgroundResource(0)
                } else {
                    state = true
                    txt_6_main_cal.setBackgroundResource(R.drawable.round_btn)

                }
            }
        })

        txt_7_main_cal.setOnClickListener(object : View.OnClickListener {
            private var state = false
            override fun onClick(v: View) {
                if (state) {
                    state = false
                    txt_7_main_cal.setBackgroundResource(0)
                } else {
                    state = true
                    txt_7_main_cal.setBackgroundResource(R.drawable.round_btn)

                }
            }
        })

        txt_8_main_cal.setOnClickListener(object : View.OnClickListener {
            private var state = false
            override fun onClick(v: View) {
                if (state) {
                    state = false
                    txt_8_main_cal.setBackgroundResource(0)
                } else {
                    state = true
                    txt_8_main_cal.setBackgroundResource(R.drawable.round_btn)

                }
            }
        })

        txt_9_main_cal.setOnClickListener(object : View.OnClickListener {
            private var state = false
            override fun onClick(v: View) {
                if (state) {
                    state = false
                    txt_9_main_cal.setBackgroundResource(0)
                } else {
                    state = true
                    txt_9_main_cal.setBackgroundResource(R.drawable.round_btn)

                }
            }
        })

        txt_10_main_cal.setOnClickListener(object : View.OnClickListener {
            private var state = false
            override fun onClick(v: View) {
                if (state) {
                    state = false
                    txt_10_main_cal.setBackgroundResource(0)
                } else {
                    state = true
                    txt_10_main_cal.setBackgroundResource(R.drawable.round_btn)

                }
            }
        })

        txt_11_main_cal.setOnClickListener(object : View.OnClickListener {
            private var state = false
            override fun onClick(v: View) {
                if (state) {
                    state = false
                    txt_11_main_cal.setBackgroundResource(0)
                } else {
                    state = true
                    txt_11_main_cal.setBackgroundResource(R.drawable.round_btn)

                }
            }
        })

        txt_12_main_cal.setOnClickListener(object : View.OnClickListener {
            private var state = false
            override fun onClick(v: View) {
                if (state) {
                    state = false
                    txt_12_main_cal.setBackgroundResource(0)
                } else {
                    state = true
                    txt_12_main_cal.setBackgroundResource(R.drawable.round_btn)

                }
            }
        })
    }

}
