package com.mindgarden.mindgarden.ui.diary

import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_mood_choice.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.mindgarden.mindgarden.R
import android.graphics.drawable.ColorDrawable
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.mindgarden.mindgarden.data.MoodChoiceData
import com.mindgarden.mindgarden.ui.base.BaseActivity

class MoodActivity : BaseActivity(R.layout.activity_mood_choice), Mood {

    lateinit var moodChoiceRecyclerViewAdapter: MoodChoiceRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setWindow()
        configureRecyclerView()
    }


    //PopUpWindow 사이즈 조절
    private fun setWindow(){
        val display = (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val width = (display.width * 0.7).toInt() //Display 사이즈의 60%
        val height = (display.height * 0.65).toInt()  //Display 사이즈의 85%


        //테두리 없애기
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window.attributes.width = width
        window.attributes.height = height
    }


    //데이터
    private fun configureRecyclerView(){
        val dataList = ArrayList<MoodChoiceData>()
        getMoodList(dataList)

        rv_mood_mood_choice_list.adapter = MoodChoiceRecyclerViewAdapter(this, dataList)
        rv_mood_mood_choice_list.layoutManager =
            LinearLayoutManager(
                this,
                RecyclerView.VERTICAL,
                false
            )

        moodChoiceRecyclerViewAdapter = MoodChoiceRecyclerViewAdapter(this,dataList)
    }
}
