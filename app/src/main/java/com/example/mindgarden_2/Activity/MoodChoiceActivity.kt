package com.example.mindgarden_2.Activity

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Display
import android.view.Window
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_mood_choice.*
import android.content.Context.WINDOW_SERVICE
import android.support.v7.widget.LinearLayoutManager
import android.widget.RelativeLayout
import com.example.mindgarden_2.Data.MoodChoiceData
import com.example.writediary.MoodChoiceRecyclerViewAdapter
import kotlinx.android.synthetic.main.rv_item_mood_choice.*
import com.example.mindgarden_2.R



class MoodChoiceActivity : AppCompatActivity() {

    lateinit var moodChoiceRecyclerViewAdapter: MoodChoiceRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mood_choice)

        setWindowSize()
        configureRecyclerView()
    }

    //PopUpWindow 사이즈 조절
    private fun setWindowSize(){
        val display = (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val width = (display.width * 0.9).toInt() //Display 사이즈의 90%
        val height = (display.height * 0.9).toInt()  //Display 사이즈의 90%

        window.attributes.width = width
        window.attributes.height = height
    }

    private fun configureRecyclerView(){
        var dataList : ArrayList<MoodChoiceData> = ArrayList()
        dataList.add(MoodChoiceData("", "기분이 신나요"))
        dataList.add(MoodChoiceData("", "기분이 좋아요"))
        dataList.add(MoodChoiceData("", "기분이 그냥 그래요"))
        dataList.add(MoodChoiceData("", "기분이 별로에요"))
        dataList.add(MoodChoiceData("", "기분이 화가나요"))
        dataList.add(MoodChoiceData("", "기분이 짜증나요"))
        dataList.add(MoodChoiceData("", "기분이 우울해요"))
        dataList.add(MoodChoiceData("", "기분이 심심해요"))
        dataList.add(MoodChoiceData("", "기분이 재미있어요"))
        dataList.add(MoodChoiceData("", "기분이 설레어요"))


        rv_mood_mood_choice_list.adapter = MoodChoiceRecyclerViewAdapter(this, dataList)
        rv_mood_mood_choice_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        moodChoiceRecyclerViewAdapter = MoodChoiceRecyclerViewAdapter(this, dataList)


    }

}
