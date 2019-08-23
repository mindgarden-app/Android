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
        val width = (display.width * 0.7).toInt() //Display 사이즈의 60%
        val height = (display.height * 0.88).toInt()  //Display 사이즈의 85%


        //테두리 없애기
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window.attributes.width = width
        window.attributes.height = height
    }


    //데이터
    private fun configureRecyclerView(){

        val icn1 = drawableToBitmap(R.drawable.img_weather1_good)
        val icn2  = drawableToBitmap(R.drawable.img_weather2_excited)
        val icn3 = drawableToBitmap(R.drawable.img_weather3_soso)
        val icn4 = drawableToBitmap(R.drawable.img_weather4_bored)
        val icn5 = drawableToBitmap(R.drawable.img_weather5_funny)
        val icn6 = drawableToBitmap(R.drawable.img_weather6_rainbow)
        val icn7 = drawableToBitmap(R.drawable.img_weather7_notgood)
        val icn8 = drawableToBitmap(R.drawable.img_weather8_sad)
        val icn9 = drawableToBitmap(R.drawable.img_weather9_annoying)
        val icn10 = drawableToBitmap(R.drawable.img_weather10_lightning)
        val icn11 = drawableToBitmap(R.drawable.img_weather11_none)

        var dataList : ArrayList<MoodChoiceData> = ArrayList()
        dataList.add(MoodChoiceData(0, icn1, "좋아요"))
        dataList.add(MoodChoiceData(1, icn2, "신나요"))
        dataList.add(MoodChoiceData(2, icn3, "그냥 그래요"))
        dataList.add(MoodChoiceData(3, icn4, "심심해요"))
        dataList.add(MoodChoiceData(4, icn5, "재미있어요"))
        dataList.add(MoodChoiceData(5, icn6, "설레요"))
        dataList.add(MoodChoiceData(6, icn7, "별로에요"))
        dataList.add(MoodChoiceData(7, icn8, "우울해요"))
        dataList.add(MoodChoiceData(8, icn9, "짜증나요"))
        dataList.add(MoodChoiceData(9, icn10, "화가나요"))
        dataList.add(MoodChoiceData(10,icn11, "기분없음"))


        rv_mood_mood_choice_list.adapter = MoodChoiceRecyclerViewAdapter(this, dataList)
        rv_mood_mood_choice_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        moodChoiceRecyclerViewAdapter = MoodChoiceRecyclerViewAdapter(this, dataList)

    }

    public fun drawableToBitmap(icnName : Int) : Bitmap{
        val drawable = resources.getDrawable(icnName) as BitmapDrawable
        val bitmap = drawable.bitmap
        return bitmap
    }

}
