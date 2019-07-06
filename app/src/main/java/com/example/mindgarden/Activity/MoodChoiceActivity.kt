package com.example.mindgarden.Activity

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Display
import android.view.Window
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_mood_choice.*
import android.content.Context.WINDOW_SERVICE
import android.graphics.Bitmap
import android.support.v7.widget.LinearLayoutManager
import com.example.mindgarden.R
import android.graphics.drawable.ColorDrawable
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.example.mindgarden.Adapter.MoodChoiceRecyclerViewAdapter
import com.example.mindgarden.Data.MoodChoiceData


class MoodChoiceActivity : AppCompatActivity() {

    lateinit var moodChoiceRecyclerViewAdapter: MoodChoiceRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mood_choice)

        setWindow()
        configureRecyclerView()


    }


    //PopUpWindow 사이즈 조절
    private fun setWindow(){
        val display = (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val width = (display.width * 0.6).toInt() //Display 사이즈의 60%
        val height = (display.height * 0.85).toInt()  //Display 사이즈의 85%

        //테두리 없애기
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        window.attributes.width = width
        window.attributes.height = height
    }


    //데이터
    private fun configureRecyclerView(){

        val icn1 = drawableToBitmap(R.drawable.icn_weather_001)

        var dataList : ArrayList<MoodChoiceData> = ArrayList()
        dataList.add(MoodChoiceData(icn1, "기분이 신나요"))
        dataList.add(MoodChoiceData(icn1, "기분이 좋아요"))
        dataList.add(MoodChoiceData(icn1, "기분이 별로에요"))
        dataList.add(MoodChoiceData(icn1, "기분이 화가나요"))
        dataList.add(MoodChoiceData(icn1, "기분이 짜증나요"))
        dataList.add(MoodChoiceData(icn1, "기분이 우울해요"))
        dataList.add(MoodChoiceData(icn1, "기분이 심심해요"))
        dataList.add(MoodChoiceData(icn1, "기분이 재미있어요"))
        dataList.add(MoodChoiceData(icn1, "기분이 설레어요"))


        rv_mood_mood_choice_list.adapter = MoodChoiceRecyclerViewAdapter(this, dataList)
        rv_mood_mood_choice_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        moodChoiceRecyclerViewAdapter = MoodChoiceRecyclerViewAdapter(this, dataList)

    }

    private fun drawableToBitmap(icnName : Int) : Bitmap{
        val drawable = resources.getDrawable(icnName) as BitmapDrawable
        val bitmap = drawable.bitmap
        return bitmap
    }

}
